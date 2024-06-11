FROM openjdk:17-jdk-alpine as builder

WORKDIR /app/msvc-eventos

COPY ./pom.xml  /app
COPY ./msvc-eventos/.mvn ./.mvn
COPY ./msvc-eventos/mvnw .
COPY ./msvc-eventos/pom.xml .

RUN ./mvnw dependency:go-offline
COPY ./msvc-eventos/src ./src

RUN ./mvnw clean package -DskipTests

EXPOSE 8002
ENTRYPOINT ["java", "-jar" ,"./target/msvc-eventos-0.0.1-SNAPSHOT.jar"]
