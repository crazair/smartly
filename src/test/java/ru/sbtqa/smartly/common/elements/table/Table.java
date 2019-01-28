package ru.sbtqa.smartly.common.elements.table;

import java.text.MessageFormat;
import java.util.List;

/** Класс Table используется в автотестах для сравнения табличных объектов */
public class Table {

    /** Заголовки */
    private List<String> headings;
    /** Матрица строк */
    private List<List<String>> rows;

    public Table(List<String> headings, List<List<String>> rows) {
        this.headings = headings;
        this.rows = rows;
    }

    public List<String> getHeadings() {
        return headings;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public int getHeadingsCount() {
        return headings.size();
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getCellCount() {
        int count = 0;
        for (List<String> row : rows) {
            count += row.size();
        }
        return count;
    }

    /** Базовый метод. Сравниваем таблицы по размеру (кол-ву колонок и ячеек) */
    public CheckResult checkSize(Table table) {
        if (getHeadingsCount() != table.getHeadingsCount()) {
            return new CheckResult(false, MessageFormat.format("Разное кол-во колонок в таблицах! " +
                    "В первой таблице {0} заголовков. Во второй таблице {1} заголовков.",
                    getHeadingsCount(), table.getHeadingsCount()));
        }
        if (getCellCount() != table.getCellCount()) {
            return new CheckResult(false, MessageFormat.format("Разное кол-во ячеек в таблицах! " +
                            "В первой таблице {0} ячеек. Во второй таблице {1} ячеек.",
                    getCellCount(), table.getCellCount()));
        }
        return new CheckResult(true, null);
    }

    /** Базовый метод. Сравниваем таблицы по наименованию колонок*/
    public CheckResult checkHadings(Table table) {
        for (String head1 : getHeadings()) {
            boolean ch = false;
            for (String head2 : table.getHeadings()) {
                if (head1.equals(head2)) ch = true;
            }
            if (!ch) return new CheckResult(false, "Таблицы содержат разные заголовки! ");
        }
        return new CheckResult(true, null);
    }

    /** Базовый метод. Сравниваем таблицы по данным в ячейках*/
    public CheckResult checkCells(Table table) {
        for (String head1 : getHeadings()) {
            for (String head2 : table.getHeadings()) {
                if (head1.equals(head2)) {

                    //получаю индексты стоблов для сравнения т.к. столбцы могут быть перемешаны в таблицах
                    int indexT1 = getHeadings().indexOf(head1);
                    int indexT2 = table.getHeadings().indexOf(head2);

                    for (int indexRow = 0; indexRow < getRowCount(); indexRow++) {
                        if (!getRows().get(indexRow).get(indexT1).equals(table.getRows().get(indexRow).get(indexT2))) {
                            return new CheckResult(false, MessageFormat.format(
                                    "Разные данные в ячейках! Номер строки : {0} Номер столбца первой таблицы: {1}" +
                                            " Номер столбца второй таблицы: {2} Данные в ячейке первой таблицы: {3}" +
                                            " Данные в ячейке второй таблицы: {4}", indexRow + 1, indexT1 + 1,
                                    indexT2 + 1, getRows().get(indexRow).get(indexT1), table.getRows().get(indexRow).get(indexT2)));
                        }
                    }
                    break;
                }
            }
        }
        return new CheckResult(true, null);
    }
}
