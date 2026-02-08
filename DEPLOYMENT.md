# Complete DevSecOps CI/CD Pipeline

## ✅ What's Included

### 1. GitHub Actions Workflows (`.github/workflows/`)
- ✅ `app-cicd.yaml` - Main application CI/CD with DevSecOps
- ✅ `infra.yaml` - Terraform infrastructure pipeline
- ✅ `platform.yaml` - Platform components deployment
- ✅ `monitoring.yaml` - Observability stack deployment

### 2. Helm Charts (`helm/`)
- ✅ `microservice/` - Reusable chart for all microservices
  - Deployment with security contexts
  - Service (ClusterIP)
  - Ingress with TLS
  - HPA (Horizontal Pod Autoscaler)
  - ServiceAccount
  - ConfigMap & Secret templates
  - PodDisruptionBudget
  - NetworkPolicy
  - ServiceMonitor (Prometheus)
- ✅ `platform/` - Platform components
  - ingress-nginx
  - cert-manager (with ClusterIssuer)
  - redis

### 3. Kubernetes Manifests (`k8s/`)
- ✅ deployment.yaml
- ✅ service.yaml
- ✅ ingress.yaml
- ✅ hpa.yaml
- ✅ configmap.yaml
- ✅ namespace.yaml

### 4. Terraform IaC (`terraform/`)
- ✅ `backend.tf` - S3 backend configuration
- ✅ `modules/vpc/` - VPC with multi-AZ setup
- ✅ `modules/eks/` - EKS cluster with node groups
- ✅ `modules/iam/` - IAM roles for IRSA
- ✅ `envs/dev/` - Dev environment config
- ✅ `envs/prod/` - Prod environment config

### 5. Monitoring (`monitoring/`)
- ✅ `prometheus-values.yaml` - Prometheus stack config
- ✅ `alert-rules.yaml` - Custom alert rules
- ✅ `grafana-dashboards/` - Pre-configured dashboards

### 6. Documentation
- ✅ `CICD-README.md` - Pipeline documentation
- ✅ `.gitignore` - Git ignore rules

## 🔐 DevSecOps Features

### Security Scanning
1. **SAST (Static Application Security Testing)**
   - OWASP Dependency Check
   - SpotBugs for Java code analysis

2. **Secret Scanning**
   - TruffleHog for exposed credentials

3. **Container Security**
   - Trivy image scanning
   - Results uploaded to GitHub Security

4. **Infrastructure Security**
   - tfsec for Terraform
   - Checkov for IaC compliance

### Best Practices Implemented
- ✅ Multi-stage pipeline with security gates
- ✅ Automated testing before deployment
- ✅ Image vulnerability scanning
- ✅ Least privilege security contexts
- ✅ Network policies
- ✅ Pod disruption budgets
- ✅ Resource limits and requests
- ✅ Health probes (liveness/readiness)
- ✅ Horizontal pod autoscaling
- ✅ Multi-AZ deployment
- ✅ Encrypted secrets
- ✅ TLS everywhere
- ✅ Monitoring and alerting

## 🚀 Quick Start

### Prerequisites
```bash
# Install required tools
brew install terraform kubectl helm aws-cli

# Configure AWS
aws configure
```

### 1. Setup Terraform Backend
```bash
# Create S3 bucket for state
aws s3 mb s3://salon-terraform-state --region us-east-1

# Create DynamoDB table for locking
aws dynamodb create-table \
  --table-name terraform-lock \
  --attribute-definitions AttributeName=LockID,AttributeType=S \
  --key-schema AttributeName=LockID,KeyType=HASH \
  --billing-mode PAY_PER_REQUEST \
  --region us-east-1
```

### 2. Deploy Infrastructure
```bash
cd terraform/envs/dev
terraform init
terraform plan
terraform apply
```

### 3. Configure kubectl
```bash
aws eks update-kubeconfig --name salon-dev-cluster --region us-east-1
```

### 4. Deploy Platform Components
```bash
# Add Helm repos
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo add jetstack https://charts.jetstack.io
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update

# Deploy ingress-nginx
helm upgrade --install ingress-nginx ingress-nginx/ingress-nginx \
  --namespace ingress-nginx \
  --create-namespace \
  --values helm/platform/ingress-nginx/values.yaml

# Deploy cert-manager
helm upgrade --install cert-manager jetstack/cert-manager \
  --namespace cert-manager \
  --create-namespace \
  --set installCRDs=true \
  --values helm/platform/cert-manager/values.yaml

# Apply ClusterIssuers
kubectl apply -f helm/platform/cert-manager/clusterissuer.yaml

# Deploy Redis
helm upgrade --install redis bitnami/redis \
  --namespace redis \
  --create-namespace \
  --values helm/platform/redis/values.yaml
```

### 5. Deploy Monitoring Stack
```bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update

helm upgrade --install kube-prometheus-stack prometheus-community/kube-prometheus-stack \
  --namespace monitoring \
  --create-namespace \
  --values monitoring/prometheus-values.yaml

kubectl apply -f monitoring/alert-rules.yaml
```

### 6. Configure GitHub Secrets
Add these secrets to your GitHub repository:
- `DOCKER_USERNAME` - Docker Hub username
- `DOCKER_PASSWORD` - Docker Hub password/token
- `AWS_ACCESS_KEY_ID` - AWS access key
- `AWS_SECRET_ACCESS_KEY` - AWS secret key

### 7. Deploy Microservices
```bash
# Push to develop branch for dev deployment
git checkout develop
git push origin develop

# Push to main branch for prod deployment (requires approval)
git checkout main
git push origin main
```

## 📊 Pipeline Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    Code Push (GitHub)                        │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              Detect Changed Services                         │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│         Security Scan (SAST + Secret Scanning)               │
│  - OWASP Dependency Check                                    │
│  - SpotBugs                                                  │
│  - TruffleHog                                                │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              Build & Test (Maven)                            │
│  - Unit Tests                                                │
│  - Code Coverage                                             │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│         Build & Push Image (Jib)                             │
│  - Container Build                                           │
│  - Push to Docker Hub                                        │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│         Image Security Scan (Trivy)                          │
│  - Vulnerability Detection                                   │
│  - Upload to GitHub Security                                 │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              Deploy to Environment                           │
│  - Dev (auto on develop branch)                              │
│  - Prod (manual approval on main branch)                     │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              Smoke Tests & Monitoring                        │
└─────────────────────────────────────────────────────────────┘
```

## 🎯 Service Deployment Example

Deploy a specific service:
```bash
helm upgrade --install user-service ./helm/microservice \
  --namespace dev \
  --create-namespace \
  --set service.name=user-service \
  --set image.tag=v1.0.0 \
  --values ./helm/microservice/values-dev.yaml
```

## 📈 Monitoring Access

### Grafana
```bash
kubectl port-forward -n monitoring svc/kube-prometheus-stack-grafana 3000:80
# Access: http://localhost:3000
# Default: admin/admin
```

### Prometheus
```bash
kubectl port-forward -n monitoring svc/kube-prometheus-stack-prometheus 9090:9090
# Access: http://localhost:9090
```

### AlertManager
```bash
kubectl port-forward -n monitoring svc/kube-prometheus-stack-alertmanager 9093:9093
# Access: http://localhost:9093
```

## 🔍 Troubleshooting

### Check Pipeline Status
```bash
# View GitHub Actions in browser or use gh CLI
gh run list
gh run view <run-id>
```

### Check Pod Status
```bash
kubectl get pods -n dev
kubectl describe pod <pod-name> -n dev
kubectl logs -f <pod-name> -n dev
```

### Check Helm Releases
```bash
helm list -n dev
helm status <release-name> -n dev
helm history <release-name> -n dev
```

### Rollback Deployment
```bash
helm rollback <release-name> <revision> -n dev
# or
kubectl rollout undo deployment/<service-name> -n dev
```

## 🛡️ Security Checklist

- ✅ All images scanned for vulnerabilities
- ✅ Secrets managed via Kubernetes Secrets (consider External Secrets Operator)
- ✅ Network policies applied
- ✅ Pod security contexts enforced
- ✅ RBAC configured
- ✅ TLS enabled on ingress
- ✅ Resource limits set
- ✅ Non-root containers
- ✅ Read-only root filesystem
- ✅ Dropped capabilities

## 📝 Next Steps

1. **Configure DNS** - Point your domain to the LoadBalancer
2. **Setup External Secrets** - Integrate with AWS Secrets Manager
3. **Configure Backup** - Setup Velero for cluster backups
4. **Add Logging** - Deploy EFK/Loki stack
5. **Setup Service Mesh** - Consider Istio/Linkerd
6. **Configure Autoscaling** - Cluster Autoscaler + Karpenter
7. **Add Chaos Engineering** - Chaos Mesh for resilience testing

## 🤝 Contributing

1. Create feature branch from `develop`
2. Make changes
3. Run local tests
4. Create PR to `develop`
5. After approval, merge to `main` for production

## 📞 Support

- GitHub Issues for bugs
- Grafana dashboards for monitoring
- Prometheus alerts for incidents
