package ru.sbtqa.smartly.pageobjects;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import ru.sbtqa.smartly.common.utils.Storage;
import ru.sbtqa.smartly.dataobjects.Dictionary;
import ru.sbtqa.smartly.dataobjects.Product;
import ru.sbtqa.smartly.pageobjects.base.BasePage;
import ru.sbtqa.smartly.pageobjects.base.PageObject;

import java.util.Optional;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@PageObject("Текущие условия по продукту")
public class ConditionPage extends BasePage {

    public ConditionPage() {
    }

    private void fillBeginEndDates(String vDateSing, String vDateBegin, String vDateEndOrLoanTerm) {
        setValueByName("Дата подписания КОД", vDateSing);
        setValueByName("Дата начала", vDateBegin);
        if (vDateEndOrLoanTerm.matches("\\d+")) {
            setValueByName("Срок договора(дн)", vDateEndOrLoanTerm);
        } else {
            setValueByName("Дата окончания", vDateEndOrLoanTerm);
        }
    }

    @Step("На вкладке \"Текущие условия\" вводим даты нового договора КД")
    public void fillFieldsDatesKD(String vDateSing, String vDateBegin, String vDateEndOrLoanTerm, String vDateIssuance) {
        fillBeginEndDates(vDateSing, vDateBegin, vDateEndOrLoanTerm);
        setValueByName("Дата выдачи", vDateIssuance);
    }

    @Step("На вкладке \"Текущие условия\" вводим даты нового договора НКЛ")
    public void fillFieldsDatesNKL(String vDateSing, String vDateBegin, String vDateEndOrLoanTerm, String vDateIssuance) {
        fillBeginEndDates(vDateSing, vDateBegin, vDateEndOrLoanTerm);
        setValueByName("Дата доступности", vDateIssuance);
    }

    @Step("На вкладке \"Текущие условия\" вводим даты нового договора ВКЛ")
    public void fillFieldsDatesVKL(String vDateSing, String vDateBegin, String vDateEndOrLoanTerm, String vDateIssuance) {
        fillBeginEndDates(vDateSing, vDateBegin, vDateEndOrLoanTerm);
        Optional.ofNullable(vDateIssuance).ifPresent(date -> setValueByName("Дата доступности (необязательно)", vDateIssuance));
    }

    @Step("На вкладке \"Текущие условия\" вводим даты нового договора БГ")
    public void fillFieldsDatesBG(String vDateSing,
                                  String vDateBegin,
                                  String vDateIssuance,
                                  String vDateEnd) {
        fillBeginEndDates(vDateSing, vDateBegin, vDateEnd);
        setValueByName("Дата доступности", vDateIssuance);
    }

    @Step("На вкладке \"Текущие условия\" вводим даты нового договора овердрафта")
    public void fillFieldsDatesOD(String vDateSing, String vDateBegin, String vDateEndOrLoanTerm) {
        fillBeginEndDates(vDateSing, vDateBegin, vDateEndOrLoanTerm);
    }

    @Step("На вкладке \"Текущие условия\" заполняем поля")
    public void fillFieldsAmounts(String amount, String amountCurrency, String issuanceCurrency) {
        setValueByName("Сумма договора", amount);
        selectFromDropBox("Валюта договора", amountCurrency);
        selectFromDropBox("Валюта выдачи", issuanceCurrency);
    }

    @Step("На вкладке \"Текущие условия\" заполняем поля")
    public void fillFieldsAmountsBG(String amount, String limitDebtCurrency, String issuanceCurrency) {
        setValueByName("Сумма", amount);
        selectFromDropBox("Валюта лимита", limitDebtCurrency);
        selectFromDropBox("Валюта выдачи", issuanceCurrency);
    }

    @Step("На вкладке \"Текущие условия\" заполняем поля")
    public void fillFieldsLimitsVKL(String limitDebt, String limitDebtCurrency, String issuanceCurrency) {
        setValueByName("Лимит задолженности", limitDebt);
        selectFromDropBox("Валюта лимита", limitDebtCurrency);
        selectFromDropBox("Валюта выдачи", issuanceCurrency);
    }

    @Step("На вкладке \"Текущие условия\" заполняем поля")
    public void fillFieldsLimitsNKL(String issuanceLimit, String limitCurrency, String issuanceCurrency) {
        setValueByName("Лимит выдачи", issuanceLimit);
        selectFromDropBox("Валюта лимита", limitCurrency);
        selectFromDropBox("Валюта выдачи", issuanceCurrency);
    }

    @Step("На вкладке \"Текущие условия\" заполняем поля")
    public void fillFieldsLimitsOB(String issuanceLimit, String limitCurrency, String issuanceCurrency) {
        setValueByName("Лимит овердрафтн. кредита", issuanceLimit);
        selectFromDropBox("Валюта лимита", limitCurrency);
        selectFromDropBox("Валюта выдачи", issuanceCurrency);
    }

    @Step("На вкладке \"Текущие условия\" заполняем поля")
    public void fillFieldsRepayment(String repaymentMetod, String repaymentPriority) {
        selectFromDropBox("Способ погашения", repaymentMetod);
        selectFromDropBox("Приоритет погашения", repaymentPriority);
    }

    @Step("На вкладке \"Текущие условия\" заполняю информацию о подразделении")
    public void fillFieldsDivision(String branch, String frontSubdivision, String backSubdivision) {
        selectFromDropBox("Филиал", branch);
        $x("//div[contains(text(), 'Подразделение от фронт-офиса')]/following-sibling::div").click();
        waitSpinner();
        clickLinkOrButtonByName(frontSubdivision);
        clickLinkOrButtonByName("Выбрать");
        $x("//div[contains(text(), 'Подразделение от бэк-офиса')]/following-sibling::div").click();
        waitSpinner();
        clickLinkOrButtonByName(backSubdivision);
        clickLinkOrButtonByName("Выбрать");
    }

    @Step("Сохраняем значения полей продукта КД в хранилище")
    public Product saveNewProductKDInStorageByKey(String saveKey) {
        Product product = Product.createProduct()
                .setContractType("Кредит ЮЛ")
                .setLoanTreatment(getValueByName("Режим кредитования"))
                .setReglament(new Dictionary(getValueByName("Регламент"), ""))
                .setTemplateProduct(new Dictionary(getValueByName("Наименование продукта"), ""))
                .setNumber(getValueByName("Номер договора"))
                .setBeginDate(getValueByName("Дата начала"))
                .setSignDate(getValueByName("Дата подписания КОД"))
                .setEndDate(getValueByName("Дата окончания"))
                .setIssuanceDate(getValueByName("Дата выдачи"))
                .setLoanTerm(getValueByName("Срок договора(дн)"))
                .setTotalValue(getValueByName("Сумма договора"))
                .setCurrency(getValueByName("Валюта договора"))
                .setIssuanceCurrency(getValueByName("Валюта выдачи"))
                .setPaymentType(getValueByName("Способ погашения"))
                .setRepaymentOption(getValueByName("Приоритет погашения"))
                .setRequestBKI(isChecked("Согласие на запрос из БКИ"))
                .setBranch(new Dictionary(getValueByName("Филиал"), ""))
                .setFrontOffice(new Dictionary(getValueByName("Подразделение от фронт-офиса"), ""))
                .setBackOffice(new Dictionary(getValueByName("Подразделение от бэк-офиса"), ""))
                .setNotes(getValueByName("Примечания по договору (необязательно)"))
                .build();

        Storage.putToStorage(saveKey, product);
        return product;
    }

    @Step("Сохраняем значения полей продукта НКЛ в хранилище")
    public Product saveNewProductNKLInStorageByKey(String saveKey) {
        Product product = Product.createProduct()
                .setContractType("Кредит ЮЛ")
                .setLoanTreatment(getValueByName("Режим кредитования"))
                .setReglament(new Dictionary(getValueByName("Регламент"), ""))
                .setTemplateProduct(new Dictionary(getValueByName("Наименование продукта"), ""))
                .setNumber(getValueByName("Номер договора"))
                .setBeginDate(getValueByName("Дата начала"))
                .setSignDate(getValueByName("Дата подписания КОД"))
                .setEndDate(getValueByName("Дата окончания"))
                .setIssuanceDate(getValueByName("Дата доступности"))
                .setLoanTerm(getValueByName("Срок договора(дн)"))
                .setTotalValue(getValueByName("Лимит выдачи"))
                .setCurrency(getValueByName("Валюта лимита"))
                .setIssuanceCurrency(getValueByName("Валюта выдачи"))
                .setPaymentType(getValueByName("Способ погашения"))
                .setRepaymentOption(getValueByName("Приоритет погашения"))
                .setRequestBKI(isChecked("Согласие на запрос из БКИ"))
                .setBranch(new Dictionary(getValueByName("Филиал"), ""))
                .setFrontOffice(new Dictionary(getValueByName("Подразделение от фронт-офиса"), ""))
                .setBackOffice(new Dictionary(getValueByName("Подразделение от бэк-офиса"), ""))
                .setNotes(getValueByName("Примечания по договору (необязательно)"))
                .build();

        Storage.putToStorage(saveKey, product);
        return product;
    }

    @Step("Сохраняем значения полей продукта ВКЛ в хранилище")
    public Product saveNewProductVKLInStorageByKey(String saveKey) {
        Product product = Product.createProduct()
                .setContractType("Кредит ЮЛ")
                .setLoanTreatment(getValueByName("Режим кредитования"))
                .setReglament(new Dictionary(getValueByName("Регламент"), ""))
                .setTemplateProduct(new Dictionary(getValueByName("Наименование продукта"), ""))
                .setNumber(getValueByName("Номер договора"))
                .setBeginDate(getValueByName("Дата начала"))
                .setSignDate(getValueByName("Дата подписания КОД"))
                .setEndDate(getValueByName("Дата окончания"))
                .setIssuanceDate(getValueByName("Дата доступности (необязательно)"))
                .setLoanTerm(getValueByName("Срок договора(дн)"))
                .setTotalValue(getValueByName("Лимит задолженности"))
                .setCurrency(getValueByName("Валюта лимита"))
                .setIssuanceCurrency(getValueByName("Валюта выдачи"))
                .setPaymentType(getValueByName("Способ погашения"))
                .setRepaymentOption(getValueByName("Приоритет погашения"))
                .setRequestBKI(isChecked("Согласие на запрос из БКИ"))
                .setBranch(new Dictionary(getValueByName("Филиал"), ""))
                .setFrontOffice(new Dictionary(getValueByName("Подразделение от фронт-офиса"), ""))
                .setBackOffice(new Dictionary(getValueByName("Подразделение от бэк-офиса"), ""))
                .setNotes(getValueByName("Примечания по договору (необязательно)"))
                .build();

        Storage.putToStorage(saveKey, product);
        return product;
    }

    @Step("Сохраняем значения полей продукта ОВ в хранилище")
    public Product saveNewProductOBInStorageByKey(String saveKey) {
        Product product = Product.createProduct()
                .setContractType("Овердрафт")
                .setLoanTreatment(getValueByName("Режим кредитования"))
                .setReglament(new Dictionary(getValueByName("Регламент"), ""))
                .setTemplateProduct(new Dictionary(getValueByName("Наименование продукта"), ""))
                .setNumber(getValueByName("Номер договора"))
                .setBeginDate(getValueByName("Дата начала"))
                .setSignDate(getValueByName("Дата подписания КОД"))
                .setEndDate(getValueByName("Дата окончания"))
                .setLoanTerm(getValueByName("Срок договора(дн)"))
                .setTotalValue(getValueByName("Лимит овердрафтн. кредита"))
                .setCurrency(getValueByName("Валюта лимита"))
                .setIssuanceCurrency(getValueByName("Валюта выдачи"))
                .setRepaymentOption(getValueByName("Приоритет погашения"))
                .setRequestBKI(isChecked("Согласие на запрос из БКИ"))
                .setBranch(new Dictionary(getValueByName("Филиал"), ""))
                .setFrontOffice(new Dictionary(getValueByName("Подразделение от фронт-офиса"), ""))
                .setBackOffice(new Dictionary(getValueByName("Подразделение от бэк-офиса"), ""))
                .setNotes(getValueByName("Примечания по договору (необязательно)"))
                .build();

        Storage.putToStorage(saveKey, product);
        return product;
    }

    @Step("Сохраняем значения полей продукта ОВ в хранилище")
    public Product saveNewProductBGInStorageByKey(String saveKey) {
        Product product = Product.createProduct()
                .setContractType("Договор банковской гарантии")
                .setLoanTreatment(getValueByName("Режим кредитования"))
                .setReglament(new Dictionary(getValueByName("Регламент"), ""))
                .setTemplateProduct(new Dictionary(getValueByName("Наименование продукта"), ""))
                .setNumber(getValueByName("Номер договора"))
                .setBeginDate(getValueByName("Дата начала"))
                .setSignDate(getValueByName("Дата подписания КОД"))
                .setIssuanceDate(getValueByName("Дата доступности"))
                .setEndDate(getValueByName("Дата окончания"))
                .setLoanTerm(getValueByName("Срок договора(дн)"))
                .setTotalValue(getValueByName("Сумма"))
                .setCurrency(getValueByName("Валюта лимита"))
                .setIssuanceCurrency(getValueByName("Валюта выдачи"))
                .setRepaymentOption(getValueByName("Приоритет погашения"))
                .setRequestBKI(isChecked("Согласие на запрос информации из БКИ"))
                .setBranch(new Dictionary(getValueByName("Филиал"), ""))
                .setFrontOffice(new Dictionary(getValueByName("Подразделение от фронт-офиса"), ""))
                .setBackOffice(new Dictionary(getValueByName("Подразделение от бэк-офиса"), ""))
                .setNotes(getValueByName("Примечания по договору (необязательно)"))
                .build();

        Storage.putToStorage(saveKey, product);
        return product;
    }

    @Step("На вкладке \"Текущие условия\" вводим дату начала договора")
    public void setConditionBeginDate(String value) {
        String fieldId = "product-period__begin-date-field";
        setInputValue(value, fieldId);
    }

    private void setInputValue(String value, String fieldId) {
        SelenideElement fieldElement = $(By.id(fieldId));
        fieldElement.$x(".//*[name() = 'input' or name() = 'textarea']").setValue(value);
    }

    @Step("На вкладке \"Текущие условия\" вводим дату подписания договора")
    public void setGuaranteeSignDate(String value) {
        String fieldId = "general-convention-guarantee__sign-date-field";
        setInputValue(value, fieldId);
    }

    @Step("На вкладке \"Текущие условия\" вводим дату окончания договора")
    public void setGuaranteeEndDate(String value) {
        String fieldId = "product-period__end-date-field";
        setInputValue(value, fieldId);
    }

    @Step("На вкладке \"Текущие условия\" вводим дату доступности договора")
    public void setGuaranteeIssuanceDate(String value) {
        String fieldId = "general-convention-guarantee__issuance-date-field";
        setInputValue(value, fieldId);
    }

    @Step("На вкладке \"Текущие условия\" вводим дату вступления в силу")
    public void setGuaranteeEntryForceDate(String value) {
        String fieldId = "general-convention-guarantee__force-date-field";
        setInputValue(value, fieldId);
    }

    @Step("На вкладке \"Текущие условия\" вводим срок договора в днях")
    public void setGuaranteeLoanTerm(String value) {
        String fieldId = "product-period__loan-term-field";
        setInputValue(value, fieldId);
    }

    @Step("На вкладке \"Текущие условия\" вводим сумму")
    public void setGuaranteeAmount(String value) {
        String fieldId = "general-convention-guarantee__amount-field";
        setInputValue(value, fieldId);
    }

    @Step("На вкладке \"Текущие условия\" вводим валюту лимита")
    public void setGuaranteeCurrency(String value) {
        String fieldId = "general-convention-guarantee__amount-currency-field";
        selectFieldByIdFromValue(fieldId, value);
    }

    @Step("На вкладке \"Текущие условия\" вводим валюту выдачи")
    public void setGuaranteeIssuanceCurrency(String value) {
        String fieldId = "general-convention-guarantee__issuance-currency-field";
        selectFieldByIdFromValue(fieldId, value);
    }

    @Step("На вкладке \"Текущие условия\" вводим филиал")
    public void setGuaranteeBranch(String value) {
        String fieldId = "product-departments__branch-field";
        selectFieldByIdFromValue(fieldId, value);
        waitSpinner();
    }

    @Step("На вкладке \"Текущие условия\" вводим департамент")
    public void setGuaranteeDepartment(String value) {
        String fieldId = "product-departments__department-field";
        selectItemInModalByIdFromValue(fieldId, value);
        waitSpinner();
    }

    @Step("На вкладке \"Текущие условия\" вводим подразделение от бэк-офиса")
    public void setGuaranteeSupportUnit(String value) {
        String fieldId = "product-departments__support-unit-field";
        selectItemInModalByIdFromValue(fieldId, value);
        waitSpinner();
    }

    @Step("На вкладке \"Текущие условия\" вводим номер договора")
    public void setGuaranteeNumber(String value) {
        String fieldId = "general-convention-guarantee__number-field";
         setInputValue(value, fieldId);
    }

    @Step("На вкладке \"Текущие условия\" вводим примечания к договору")
    public void setGuaranteeNotes(String value) {
        String fieldId = "general-convention-guarantee__note-field";
        setInputValue(value, fieldId);
    }
}