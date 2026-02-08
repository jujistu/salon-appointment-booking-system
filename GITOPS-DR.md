# GitOps with ArgoCD & Disaster Recovery

## 🎯 Overview

This setup provides:
- **GitOps** with ArgoCD for declarative deployments
- **Canary & Blue-Green** deployments with Argo Rollouts
- **Automated Rollbacks** based on metrics
- **Disaster Recovery** with Velero backups

## 🏗️ Architecture

```
GitHub Repo (Source of Truth)
    ↓
ArgoCD (Continuous Sync)
    ↓
Argo Rollouts (Progressive Delivery)
    ↓
Kubernetes Cluster
    ↓
Velero (Backup & Restore)
```

## 🚀 Setup

### 1. Install ArgoCD & Argo Rollouts

```bash
# Run the workflow
gh workflow run argocd-setup.yaml

# Or manually
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

kubectl create namespace argo-rollouts
kubectl apply -n argo-rollouts -f https://github.com/argoproj/argo-rollouts/releases/latest/download/install.yaml
```

### 2. Access ArgoCD UI

```bash
# Port forward
kubectl port-forward svc/argocd-server -n argocd 8080:443

# Get password
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d

# Access: https://localhost:8080
# Username: admin
```

### 3. Install Velero for Backups

```bash
# Create S3 bucket
aws s3 mb s3://salon-velero-backups

# Create IAM role (update ACCOUNT_ID in velero.yaml)
# Then apply
kubectl apply -f argocd/applications/velero.yaml
```

### 4. Deploy Applications

```bash
# Apply ArgoCD applications
kubectl apply -f argocd/projects/
kubectl apply -f argocd/applications/user-service.yaml
```

## 📦 Deployment Strategies

### Canary Deployment (Default)

Progressive rollout with traffic shifting:
- 20% → 2min pause → 40% → 2min → 60% → 2min → 80% → 100%
- Automated analysis checks success rate, error rate, latency
- Auto-rollback if metrics fail

**Configuration:**
```yaml
strategy:
  type: canary

canary:
  enabled: true
  steps:
    - setWeight: 20
    - pause: {duration: 2m}
    - setWeight: 50
    - pause: {duration: 2m}
  analysis:
    enabled: true
    successRate: 95
    errorRate: 5
```

### Blue-Green Deployment

Full environment switch with preview:
- Deploy new version (green)
- Test preview environment
- Manual/auto promotion
- Instant rollback capability

**Configuration:**
```yaml
strategy:
  type: blueGreen

blueGreen:
  enabled: true
  autoPromotionEnabled: false
  scaleDownDelaySeconds: 300
```

## 🔄 GitOps Workflow

### Update Service Version

```bash
# CI/CD automatically updates GitOps repo
# Or manually:
sed -i 's/tag: .*/tag: "v1.2.3"/' gitops/overlays/prod/user-service/values.yaml
git add .
git commit -m "Update user-service to v1.2.3"
git push

# ArgoCD auto-syncs within 3 minutes
```

### Monitor Rollout

```bash
# Watch rollout progress
kubectl argo rollouts get rollout user-service -n prod --watch

# Check status
kubectl argo rollouts status user-service -n prod

# View analysis
kubectl argo rollouts get rollout user-service -n prod --show-analysis
```

### Manual Promotion

```bash
# Promote canary to stable
kubectl argo rollouts promote user-service -n prod

# Abort rollout
kubectl argo rollouts abort user-service -n prod
```

## 🔙 Rollback Strategies

### 1. Automatic Rollback (Recommended)

Argo Rollouts automatically rolls back if:
- Success rate < 95%
- Error rate > 5%
- P95 latency > 2000ms
- Analysis fails 3 times

### 2. Manual Rollback via Script

```bash
# Rollback to previous version
./scripts/rollback.sh user-service

# Rollback to specific revision
./scripts/rollback.sh user-service 3

# Check revision history
kubectl argo rollouts history user-service -n prod
```

### 3. ArgoCD Rollback

```bash
# Via UI: Application → History → Rollback
# Or CLI:
argocd app rollback user-service <revision>
```

### 4. Git Revert

```bash
# Revert commit
git revert HEAD
git push

# ArgoCD auto-syncs to previous state
```

## 🛡️ Disaster Recovery

### Automated Backups

Daily backups at 2 AM via GitHub Actions:
- Includes: prod, dev namespaces
- Retention: 7 days
- Storage: S3 (salon-velero-backups)

### Manual Backup

```bash
# Create backup
velero backup create salon-manual-backup \
  --include-namespaces prod,dev \
  --wait

# List backups
velero backup get

# Describe backup
velero backup describe salon-manual-backup
```

### Restore from Backup

```bash
# List available backups
velero backup get

# Restore entire namespace
./scripts/restore.sh salon-backup-20240206-020000 prod

# Or manually
velero restore create --from-backup salon-backup-20240206-020000 \
  --include-namespaces prod \
  --wait

# Check restore status
velero restore get
velero restore describe <restore-name>
```

### Restore Specific Resources

```bash
# Restore only specific service
velero restore create --from-backup <backup-name> \
  --include-resources deployments,services \
  --selector app=user-service

# Restore with namespace mapping
velero restore create --from-backup <backup-name> \
  --namespace-mappings prod:prod-restore
```

## 📊 Monitoring Rollouts

### Prometheus Metrics

```promql
# Success rate
sum(rate(http_server_requests_seconds_count{status!~"5.."}[2m])) 
/ 
sum(rate(http_server_requests_seconds_count[2m])) * 100

# Error rate
sum(rate(http_server_requests_seconds_count{status=~"5.."}[2m])) 
/ 
sum(rate(http_server_requests_seconds_count[2m])) * 100

# P95 latency
histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[2m])) * 1000
```

### Grafana Dashboard

Access rollout metrics:
```bash
kubectl port-forward -n monitoring svc/kube-prometheus-stack-grafana 3000:80
```

## 🚨 Emergency Procedures

### Complete Service Failure

```bash
# 1. Abort current rollout
kubectl argo rollouts abort user-service -n prod

# 2. Rollback to last stable
./scripts/rollback.sh user-service

# 3. If still failing, restore from backup
./scripts/restore.sh <latest-backup> prod
```

### Cluster Disaster

```bash
# 1. Provision new cluster (Terraform)
cd terraform/envs/prod
terraform apply

# 2. Install ArgoCD & Velero
kubectl apply -f argocd/applications/

# 3. Restore from backup
velero restore create disaster-recovery \
  --from-backup <latest-backup> \
  --wait

# 4. Verify services
kubectl get pods -n prod
```

### Database Corruption

```bash
# Restore database from RDS snapshot
aws rds restore-db-instance-from-db-snapshot \
  --db-instance-identifier salon-db-restored \
  --db-snapshot-identifier <snapshot-id>

# Update service configs to point to restored DB
```

## 🔍 Troubleshooting

### Rollout Stuck

```bash
# Check rollout status
kubectl argo rollouts get rollout user-service -n prod

# Check analysis
kubectl get analysisrun -n prod

# View logs
kubectl logs -l app=user-service -n prod

# Force abort and rollback
kubectl argo rollouts abort user-service -n prod
./scripts/rollback.sh user-service
```

### ArgoCD Out of Sync

```bash
# Force sync
argocd app sync user-service --force

# Or via UI: Sync → Force
```

### Backup Failed

```bash
# Check Velero logs
kubectl logs -n velero -l component=velero

# Check backup details
velero backup describe <backup-name> --details

# Retry backup
velero backup create retry-backup --from-backup <failed-backup>
```

## 📋 Best Practices

1. **Always use canary for production** - Gradual rollout minimizes risk
2. **Enable analysis templates** - Automated rollback on failures
3. **Test in dev first** - Validate changes before prod
4. **Monitor metrics during rollout** - Watch Grafana dashboards
5. **Keep backups for 7+ days** - Allows recovery from delayed issues
6. **Test restore procedures** - Regular DR drills
7. **Use Git tags for releases** - Easy rollback reference
8. **Document rollback procedures** - Team readiness

## 🎯 Quick Reference

```bash
# Deploy new version (GitOps)
git commit -m "Update to v1.2.3" && git push

# Monitor rollout
kubectl argo rollouts get rollout <service> -n prod -w

# Promote canary
kubectl argo rollouts promote <service> -n prod

# Rollback
./scripts/rollback.sh <service>

# Create backup
velero backup create manual-backup --include-namespaces prod

# Restore
./scripts/restore.sh <backup-name> prod

# Check health
kubectl get rollouts -n prod
kubectl get pods -n prod
```

## 📞 Support

- **ArgoCD UI**: https://localhost:8080
- **Rollout Dashboard**: `kubectl argo rollouts dashboard`
- **Grafana**: http://localhost:3000
- **Backup Status**: `velero backup get`
