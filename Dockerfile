FROM openjdk:8-jre

ADD target/*.jar /inxupt.jar

ENTRYPOINT ["java", "-jar","/inxupt.jar"]