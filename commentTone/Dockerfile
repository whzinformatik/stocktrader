FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
COPY wait-for-it.sh /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY --from=build /home/app/target/commentTone-*-jar-with-dependencies.jar commentTone.jar
COPY --from=build /home/app/wait-for-it.sh wait-for-it.sh
RUN apk add --no-cache bash && chmod 755 wait-for-it.sh
CMD ./wait-for-it.sh rabbitmq:5672 -t 20 -- java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar commentTone.jar