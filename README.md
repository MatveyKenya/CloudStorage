Дипломная работа “Облачное хранилище”
---
---
###Описание проекта

Разработать приложение - REST-сервис.

Сервис должен предоставить REST интерфейс для возможности загрузки файлов и вывода списка уже загруженных файлов пользователя. Все запросы к сервису должны быть авторизованы. Заранее подготовленное веб-приложение (FRONT) должно подключаться к разработанному сервису без доработок, а также использовать функционал FRONT для авторизации, загрузки и вывода списка файлов пользователя.

---

База данных Postgres используется для хранения
1. данных авторизации пользователя (лог + пасс)
2. Полных имен файлов с привязкой к логинам

имена файлов уникальны по паре id_user + full filename

сами файлы хранятся по пути
upload.path=D:/fileStorage (в частном случае на локальном диске)
