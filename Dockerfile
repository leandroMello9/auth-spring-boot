FROM maven:3.9.6-sapmachine-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install
RUN ls

FROM openjdk:17 AS deploy
WORKDIR /app
COPY --from=build /app/target/*jar ./app.jar
RUN ls
EXPOSE 8080
CMD [ "java", "-jar", "app.jar" ]
