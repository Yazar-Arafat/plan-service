FROM openjdk:latest
VOLUME [ "/tmp" ]
ADD target/plan-service-1.0.jar plan-service.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "plan-service.jar"]