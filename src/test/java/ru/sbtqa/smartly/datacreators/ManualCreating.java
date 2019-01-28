package ru.sbtqa.smartly.datacreators;

import org.apache.log4j.Logger;
import ru.sbtqa.smartly.common.BaseTestSuit;
import ru.sbtqa.smartly.dataobjects.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManualCreating {

    public static final Logger LOG = Logger.getRootLogger();

    /**
     * Создание продуктов "руками"
     * <p>
     * Внимание! Перед запуском необходимо изменить рабочую дирректорию в конфигурации запуска:
     * необходимо указание папки модуля!
     * Путь в IDEA: Меню Run -> Edit Configurations... -> Working Directory
     * Например: "C:\IdeaProjects\loans4bAT\loans4buitest"
     * <p>
     * int countProductsGen - Количество продуктов для генерации
     * Product productGen   - Продукты, которые будут создаваться
     */
    public static void main(String[] args) throws Exception {
        int countProductsGen = 2;
        Product productGen = EtalonProducts.etalonProductKD();

        manualCreateProduct(countProductsGen, productGen);
    }

    /**
     * Метод массового создания продуктов
     *
     * @param countProducts количество продуктов
     * @param product       продукт
     */
    private static void manualCreateProduct(int countProducts, Product product) {
        new BaseTestSuit().beforeTestSuite();

        List<String> numberProducts = new ArrayList<String>();
        String number = product.getNumber();

        for (int i = 1; i <= countProducts; i++) {
            try {
                product.setNumber(number + "_" + i);
                Product pResult = ProductCreator.create(product);
                Optional.ofNullable(pResult).ifPresent(r -> numberProducts.add(r.getNumber()));
            } catch (Exception e) {
                LOG.error("Exception ManualCreating manualCreateProduct", e);
            }
        }

        LOG.info("***** Нагенерировали новых продуктов!!! *****");
        numberProducts.forEach(s -> LOG.info(s));
        LOG.info("*********************************************");
    }
}