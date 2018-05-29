FROM nginx:1.13

ADD startup.sh /usr/bin/startup.sh
RUN chmod 777 /usr/bin/startup.sh

ADD target/generated-docs /usr/share/nginx/html

ENV  SERVICE_PROJECT_NAME=SkaLogs \
     SERVICE_KIBANA_ENDPOINT=http://kibana:5601 \
     SERVICE_ELASTICSEARCH_ENDPOINT@=http://elasticsearch:9200 \
     SERVICE_KAFKA_ENDPOINT=kafka:9092 \
     SERVICE_SUPPORT_EMAIL=contact@skalogs.com


ENTRYPOINT ["bash", "-c","/usr/bin/startup.sh" ]
