# Сервис для управления автосалоном. Приложение позволяет пользователям управлять базой данных автомобилей, обрабатывать заказы клиентов на покупку и обслуживание автомобилей, а также управлять учетными записями пользователей.

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

Перейдите в корень проекта(блок homework_2) через командную строку:
```
cd /home/......../IdeaProjects/YLabCarShopService/homework_2
``` 
и выполните команды:
```
mvn clean install
java Main
```



## Have a good job!
