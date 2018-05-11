FROM openjdk:8u151-jdk

ADD startup.sh /usr/bin/startup.sh

CMD ["/usr/bin/startup.sh"]

# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/skalogs/metric-importer.jar
