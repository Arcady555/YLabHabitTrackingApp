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
docker exec -it db_habit_tracker psql -U role_arcady postgres
```
после чего:
```
CREATE DATABASE y_lab_habit_tracker;
exit 
```

В последующем, если вам придётся заново стартовать этот контейнер, выполните в командной строке следующие манипуляции:
![image](images/containerRestart.png)


Перейдите в корень проекта(блок homework3.separatedrun3) через командную строку:
```
cd /home/......../IdeaProjects/YLabHabitTrackingApp/homework3/separatedrun3
``` 
и выполните команды:
```
mvn clean install
java Main
```
Вы запустили liquibase, создали таблицы

Пройдите в соседний блок:

```
cd /home/......../IdeaProjects/YLabHabitTrackingApp/homework3/tomcatrun3
```

Запустите TomCat. Например в консоли:
```
sudo systemctl start tomcat
```
Поместите файл homework3/tomcatrun3/target/ht.war  в Тomcat  в папку webapps .



### Работа с приложением
Отправляйте запросы, например через PostMan. (номер порта localhost зависит от настроек Вашего TomCat!)
* Зарегистрируйтесь (http://localhost:8080/sign-up):
![image](images/1.png) Запомните ID в ответе сервера! Под ним и под паролем Вы будете заходить в систему.
* Теперь можете войти под своим ID и паролем(http://localhost:7070/sign-in)(Сейчас реализована БАЗОВАЯ авторизация):
![image](images/2.png)

Как обычный клиент, вы можете:


Следующие запросы доступны только админу
* Посмотреть карточку любого пользователя (http://localhost:8080/user?id=) + ID:
  ![image](images/9.png)
* Найти группу пользователей по параметрам () где вместо X впишите интересующие Вас параметры. Или пробел:
  ![image](images/10.png)
* Обновить карточку с данными пользователя (http://localhost:8080/update-user) :
  ![image](images/11.png)

* Удалить карточку пользователя из БД (http://localhost:8080/delete-user?id=) + ID:
  ![image](images/12.png)

* Посмотреть список всех пользователей (http://localhost:8080/all-users):
  ![image](images/12.png)


## Have a good job!
