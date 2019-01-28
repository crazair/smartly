package ru.sbtqa.smartly.pageobjects;

import io.qameta.allure.Step;
import ru.sbtqa.smartly.pageobjects.base.BasePage;
import ru.sbtqa.smartly.pageobjects.base.PageObject;

@PageObject("Выбор шаблона для создания продукта")
public class ProductCreationTemplatePage extends BasePage {

    @Step("На вкладке \"Выбор шаблона для создания продукта\" вводим значение полей")
    public void fillFieldsNewProduct(String type, String mode, String regulation, String productTemplate) {
        selectFromDropBox("Тип продукта", type);
        selectFromDropBox("Режим кредитования (необязательно)", mode);
        clickLinkOrButtonBySiblingName("Регламент (необязательно)");
        waitSpinner();
        clickLinkOrButtonByName(regulation);
        clickLinkOrButtonByName("Выбрать");
        clickLinkOrButtonBySiblingName("Шаблон продукта");
        waitSpinner();
        clickLinkOrButtonByName(productTemplate);
        clickLinkOrButtonByName("Выбрать");
    }


    @Step("На вкладке \"Выбор шаблона для создания продукта\" вводим значение полей")
    public void fillFieldsNewProduct(String type, String mode, String productTemplate) {
        selectFromDropBox("Тип продукта", type);
        selectFromDropBox("Режим кредитования (необязательно)", mode);
        clickLinkOrButtonBySiblingName("Шаблон продукта");
        waitSpinner();
        clickLinkOrButtonByName(productTemplate);
        clickLinkOrButtonByName("Выбрать");
    }

}