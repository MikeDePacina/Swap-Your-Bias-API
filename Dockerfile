FROM openjdk:17
ADD target/swap-your-bias-api-dockerize.jar swap-your-bias-api-dockerize.jar
ENTRYPOINT ["java","-jar","/swap-your-bias-api-dockerize.jar"]
