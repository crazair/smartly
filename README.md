# smartly
#### Фреймворк для автоматизированного тестирования веб-приложений

###**Selenide && ContextPageObject**

    _JUnit 5_     - библиотека запуска и работы с автотестами на языке Java
    _Selenide 4_  - фреймворк-обёртка вокруг Selenium WebDriver
    _Allure 2_    - фреймворк для получения отчётов о прохождении автотестов
    _Нamcrest 2_  - библиотека матчеров

### Запуск UI автотестов

        mvn clean test site


##### Структура модулей и пакетов:

            test/java
                ru.sbtqa.smartly.common              - пакет общих настроек для проекта, Before и After методы, управление WebDriver`ом
                ru.sbtqa.smartly.common.elements     - пакет для различных описательных объектов с веб страниц (Например: таблиц)
                ru.sbtqa.smartly.common.lifecycle    - пакет классов с настройкой жизненного цикла тестов
                ru.sbtqa.smartly.common.matchers     - пакет классов сравнения/сопоставления, расширение hamcrest
                ru.sbtqa.smartly.common.utils        - пакет утилитных классов, используемых в проекте, прежде всего для тестовых данных

                ru.sbtqa.smartly.data                - пакет c тестовыми данными, константами
                ru.sbtqa.smartly.datacreators        - пакет классов для создания тестовых данных (Например: продуктов)
                ru.sbtqa.smartly.dataobjects         - пакет с классами данных, которые используются в тестах

                ru.sbtqa.smartly.pageobjects         - пакет с описанием элементов/объектов на страницах проекта (PageObjects или PageObjectsContext)
                ru.sbtqa.smartly.pageobjects.base    - пакет базовых методов на страницах проекта, низкоуровневые методы для описания тестов (кликеры, сетеры, гетеры)
                ru.sbtqa.smartly.testsuites          - тестовые наборы (параллельность ощсуществляется по ним)

##### Настройка "app.properties" в корне головного проекта:

| Ключ                    |Описание                                 | Обязательность
| ------------------------|-----------------------------------------|----------------
| url                     | ссылка на стенд                         | Да (Для UI тестов)
| login                   | логин пользователя                      | Да (Для UI тестов)
| pass                    | пароль пользователя                     | Да (Для UI тестов)
| webdriver.drivers.path  | путь к вебдрайверу                      | Да (Для UI тестов)
| selenide.browser        | браузер                                 | Да (Для UI тестов)
| timeoutMilliseconds     | таймаут для спинера                     | Да (Для UI тестов)
| clientINN               | ИНН тестового клиента                   | Да (Для UI тестов)

Пример "ключ=значение":

    url=http://l4b.ovsat.ca.sbrf.ru/azimuth/resource/loans-for-business/index-new.html
    login=super_user
    pass=super_user1
    webdriver.drivers.path=src/test/resources/webdrivers/chromedriver-0.0.1.exe
    selenide.browser=chrome
    timeoutMilliseconds=60000
    clientINN=7708754456

##### Многопоточный (параллельный) запуск тестов с помощью Selenoid (RemoteWebDriver)

Чтобы для каждого запроса нового браузера (в нашем случае - для каждого сьюта) запускался отдельный контейнер
необходимо в файле app.properties указать selenide.browser=ru.sbtqa.smartly.common.CustomWebDriverProvider
Максимальное количество потоков указывается ключом -DforkCount=<кол-во потоков>
