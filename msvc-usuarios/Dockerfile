FROM openjdk:17-jdk-alpine as builder

WORKDIR /app/msvc-usuarios

COPY ./pom.xml  /app
COPY ./msvc-usuarios/.mvn ./.mvn
COPY ./msvc-usuarios/mvnw .
COPY ./msvc-usuarios/pom.xml .

RUN ./mvnw dependency:go-offline
COPY ./msvc-usuarios/src ./src

RUN ./mvnw clean package -DskipTests


EXPOSE 8001

ENTRYPOINT ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]
