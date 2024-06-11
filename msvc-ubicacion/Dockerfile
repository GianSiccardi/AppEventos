FROM openjdk:17-jdk-alpine as builder

WORKDIR /app/msvc-ubicacion

COPY ./pom.xml  /app
COPY ./msvc-ubicacion/.mvn ./.mvn
COPY ./msvc-ubicacion/mvnw .
COPY ./msvc-ubicacion/pom.xml .

RUN ./mvnw dependency:go-offline
COPY ./msvc-ubicacion/src ./src

RUN ./mvnw clean package -DskipTests

EXPOSE 8003
ENTRYPOINT ["java", "-jar" ,"./target/msvc-ubicacion-0.0.1-SNAPSHOT.jar"]
