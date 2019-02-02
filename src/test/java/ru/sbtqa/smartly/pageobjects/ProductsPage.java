package ru.sbtqa.smartly.pageobjects;

import com.codeborne.selenide.SelenideElement;
import ru.sbtqa.smartly.pageobjects.base.BasePage;
import io.qameta.allure.Step;
import ru.sbtqa.smartly.pageobjects.base.PageObject;

import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.By.xpath;

@PageObject("Страница со списком продуктов")
public class ProductsPage extends BasePage {

    private SelenideElement table = $x("//div[contains(@class, 'table table_simple')]");

    @Step("Выбираем продукт по номеру договора <{0}>")
    public void selectProductByAgrNum(String agreementNumber) {
        setValueByName("Номер договора", agreementNumber);
        clickLinkOrButtonByName("Применить");
        selectInTable(agreementNumber);
    }

    @Step("На вкладке \"Продукты\" в фильтрах вводим Номер договора {0} в <Фильтры>")
    public void setAgreementNumber(String value) {
        setValueByName("Номер договора", value);
    }

    @Step("Получаю кол-во найденных продуктов из списка на странице Продуктов(без пагинации)")
    public int getCountProductsFromTable() {
        return table.findAll(xpath("//tbody//tr")).size();
    }

    @Step("Получаю текст первого продукта из списка на странице Продуктов")
    public String getFirstProductFromTableText() {
        return table.find(xpath("//tbody//tr")).getText();
    }

    @Step("Выбираем продукт по номеру договора")
    public SelenideElement selectProductBySrcId(String srcId) {
        waitSpinner();
        setValueByName("ID ЕКС", srcId);
        clickLinkOrButtonByName("Применить");
        return getTableRowByValueIntoColumn("ID ЕКС", srcId);
    }
}