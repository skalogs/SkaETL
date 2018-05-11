FROM nginx:1.13

ADD startup.sh /usr/bin/startup.sh
RUN chmod 777 /usr/bin/startup.sh

ADD target/generated-docs /usr/share/nginx/html

ENTRYPOINT ["bash", "-c","/usr/bin/startup.sh" ]
