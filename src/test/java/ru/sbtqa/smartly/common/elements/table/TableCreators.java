package ru.sbtqa.smartly.common.elements.table;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/** Класс с методами для создание объектов Table */
public class TableCreators {

    private TableCreators() throws IllegalAccessException {
        throw new IllegalAccessException("TableCreators is utility class!");
    }

    /** Метод для создания объекта Table из selenideTable (HTML: <table>...<table/>) */
    public static Table createTable(SelenideElement selenideTable) {
        int countHeads = selenideTable.$$x(".//thead//td").size();
        Document table = Jsoup.parse(selenideTable.innerHtml(), WebDriverRunner.url(), new Parser(new XmlTreeBuilder()));
        Elements elements = table.select("td[class*=table__cell]");

        List<String> headings = new ArrayList<>(countHeads);
        for (int i = 0; i < countHeads; i++) {
            headings.add(elements.get(i).text());
        }

        List<List<String>> rows = new ArrayList<>();
        for (int i = countHeads; i < elements.size(); i += countHeads) {
            List<String> row = new ArrayList<>(countHeads);
            for (int j = 0; j < countHeads; j++) {
                row.add(elements.get(i + j).text());
            }
            rows.add(row);
        }

        return new Table(headings, rows);
    }

    /** Метод для создания объекта Table из шаблона JSON */
    public static Table createTable(ArrayNode graphNode) {
        List<String> headings = new ArrayList<>();
        List<List<String>> rows = new ArrayList<>();

        graphNode.get(0).fields().forEachRemaining(el -> headings.add(el.getKey()));

        graphNode.forEach(jsonNode -> {
            List<String> row = new ArrayList<>();
            jsonNode.fields().forEachRemaining(stringJsonNodeEntry -> row.add(stringJsonNodeEntry.getValue().asText()));
            rows.add(row);
        });

        return new Table(headings, rows);
    }
}
