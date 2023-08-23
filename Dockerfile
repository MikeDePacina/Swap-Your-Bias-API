FROM openjdk:17-alpine
COPY target/Swap-Your-Bias-API-0.0.1-SNAPSHOT.jar Swap-Your-Bias-API-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","Swap-Your-Bias-API-0.0.1-SNAPSHOT.jar"]