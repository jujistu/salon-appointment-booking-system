#!/bin/bash
set -e

SERVICE=$1
REVISION=${2:-0}

if [ -z "$SERVICE" ]; then
  echo "Usage: ./rollback.sh <service-name> [revision]"
  echo "Example: ./rollback.sh user-service 2"
  exit 1
fi

echo "🔄 Rolling back $SERVICE..."

# Argo Rollouts rollback
if kubectl get rollout $SERVICE -n prod &>/dev/null; then
  echo "Using Argo Rollouts..."
  if [ "$REVISION" -eq 0 ]; then
    kubectl argo rollouts undo $SERVICE -n prod
  else
    kubectl argo rollouts undo $SERVICE -n prod --to-revision=$REVISION
  fi
  kubectl argo rollouts status $SERVICE -n prod
else
  # Standard Kubernetes rollback
  echo "Using Kubernetes Deployment..."
  if [ "$REVISION" -eq 0 ]; then
    kubectl rollout undo deployment/$SERVICE -n prod
  else
    kubectl rollout undo deployment/$SERVICE -n prod --to-revision=$REVISION
  fi
  kubectl rollout status deployment/$SERVICE -n prod
fi

echo "✅ Rollback completed for $SERVICE"
