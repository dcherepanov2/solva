FROM openjdk:17-alpine

# Установка рабочей директории
WORKDIR /app

# Копирование JAR файла в образ
COPY target/solva.jar /app/

# Команда для запуска приложения
CMD ["java", "-jar", "solva.jar"]
