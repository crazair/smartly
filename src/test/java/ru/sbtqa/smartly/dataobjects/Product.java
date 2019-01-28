package ru.sbtqa.smartly.dataobjects;

import ru.sbtqa.smartly.common.utils.Storage;
import ru.sbtqa.smartly.datacreators.references.AbstractReferenceProduct;

import static org.junit.platform.commons.util.StringUtils.isNotBlank;
import static ru.sbtqa.smartly.common.utils.DateUtils.getDateWithDeltaDays;
import static ru.sbtqa.smartly.common.utils.DateUtils.getPeriodDates;

public class Product {

    //заполнять можно только в билдере
    private String contractType;         //Тип продукта
    private String loanTreatment;        //Режим кредитования
    private Dictionary reglament;        //Регламент
    private Dictionary templateProduct;  //Шаблон продукта

    private String number;               //Номер договора
    private String beginDate;            //Дата начала
    private String signDate;             //Дата подписания КОД
    private String endDate;              //Дата окончания
    private String loanTerm;             //Срок договора(дн)
    private String totalValue;           //Сумма договора / Лимит задолженности (Для ВКЛ) / Лимит выдачи (Для НКЛ)
    private String currency;             //Валюта договора / лимита
    private String issuanceCurrency;     //Валюта выдачи
    private String issuanceDate;         //Дата доступности (Для ВКЛ) / Дата выдачи(Для КД, НКЛ)

    private String qualityCategory;      //категория качества
    private String feesAccrualDay;       //День начисления плат и процентов
    private String feesPaymentDay;       //День уплаты плат и процентов
    private String debtPaymentDay;       //День платежа по основному долгу

    private String paymentType;          //Способ погашения
    private String repaymentOption;      //Приоритет погашения
    private Boolean requestBKI;          //Чекбокс "Согласие на запрос из БКИ"
    private String notes;                //Примечания

    private Dictionary branch;           //Филиал
    private Dictionary frontOffice;      //Подразделение от фронт-офиса
    private Dictionary backOffice;       //Подразделение от бэк-офиса

    private String sourceId;             //id продукта в legacy системе
    private String jsonFileName;        //Имя файла шаблона в формате json

    private GridGainId contractId;       //id коннтракта в хранилище
    private GridGainId conditionId;      //id объекта условия в хранилище

    private AbstractReferenceProduct  reference;


    private Product() {
    }

    public static Product getProductFromStorage(String key) {
        return Storage.getFromStorage(key);
    }

    public String getContractType() {
        return contractType;
    }

    public String getLoanTreatment() {
        return loanTreatment;
    }

    public Dictionary getReglament() {
        return reglament;
    }

    public Dictionary getTemplateProduct() {
        return templateProduct;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;

        if(isNotBlank(beginDate) && isNotBlank(endDate)){
            loanTerm = getPeriodDates(beginDate, endDate);
        }
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIssuanceCurrency() {
        return issuanceCurrency;
    }

    public void setIssuanceCurrency(String issuanceCurrency) {
        this.issuanceCurrency = issuanceCurrency;
    }

    public String getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(String issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public String getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(String loanTerm) {
        this.loanTerm = loanTerm;

        if(isNotBlank(beginDate) && isNotBlank(loanTerm)){
            endDate = getDateWithDeltaDays(beginDate, Integer.parseInt(loanTerm));
        }
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getRepaymentOption() {
        return repaymentOption;
    }

    public void setRepaymentOption(String repaymentOption) {
        this.repaymentOption = repaymentOption;
    }

    public Boolean getRequestBKI() {
        return requestBKI;
    }

    public void setRequestBKI(Boolean requestBKI) {
        this.requestBKI = requestBKI;
    }

    public Dictionary getBranch() {
        return branch;
    }

    public void setBranch(Dictionary branch) {
        this.branch = branch;
    }

    public Dictionary getFrontOffice() {
        return frontOffice;
    }

    public void setFrontOffice(Dictionary frontOffice) {
        this.frontOffice = frontOffice;
    }

    public Dictionary getBackOffice() {
        return backOffice;
    }

    public void setBackOffice(Dictionary backOffice) {
        this.backOffice = backOffice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getJsonFileName() {
        return jsonFileName;
    }

    public void setJsonFileName(String jsonFileName) {
        this.jsonFileName = jsonFileName;
    }

    public GridGainId getContractId() {
        return contractId;
    }

    public void setContractId(GridGainId contractId) {
        this.contractId = contractId;
    }

    public GridGainId getConditionId() {
        return conditionId;
    }

    public void setConditionId(GridGainId conditionId) {
        this.conditionId = conditionId;
    }

    public AbstractReferenceProduct  getReference() {
        return reference;
    }

    public void setReference(AbstractReferenceProduct  reference) {
        this.reference = reference;
    }

    public String getQualityCategory() {
        return qualityCategory;
    }

    public void setQualityCategory(String qualityCategory) {
        this.qualityCategory = qualityCategory;
    }

    public String getFeesAccrualDay() {
        return feesAccrualDay;
    }

    public void setFeesAccrualDay(String feesAccrualDay) {
        this.feesAccrualDay = feesAccrualDay;
    }

    public String getFeesPaymentDay() {
        return feesPaymentDay;
    }

    public void setFeesPaymentDay(String feesPaymentDay) {
        this.feesPaymentDay = feesPaymentDay;
    }

    public String getDebtPaymentDay() {
        return debtPaymentDay;
    }

    public void setDebtPaymentDay(String debtPaymentDay) {
        this.debtPaymentDay = debtPaymentDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Product product = (Product) o;

        if (!contractType.equals(product.contractType)) {
            return false;
        }
        if (!loanTreatment.equals(product.loanTreatment)) {
            return false;
        }
        if (!reglament.equals(product.reglament)) {
            return false;
        }
        if (!templateProduct.equals(product.templateProduct)) {
            return false;
        }
        if (!number.equals(product.number)) {
            return false;
        }
        if (!beginDate.equals(product.beginDate)) {
            return false;
        }
        if (!signDate.equals(product.signDate)) {
            return false;
        }
        if (!endDate.equals(product.endDate)) {
            return false;
        }
        if (!loanTerm.equals(product.loanTerm)) {
            return false;
        }
        if (!totalValue.equals(product.totalValue)) {
            return false;
        }
        if (!currency.equals(product.currency)) {
            return false;
        }
        if (!issuanceCurrency.equals(product.issuanceCurrency)) {
            return false;
        }
        if (!issuanceDate.equals(product.issuanceDate)) {
            return false;
        }
        if (!paymentType.equals(product.paymentType)) {
            return false;
        }
        if (!repaymentOption.equals(product.repaymentOption)) {
            return false;
        }
        if (!requestBKI.equals(product.requestBKI)) {
            return false;
        }
        if (!branch.equals(product.branch)) {
            return false;
        }
        if (!frontOffice.equals(product.frontOffice)) {
            return false;
        }
        if (!notes.equals(product.notes)) {
            return false;
        }

        return backOffice.equals(product.backOffice);
    }

    @Override
    public int hashCode() {
        int result = contractType.hashCode();
        result = 31 * result + loanTreatment.hashCode();
        result = 31 * result + reglament.hashCode();
        result = 31 * result + templateProduct.hashCode();
        result = 31 * result + number.hashCode();
        result = 31 * result + beginDate.hashCode();
        result = 31 * result + signDate.hashCode();
        result = 31 * result + endDate.hashCode();
        result = 31 * result + loanTerm.hashCode();
        result = 31 * result + totalValue.hashCode();
        result = 31 * result + currency.hashCode();
        result = 31 * result + issuanceCurrency.hashCode();
        result = 31 * result + issuanceDate.hashCode();
        result = 31 * result + paymentType.hashCode();
        result = 31 * result + repaymentOption.hashCode();
        result = 31 * result + requestBKI.hashCode();
        result = 31 * result + branch.hashCode();
        result = 31 * result + frontOffice.hashCode();
        result = 31 * result + backOffice.hashCode();
        result = 31 * result + notes.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "contractType='" + contractType +
                ", loanTreatment='" + loanTreatment + '\'' +
                ", reglament='" + reglament + '\'' +
                ", templateProduct='" + templateProduct + '\'' +
                ", number='" + number + '\'' +
                ", beginDate='" + beginDate + '\'' +
                ", signDate='" + signDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", loanTerm='" + loanTerm + '\'' +
                ", totalValue='" + totalValue + '\'' +
                ", currency='" + currency + '\'' +
                ", issuanceCurrency='" + issuanceCurrency + '\'' +
                ", issuanceDate='" + issuanceDate + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", repaymentOption='" + repaymentOption + '\'' +
                ", requestBKI=" + requestBKI +
                ", branch='" + branch + '\'' +
                ", frontOffice='" + frontOffice + '\'' +
                ", backOffice='" + backOffice + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

    public static Builder createProduct() {
        return new Product().new Builder();
    }

    public class Builder {

        private Builder() {
        }

        public Builder setContractType(String contractType) {
            Product.this.contractType = contractType;
            return this;
        }

        public Builder setLoanTreatment(String loanTreatment) {
            Product.this.loanTreatment = loanTreatment;
            return this;
        }

        public Builder setReglament(Dictionary reglament) {
            Product.this.reglament = reglament;
            return this;
        }

        public Builder setTemplateProduct(Dictionary templateProduct) {
            Product.this.templateProduct = templateProduct;
            return this;
        }

        public Builder setNumber(String number) {
            Product.this.number = number;
            return this;
        }

        public Builder setBeginDate(String beginDate) {
            Product.this.beginDate = beginDate;
            return this;
        }

        public Builder setSignDate(String signDate) {
            Product.this.signDate = signDate;
            return this;
        }

        public Builder setEndDate(String endDate) {
            Product.this.endDate = endDate;
            if(isNotBlank(beginDate) && isNotBlank(endDate)){
                loanTerm = getPeriodDates(beginDate, endDate);
            }
            return this;
        }

        public Builder setLoanTerm(String loanTerm) {
            Product.this.loanTerm = loanTerm;
            if(isNotBlank(beginDate) && isNotBlank(loanTerm)){
                endDate = getDateWithDeltaDays(beginDate, Integer.parseInt(loanTerm));
            }
            return this;
        }

        public Builder setTotalValue(String totalValue) {
            Product.this.totalValue = totalValue;
            return this;
        }

        public Builder setCurrency(String currency) {
            Product.this.currency = currency;
            return this;
        }

        public Builder setIssuanceCurrency(String issuanceCurrency) {
            Product.this.issuanceCurrency = issuanceCurrency;
            return this;
        }

        public Builder setIssuanceDate(String issuanceDate) {
            Product.this.issuanceDate = issuanceDate;
            return this;
        }

        public Builder setPaymentType(String paymentType) {
            Product.this.paymentType = paymentType;
            return this;
        }

        public Builder setRepaymentOption(String repaymentOption) {
            Product.this.repaymentOption = repaymentOption;
            return this;
        }

        public Builder setRequestBKI(Boolean requestBKI) {
            Product.this.requestBKI = requestBKI;
            return this;
        }

        public Builder setBranch(Dictionary branch) {
            Product.this.branch = branch;
            return this;
        }

        public Builder setFrontOffice(Dictionary frontOffice) {
            Product.this.frontOffice = frontOffice;
            return this;
        }

        public Builder setBackOffice(Dictionary backOffice) {
            Product.this.backOffice = backOffice;
            return this;
        }

        public Builder setNotes(String notes) {
            Product.this.notes = notes;
            return this;
        }

        public Builder setSourceId(String sourceId) {
            Product.this.sourceId = sourceId;
            return this;
        }

        public Builder setJsonFileName(String jsonFilePath) {
            Product.this.jsonFileName = jsonFilePath;
            return this;
        }


        public Builder setReference(AbstractReferenceProduct reference) {
            Product.this.setReference(reference);
            return this;
        }

        public Builder setQualityCategory(int qualityCategory) {
            Product.this.qualityCategory = String.valueOf(qualityCategory);
            return this;
        }

        public Builder setFeesAccrualDay(String accrualDate) {
            Product.this.feesAccrualDay = accrualDate;
            return this;
        }

        public Builder setFeesPaymentDay(String paymentDate) {
            Product.this.feesPaymentDay = paymentDate;
            return this;
        }

        public Builder setDebtPaymentDay(String debtPaymentDay){
            Product.this.debtPaymentDay = debtPaymentDay;
            return this;
        }

        public Product build() {
            return Product.this;
        }
    }

}
