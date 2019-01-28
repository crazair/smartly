package ru.sbtqa.smartly.pageobjects;

import com.codeborne.selenide.SelenideElement;
import ru.sbtqa.smartly.pageobjects.base.BasePage;
import io.qameta.allure.Step;
import ru.sbtqa.smartly.pageobjects.base.PageObject;

import static com.codeborne.selenide.Selenide.*;

@PageObject("Реквизиты для перечисления средств - редактирование счета")
public class ExtraditionPage extends BasePage {

    private SelenideElement checkboxWork = $x("//div[contains(@class,'checkable')][text()='Работает']");

    @Step("Устанавливаем чекбокс <Работает> в положение <{0}>")
    public void checkboxWorkClick(boolean checked){
        if (isChecked("Работает") != checked) checkboxWork.click();
    }

    @Step("Заполняем информацию по номеру счета. Другой банк")
    public void addAccountOtherBank(String accountNumber, String bik, String name) {
        clickLinkOrButtonByName("Добавить счет");
        clickLinkOrButtonByName("Другой банк");
        setValueByName("Номер счета", accountNumber);
        clickLinkOrButtonByName("Работает");
        setValueByName("БИК", bik);
        setValueByName("Наименование", name);
        clickLinkOrButtonByName("Сохранить");
    }

    @Step("Заполняем информацию по номеру счета")
    public void addAccount(String accountNumber, String bik, String name) {
        clickLinkOrButtonByName("Добавить счет");
        setValueByName("Номер счета", accountNumber);
        clickLinkOrButtonByName("Работает");
        setValueByName("БИК", bik);
        setValueByName("Наименование", name);
        clickLinkOrButtonByName("Сохранить");
    }
}

