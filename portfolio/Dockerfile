FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
COPY wait-for-it.sh /home/app
RUN mvn -DskipTests -f /home/app/pom.xml clean package -P prod

FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY --from=build /home/app/target/portfolio.jar portfolio.jar
COPY --from=build /home/app/wait-for-it.sh wait-for-it.sh
EXPOSE 18082
RUN apk add --no-cache bash && chmod 755 wait-for-it.sh
CMD ./wait-for-it.sh rabbitmq:5672 -t 20 && ./wait-for-it.sh account:18081 -t 20 -- java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar portfolio.jar