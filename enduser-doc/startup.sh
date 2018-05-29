#!/bin/bash

cd /usr/share/nginx/html/

for file in $(find /usr/share/nginx/html/ -name '*.html')
do
   #echo $file
   sed -i "s/SERVICE_PROJECT_NAME/${SERVICE_PROJECT_NAME}/g" $file
   sed -i "s@SERVICE_KIBANA_ENDPOINT@${SERVICE_KIBANA_ENDPOINT}@g" $file
   sed -i "s@SERVICE_ELASTICSEARCH_ENDPOINT@${SERVICE_ELASTICSEARCH_ENDPOINT}@g" $file
   sed -i "s@SERVICE_KAFKA_ENDPOINT@${SERVICE_KAFKA_ENDPOINT}@g" $file
   sed -i "s/SERVICE_SUPPORT_EMAIL/${SERVICE_SUPPORT_EMAIL}/g" $file
done

chown -R nginx:nginx /usr/share/nginx/
#folder needs to be executable otherwise nginx is not able to do stats on the files contained in the folder resulting in permission denied
find /usr/share/nginx -type d -exec chmod 750 {} \;
nginx -g 'daemon off;'