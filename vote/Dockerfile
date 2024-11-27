FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/vote-0.0.1-SNAPSHOT.jar VoteService.jar
ENTRYPOINT ["java", "-jar", "VoteService.jar"]