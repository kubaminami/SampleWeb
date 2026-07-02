FROM gradle:8.14-jdk17

WORKDIR /app

COPY . .

RUN gradle build -x test

CMD sh -c "java -jar build/libs/*.jar"