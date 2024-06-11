FROM openjdk:17-jdk-alpine as builder

WORKDIR /app/auth-eventos

COPY ./pom.xml  /app
COPY ./auth-eventos/.mvn ./.mvn
COPY ./auth-eventos/mvnw .
COPY ./auth-eventos/pom.xml .

RUN ./mvnw dependency:go-offline
COPY ./auth-eventos/src ./src

RUN ./mvnw clean package -DskipTests



EXPOSE 9000

ENTRYPOINT ["java", "-jar", "./target/auth-eventos-0.0.1-SNAPSHOT.jar"]
