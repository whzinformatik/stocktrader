FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY target/stock-quote-*-jar-with-dependencies.jar stock-quote.jar
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar stock-quote.jar