FROM adoptopenjdk/openjdk22:alpine

RUN apk --no-cache add postgresql-client

COPY target/JokiBackend-0.0.1-SNAPSHOT.jar /app/app.jar

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/jokidb
ENV SPRING_DATASOURCE_USERNAME=jokiuser
ENV SPRING_DATASOURCE_PASSWORD=jokiuser

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]