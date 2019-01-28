package ru.sbtqa.smartly.datacreators.references;

import org.apache.log4j.Logger;
import ru.sbtqa.smartly.dataobjects.Product;
import ru.sbtqa.smartly.pageobjects.*;
import ru.sbtqa.smartly.pageobjects.base.*;

import java.text.MessageFormat;
import java.util.Optional;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractReferenceProduct {

    public static final Logger LOG = Logger.getRootLogger();
    protected ClientsPage clientsPage = new ClientsPage();
    protected CommonPage commonPage = new CommonPage();
    protected ConditionPage conditionPage = new ConditionPage();
    protected FirstPage firstPage = new FirstPage();
    protected ProductCreationTemplatePage productTmpPage = new ProductCreationTemplatePage();

    public abstract Product getTemplateProduct();

    public Product createProduct(Product product) {
        Product result = null;
        if (result == null) {
            result = createUI(product);
        } else if (result == null) {
            throw new IllegalStateException("ProductCreator create! Создать продукт не удалось ни одним из креаторов!");
        }
        return result;
    }


    /**
     * Метод генерирующий номер договора
     */
    protected static String generateNumberProduct() {
        return randomNumeric(8);
    }

    /**
     * Метод создания договора через UI
     *
     * @param product - продукт с данными для создания
     */
    protected Product createUI(Product product) {
        commonPage.clickHomeButton();
        createProductTemplate(product);
        fillCondition(product);
        fillExtraCondition(product);
        LOG.info("#################################");
        LOG.info(" Создали договор с номером " + product.getNumber());
        LOG.info("#################################\n");
        commonPage.clickHomeButton();
        return product;
    }

    public void fillCondition(Product product) {
        conditionPage.setValueByName("Номер договора", product.getNumber());
        fillDates(product);
        fillAmount(product);
        fillLoanParam(product);
        fillDivision(product);
        conditionPage.clickNotificationMessages();
        conditionPage.clickLinkOrButtonByName("Сохранить");
        conditionPage.clickLinkOrButtonByNameInDialogWindow("Да");
        assertTrue(commonPage.getNotificationsWrapperText().contains(
                generateSaveMessage(product)));
    }

    private void createProductTemplate(Product product) {
        firstPage.clickLinkOrButtonByName("Клиенты");
        clientsPage.createProduct();
        setupTemplate(product);
        productTmpPage.clickLinkOrButtonByName("Создать договор");
    }

    /**
     * Метод настройки  мастера создания продукта
     *
     * @param product - продукт с данными для создания
     */
    protected void setupTemplate(Product product) {
        productTmpPage.fillFieldsNewProduct(product.getContractType(), product.getLoanTreatment(),
                                            product.getTemplateProduct().getName());
    }

    /**
     * Метод заполения полей отвечающих за ключевые даты договора
     *
     * @param product - продукт с данными для создания
     */
    protected void fillDates(Product product) {
        conditionPage.fillFieldsDatesKD(
                product.getSignDate(),
                product.getBeginDate(),
                product.getLoanTerm(),
                product.getIssuanceDate());
    }

    /**
     * Метод заполения полей отвечающих за параметры сумм договора
     *
     * @param product - продукт с данными для создания
     */
    protected void fillAmount(Product product) {
        conditionPage.fillFieldsAmounts(
                product.getTotalValue(),
                product.getCurrency(),
                product.getIssuanceCurrency());
    }

    /**
     * Метод заполения полей отвечающих за подразделение обслуживающие договора
     *
     * @param product - продукт с данными для создания
     */
    protected void fillDivision(Product product) {
        conditionPage.fillFieldsDivision(
                product.getBranch().getName(),
                product.getFrontOffice().getName(),
                product.getBackOffice().getName());
    }

    /**
     * Метод заполения полей отвечающих за доп. параметры договора
     *
     * @param product - продукт с данными для создания
     */
    protected void fillLoanParam(Product product) {
        conditionPage.setCheckbox("Согласие на запрос из БКИ", product.getRequestBKI());
        conditionPage.fillFieldsRepayment(product.getPaymentType(), product.getRepaymentOption());
        Optional.ofNullable(product.getNotes()).ifPresent(
                notes -> conditionPage.setValueByName("Примечания по договору (необязательно)", product.getNotes()));
    }

    /**
     * Метод определения строки успешного сохранения договора
     *
     * @param product - продукт с данными для создания
     */
    protected String generateSaveMessage(Product product) {
        return MessageFormat.format("Продукт номер {0} сохранен", product.getNumber());
    }

    /**
     * Метод заполнения параметров договра не связанных ч текущими условиями
     *
     * @param product - продукт с данными для создания
     */
    protected void fillExtraCondition(Product product) {

    }
}
