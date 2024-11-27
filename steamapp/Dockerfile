FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/steamapp-0.0.1-SNAPSHOT.jar SteamAppService.jar
ENTRYPOINT ["java", "-jar", "SteamAppService.jar"]