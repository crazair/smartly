package ru.sbtqa.smartly.pageobjects;

import io.qameta.allure.Step;

import ru.sbtqa.smartly.pageobjects.base.BasePage;
import ru.sbtqa.smartly.pageobjects.base.PageObject;

import static com.codeborne.selenide.Selenide.$x;
import static ru.sbtqa.smartly.common.utils.PropertyUtils.getProperty;

@PageObject("Страница \"Клиенты\"")
public class ClientsPage extends BasePage {

    @Step("На вкладке \"Клиенты\" находим и выбираем клиента по ИНН <{0}>")
    public void findAndSelectClient(String inn) {
        setValueByName("ИНН", inn);
        clickLinkOrButtonByName("Применить");
        selectInTable(inn);
    }

    @Step("В карточке клиента кликаем ссылку <{0}>")
    public void clickLinkInCustomerCard(String linkName) {
        String xPathExpression = "//div[contains(@class,'nested-form')]//*[text()='" + linkName + "']";
        LOG.info("Кликаем по ссылке: " + linkName + ". xpath: " + xPathExpression);
        $x(xPathExpression).click();
    }

    @Step("Создаём продукт")
    public void createProduct() {
        selectFromDropBox("Искать по","ИНН");
        findAndSelectClient(getProperty("clientINN"));
        clickLinkOrButtonByName("Создать продукт");
        LOG.info("Ищем клиента, выбираем его по ИНН и нажимаем кнопку создать продукт");
    }
}