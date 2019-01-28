package ru.sbtqa.smartly.testsuites;

import ru.sbtqa.smartly.common.BaseTestSuit;
import ru.sbtqa.smartly.datacreators.EtalonProducts;
import ru.sbtqa.smartly.datacreators.ProductCreator;
import ru.sbtqa.smartly.dataobjects.Product;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.sbtqa.smartly.common.matchers.StringContainsInOrderIgnoringCase.stringContainsInOrderIgnoringCase;
import static ru.sbtqa.smartly.common.utils.DateUtils.getCurrentDate;
import static ru.sbtqa.smartly.common.utils.PropertyUtils.getProperty;
import static ru.sbtqa.smartly.dataobjects.Product.getProductFromStorage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Tag("SMOKE")
@DisplayName("СМОУК тесты продукта!!!")
public class SmokeTests extends BaseTestSuit {

    @Test
    @Owner("anosv")
    @Tag("DEVBARRIER")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("01 Проверка авторизации в системе")
    public void smoke01() {
        firstPage.clickLinkOrButtonByName("Клиенты");

        assertThat(commonPage.getRootText(), stringContainsInOrderIgnoringCase("Клиенты", "Фильтр клиентов ЮЛ"));

        commonPage.clickHomeButton();
        firstPage.clickLinkOrButtonByName("Продукты");

        assertThat(commonPage.getRootText(), stringContainsInOrderIgnoringCase("Продукты", "Фильтры"));
    }

    @Test
    @Owner("anosv")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("02 Проверка создания и сохранения продукта")
    public void smoke02() {
        firstPage.clickLinkOrButtonByName("Клиенты");
        clientsPage.createProduct();
        productCreationTemplatePage.fillFieldsNewProduct(
                "Кредит ЮЛ",
                "Не выбран",
                "931 Регламент корпоративного кредитования юридических лиц и индивидуальных предпринимателей",
                "931 Корпоративное кредитование - кредитный договор");
        productCreationTemplatePage.selectFromDropBox("Тип продукта", "Овердрафт");
        assertThat("Проверяю что значение в поле <Режим кредитования (необязательно)> не выбрано",
                   productCreationTemplatePage.getValueByName("Режим кредитования (необязательно)"),
                   equalTo("Овердрафт"));
        assertThat("Проверяю что значение в поле <Регламент (необязательно)> не выбрано",
                   productCreationTemplatePage.getValueByName("Регламент (необязательно)"),
                   equalTo("Выберите из справочника"));
        assertThat("Проверяю что значение в поле <Шаблон продукта> не выбрано",
                   productCreationTemplatePage.getValueByName("Шаблон продукта"), equalTo("Выберите из справочника"));

        productCreationTemplatePage.fillFieldsNewProduct(
                "Кредит ЮЛ",
                "Не выбран",
                "931 Регламент корпоративного кредитования юридических лиц и индивидуальных предпринимателей",
                "931 Корпоративное кредитование - кредитный договор");

        productCreationTemplatePage.clickLinkOrButtonByName("Создать договор");
        conditionPage.saveNewProductKDInStorageByKey("Product_1115");

        commonPage.clickHomeButton();
        firstPage.clickLinkOrButtonByName("Продукты");
        productsPage.selectProductByAgrNum(getProductFromStorage("Product_1115").getNumber());
        assertThat(commonPage.getRootText(), stringContainsInOrderIgnoringCase("Договор", "Текущие условия"));
        assertThat("Проверяю что значение в поле <Дата начала> пустое",
                   productsPage.getValueByName("Дата начала"), equalTo(""));
        assertThat("Проверяю что значение в поле <Дата окончания> пустое",
                   productsPage.getValueByName("Дата окончания"), equalTo(""));
    }

    @Test
    @Owner("anosv")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("03 Создание КД (обязательные реквизиты)")
    public void smoke03() {
        firstPage.clickLinkOrButtonByName("Клиенты");
        clientsPage.createProduct();
        productCreationTemplatePage.fillFieldsNewProduct(
                "Кредит ЮЛ",
                "Кредитный договор",
                "931 Регламент корпоративного кредитования юридических лиц и индивидуальных предпринимателей",
                "931 Корпоративное кредитование - кредитный договор");
        productCreationTemplatePage.clickLinkOrButtonByName("Создать договор");

        assertThat(commonPage.getBoardBarFullText(),
                   stringContainsInOrderIgnoringCase(
                           "Кредитный договор №",
                           "Ожидает подтверждения",
                           getProperty("clientShortName"),
                           getProperty("clientINN")));

        conditionPage.fillFieldsDatesKD(
                "18.02.2016",
                "18.02.2016",
                "17.03.2017",
                "18.02.2016");
        conditionPage.fillFieldsAmounts(
                "1000000.00",
                "Рубль",
                "Рубль");
        conditionPage.fillFieldsRepayment(
                "Дифференцированный платеж",
                "Крупный бизнес");
        conditionPage.fillFieldsDivision(
                "013 - ЦЕНТРАЛЬНО-ЧЕРНОЗЕМНЫЙ БАНК ПАО СБЕРБАНК",
                "0013-130013 - Центрально-Черноземный банк",
                "0042-86-12 - Управление поддержки корпоративных кредитов и операций на финансовых рынках");

        conditionPage.saveNewProductKDInStorageByKey("Product_1116");

        conditionPage.clickLinkOrButtonByName("Сохранить");
        conditionPage.clickLinkOrButtonByNameInDialogWindow("Да");

        assertThat(commonPage.getNotificationsWrapperText(),
                   stringContainsInOrderIgnoringCase("Продукт номер", "сохранен"));

        /*
         * Сохраняю продукт в хранилище для дальнешшего использования
         *
         ******* Как получать значения продукта из хранилища:
         * ((Product) getFromStorage("Product_1116")).getNumber();
         * ((Product) getFromStorage("Product_1116")).getBeginDate();
         *
         ******* Как изменять значения конкретного продукта в хранилище:
         * ((Product) getFromStorage("Product_1116")).setNumber("Новый номер_123");
         *
         *  ******* Или ещё вариант с продуктом....
         *  getProductFromStorage("Product_1116").getNumber()
         *
         */
    }

    @Test
    @Owner("anosv")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("04 Проверка соответствия шаблонов статей учета для КД")
    public void smoke04() {
        Product product_1116 = getProductFromStorage("Product_1116");
        firstPage.clickLinkOrButtonByName("Продукты");
        productsPage.selectProductByAgrNum(product_1116.getNumber());
        basePage.clickLinkOrButtonByName("Статьи учета");

        //Добавить параметр - Плата за обслуживание кредита
        debtsPage.selectInTable("Плата за обслуживание кредита");
        debtsPage.clickLinkOrButtonByName("Планирование");
        debtsPage.clickLinkOrButtonByName("Добавить параметр");

        assertThat("Проверяем, что в поле <Тип даты начала планирования> стоит значение <Дата начала договора>",
                   debtsPage.getValueByName("Тип даты начала планирования"), equalTo("Дата начала договора"));
        assertThat("Проверяем, что в поле <Тип даты окончания планирования> стоит значение <Дата окончания договора>",
                   debtsPage.getValueByName("Тип даты окончания планирования"), equalTo("Дата окончания договора"));

        assertThat("Проверяем, что в поле <Тип плановой операции> стоит значение <Погашение платы за обслуживание кредита>",
                   debtsPage.getValueByName("Тип плановой операции"), equalTo("Погашение платы за обслуживание кредита"));
        assertThat("Проверяем, что в поле <Способ определения дат> стоит значение <Периодическое изменение>",
                   debtsPage.getValueByName("Способ определения дат"), equalTo("Периодическое изменение"));

        debtsPage.clickLinkOrButtonByName("Обход выходных");

        assertThat("Проверяем, что в поле <По дате исполнения> стоит значение <Следующий раб.день>",
                   debtsPage.getValueByName("По дате исполнения"), equalTo("Следующий раб.день"));
        assertThat("Проверяем, что чекбокс <Учитывать в графике>", debtsPage.isChecked("Учитывать в графике"),
                   equalTo(false));
        assertThat("Проверяем, что в поле <По дате окончания периода начисления> стоит значение <Не сдвигать>",
                   debtsPage.getValueByName("По дате окончания периода начисления"), equalTo("Не сдвигать"));
        assertThat("Проверяем, что в поле <В последнем периоде> стоит значение <Не сдвигать>",
                   debtsPage.getValueByName("В последнем периоде"), equalTo("Не сдвигать"));

        assertThat("Проверяем, что в поле <Вид периода> стоит значение <Месяц>",
                   debtsPage.getValueByName("Вид периода"), equalTo("Месяц"));
        assertThat("Проверяем, что в поле <Кол-во периодов> стоит значение <1>",
                   debtsPage.getValueByName("Кол-во периодов"), equalTo("1"));
        assertThat("Проверяем, что в поле <Тип даты начала исполнения> стоит значение <Дата начала договора + 1 день>",
                   debtsPage.getValueByName("Тип даты начала исполнения"), equalTo("Дата начала договора + 1 день"));
        assertThat("Проверяем, что чекбокс <Включительно> не установлен", debtsPage.isChecked("Включительно"),
                   equalTo(false));

        assertThat("Проверяем, что чекбокс <Исполнение в последний рабочий день месяца> не установлен",
                   debtsPage.isChecked("Исполнение в последний рабочий день месяца"), equalTo(false));
        assertThat("Проверяем, что в поле <Дата исполнения> стоит значение <В дату окончания планирования>",
                   debtsPage.getValueByName("Дата исполнения"), equalTo("В дату окончания планирования"));
        assertThat("Проверяем, что чекбокс <Отсрочка> не установлен",
                   debtsPage.isChecked("Отсрочка"), equalTo(false));

        assertThat("Проверяем, что в поле <Способ обработки суммы> стоит значение <Не планировать>",
                   debtsPage.getValueByName("Способ обработки суммы"), equalTo("Не планировать"));

        assertThat("Проверяем, что в поле <Способ получения сумм> стоит значение <Из журнала процентов>",
                   debtsPage.getValueByName("Способ получения сумм"), equalTo("Из журнала процентов"));

        commonPage.closeWorkspace("Добавить параметр - Плата за обслуживание кредита");

        //Добавить параметр - Плата за обслуживание кредита (фиксир. сумма)
        debtsPage.selectInTable("Плата за обслуживание кредита (фиксир. сумма)");
        debtsPage.clickLinkOrButtonByName("Планирование");
        debtsPage.clickLinkOrButtonByName("Добавить параметр");

        assertThat("Проверяем, что в поле <Тип даты начала планирования> стоит значение <Дата начала договора>",
                   debtsPage.getValueByName("Тип даты начала планирования"), equalTo("Дата начала договора"));
        assertThat("Проверяем, что в поле <Тип даты окончания планирования> стоит значение <Дата окончания договора>",
                   debtsPage.getValueByName("Тип даты окончания планирования"), equalTo("Дата окончания договора"));

        assertThat("Проверяем, что в поле <Тип плановой операции> стоит значение <Погашение платы за обслуживание кредита (фиксир. сумма)>",
                   debtsPage.getValueByName("Тип плановой операции"), equalTo("Погашение платы за обслуживание кредита (фиксир. сумма)"));
        assertThat("Проверяем, что в поле <Способ определения дат> стоит значение <Периодическое изменение>",
                   debtsPage.getValueByName("Способ определения дат"), equalTo("Периодическое изменение"));

        debtsPage.clickLinkOrButtonByName("Обход выходных");

        assertThat("Проверяем, что в поле <По дате исполнения> стоит значение <Следующий раб.день>",
                   debtsPage.getValueByName("По дате исполнения"), equalTo("Следующий раб.день"));
        assertThat("Проверяем, что чекбокс <Учитывать в графике>", debtsPage.isChecked("Учитывать в графике"),
                   equalTo(false));
        assertThat("Проверяем, что в поле <По дате окончания периода начисления> стоит значение <Не сдвигать>",
                   debtsPage.getValueByName("По дате окончания периода начисления"), equalTo("Не сдвигать"));
        assertThat("Проверяем, что в поле <В последнем периоде> стоит значение <Не сдвигать>",
                   debtsPage.getValueByName("В последнем периоде"), equalTo("Не сдвигать"));

        assertThat("Проверяем, что в поле <Вид периода> стоит значение <Месяц>",
                   debtsPage.getValueByName("Вид периода"), equalTo("Месяц"));
        assertThat("Проверяем, что в поле <Кол-во периодов> стоит значение <1>",
                   debtsPage.getValueByName("Кол-во периодов"), equalTo("1"));
        assertThat("Проверяем, что в поле <Тип даты начала исполнения> стоит значение <Дата начала договора>",
                   debtsPage.getValueByName("Тип даты начала исполнения"), equalTo("Дата начала договора"));
        assertThat("Проверяем, что чекбокс <Включительно> не установлен", debtsPage.isChecked("Включительно"),
                   equalTo(false));

        assertThat("Проверяем, что в поле <Дата исполнения> стоит значение <В дату окончания планирования>",
                   debtsPage.getValueByName("Дата исполнения"), equalTo("В дату окончания планирования"));
        assertThat("Проверяем, что чекбокс <Отсрочка> не установлен",
                   debtsPage.isChecked("Отсрочка"), equalTo(false));

        assertThat("Проверяем, что в поле <Способ обработки суммы> стоит значение <Сумма определена комиссией>",
                   debtsPage.getValueByName("Способ обработки суммы"), equalTo("Сумма определена комиссией"));

        assertThat("Проверяем, что в поле <Способ получения сумм> стоит значение <Из графика>",
                   debtsPage.getValueByName("Способ получения сумм"), equalTo("Из графика"));

        commonPage.closeWorkspace("Добавить параметр - Плата за обслуживание кредита (фиксир. сумма)");

        //Проверяем форму: Добавить параметр - Плата за резервирование
        debtsPage.selectInTable("Плата за резервирование");
        debtsPage.clickLinkOrButtonByName("Планирование");
        debtsPage.clickLinkOrButtonByName("Добавить параметр");

        assertThat("Проверяем, что в поле <Тип даты начала планирования> стоит значение <Дата начала договора>",
                   debtsPage.getValueByName("Тип даты начала планирования"), equalTo("Дата начала договора"));
        assertThat("Проверяем, что в поле <Тип даты окончания планирования> стоит значение <Дата окончания договора>",
                   debtsPage.getValueByName("Тип даты окончания планирования"), equalTo("Дата окончания договора"));

        assertThat("Проверяем, что в поле <Тип плановой операции> стоит значение <Погашение процентов за кредит>",
                   debtsPage.getValueByName("Тип плановой операции"), equalTo("Погашение платы за резервирование"));
        assertThat("Проверяем, что в поле <Способ определения дат> стоит значение <Единовременно в дату выдачи>",
                   debtsPage.getValueByName("Способ определения дат"), equalTo("Единовременно в дату выдачи"));

        debtsPage.clickLinkOrButtonByName("Обход выходных");

        assertThat("Проверяем, что в поле <По дате исполнения> стоит значение <Следующий раб.день>",
                   debtsPage.getValueByName("По дате исполнения"), equalTo("Следующий раб.день"));
        assertThat("Проверяем, что чекбокс <Учитывать в графике>", debtsPage.isChecked("Учитывать в графике"),
                   equalTo(false));
        assertThat("Проверяем, что в поле <По дате окончания периода начисления> стоит значение <Не сдвигать>",
                   debtsPage.getValueByName("По дате окончания периода начисления"), equalTo("Не сдвигать"));
        assertThat("Проверяем, что в поле <В последнем периоде> стоит значение <Не сдвигать>",
                   debtsPage.getValueByName("В последнем периоде"), equalTo("Не сдвигать"));


        assertThat("Проверяем, что в поле <Способ обработки суммы> стоит значение <Сумма определена комиссией>",
                   debtsPage.getValueByName("Способ обработки суммы"), equalTo("Сумма определена комиссией"));

        assertThat("Проверяем, что в поле <Способ получения сумм> стоит значение <Из графика>",
                   debtsPage.getValueByName("Способ получения сумм"), equalTo("Из графика"));

        commonPage.closeWorkspace("Добавить параметр - Плата за резервирование");

        //Добавить параметр - Проценты за кредит
        debtsPage.selectInTable("Проценты за кредит");
        debtsPage.clickLinkOrButtonByName("Планирование");
        debtsPage.clickLinkOrButtonByName("Добавить параметр");

        assertThat("Проверяем, что в поле <Тип даты начала планирования> стоит значение <Дата начала договора>",
                   debtsPage.getValueByName("Тип даты начала планирования"), equalTo("Дата начала договора"));
        assertThat("Проверяем, что в поле <Тип даты окончания планирования> стоит значение <Дата окончания договора>",
                   debtsPage.getValueByName("Тип даты окончания планирования"), equalTo("Дата окончания договора"));

        assertThat("Проверяем, что в поле <Тип плановой операции> стоит значение <Погашение процентов за кредит>",
                   debtsPage.getValueByName("Тип плановой операции"), equalTo("Погашение процентов за кредит"));
        assertThat("Проверяем, что в поле <Способ определения дат> стоит значение <Периодическое изменение>",
                   debtsPage.getValueByName("Способ определения дат"), equalTo("Периодическое изменение"));

        debtsPage.clickLinkOrButtonByName("Обход выходных");

        assertThat("Проверяем, что в поле <По дате исполнения> стоит значение <Следующий раб.день>",
                   debtsPage.getValueByName("По дате исполнения"), equalTo("Следующий раб.день"));
        assertThat("Проверяем, что чекбокс <Учитывать в графике>", debtsPage.isChecked("Учитывать в графике"),
                   equalTo(false));
        assertThat("Проверяем, что в поле <По дате окончания периода начисления> стоит значение <Не сдвигать>",
                   debtsPage.getValueByName("По дате окончания периода начисления"), equalTo("Не сдвигать"));
        assertThat("Проверяем, что в поле <В последнем периоде> стоит значение <Не сдвигать (как в ЕКС)>",
                   debtsPage.getValueByName("В последнем периоде"), equalTo("Не сдвигать (как в ЕКС)"));

        assertThat("Проверяем, что в поле <Вид периода> стоит значение <Месяц>",
                   debtsPage.getValueByName("Вид периода"), equalTo("Месяц"));
        assertThat("Проверяем, что в поле <Кол-во периодов> стоит значение <1>",
                   debtsPage.getValueByName("Кол-во периодов"), equalTo("1"));
        assertThat("Проверяем, что в поле <Тип даты начала исполнения> стоит значение <Дата начала договора + 1 день>",
                   debtsPage.getValueByName("Тип даты начала исполнения"), equalTo("Дата начала договора + 1 день"));
        assertThat("Проверяем, что чекбокс <Включительно> не установлен", debtsPage.isChecked("Включительно"),
                   equalTo(false));

        assertThat("Проверяем, что чекбокс <Исполнение в последний рабочий день месяца> не установлен",
                   debtsPage.isChecked("Исполнение в последний рабочий день месяца"), equalTo(false));
        assertThat("Проверяем, что в поле <Дата исполнения> стоит значение <В дату окончания планирования>",
                   debtsPage.getValueByName("Дата исполнения"), equalTo("В дату окончания планирования"));
        assertThat("Проверяем, что чекбокс <Отсрочка> не установлен",
                   debtsPage.isChecked("Отсрочка"), equalTo(false));

        assertThat("Проверяем, что в поле <Способ обработки суммы> стоит значение <Не планировать>",
                   debtsPage.getValueByName("Способ обработки суммы"), equalTo("Не планировать"));

        assertThat("Проверяем, что в поле <Способ получения сумм> стоит значение <Из журнала процентов>",
                   debtsPage.getValueByName("Способ получения сумм"), equalTo("Из журнала процентов"));

        commonPage.closeWorkspace("Добавить параметр - Проценты за кредит");

        //Добавить параметр - Срочная ссудная задолженность
        debtsPage.selectInTable("Срочная ссудная задолженность");
        debtsPage.clickLinkOrButtonByName("Добавить параметр");

        assertThat("Проверяем, что в поле <Тип даты начала планирования> стоит значение <Дата начала договора>",
                   debtsPage.getValueByName("Тип даты начала планирования"), equalTo("Дата начала договора"));
        assertThat("Проверяем, что в поле <Тип даты окончания планирования> стоит значение <Дата окончания договора>",
                   debtsPage.getValueByName("Тип даты окончания планирования"), equalTo("Дата окончания договора"));

        assertThat("Проверяем, что в поле <Тип плановой операции> стоит значение <Погашение срочной ссудной задолженности>",
                   debtsPage.getValueByName("Тип плановой операции"), equalTo("Погашение срочной ссудной задолженности"));
        assertThat("Проверяем, что в поле <Способ определения дат> стоит значение <Периодическое изменение>",
                   debtsPage.getValueByName("Способ определения дат"), equalTo("Периодическое изменение"));

        debtsPage.clickLinkOrButtonByName("Обход выходных");

        assertThat("Проверяем, что в поле <По дате исполнения> стоит значение <Следующий раб.день>",
                   debtsPage.getValueByName("По дате исполнения"), equalTo("Следующий раб.день"));
        assertThat("Проверяем, что чекбокс <Учитывать в графике>", debtsPage.isChecked("Учитывать в графике"),
                   equalTo(false));


        assertThat("Проверяем, что в поле <Вид периода> стоит значение <Месяц>",
                   debtsPage.getValueByName("Вид периода"), equalTo("Месяц"));
        assertThat("Проверяем, что в поле <Кол-во периодов> стоит значение <1>",
                   debtsPage.getValueByName("Кол-во периодов"), equalTo("1"));


        assertThat("Проверяем, что в поле <Тип даты начала исполнения> стоит значение <Дата начала договора + 1 день>",
                   debtsPage.getValueByName("Тип даты начала исполнения"), equalTo("Дата начала договора + 1 день"));

        assertThat("Проверяем, что чекбокс <Включительно>", debtsPage.isChecked("Включительно"),
                   equalTo(false));

        assertThat("Проверяем, что чекбокс <Зависит от периода>", debtsPage.isChecked("Зависит от периода"),
                   equalTo(false));

        assertThat("Проверяем, что чекбокс <Исполнение в последний рабочий день месяца>", debtsPage.isChecked("Исполнение в последний рабочий день месяца"),
                   equalTo(false));


        assertThat("Проверяем, что в поле <Дата исполнения> стоит значение <В дату окончания планирования>",
                   debtsPage.getValueByName("Дата исполнения"), equalTo("В дату окончания планирования"));

        assertThat("Проверяем, что в поле <Способ обработки суммы> стоит значение <Общая сумма графика равными суммами>",
                   debtsPage.getValueByName("Способ обработки суммы"), equalTo("Общая сумма графика равными суммами"));


        assertThat("Проверяем, что в поле <Общая сумма графика> стоит значение <Общая сумма графика равныме договора>",
                   debtsPage.getValueByName("Общая сумма графика"), equalTo("1 000 000"));


        assertThat("Проверяем, что в поле <Способ получения сумм> стоит значение <Из графика>",
                   debtsPage.getValueByName("Способ получения сумм"), equalTo("Из графика"));


        assertThat("Проверяем, что в поле <Позиция планирования> стоит значение <От конца периода>",
                   debtsPage.getValueByName("Позиция планирования"), equalTo("От конца периода"));

        assertThat("Проверяем, что кнопка <Сохранить> присутствует",
                   commonPage.getRootText(), stringContainsInOrderIgnoringCase("Сохранить"));

        commonPage.closeWorkspace("Добавить параметр - Срочная ссудная задолженность");
    }

    @Test
    @Owner("anosv")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("05 Расчеты_обработка остатка - по входящему остатку")
    public void smoke05() {
        Product product_1116 = getProductFromStorage("Product_1116");
        firstPage.clickLinkOrButtonByName("Продукты");
        productsPage.selectProductByAgrNum(product_1116.getNumber());
        //Планирование %%
        extraditionPage.clickLinkOrButtonByName("Статьи учета");
        debtsPage.selectInTable("Проценты за кредит");
        debtsPage.clickLinkOrButtonByName("Схемы и тарифы");

        //Проценты за кредит
        debtsPage.clickLinkOrButtonByName("Добавить");
        debtsPage.addPercentSchemeOrTariff(
                product_1116.getBeginDate(),
                "Ставка в % годовых",
                "По входящему остатку",
                "Срочная ссудная задолженность",
                "Фиксированная",
                "10");
        debtsPage.setCheckbox("Банковский год", true);
        debtsPage.setCheckbox("Банковский месяц", true);
        debtsPage.clickLinkOrButtonByNameInModal("Сохранить");
        commonPage.softCheckNotificationsWrapperText("Процентная схема успешно добавлена");
        debtsPage.closeWorkspace("Проценты за кредит");

        //Планирование ОД (Срочная ссудная задолженность)
        debtsPage.clickLinkOrButtonByName("Статьи учета");
        debtsPage.selectInTable("Срочная ссудная задолженность");
        debtsPage.clickLinkOrButtonByName("Планирование");
        debtsPage.clickLinkOrButtonByName("Добавить параметр");
        debtsPage.setValueByName("День", "18");
        debtsPage.clickLinkOrButtonByNameInModal("Сохранить");
        commonPage.softCheckNotificationsWrapperText("Параметр планирования успешно сохранен");
        debtsPage.closeWorkspace("Срочная ссудная задолженность");

        // Выдача
        commonPage.clickButtonInToolbar("Сервисы");
        servicesPage.clickLinkOrButtonByName("Запустить сервис");
        servicesPage.clickTableListFromServicesPageSide("Ручное создание фактических операций");
        servicesPage.clickLinkOrButtonByName("Исполнить");
        servicesPage.clickLinkOrButtonByName("Условие");
        servicesPage.setValueByName("Дата", product_1116.getBeginDate());
        servicesPage.clickLinkOrButtonByName("Выберите из справочника");
        servicesPage.clickLinkOrButtonByName("Выдача на р/сч");
        servicesPage.clickLinkOrButtonByName("Выбрать");
        servicesPage.findAndSelectFromPageList("Увеличение срочной ссудной задолженности");
        servicesPage.clickLinkOrButtonByName("Выбрать шаблон");
        servicesPage.setAmountManualTrnx("1000000,00");
        servicesPage.clickLinkOrButtonByName("Сохранить");
        assertThat(commonPage.getNotificationsWrapperText(), stringContainsInOrder("Успешно"));

        extraditionPage.clickLinkOrButtonByName("Статьи учета");
        debtsPage.selectInTable("Проценты за кредит");
        debtsPage.clickLinkOrButtonByName("Журнал процентов");

        debtsPage.compareTable("Проценты за кредит",
                "patterns/PercentsJournal_1350.json",
                "1350 Журнал процентов");
    }

    @Test
    @Owner("anosv")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("06 Ввод тарифа для платы за досрочный возврат")
    public void test06() {
        Product product = EtalonProducts.etalonProductKD();
        product.setSignDate(getCurrentDate());
        product.setBeginDate(getCurrentDate());
        product.setIssuanceDate(getCurrentDate());
        product.setLoanTerm("365");
        ProductCreator.create(product);

        firstPage.clickLinkOrButtonByName("Продукты");
        productsPage.selectProductByAgrNum(product.getNumber());

        //Планирование %%
        extraditionPage.clickLinkOrButtonByName("Статьи учета");
        debtsPage.selectInTable("Плата за досрочный возврат кредита");
        debtsPage.clickLinkOrButtonByName("Схемы и тарифы");

        //Проценты за кредит
        debtsPage.clickLinkOrButtonByName("Добавить");

        assertEquals(product.getBeginDate(),
                debtsPage.getValueByName("Дата начала действия"),
                "Проверяю что значение в поле <Дата начала действия> = Дате начала договора");

        debtsPage.selectFromDropBox("Тип процентного периода", "Ставка в % за день");

        debtsPage.setValueByName("Количество дней для уведомления банка (необязательно)", "10");
        debtsPage.setCheckboxFull(5);

        debtsPage.setValueByName("Количество дней до планового погашения без штрафа (необязательно)", "1");
        debtsPage.setCheckboxFull(6);

        debtsPage.setValueByName("Максимальное количество дней для взимания штрафа (необязательно)", "9");
        debtsPage.setCheckboxFull(7);

        debtsPage.setValueByName("Минимальная сумма штрафа (необязательно)", "9 999,99");
        debtsPage.setValueByName("Максимальная сумма штрафа (необязательно)", "99 999,99");
        debtsPage.setValueByName("Значение ставки", "98,765432");

        debtsPage.clickLinkOrButtonByNameInModal("Сохранить");
        commonPage.softCheckNotificationsWrapperText("Процентная схема успешно добавлена");

        assertThat("Проверка сохраненных данныx в таблице Плата за досрочный возврат кредита",
                servicesPage.getTableLineTextFromValue("Ставка в % за день"),
                stringContainsInOrderIgnoringCase(getCurrentDate(),"10", "Рабочие", "1", "Рабочие","9",
                        "Рабочие","9 999,99","99 999,99","98,765432","Ставка в % за день"));

    }
}