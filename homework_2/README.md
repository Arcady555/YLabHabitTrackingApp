# Приложение для отслеживания привычек. Позволяет пользователям регистрироваться, создавать, управлять своими привычками, отслеживать их выполнение и анализировать прогресс

## Welcome!

Консольное приложение.

## Используемые технологии:

* Java 17

* Maven

* Liquibase

* Docker

### Запуск приложения с Maven
Перейдите в корень проекта через командную строку:
```
cd /home/......../IdeaProjects/YLabHabitTrackingApp
``` 
и выполните команду:
```
docker-compose up -d
```
Вы запустили контейнер для Базы данных.В той же директории выполните команду:
```
docker exec -it db_car_shop psql -U role_arcady postgres
```
после чего:
```
CREATE DATABASE y_lab_habit_tracker;
exit 
```

В последующем, если вам придётся заново стартовать этот контейнер, выполните в командной строке следующие манипуляции:
![image](images/containerRestart.png)

Перейдите в корень проекта(блок homework_2) через командную строку:
```
cd /home/......../IdeaProjects/YLabHabitTrackingApp/homework_2
``` 
и выполните команды:
```
mvn clean install
java Main
```



## Have a good job!
