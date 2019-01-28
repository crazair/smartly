package ru.sbtqa.smartly.datacreators.references;

import ru.sbtqa.smartly.dataobjects.Dictionary;
import ru.sbtqa.smartly.dataobjects.Product;

public class ReferenceProductKD extends AbstractReferenceProduct {

    @Override
    public Product getTemplateProduct() {
        return Product.createProduct()
                .setContractType("Кредит ЮЛ")
                .setLoanTreatment("Кредитный договор")
                .setReglament(new Dictionary(
                        "931 Регламент корпоративного кредитования юридических лиц и индивидуальных предпринимателей",
                        "931"))
                .setTemplateProduct(new Dictionary("931 Корпоративное кредитование - кредитный договор", "10001.1"))
                .setNumber("КД" + generateNumberProduct())
                .setBeginDate("18.02.2016")
                .setSignDate("16.02.2016")
                .setIssuanceDate("18.02.2016")
                .setEndDate("17.03.2017")
                .setLoanTerm("393")
                .setTotalValue("1300000")
                .setCurrency("Рубль")
                .setIssuanceCurrency("Рубль")
                .setPaymentType("Дифференцированный платеж")
                .setRepaymentOption("Крупный бизнес")
                .setRequestBKI(true)
                .setBranch(new Dictionary("013 - ЦЕНТРАЛЬНО-ЧЕРНОЗЕМНЫЙ БАНК ПАО СБЕРБАНК", "013"))
                .setFrontOffice(new Dictionary("0013-130013 - Центрально-Черноземный банк", "0013-130013"))
                .setBackOffice(new Dictionary(
                        "0042-86-12 - Управление поддержки корпоративных кредитов и операций на финансовых рынках",
                        "0042-86-12"))
                .setNotes("Тест")
                .setJsonFileName("etalonKD")
                .setReference(this)
                .build();
    }
}
