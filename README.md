Solva тестовое задание
---


### Предварительные шаги до запуска сервиса или тестов этого сервиса:
---

1) Установка Java 17.0.7 Amazon Corretto, можно скачать по следующей ссылке: https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html
2) Далее вам нужно установить Postgresql 13, можно скачать по следующей ссылке: https://www.postgresql.org/download/
3) Далее установить Apache Maven 3.9.4, скачать можно по следующей ссылке: https://maven.apache.org/download.cgi
4) Скачайте мой сервис или же сделайте git clone, ссылка: https://github.com/dcherepanov2/solva
5) Создайте 2 базы данных, если они еще не созданы с помощью скрипта: CREATE DATABASE название_базы; CREATE DATABASE название_базы_для_тестов;
6) Создайте схему с именем public, если она еще не создана с помощью скрипта для двух ранее созданных баз данных: CREATE SCHEMA название_схемы;

### Запуск сервиса:
---

1) spring.datasource.url* - замените значение этой настройки на свою ссылку с работающей базой данных
2) spring.datasource.username* - поставьте свой логин от постгреса, важно, чтобы у пользователя был полный доступ
3) spring.datasource.password* - поставьте свой пароль от ранее созданного пользователя с полными правами
4) twelvedata.api.access.key** - замените значение этой настройки на свой ключ API tweldata
5) Далее нужно собрать сервис c помощью команды:  mvn clean package -D skipTests
6) Далее нужно запустить сервис с помощью команды: java -jar tz.expense.tracker.api-0.0.1-SNAPSHOT.jar
   
### Для работы тестов:
---

1) spring.datasource.url*** - замените значение этой настройки на свою ссылку с работающей базой данных,
   ВАЖНО: база данных должна отличаться от базы созданной на первом шаге запуске сервера 
2) spring.datasource.username*** - поставьте свой логин от постгреса, важно, чтобы у пользователя был полный доступ
3) spring.datasource.password*** - поставьте свой пароль от ранее созданного пользователя с полными правами
4) twelvedata.api.access.key*** - замените значение этой настройки на свой ключ API tweldata
5) Раскоментировать следующую строчку <!--    <include file="/v.1.0/test/db.changelog-master-test.xml" relativeToChangelogFile="true"/>-->
   в файле по пути: db/changelog/db.changelog-master.xml
   ВАЖНО: при запуске сервера эту строчку нужно снова закоментировать, так как там содержаться мок данные для работы тестов, которые могут накатиться на прод
6) Запустить тесты с помощью команды:  mvn clean test

### Запуск через docker-compose:
---

1) Выполнить все шаги из части инструкции "Запуск сервиса"
2) Установить docker и docker-compose на машину
3) Перейти в папку с проектом cd {каталог где находиться проект}\tz.expense.tracker.api
4) Запустить сборку через докер с помощью команды: docker-compose up -d
   ВАЖНО: чтобы порты 8080 и 5432 были свободны, так как на них будут работать базы данных и сервис

* - заменить свойства нужно в файле по следующему пути: src/main/resources/application.properties
  
** - получить API ключ tweldata можно по следующей ссылке: https://twelvedata.com/

*** - заменить свойства нужно в файле по следующему пути: src/test/resources/application-test.properties
