# java-kanban
Как системы контроля версий помогают команде работать с общим кодом, так и трекеры задач позволяют эффективно организовать совместную работу над задачами. Вам предстоит написать бэкенд для такого трекера. В итоге должна получиться программа, отвечающая за формирование модели данных для этой страницы:

![](https://github.com/SemenenkoEugene/java-kanban/blob/main/kanban.jpg)
# Типы задач
Простейшим кирпичиком такой системы является задача (англ. task). У задачи есть следующие свойства:
1. **Название**, кратко описывающее суть задачи (например, «Переезд»).
2. **Описание**, в котором раскрываются детали.
3. **Уникальный идентификационный номер задачи**, по которому её можно будет найти.
4. **Статус**, отображающий её прогресс. 
Мы будем выделять следующие этапы жизни задачи:

- 4.1. **NEW** — задача только создана, но к её выполнению ещё не приступили.
- 4.2. **IN_PROGRESS** — над задачей ведётся работа.
- 4.3. **DONE** — задача выполнена.
  
Иногда для выполнения какой-нибудь масштабной задачи её лучше разбить на подзадачи (англ. subtask). Большую задачу, которая делится на подзадачи, мы будем называть эпиком (англ. epic). 
Таким образом, в нашей системе задачи могут быть трёх типов: обычные задачи, эпики и подзадачи. Для них должны выполняться следующие условия:
 - Для каждой подзадачи известно, в рамках какого эпика она выполняется.
 - Каждый эпик знает, какие подзадачи в него входят.
 - Завершение всех подзадач эпика считается завершением эпика.

Вам нужно реализовать API, где эндпоинты будут соответствовать вызовам базовых методов интерфейса TaskManager. 

![](https://github.com/SemenenkoEugene/java-kanban/blob/main/S7_33-2_1649410009.png)

Для получения данных должны быть **GET**-запросы. Для создания и изменения — **POST**-запросы. Для удаления — **DELETE**-запросы. Задачи передаются в теле запроса в формате **JSON**. Идентификатор (id) задачи следует передавать параметром запроса (через вопросительный знак). 
