package ru.sbtqa.smartly.pageobjects;

import ru.sbtqa.smartly.pageobjects.base.BasePage;
import io.qameta.allure.Step;
import ru.sbtqa.smartly.pageobjects.base.PageObject;

import java.util.stream.Collectors;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;

@PageObject("Сервисы")
public class ServicesPage extends BasePage {

    @Step("Получаю список сервисов из таблицы <Доступные сервисы> при нажатии на кнопку Сервисы")
    public void clickTableListFromServicesPageSide(String serviceName) {
        clickLinkOrButtonByNameInModal(serviceName);
    }

    @Step("Получаю текст записи из таблицы <Учет процентов> при выполнении сервиса <Учет процентов Исполнить>")
    public String getTableFromServicesPageSide() {
        return $x("//div[@class = 'workspace workspace_has-bars workspace_side workspace_side-right']").getText();
    }

    @Step("Закрыть диалоговое окно исполнения сервиса")
    public void closePopupWindow() {
        $x("(//*[contains(@class,'icon_img-close')])[last()]").click();
    }

    /**
     * Метод для получения текста контролей (диалоговое окно) при запуске сервиса
     */
    public String getServiceErrors() {
        String messagesXpath = "//span[contains(@class,'locked')]/ancestor::tr//div[. != '']";
        LOG.info("Количество ошибок при запуске сервиса: " + $$x(messagesXpath).size());
        String text = $$x(messagesXpath).texts().stream().collect(Collectors.joining());
        LOG.info("Текст ошибок: " + text);
        closePopupWindow();
        return text;
    }

    @Step("Получаю текст из записей таблицы <Учет процентов> при нажатии на кнопку Сервис")
    public String getTableFromServicesPageMain() {
        return $x("//tbody[@class='table__tbody']").getText();
    }

    @Step("Получаю текст из записей таблицы <Учет процентов (боковая панель)> при нажатии на кнопку Сервис")
    public String getTableFromServicesPageMainSide() {
        return $x("//div[@class = 'grid__col grid__col_span-4']").getText();
    }

    @Step("На вкладке \"Учет процентов\" вводим дату: расчета")
    public void fillFieldsDates(String vDateIssuance) {
        setValueByName("Дата расчета", vDateIssuance);
    }

    @Step("На вкладке \"Учет процентов\" вводим дату: проводки")
    public void fillFieldsDatesTransact(String vDateTransact) {
        setValueByName("Дата проводки", vDateTransact);
    }

    @Step("На вкладке \"Текущие условия\" вводим значения полей")
    public void fillFieldsAmountsPay(String numberClientDisposition, String dateClientDisposition) {
        setValueByName("Номер клиентского распоряжения", numberClientDisposition);
        setValueByName("Дата клиентского распоряжения", dateClientDisposition);
    }

    @Step("Добавляем фактическую операцию")
    public void addManualFactOperation(String factOpDate, String processName, String factOpName){
        selectInTable("Условие");
        setValueByName("Дата", factOpDate);
        clickLinkOrButtonBySiblingName("Подпроцесс");
        clickLinkOrButtonByName(processName);
        clickLinkOrButtonByNameInModal("Выбрать");
        waitSpinner();
        findAndSelectFromPageList(factOpName);
        clickLinkOrButtonByNameInModal("Выбрать шаблон");
    }

    //TODO Пока временно сделаю так, после переделаю Pylnov
    @Step("Добавляем фактическую операцию для ВКЛ")
    public void addManualFactOperationVKL(String factOpDate, String processName, String factOpName){
        selectInTable("Обязательство");
        setValueByName("Дата", factOpDate);
        clickLinkOrButtonBySiblingName("Подпроцесс");
        clickLinkOrButtonByName(processName);
        clickLinkOrButtonByNameInModal("Выбрать");
        waitSpinner();
        findAndSelectFromPageList(factOpName);
        clickLinkOrButtonByNameInModal("Выбрать шаблон");
    }

    @Step("Добавляем фактическую операцию")
    public void creditIssuance(String orderNumber , String orderDate, String orderSum){
        waitSpinner();
        setValueByName("Номер клиентского распоряжения", orderNumber);
        setValueByName("Дата клиентского распоряжения", orderDate);
        setValueByName("Сумма выдачи", orderSum);
    }

    @Step("Добавляем фактическую операцию")
    public void creditIssuance(String orderNumber , String orderDate){
        waitSpinner();
        setValueByName("Номер клиентского распоряжения", orderNumber);
        setValueByName("Дата клиентского распоряжения", orderDate);
    }

    @Step("На вкладке \"подготовка к закрытию договора\" вводим дату")
    public void fillDateCloseLoans(String vDate) {
        setValueByName("Дата закрытия договора", vDate);
    }

    @Step("На вкладке \"подготовка урегулирования лимита\" вводим дату")
    public void fillSetlementLimitDate(String vDate) {
        setValueByName("Дата расчета", vDate);
    }

    @Step("Заполнить поле Сумма")
    public void setAmountManualTrnx(String value) {
        $(byName("ffoForm.factOperations[0].operationSumm")).setValue(value);
    }

    @Step("Заполнить поле \"Фактическая дата выполнения\".")
    public void setActualExecuteDate(String date) {
        $(byName("local.conditions[0].factDateOfPerformance")).setValue(date);
    }

    @Step("Получаю текст из записей таблицы <Исполненные сервисы")
    public String getSumOfFactOperations(String fact) {
     return $x("//tr[@class='table__row']//*[text() = '" + fact + "']/parent::node()/parent::node()" +
             "/td[contains(@class, 'white-space_nowrap')]/div").getText();
    }
    /**
     * Метод для выбора значения в ячейке таблицы типа Dropbox(выпадающий список)
     * Метод работает только, если дропбокс существует в одной колонке таблицы
     *
     * @param seachValue - значение, установленное в любой ячейке той строки, в которой имеется нужный выпадающий список
     * @param fieldValue - значение, которое необходимо выбрать из списка
     */
    @Step("Выбираем значение <{1}> в ячейке таблицы типа Dropbox строка которой находится по значению <{0}>")
    public void selectFromTableDropBox(String seachValue, String fieldValue) {
        String xpathField = "//td//div[contains(text(),'" + seachValue + "')]" +
                "/../..//td[contains(@class, 'table__cell_editable')and //div[@class='selectbox']]";
        String xpathValue = "//div[contains(@class,'dropdown__list-item')]//div[@class='dropdown__title' and text()='"
                + fieldValue + "']";
        LOG.info("Выбираем значение в таблице по значению \"" + seachValue + "\" из выпадающего списка значение \"" + fieldValue + "\". " +
                "xpathField: " + xpathField + ". xpathValue:" + xpathValue);
        $x(xpathField).click();
        $x(xpathValue).click();
        waitSpinner();
    }

}