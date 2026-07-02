FROM gradle:8.14-jdk17

WORKDIR /app

COPY . .

RUN gradle build -x test

CMD ["java","-jar","build/libs/SampleWeb-0.0.1-SNAPSHOT.jar"]