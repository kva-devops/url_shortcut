# Приложение ShortcutURL
[![Build Status](https://app.travis-ci.com/kva-devops/url_shortcut.svg?branch=master)](https://app.travis-ci.com/kva-devops/url_shortcut)
[![codecov](https://codecov.io/gh/kva-devops/url_shortcut/branch/master/graph/badge.svg?token=j5QTwb0y1k)](https://codecov.io/gh/kva-devops/url_shortcut)

## О проекте.
Приложение реализует функционал сервиса по сокращению ссылок.
 
## Сборка.
JDK14, Maven, Spring Boot, PostgreSQL, Liquibase, JWT

## Запуск через Docker Compose
1. Создать директорию на сервере и скопировать файлы репозитория
2. Перейти в созданную директорию (корень проекта) и собрать приложение командой: "mvn install"
3. Собирать docker-образ приложения командой: "docker build -t shortcut ."
4. При необходимости отредактировать порты, используемые приложением в файле docker-compose.yml
5. Запустить приложение командой: "docker-compose up"

## Запуск в кластере K8s
Файлы конфигурации *.yml находятся в корне проекта, в директории k8s
1. Создаем Secret: "kubectl apply -f postgresdb-secret.yml"
2. Создаем ConfigMap: "kubectl apply -f postgresdb-configmap.yml"
3. Создаем Deployment для БД: "kubectl apply -f postgresdb-deployment.yml"
4. Создаем Deployment для Spring Boot: "kubectl apply -f spring-deployment.yml"


## Как пользоваться.
После старта приложения необходимо зарегистрироваться в нем, либо ввести свои логин и пароль.
Регистрация производится по названию сайта, например: "mysite.ru".

![registration](images/Selection_147.png)

После регистрации для пользователя генерируются логин и пароль для доступа с систему. 
Их необходимо сохранить для дальнейшего входа в систему. 
Флаг "registered" означает, был ли пользователь ранее зарегистрирован в системе.

После входа в систему пользователю присваивается уникальный токен,
т.к. приложение использует JWT аутентификацию и авторизацию.

![login](images/Selection_148.png)

Для получения сокращенной ссылки необходимо выполнить запрос, передав в нем 
исходную ссылку. Поле "siteId" - это уникальный идентификатор пользователя, 
который присваивается ему при регистрации. 
Глубина ссылки не имеет значения. Результат будет отправлен в теле ответа.

![shortcut](images/Selection_149.png)  

Важно учитывать, что корневой адрес исходной ссылки должен быть таким же
как название сайта указанное при регистрации, например:

    адрес, указанный при регистрации - site.ru
    адрес, который можно использовать - site.ru/***
    адрес, который нельзя использовать - some-address.ru/***

Если воспользоваться полученной ссылкой, произойдет перенаправление по исходному url адресу

![redirect](images/Selection_150.png)

Приложение ведет статистику по количеству обращений к каждой хранящейся ссылке.
Для получения статистики необходимо выполнить соответствующий запрос.

![stat](images/Selection_151.png)
  
## Контакты.
Кутявин Владимир Анатольевич

skype: tribuna87

email: tribuna87@mail.ru

telegram: @kutiavinvladimir