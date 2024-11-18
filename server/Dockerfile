FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . .
RUN javac chat/*.java
CMD ["java", "chat.MultiClientServerRunner"]
