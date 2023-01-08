FROM openjdk:17-slim-bullseye

LABEL org.opencontainers.image.authors="Georgios Marmaris"

VOLUME ["/app/log"]

ENTRYPOINT ["java", "-jar", "/app/orders.jar"]

COPY target/orders-*.jar /app/orders.jar