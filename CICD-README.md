# DevSecOps CI/CD Pipeline

Complete CI/CD pipeline with security scanning, infrastructure as code, and monitoring for the Salon Appointment Booking System.

## 🏗️ Architecture

```
GitHub Actions → Security Scan → Build → Test → Image Scan → Deploy → Monitor
```

## 📁 Structure

```
.
├── .github/workflows/          # CI/CD pipelines
├── helm/                      # Helm charts
├── k8s/                       # Kubernetes manifests
├── terraform/                 # Infrastructure as Code
└── monitoring/                # Observability configs
```

## 🔐 Security Features

- **SAST:** OWASP Dependency Check, SpotBugs
- **Secret Scanning:** TruffleHog
- **Container Security:** Trivy
- **IaC Security:** tfsec, Checkov

## 🚀 Setup

1. Configure AWS credentials
2. Deploy infrastructure: `terraform apply`
3. Configure kubectl: `aws eks update-kubeconfig`
4. Push code to trigger pipeline

## 📊 Monitoring

- Prometheus + Grafana
- Custom alerts for microservices
- Pre-configured dashboards
