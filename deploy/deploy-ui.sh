#!/bin/bash

if [[ ! -z "$KLESS_DEST_REGISTRY" ]]; then
  sed -e "s/KLESS_NAMESPACE/$KLESS_NAMESPACE/g" -e "s/KLESS_DEST_REGISTRY_HOSTPORT/$KLESS_DEST_REGISTRY\//g" -e "s/KLESS_DEST_REGISTRY_USERNAME/$KLESS_DEST_REGISTRY_USERNAME/g" -e "s/KLESS_DEST_REGISTRY_PASSWORD/$KLESS_DEST_REGISTRY_PASSWORD/g" -e "s/BUILD_ID/$BUILD_ID/g" deploy/kless-ui.yaml > /tmp/kless-ui.yaml
else
  sed -e "s/KLESS_NAMESPACE/$KLESS_NAMESPACE/g" -e "s/KLESS_DEST_REGISTRY_HOSTPORT//g" -e "s/KLESS_DEST_REGISTRY_USERNAME/$KLESS_DEST_REGISTRY_USERNAME/g" -e "s/KLESS_DEST_REGISTRY_PASSWORD/$KLESS_DEST_REGISTRY_PASSWORD/g" -e "s/BUILD_ID/$BUILD_ID/g" deploy/kless-ui.yaml > /tmp/kless-ui.yaml
fi

kubectl create -f /tmp/kless-ui.yaml
rm /tmp/kless-ui.yaml
kubectl describe svc kless-ui -n ${KLESS_NAMESPACE}
