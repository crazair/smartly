package ru.sbtqa.smartly.datacreators;

import ru.sbtqa.smartly.datacreators.references.ReferenceProductKD;
import ru.sbtqa.smartly.dataobjects.Product;

public class EtalonProducts {
    static ReferenceProductKD referenceProductKD = new ReferenceProductKD();

    /**
     * Метод создает эталонный договор КД (Текущие условия)
     */
    public static Product etalonProductKD() {
        return referenceProductKD.getTemplateProduct();
    }

}
