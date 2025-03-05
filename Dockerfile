FROM maven:3-amazoncorretto-23-alpine

COPY . .

RUN mvn clean package -Dmaven.test.skip

ENTRYPOINT ["java", "-jar", "./target/auth-1.0.jar", "--spring.profiles.active=docker"]
