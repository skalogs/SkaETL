#!/bin/bash
# fail fast
set -e

cd "$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

#if no version is set let's use the environment name
if [ -z ${image_version} ]; then
    image_version=$(mvn -q -Dexec.executable='echo' -Dexec.args='${project.version}' --non-recursive exec:exec)
fi

 time mvn clean package
 echo push skalogs/demo-petclinic:${image_version}
 docker push skalogs/demo-petclinic:${image_version}
 echo skalogs/demo-petclinic:${image_version} is pushed
