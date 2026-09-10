FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY src/Main.java .
RUN javac Main.java

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/Main.class .

EXPOSE 8080

CMD ["java", "Main"]