FROM maven:3.9.3-eclipse-temurin-20-alpine as build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD pom.xml $HOME
RUN mvn verify --fail-never
ADD . $HOME
RUN mvn package -DskipTests

FROM openjdk:19-alpine
COPY --from=build /usr/app/target/iris-0.0.1-SNAPSHOT.jar /app/runner.jar
ENTRYPOINT ["java","-jar","/app/runner.jar"]

# AS <NAME> to name this stage as maven
# FROM maven:3.9.3 AS maven

# WORKDIR /usr/src/app
# COPY . /usr/src/app
# # Compile and package the application to an executable JAR
# WORKDIR /usr/src/app/iris
# RUN mvn package -DskipTests

# # For Java 11, 
# FROM openjdk:19-alpine

# ARG JAR_FILE=iris-0.0.1-SNAPSHOT.jar

# WORKDIR /opt/app

# # Copy the spring-boot-api-tutorial.jar from the maven stage to the /opt/app directory of the current stage.
# COPY --from=maven /usr/src/app/iris/target/${JAR_FILE} /opt/app/

# ENTRYPOINT ["java","-jar","iris-0.0.1-SNAPSHOT.jar"]