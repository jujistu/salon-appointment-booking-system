#!/bin/bash
set -e

BACKUP_NAME=$1
NAMESPACE=${2:-prod}

if [ -z "$BACKUP_NAME" ]; then
  echo "Available backups:"
  velero backup get
  echo ""
  echo "Usage: ./restore.sh <backup-name> [namespace]"
  echo "Example: ./restore.sh salon-backup-20240206-020000 prod"
  exit 1
fi

echo "🔄 Restoring from backup: $BACKUP_NAME"
echo "Target namespace: $NAMESPACE"

# Create restore
velero restore create ${BACKUP_NAME}-restore \
  --from-backup $BACKUP_NAME \
  --include-namespaces $NAMESPACE \
  --wait

# Check restore status
velero restore describe ${BACKUP_NAME}-restore

echo "✅ Restore completed"
