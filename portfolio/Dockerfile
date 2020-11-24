FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY target/portfolio-*.jar portfolio.jar
EXPOSE 18082
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar portfolio.jar