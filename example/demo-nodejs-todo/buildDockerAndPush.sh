#!/bin/bash
# fail fast
set -e

#if no version is set let's use the environment name
if [ -z ${image_version} ]; then
    image_version=1.0.0-SNAPSHOT
fi

rm -rf node-todo/node_modules

echo skalogs/demo-node-todo:${image_version} builed !
time docker build -t skalogs/demo-node-todo:${image_version} .

echo push skalogs/demo-node-todo:${image_version}
docker push skalogs/demo-node-todo:${image_version}

echo skalogs/demo-node-todo:${image_version} is pushed
