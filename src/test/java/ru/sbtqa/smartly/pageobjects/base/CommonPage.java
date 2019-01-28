package ru.sbtqa.smartly.pageobjects.base;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static ru.sbtqa.smartly.common.utils.PropertyUtils.prop;

@PageObject("Класс с общими страничными методами! Не совсем Page...")
public class CommonPage extends BasePage {

    /** Авторизация с параметрами из app.properties */
    public static void login() {
            login(prop("login"), prop("pass"));
    }

    public static void login(String user, String password) {
        LOG.info("CommonPage login user=" + user + " password=" + password);
        $x("//input[@placeholder='Логин' or @name='username']").setValue(user);
        $x("//input[@type='password' or @type='PASSWORD']").setValue(password);
        $x("//*[(@class='button__text' and text() = 'Войти') or (@type='button' and @alt = 'ВХОД')]").click();
        waitSpinner();
    }

    @Step("Выход из системы")
    public void exit() {
        LOG.info("CommonPage exit");
        $x("//span[contains(text(), 'Выйти')]").click();
        sleep(500); //для ожидания завершения в СУДИРе окна LogOut'a для ИФТ
        open(prop("url"));
    }

    /**
     * Метод для получения текста всех выпадающих сообщений (notifications-wrapper)
     * Тест упадет если сообщение не соответствует, но перейдет к следующему шагу если сообщение отсутствует
     */
    @Step("Проверяем, что всплывающие сообщения содержат искомый текст")
    public void softCheckNotificationsWrapperText(String... strings) {
        String text = getNotificationsWrapperText();
        if (org.apache.commons.lang3.StringUtils.isBlank(text)) {
            LOG.info("Не получено ни одного сообщения");
            return;
        }
        assertThat(text, stringContainsInOrder(strings));
    }

    /** Метод для получения текста всей страницы */
    public String getRootText() {
        waitSpinner();
        String xpathExpression = "//*[@id='root']";
        LOG.info("Получаем текст всей страницы. xpath: " + xpathExpression);
        String text = $x(xpathExpression).getText();
        LOG.info("RootText: " + text);
        return text;
    }

    @Step("Нажимаем кнопку \"Домой\" (знак \"Сбербанка\" в левом верхнем углу)")
    public void clickHomeButton() {
        String xpathExpression = "//div[@class='dockbar__home']";
        LOG.info("Нажимаем кнопку \"Домой\". xpath: " + xpathExpression);
        $x(xpathExpression).click();
    }

    /** Метод для получения текста из шапки продукта */
    public String getBoardBarFullText() {
        String xpathExpression = "//div[contains(@class, 'boardbar_border-bottom')]";
        LOG.info("Получаем текст из шапки продукта. xpath: " + xpathExpression);
        String text = $x(xpathExpression).waitUntil(exist, TIMEOUT).getText();
        LOG.info("BoardBarFullText: " + text);
        return text;
    }

    /** Метод для принудительной загрузки справочников через UI */
    public void initDictionaries() {
        LOG.info("!!! Start initDictionaries !!!");
        // code...
        LOG.info("!!! Stop initDictionaries !!!");
    }
}
