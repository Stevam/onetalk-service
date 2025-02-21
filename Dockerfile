FROM registry.access.redhat.com/ubi8/openjdk-21:1.20

WORKDIR /app

COPY target/quarkus-app /app/

CMD ["java", "-jar", "/app/quarkus-run.jar"]
