FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar logopedia.jar
ENTRYPOINT ["java","-jar","/logopedia.jar"]
EXPOSE 8080