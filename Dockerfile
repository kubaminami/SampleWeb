FROM gradle:8.14-jdk17

WORKDIR /app

COPY . .

RUN gradle build

CMD ["java", "-jar", "build/libs/*.jar"]