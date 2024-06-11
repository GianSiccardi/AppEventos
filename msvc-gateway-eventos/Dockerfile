FROM openjdk:17-jdk-alpine as builder

WORKDIR /app/msvc-gateway-eventos

COPY ./pom.xml  /app
COPY ./msvc-gateway-eventos/.mvn ./.mvn
COPY ./msvc-gateway-eventos/mvnw .
COPY ./msvc-gateway-eventos/pom.xml .


#RUN ./mvn clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

RUN ./mvnw dependency:go-offline
COPY ./msvc-gateway-eventos/src ./src

RUN ./mvnw  clean package -DskipTests


EXPOSE 8090
ENTRYPOINT ["java", "-jar" ,"./target/msvc-gateway-eventos-0.0.1-SNAPSHOT.jar"]