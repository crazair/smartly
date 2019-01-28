package ru.sbtqa.smartly.pageobjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;
import ru.sbtqa.smartly.pageobjects.base.BasePage;
import ru.sbtqa.smartly.pageobjects.base.PageObject;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.*;
import static ru.sbtqa.smartly.common.utils.DateUtils.getDateWithDeltaDays;
import static ru.sbtqa.smartly.common.utils.DateUtils.getDateWithFormat;

@PageObject("Статьи учета")
public class DebtsPage extends BasePage {

    @Step("На вкладке <Добавление процентной схемы/тарифа> заполняем поля")
    public void addPercentSchemeOrTariff(String date, String rate, String baseBalanceProcessing, String baseDebtCalc, String rateType, String... rateValues) {
        setValueByName("Дата начала действия", date);
        selectFromDropBox("Правило расчета", rate);
        selectFromDropBox("Способ обработки базового остатка", baseBalanceProcessing);
        selectFromDropBox("Задолженность базы расчёта", baseDebtCalc);
        selectFromDropBox("Тип ставки", rateType);
        if(rateValues.length == 1) setValueByName("Значение ставки", rateValues[0]);
        else addVariableInterestRates(rateValues);
    }

    @Step("На воркспейсе <Добавление процентной схемы/тарифа> заполняем значения переменной ставки")
    public void addVariableInterestRates(String... rates){
        for (int i = 0; i < rates.length; i++) {
            clickLinkOrButtonByNameInModal("Добавить запись");
            $(byName("loans4b.interestSchemes.entity.scheme.rateValueOptions[" + i + "].rateValue")).setValue(rates[i]);
        }
    }

    @Step("На вкладке <Добавление процентной схемы/тарифа> заполняем поля")
    public void addVariableInterestScheme(String criterion, String calcPeriod, String interestPeriod,
                                          String calcPeriodBegindate, String interestPeriodBegindate) {
        selectFromDropBox("Критерии установки ставки", criterion);
        selectFromDropBox("Расчётный период", calcPeriod);
        selectFromDropBox("Процентный период", interestPeriod);
        setValueByName("Дата начала расчётного периода", calcPeriodBegindate);
        setValueByName("Дата начала процентного периода", interestPeriodBegindate);
    }

    @Step("Заполняем поля День Исполнения: {0}, День начисления: {1}")
    public void setPaymentAndAccrualDays(String paymentDay, String accrualDay) {
        setPaymentDay(paymentDay);
        setAccrualDate(accrualDay);
    }

    @Step("Получаю текст первой строки графика")
    public String getFirstLineScheduleText() {
        return $$x("//div[contains(@class,'modal')]//table[contains(@class, 'table__table')]//tbody//tr")
                .filter(Condition.visible).last().getText();
    }

    @Step("Получаю значение даты начала в объекте календарь")
    public String getBeginAccountDate() {
        String fieldAccountName = "loans4b.planConditions.limitDebtGraph.limitDebts[0].beginAccountDate";
        return $(byName(fieldAccountName)).getValue();
    }

    @Step("Получаю значение даты окончания в объекте календарь")
    public String getEndAccountDate() {
        String fieldAccountName = "loans4b.planConditions.limitDebtGraph.limitDebts[0].endAccountDate";
        return $(byName(fieldAccountName)).getValue();
    }

    @Step("Заполняем поле \"День Исполнения\"")
    public void setPaymentDay(String value) {
        $(byName("loans4b.planConditions.planConditionForm.plan.executionDateCalculator.dayInAllPeriods"))
                .setValue(value);
    }

    @Step("Заполняем поле \"День Начисления\"")
    public void setAccrualDate(String value) {
        $(byName("loans4b.planConditions.planConditionForm.plan.interestAccrualDateCalculator.dayInAllPeriods"))
                .setValue(value);
    }

    public void fillPlanParameters(String beginPlanDateType, String endPlanDateType, String planOperationType,
                                   String dateDetermineMethod, String onExecutionDate) {
        selectFromDropBox("Тип даты начала планирования", beginPlanDateType);
        selectFromDropBox("Тип даты окончания планирования", endPlanDateType);
        fillPlanOperationType(planOperationType);
        selectFromDropBox("Способ определения дат", dateDetermineMethod);
        selectFromDropBox("По дате исполнения", onExecutionDate);
    }

    public void fillDateDetermineMethod(String executionBeginDateType,
                                        String baseItem, String planOperationType, String startPoint, String day) {
        selectFromDropBox("Тип даты начала исполнения", executionBeginDateType);
        selectFromDropBox("Базовая статья", baseItem);
        fillPlanOperationType1(planOperationType);
        selectFromDropBox("Точка отсчета", startPoint);
        setValueByName("День", day);
    }

    public void fillPlanOperationsAmount(String recievingSumMethod, String calcMethod, String planPosition) {
        selectFromDropBox("Способ получения сумм", recievingSumMethod);
        selectFromDropBox("Метод расчёта", calcMethod);
        selectFromDropBox("Позиция планирования", planPosition);
    }

    /** Используется, т.к. на форме 2 дропбокса с наименованием <Тип плановой операции> */
    @Step("Заполняем поле <Тип плановой операции> значением <{0}>")
    public void fillPlanOperationType(String fieldValue) {
        $(byName("loans4b.planConditions.planConditionForm.plan.plannedOperationType")).click();
        $x("//div[contains(@class,\"dropdown__list-item\")]//div[@class='dropdown__title' and text()='"
                + fieldValue + "']").click();
    }

    /**
     * Используется, т.к. на форме 2 дропбокса с наименованием <Тип плановой операции>.
     * Заполняет в блоке <Метод опеределения дат>
     */
    @Step("Заполняем поле <Тип плановой операции> блока <Метод определения дат> значением <{0}>")
    public void fillPlanOperationType1(String fieldValue) {
        $(byName("loans4b.planConditions.planConditionForm.plan.dateOperationType")).click();
        $x("//div[contains(@class,\"dropdown__list-item\")]//div[@class='dropdown__title' and text()='"
                + fieldValue + "']").click();
    }

    /**
     * Используется для заполнения графика лимита
     *
     * @param beginDate - дата начала
     * @param endDate - дата окончания
     * @param sum - сумма
     * @param index - номер строки (начинается с 0)
     */
    @Step("Заполняем строку данных графика лимита")
    public void fillLimitGraph(String beginDate, String endDate, String sum, int index) {
        $(byName("loans4b.planConditions.limitDebtGraph.limitDebts[" + index + "].beginAccountDate")).setValue("").setValue(beginDate);
        $(byName("loans4b.planConditions.limitDebtGraph.limitDebts[" + index + "].endAccountDate")).setValue("").setValue(endDate);
        $(byName("loans4b.planConditions.limitDebtGraph.limitDebts[" + index + "].planSum")).setValue("").setValue(sum);
    }

    /** Используется для заполнения графика лимита */
    @Step("Удаляем все записи в графике лимита")
    public void deleteRows() {
        ElementsCollection elements = $$x("//td//span[@class = 'icon icon_size-20 icon_img-close']");
        elements.forEach(element -> element.click());
    }

    @Step("Удаляем строку графика по номеру строки <{0}>")
    public void deleteTableRow(String rowNumber) {
        $x("//tr[@class='table__row']//*[text() = '" + rowNumber + "']/parent::node()/parent::node()//span[@class = 'button__content']").click();
        LOG.info("Удаляем строку графика по номеру строки " + rowNumber + "");
    }

    /** Используется для проверки заполениня корректности факт операций в целом по договору*/
    @Step("Получаю текст из записей таблицы <Фактические операции> при нажатии на кнопку в целом по договору")
    public String getTableFromDebtsPageMainSide() {
        return $x("//div[@class='grid__col grid__col_span-8']").getText();
    }


    /**
     * Используется для заполнения нужного чекбокса
     * @param index - номер чекбокса (начинается с 5 по 7)
     */
    @Step("Заполняем чекбокс <{0}> на странице Добавление условия досрочного гашения")
    public void setCheckboxFull(int index) {
        $x("(//div[@class='checkable'])[" + index + "]").click();

    }

    /**
     * Используется для выбора дня ежемесячного платежа
     * @param date      - опорная дата (например, дата выдачи)
     * @param deltaDays - на сколько дней (как минимум) нужно отодвинуть дату первого платежа (от 0 до 14)
     */
    @Step("Выбираем день платежа по кредиту в месяце не ближе <{1}> от указанной даты <{0}>")
    public String getRepaymentDayInMonth(String date, int deltaDays) {
        int day = Integer.valueOf(getDateWithFormat(getDateWithDeltaDays(date, deltaDays),"dd"));
        if (day > 28) {
            day = Integer.valueOf(getDateWithFormat(getDateWithDeltaDays(date, -deltaDays),"dd"));
        }
        return Integer.toString(day);
    }
}