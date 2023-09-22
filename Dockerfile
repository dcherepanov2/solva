# Используем базовый образ с Java 17 и Alpine Linux
FROM adoptopenjdk/openjdk17:alpine

# Установка рабочей директории
WORKDIR /app

# Копирование JAR файла в образ
COPY target/your-application.jar /app/

# Команда для запуска приложения
CMD ["java", "-jar", "your-application.jar"]