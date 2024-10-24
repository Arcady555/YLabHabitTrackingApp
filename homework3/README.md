# Сервис для управления автосалоном. Приложение позволяет пользователям управлять базой данных автомобилей, обрабатывать заказы клиентов на покупку и обслуживание автомобилей, а также управлять учетными записями пользователей.

## Welcome!

WEB приложение. Отправка данных через HTTP-запросы

## Используемые технологии:

* Java 17

* Maven

* TomCat

* Liquibase

### 1. Запуск приложения.
Перейдите в корень проекта(блок homework_3) через командную строку и выполните команды:

```
mvn clean install
``` 
Запустите TomCat. Поместите файл homework_3/target/cs.war  в Тomcat  в папку webapps .

Отправляйте запросы, например через PostMan. (номер порта localhost зависит от настроек Вашего TomCat!)
* Зарегистрируйтесь (http://localhost:7070/sign-up):
![image](images/1.png) Запомните ID в ответе сервера! Под ним и под паролем Вы будете заходить в систему.
* Теперь можете войти под своим ID и паролем(http://localhost:7070/sign-in):
![image](images/2.png)

Как обычный клиент, вы можете:
* Посмотреть любую машину, введя её ID, (http://localhost:7070/car?id=) + ID:
![image](images/3.png)
* Обновить данные по своей машине, введя ID вашей машины и новые данные в форме JSON (http://localhost:7070//update-car):
![image](images/4.png) 
* Удалить свой заказ на машину (http://localhost:7070/delete-order?id=) + ID.
* Удалить свою машину из БД (http://localhost:7070/delete-car?id=) + ID.
* Создать заказ на машину(сервис - если машина Ваша, покупка - если машина ещё не Ваша) (http://localhost:7070/create-order):
* Создать карточку машины для записи в БД (http://localhost:7070/create-car):
  ![image](images/8.png)
* Есть поиск машины по интересующим Вас параметрам (http://localhost:7070/cars-with-parameters?ownerId=X&brand=X&model=X&yearOfProd=X&priceFrom=X&priceTo=X&condition=X) где вместо X впишите интересующие Вас параметры. Или пробел:
* Посмотреть список всех доступных машин (http://localhost:7070/all-cars):

Следующие запросы доступны только админу или менеджеру
* Посмотреть любой заказ (http://localhost:7070/order?id=) + ID:
![image](images/9.png)
* Найти заказы по параметрам (http://localhost:7070/orders-with-parameters?authorId=X&carId=X&type=X&status=X) где вместо X впишите интересующие Вас параметры. Или пробел:
![image](images/10.png)
* Закрыть любой заказ (http://localhost:7070/close-order?id=) + ID:
![image](images/11.png)
* Посмотреть все заказы (http://localhost:7070/all-orders):
![image](images/12.png)

Следующие запросы доступны только админу
* Посмотреть карточку любого пользователя (http://localhost:7070/user?id=) + ID:
  ![image](images/9.png)
* Найти группу пользователей по параметрам (http://localhost:7070/users-with-parameters?role=X?name=X&contactInfo=X&buysAmount=X) где вместо X впишите интересующие Вас параметры. Или пробел:
  ![image](images/10.png)
* Обновить карточку с данными пользователя (http://localhost:7070/update-user) :
  ![image](images/11.png)
* Посмотреть лог действий любого пользователя(выбрать по параметрам) (http://localhost:7070/log-with-parameters?userId=X?action=X&dateTimeFom=X&dateTimeTo=X) где вместо X впишите интересующие Вас параметры. Или пробел:
  ![image](images/12.png)
* Удалить карточку пользователя из БД (http://localhost:7070/delete-user?id=) + ID:
  ![image](images/12.png)
* Создать карточку пользователя (http://localhost:7070/create-user):
  ![image](images/12.png)
* Посмотреть список всех пользователей (http://localhost:7070/all-users):
  ![image](images/12.png)


## Have a good job!
