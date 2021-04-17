FROM adoptopenjdk/openjdk11:latest
COPY . /home/gradle/src
WORKDIR /home/gradle/src
EXPOSE 8080
ENTRYPOINT /bin/sh ./gradlew bootRun

