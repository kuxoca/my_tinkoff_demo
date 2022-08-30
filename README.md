# my Tinkoff Demo
### Демонстративный проект на Spring Boot.<br/>
В проекте использовались:
+ JPA
+ JPQL
+ MySQL
+ Log4j2
+ Lombok

### !!! Это НЕофициальный сервис.
Сервис производит сбор курсов валют в базу данных с API Tinkoff банка. </br>
Далее будет добавлены сервисы:
+ формирования графиков
+ телеграмм бот
### Настройки
Базовые пары валют сбора данных: <i>USD:RUB,EUR:RUB,GBP:RUB</i> </br>
Для добавления пар валют необходимо в файле application.properties через запятую (без пробелов) добавить пары (пример: CURRENCY="BYN:RUB,KZT:RUB") </br>
Базовая категория сбора данных: <i>DebitCardsOperations </i> </br>
Для добавления категории необходимо в файле application.properties через запятую (без пробелов) добавить категории (пример: CATEGORY="DepositPayments,C2CTransfers") </br>
### Официальная страница банка в интернете: <link>https://www.tinkoff.ru/ </link> <br/>