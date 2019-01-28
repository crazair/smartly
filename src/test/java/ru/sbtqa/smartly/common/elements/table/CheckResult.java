package ru.sbtqa.smartly.common.elements.table;

/** Класс CheckResult используется для предоставления результатов сверок (например тублиц) */
public class CheckResult {

    /** Результат проверки */
    private boolean result;
    /** Результат сообщение, например об ошибке */
    private String mess;

    public CheckResult(boolean result, String mess) {
        this.result = result;
        this.mess = mess;
    }

    public boolean isResult() {
        return result;
    }

    public String getMess() {
        return mess;
    }
}
