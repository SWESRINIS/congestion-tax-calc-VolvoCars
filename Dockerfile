FROM openjdk:19
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY target/*.jar app.jar
EXPOSE 8000
CMD ["java", "-jar", "app.jar"]