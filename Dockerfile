FROM maven:3.9.3-amazoncorretto-20 as build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD iris/pom.xml $HOME
RUN mvn verify --fail-never
ADD ./iris $HOME
RUN mvn package -DskipTests

FROM openjdk:22-jdk-slim
RUN apt-get update && apt-get install -y curl
COPY --from=build /usr/app/src/main/resources/ /app/resources/
RUN chmod +x /app/resources/scripts/*
COPY --from=build /usr/app/target/iris-*.jar /app/runner.jar
WORKDIR /app
RUN mkdir -p logs files
ENTRYPOINT ["java","-jar","./runner.jar"]
