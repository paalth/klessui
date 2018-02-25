cd klessui

echo "Running Maven build"
mvn install

cd ../build

IMGNAME=klessv1/klessui

TAG=$IMGNAME:$BUILD_ID

if [[ ! -z "$KLESS_DEST_REGISTRY" ]]; then
  TAG=$KLESS_DEST_REGISTRY/$TAG
fi

echo "Building image with tag $TAG"

cp ../klessui/target/klessui-1.0-SNAPSHOT-shaded.jar klessui.jar

echo "Logging into docker registry $KLESS_DEST_REGISTRY"
sudo docker login -u $KLESS_DEST_REGISTRY_USERNAME -p "$KLESS_DEST_REGISTRY_PASSWORD" $KLESS_DEST_REGISTRY
sudo docker build -t $TAG .
sudo docker push $TAG

rm klessui.jar

cd ..
