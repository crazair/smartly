package ru.sbtqa.smartly.testsuites;

import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.sbtqa.smartly.common.BaseTestSuit;

@Tag("REGRESS")
@DisplayName("Regress тесты продукта!!!")
public class RegressTests extends BaseTestSuit {

    @Test
    @Owner("anosv")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("01 Проверка Regress")
    public void regress01() {
        //TODO

        Assertions.assertEquals(1, 1);
    }

}
