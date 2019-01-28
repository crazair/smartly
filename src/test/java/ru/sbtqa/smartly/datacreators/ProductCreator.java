package ru.sbtqa.smartly.datacreators;

import org.apache.log4j.Logger;
import ru.sbtqa.smartly.dataobjects.Product;

public class ProductCreator {

    public static final Logger LOG = Logger.getRootLogger();

    public static Product create(Product product) {
        LOG.info("Пытаемся создать продукт: " + product.getLoanTreatment());
        return product.getReference().createProduct(product);
    }
}


