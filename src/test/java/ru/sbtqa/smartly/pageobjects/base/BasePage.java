package ru.sbtqa.smartly.pageobjects.base;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import ru.sbtqa.smartly.common.elements.table.CheckResult;
import ru.sbtqa.smartly.common.elements.table.Table;
import ru.sbtqa.smartly.common.elements.table.TableCreators;
import ru.sbtqa.smartly.common.utils.JsonUtils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.fail;
import static ru.sbtqa.smartly.common.utils.PropertyUtils.prop;

/**
 * НАБОРЫ БАЗОВЫХ НИЗКОУРОВНЕВЫХ МЕТОДОВ:
 * - Методы "кликеры"   (кликающие на link, button)
 * - Методы "сетторы"   (устанавливающие или очищающие значения в input, selectbox)
 * - Методы "геттеры"   (получающие значения из input, selectbox)
 * - Методы "утилитные" (Например: ожидание спинера)
 * - Методы "табличные"
 */
public class BasePage {

    public static final Logger LOG = Logger.getRootLogger();
    public static final int TIMEOUT = Integer.parseInt(prop("timeoutMilliseconds", "120000"));

    /*################################################################################################*/
    /*######################################## Методы кликеры ########################################*/

    /**
     * Универсальный метод, используемый для нажатия на ссылки/кнопки по их имени.
     *
     * @param fieldName - имя поля/ссылки, на которую произойдет нажатие
     */
    @Step("Переходим по ссылке или кнопке <{0}>")
    public void clickLinkOrButtonByName(String fieldName) {
        waitSpinner();
        String xpathExpression = "//*[text()='" + fieldName + "']";
        LOG.info("Кликаем по ссылке/кнопке: " + fieldName + ". xpath: " + xpathExpression);
        click(xpathExpression, 10, 10);
    }

    /**
     * Универсальный метод, используемый для нажатия на ссылки/кнопки по их имени В сплывающем окне
     *
     * @param fieldName - имя поля/ссылки, на которую произойдет нажатие
     */
    @Step("Переходим по ссылке или кнопке <{0}> в диалоговом окне")
    public void clickLinkOrButtonByNameInDialogWindow(String fieldName) {
        String xpathExpression = "//div[contains(@class,'dialog-window__content')]//*[text()='" + fieldName + "']";
        LOG.info("Кликаем по ссылке/кнопке : " + fieldName + " в диалоговом окне. xpath: " + xpathExpression);
        click(xpathExpression, 10, 10);
    }

    /**
     * Универсальный метод, используемый для нажатия на ссылки/кнопки по их имени, которое расположено сбоку
     *
     * @param fieldName - имя поля/ссылки, на которую произойдет нажатие
     */
    @Step("Переходим по ссылке или кнопке <{0}>")
    public void clickLinkOrButtonBySiblingName(String fieldName) {
        String xpathExpression = "//div[contains(text(), '" + fieldName + "')]/following-sibling::div//a";
        LOG.info("Кликаем по ссылке/кнопке : " + fieldName + ". xpath: " + xpathExpression);
        click(xpathExpression, 10, 10);
    }

    /**
     * Универсальный метод, используемый для нажатия на ссылки/кнопки по их имени в модальных окнах
     *
     * @param fieldName - имя поля/ссылки, на которую произойдет нажатие (в модальных окнах)
     */
    @Step("Переходим по ссылке или кнопке <{0}> в модальном окне")
    public void clickLinkOrButtonByNameInModal(String fieldName) {
        clickElementInModal(fieldName, false);
    }

    /**
     * Метод для нажатия кнопок на тулбаре (правый верхний угол), например: Сервисы, Мастер система и т.п.
     *
     * @param buttonName - имя/хинт кнопки на тулбаре
     */
    @Step("Переходим по кнопке <{0}> в тулбаре")
    public void clickButtonInToolbar(String buttonName) {
        String xpathExpression = "//button[@title='" + buttonName + "' or span//text() = '" + buttonName + "']";
        LOG.info("Кликаем по кнопке : " + buttonName + " на тулбаре. xpath: " + xpathExpression);
        $x(xpathExpression).waitUntil(visible, 1000).click();
    }

    /**
     * Метод, используемый для нажатия на кнопки по элементу в определённой области с именем.
     *
     * @param workspaceName - область, в которой находится кнопка
     * @param buttonName - название кнопки
     */
    @Step("Кликаем по кнопке <{1}> в области <{0}>")
    public void clickButtonInWorkspaceByName(String workspaceName, String buttonName) {
        clickLinkOrButtonInWorkspaceByName(workspaceName, buttonName);
    }

    /**
     * Метод, используемый для нажатия на кнопки по элементу в определённой области с именем.
     *
     * @param workspaceName - область, в которой находится кнопка
     * @param fieldName - имя поля/ссылки, на которую произойдет нажатие
     */
    @Step("Кликаем по значению <{1}> в рабочей области <{0}>")
    public void clickLinkOrButtonInWorkspaceByName(String workspaceName, String fieldName) {
        LOG.info("Кликаем по ссылке/кнопке : " + fieldName + " в рабочей области: " + workspaceName);
        SelenideElement selectElement = getWorkspaceElement(workspaceName);
        selectElement.$x("..//*[contains(text(), '" + fieldName + "')]").click();
        waitSpinner();
    }

    /**
     * Базовый ПРИВАТНЫЙ метод нажатия на ссылку/кнопку по xPath
     * Метод использует смещение, чтобы при нажатии не попадать в границы элемента
     *
     * @param xpathExpression - xPath для поиска элемента
     *
     * with a relative offset from the upper left corner of the element
     */
    private void click(String xpathExpression, int offsetX, int offsetY) {
        $x(xpathExpression).scrollTo().click(offsetX, offsetY);
        waitSpinner();
    }

    /*################################################################################################*/
    /*######################################## Методы сетторы ########################################*/

    /**
     * Метод для проверки возможности заполнения текстовых полей ввода
     *
     * @param fieldName - имя поля, в которое вводится значение
     **/
    public boolean isEditableFieldByName(String fieldName) {
        String xpathExpression = "//div[@class='field__label'][text()='" + fieldName + "']/following-sibling::div//*" +
                "[local-name()='input' or local-name()='textarea' or div[contains(@class,'value')]]";
        LOG.info("Проверяем возможность заполнения поля \"" + fieldName + "\". xpath: " + xpathExpression);
        boolean result = $x(xpathExpression).getAttribute("class").contains("value");
        LOG.info("Результат: " + result);
        return result;
    }

    /**
     * Метод для заполнения полей ввода
     *
     * @param fieldName - имя поля, в которое вводится значение
     * @param value - значение, которое вводится в поле
     **/
    @Step("Устанавливаем значение <{1}> в поле <{0}>")
    public void setValueByName(String fieldName, Object value) {
        String xpathExpression = "//div[@class='field__label'][text()='" + fieldName + "']/following-sibling::div//*" +
                "[local-name()='input' or local-name()='textarea' or div[contains(@class,'value')]]";
        LOG.info("Вводим значение \"" + value + "\" в поле \"" + fieldName + "\". xpath: " + xpathExpression);
        $x(xpathExpression).setValue(value.toString());
    }

    /**
     * Метод для заполнения полей ввода
     *
     * @param workSpaceName - имя области, в которое нужно вводить значение
     * @param fieldName - имя поля, в которое вводим значение
     * @param value - значение, которое вводится в поле
     **/
    @Step("Устанавливаем значение <{2}> в поле <{1}> в области <{0}>")
    public void setValueInWorkSpaceByName(String workSpaceName, String fieldName, String value) {
        String xpathExpression = "//div[contains(concat(' ', @class, ' '), ' workspace ') and .//div[@class='workspace__bar'"
                + " and .//div[contains(@class, 'h3') and text()='" + workSpaceName + "']]]//"
                + "div[contains(concat(' ', @class, ' '), ' field ') and .//div[contains(concat(' ', @class, ' '), ' field__label ')"
                + " and text()='" + fieldName + "']]//*[local-name()='input' or local-name()='textarea' or div[contains(@class,'value')]]";
        LOG.info("Вводим значение \"" + value + "\" в поле \"" + workSpaceName + "\". xpath: " + xpathExpression);
        $x(xpathExpression).setValue(value).click();
    }

    /**
     * Метод для выбора значения в элементе типа Dropbox(выпадающий список)
     *
     * @param fieldName - имя поля (обычно находится над списком, например, "Валюта договора")
     * @param fieldValue - значение, которое необходимо выбрать из списка
     */
    @Step("Выбираем значение <{1}> в ниспадающем списке <{0}>")
    public void selectFromDropBox(String fieldName, String fieldValue) {
        String xpathField = "//div[@class='field__label'][text()='" + fieldName + "']/following-sibling::div";
        String xpathValue = "//div[contains(@class,'dropdown__list-item')]//div[@class='dropdown__title' and text()='"
                + fieldValue + "']";
        LOG.info("Выбираем значение \"" + fieldValue + "\" из выпадающего списка \"" + fieldName + "\". " +
                         "xpathField: " + xpathField + ". xpathValue:" + xpathValue);
        $x(xpathField).click();
        $x(xpathValue).click();
        waitSpinner();
    }



    /**
     * Метод для выбора значения в элементе типа Dropbox(выпадающий список)
     *
     * @param elementSubtitle - имя поля (обычно находится над списком, например, "Валюта договора")
     * @param selectValue - значение, которое необходимо выбрать из списка
     */
    @Step("Выбираем значение <{1}> в ниспадающем списке <{0}>")
    public void selectFromDropBoxBySubtitle(String elementSubtitle, String selectValue) {
        SelenideElement selectElement = $x("//div[@class='field__label'][contains(text(),'" +
                                             elementSubtitle +
                                             "')]/following-sibling::div");
        selectElement.click();
        LOG.info("Выбираем в списке значение: " + selectValue);
        selectElement.$x("//div[contains(@class, 'dropdown__list-item')]//div[contains(@class, 'dropdown')"
                                 + " and text() = '" + selectValue + "' ]").click();
        waitSpinner();
    }

    /**
     * Метод для выбора значения в элементе типа Dropbox(выпадающий список)
     *
     * @param elementSubtitle - имя поля (обычно находится над списком, например, "Валюта договора")
     * @param selectValue - значение, которое необходимо выбрать из списка
     * @param workspaceTitle - заголовок workspace
     */
    @Step("Выбираем значение <{1}> в ниспадающем списке <{0}> в рабочей области <{2}>")
    public void selectFromDropBoxBySubtitleinWorkspace(String elementSubtitle, String selectValue, String workspaceTitle) {
        SelenideElement selectElement = $x("//div[contains(concat(' ', @class, ' '), ' workspace ') and .//div[@class='workspace__bar'"
                                      + " and .//div[contains(@class, 'h3') and text()='" +
                                         workspaceTitle + "']]]//div[@class='field__label'][contains(text(),'" +
                                                   elementSubtitle + "')]/following-sibling::div");
        selectElement.click();
        LOG.info("Выбираем в списке значение: " + selectValue);
        selectElement.$x("//div[contains(@class, 'dropdown__list-item')]//div[contains(@class, 'dropdown')"
                                 + " and text() = '" + selectValue + "' ]").click();
        waitSpinner();
    }

    /**
     * Метод для получения элемента в workspace
     *
     * @param elementSubtitle - имя поля (обычно находится над списком, например, "Валюта договора")
     * @param workspaceTitle - заголовок workspace
     * @return найденный элемент
     */
    @Step("Выбираем в рабочей области <{0}> элемент <{1}> ")
    public SelenideElement getElementFromWorkspace(String workspaceTitle, String elementSubtitle) {
        SelenideElement selectElement = $x("//div[contains(concat(' ', @class, ' '), ' workspace ') and .//div[@class='workspace__bar'"
                                                   + " and .//div[contains(@class, 'h3') and text()='" +
                                                   workspaceTitle + "']]]//div[@class='field__label'][contains(text(),'" +
                                                   elementSubtitle + "')]/following-sibling::div");
        return selectElement;
    }

    /**
     * Метод для получения элемента в workspace
     *
     * @param workspaceTitle - заголовок workspace
     * @param elementSubtitle - имя поля (обычно находится над списком, например, "Валюта договора")
     * @param value - значение элемента
     */
    @Step("Устанавливаем в рабочей области <{0}> поле <{1}> в значение  <{2}>")
    public void setFieldByNameInWorkspace(String workspaceTitle, String elementSubtitle, String value) {
        SelenideElement selectElement = getElementFromWorkspace(workspaceTitle, elementSubtitle);
        selectElement.$x(".//*[local-name()='input' or local-name()='textarea' or div[contains(@class,'value')]]").setValue(value);
    }

    /**
     * Метод для выбора значения в элементе типа Dropbox(выпадающий список)
     *
     * @param htmlName - значение атрибута name договора
     * @param selectValue - значение, которое необходимо выбрать из списка
     */
    @Step("Выбираем значение <{1}> в ниспадающем списке <{0}>")
    public void selectFromDropBoxByElementName(String htmlName, String selectValue) {
        SelenideElement selectElement = $(By.name(htmlName));
        selectElement.click();
        LOG.info("Выбираем в списке значение: " + selectValue);
        selectElement.$x("//div[contains(@class, 'dropdown__list-item')]//div[contains(@class, 'dropdown')"
                                + " and text() = '" + selectValue + "' ]").click();
        waitSpinner();
    }

    /**
     * Метод для установки значения в элементе типа Checkbox
     *
     * @param checkBoxName - имя поля (обычно находится над списком, например, "Валюта договора")
     * @param checkStatus - значение, которое необходимо установить
     */
    @Step("Устанавливаем значение <{1}> в чекбоксе <{0}>")
    public void setCheckbox(String checkBoxName, Boolean checkStatus) {
        if (isChecked(checkBoxName) != checkStatus) {
            $x("//div[contains(@class,'checkable') and text()='" + checkBoxName + "']").click();
        }
        LOG.info("Устанавливаем значение \"" + checkStatus + "\" в чекбоксе \"" + checkBoxName);
    }

    /*################################################################################################*/
    /*######################################## Методы геттеры ########################################*/

    /**
     * Метод для получения значения из поля/ссылки по имени
     *
     * @param fieldName - имя поля, из которого получаем значение
     **/
    @Step("Получаем значение из списка с именем <{0}>")
    public String getValueByName(String fieldName) {
        String xpathExpression = "//div[@class='field__label'][text()='" + fieldName + "']/following-sibling::div//*" +
                "[local-name()='a' or local-name()='input' or local-name()='textarea' or div[contains(@class,'value')]]";
        LOG.info("Получаем значение из поля \"" + fieldName + "\". xpath: " + xpathExpression);
        SelenideElement element = $x(xpathExpression);
        return element.getValue() == null ? element.getText() : element.getValue();
    }

    /*################################################################################################*/
    /*######################################## Методы утилитные ######################################*/

    /**
     * Метод для ожидания скрытия спинера или окончания загрузки страницы
     * Метод реализовал как поиск всех спинеров т.к. в DOM их может быть несколько!
     *
     * PROTECTED т.к. не правильно его использовать из тестов - это своего рода
     * костыль. Использовать его только из Pages!
     */
    protected static void waitSpinner() {
        long startTime = System.nanoTime();
        $$x("//div[starts-with(@class, 'spinner')]").forEach(el -> el.waitUntil(disappears, TIMEOUT));
        LOG.info("Ожидание исчезновения спинера мс: " + ((System.nanoTime() - startTime) / 1000000));
    }

    /**
     * Метод для проверки установлен или снят чекбокс. Возвращает true - если установлен
     *
     * @param checkBoxName - имя чекбокса
     */
    public boolean isChecked(String checkBoxName) {
        String xpath = "//div[contains(@class,'checkable') and text()='" + checkBoxName + "']/preceding-sibling::div/div" +
                " | //div[contains(@class,'checkable')]/span[text()='" + checkBoxName + "']/../preceding-sibling::div/div";
        LOG.info("Проверяем установлен или снят чекбокс. xpath: " + xpath);
        boolean checked = $x(xpath).getAttribute("class").contains("checked");
        LOG.info("Результат: " + checked);
        return checked;
    }

    /**
     * Метод возвращает сообщение об ошибке, выпадающее снизу поля при вводе недопустимых значений
     *
     * @param fieldName - имя поля
     */
    @Step("Проверяем сообщение о недопустимости значения в поле <{0}>")
    public String getDropDownErrorMessage(String fieldName) {
        String xpath = "//div[@class='page__dropdown-layer']";
        $x("//div[@class='field__label'][text()='" + fieldName + "']/following-sibling::div//*" +
                   "[local-name()='input' or local-name()='textarea' or div[contains(@class,'value')]]").click();
        LOG.info("Проверяем сообщение о недопустимости значения в поле " + fieldName + ". xpath: " + xpath);
        return $x(xpath).getText();
    }

    /**
     * Метод для закрытия рабочей области (значок "X" в верхнем правом углу)
     *
     * @param workspaceName - передаем имя области, чтобы исключить закрытие других элементов и обеспечить уникальность
     */
    @Step("Закрываем рабочую область <{0}>")
    public void closeWorkspace(String workspaceName) {
        LOG.info("Закрываем рабочую область " + workspaceName);
        getWorkspaceElement(workspaceName).$x(".//button[contains(@class,'button_textless')]//span[contains(@class,'close')]").click();
    }

    public SelenideElement getWorkspaceElement(String workspaceName) {
        return $x("//div[@class='workspace__bar' and .//div[contains(@class, 'h3') and "
                   + "contains(text(),'" + workspaceName + "')]]");
    }

    /*################################################################################################*/
    /*######################################## Методы для работы с таблицами #########################*/

    /**
     * Метод для выбора строки из таблицы по значению из любого столбца
     *
     * @param value - значение, по которому ищем в таблице
     */
    @Step("Выбираем строку из таблицы по значению <{0}>")
    public void selectInTable(String value) {
        String xpathExpression = "//div[@class='table__cell-content'][text()='" + value + "']";
        LOG.info("Выбираем строку из таблицы по значению " + value + ". xpath: " + xpathExpression);
        $$x(xpathExpression).filter(visible).last().click();
        waitSpinner();
    }

    /**
     * Метод для получения текста строки из таблицы по значению в строке
     *
     * @param value - значение, по которому ищем в таблице
     */
    @Step("Получает текст строки из таблицы по значению <{0}> в строке")
    public String getTableLineTextFromValue(String value) {
        String xpathExpression = "//div[@class='table__cell-content'][text()='" + value + "']/../..";
        LOG.info("Получаем текст строки из таблицы по значению в строке " + value + ". xpath: " + xpathExpression);
        String text = $x(xpathExpression).getText();
        LOG.info("Text: " + text);
        return text;
    }

    /**
     * Метод для получения текста строки из таблицы. Берем любое уникальное наименование столбца и с него выходим на строку
     *
     * @param columnName - имя столбца, по которому выходим на строку таблицы
     */
    @Step("Получает текст строки из таблицы по значению <{0}> в строке")
    public String getTableLineTextByColumnName(String columnName) {
        String xpathExpression = "//div[text()='" + columnName + "']//..//..//..//following-sibling::tbody";
        LOG.info(
                "Получаем текст строки из таблицы по значению в столбце " + columnName + ". xpath: " + xpathExpression);
        String text = $x(xpathExpression).getText();
        LOG.info("Text: " + text);
        return text;
    }

    /**
     * Метод для получения текста строки из таблицы по значению в строке. Для модального окна!
     *
     * @param value - значение, по которому ищем в таблице
     */
    @Step("Получает текст строки из таблицы по значению <{0}> в строке")
    public String getTableLineTextFromValueInModal(String value) {
        String xpathExpression = "//div[contains(@class,'modal')]//div[@class='table__cell-content'][text()='" + value + "']/../..";
        LOG.info(
                "Получаем текст строки из таблицы (в модальном окне) по значению в строке " + value + ". xpath: " + xpathExpression);
        String text = $x(xpathExpression).getText();
        LOG.info("Text: " + text);
        return text;
    }

    /**
     * Метод для выбора значения в постраничном списке
     *
     * @param textToFind - искомое значение
     */
    @Step("Ищем и выбираем значение <{0}> в постраничном списке")
    public void findAndSelectFromPageList(String textToFind) {
        LOG.info("Ищем и выбираем значение " + textToFind + " в постраничном списке");
        SelenideElement firstPage = $x("//div[contains(@class,'table__pagination-item') and text()='1']");
        SelenideElement lastPage = $x("(//div[@class='table__pagination-item' or " +
                                              "@class='table__pagination-item table__pagination-item_current'])[last()]");
        SelenideElement currentPage = $x("//div[@class='table__pagination-item table__pagination-item_current']");
        SelenideElement webEl = $x("//tbody[@class='table__tbody']//*[text()='" + textToFind + "']");
        SelenideElement xpathArrow = $x("//div[@class='forward table__pagination-item table__pagination-item_arrow']");
        if (webEl.is(exist)) {
            webEl.click();
        } else {
            firstPage.click();
            while (!webEl.is(exist) && !currentPage.getText().equals(lastPage.getText())) {
                xpathArrow.click();
            }
            webEl.click();
            LOG.info("Выбираем элемент " + textToFind + " на странице " + currentPage.getText());
        }
    }

    /**
     * Метод для заполнения полей для ввода (!input!) в таблице
     *
     * @param tableName - уникальный идентификатор таблицы (заголовок или имя)
     * @param rowNumber - номер строки
     * @param columnNamesAndValues - переменное число параметров в формате "Наименование столбца:значение для столбца",
     * разделитель - ":". Например: Дата начала:02.02.2018
     */
    @Step("Заполняем строку <{1}> таблицы <{0}> значениями...")
    public void fillTableRow(String tableName, int rowNumber, String... columnNamesAndValues) {
        SelenideElement table = findTableByTitle(tableName).first();
        List<String> headers = table.$$x("./thead//td").texts();

        Arrays.stream(columnNamesAndValues)
              .map(s -> s.split(":"))  // преобразуем каждый элемент стрима: String в массив String[]
              .forEach(s -> {
                  String inputXPath = "./tbody/tr[" + rowNumber + "]/td[" + (headers.indexOf(
                          s[0]) + 1) + "]//input"; // создаём XPath поля для ввода
                  LOG.info("Вводим в поле " + s[0] + " по xPath " + inputXPath + " Значение " + s[1]);
                  table.$x(inputXPath).setValue(
                          s[1]);                                                                // заполняем поле для ввода
              });
    }

    /**
     * Метод посторочной сверки таблицы UI с шаблоном
     *
     * @param tableName - уникальный идентификатор таблицы (заголовок таблицы или рабочей области)
     * @param templateResource - имя ресурса файла json содержащего шаблоны
     * @param patternCode - код шаблона внутри json файла
     */
    @Step("Сверяем идентичность таблицы <{0}> с шаблоном <{2}> из файла ресурсов <{1}>")
    public void compareTable(String tableName, String templateResource, String patternCode) {
        LOG.info("Находим таблицу на форме!");
        SelenideElement selenideTable = findTableByTitle(tableName).first();
        LOG.info("Создаём объект Table из SelenideElement!");
        Table tableForm = TableCreators.createTable(selenideTable);

        LOG.info("Находим JSON и создаём из него объект Table!");
        Table tableJSON = TableCreators.createTable(JsonUtils.loadJsonTemplate(templateResource, patternCode));

        LOG.info("Сравниваем таблицы по размеру!");
        CheckResult checkResultSize = tableForm.checkSize(tableJSON);
        if (!checkResultSize.isResult()) {
            fail(checkResultSize.getMess());
        }

        LOG.info("Сравниваем таблицы по всем ячейкам! (колонки могут быть плавающими)");
        CheckResult checkResultCells = tableForm.checkCells(tableJSON);
        if (!checkResultCells.isResult()) {
            fail(checkResultCells.getMess());
        }
    }

    /**
     * @param tableName - уникальный идентификатор таблицы (заголовок таблицы или рабочей области)
     * @param DBTable - Объект Table
     */
    @Step("Сверяем идентичность таблицы {0} в ЕКС и ППРБ")
    public void compareTable(String tableName, Table DBTable) {
        LOG.info("Находим таблицу на форме!");
        SelenideElement selenideTable = findTableByTitle(tableName).first();

        LOG.info("Создаём объект Table из SelenideElement!");
        Table tableForm = TableCreators.createTable(selenideTable);

        LOG.info("Сравниваем таблицы по всем ячейкам! (колонки могут быть плавающими)");
        CheckResult checkResultCells = tableForm.checkCells(DBTable);
        if (!checkResultCells.isResult()) {
            fail(checkResultCells.getMess());
        }
    }

    /**
     * Метод определения кол-ва строк в таблице по заголовку таблицы или рабочей области
     *
     * @param tableName - уникальный идентификатор таблицы (заголовок таблицы или рабочей области)
     */
    public int numberOfRowsTable(String tableName) {
        LOG.info("Находим таблицу на форме!");
        SelenideElement selenideTable = findTableByTitle(tableName).first();

        LOG.info("Создаём объект Table из SelenideElement!");
        Table tableForm = TableCreators.createTable(selenideTable);

        return (tableForm.getRowCount());
    }

    /**
     * @param workspace - заголовок таблицы или рабочей области (находит по вхождению)
     * @param keyValue - Строка в которой ищем нужное значение(строка в формате "Наименование столбца:значение для столбца",
     *                 разделитель - ":". Например - Наименование:Плата за резервирование)
     * @param column - Заголовок столбца, из которого берем значение
     * @return Возвращает значение в указанной ячейке
     *
     * Пример использования:    getValueFromTable("Невозобновляемая кредитная линия",
     *                                            "Наименование:Подписание договора",
     *                                            "Статус")
     *
     *                          Возвращает: "Исполнен"
     */
    public String getValueFromTable(String workspace, String keyValue, String column){
        String keyColumn = keyValue.split(":")[0];
        String keyVal = keyValue.split(":")[1];

        SelenideElement tableElement = findTableByTitle(workspace).stream().filter(it -> it.getText().contains(keyVal)).findFirst().get();
        Table table = TableCreators.createTable(tableElement);

        int keyColumnIndex = table.getHeadings().indexOf(keyColumn);
        List<String> res = table.getRows().stream().filter(it -> it.get(keyColumnIndex).equals(keyVal)).collect(Collectors.toList()).get(0);

        return res.get(table.getHeadings().indexOf(column));
    }

    /**
     * Метод поиска элемента таблицы по заголовку таблицы или рабочей области
     *
     * @param title - заголовок таблицы или рабочей области
     */
    private ElementsCollection findTableByTitle(String title) {
        LOG.info("Ищем таблицу по заголовку таблицы/рабочей области");

        String workspaceTitleXpath = "//div[contains(@class, 'workspace ') and .//div[contains(@class, 'workspace__bar')]" +
                "//div[contains(@class, '_h') and text()='" + title + "']]//table";
        String tableTitleXpath = "//div[@class = 'form-layout__element' and .//div[contains(@class, '_h') and text()='" +
                title + "']]//table";
        ElementsCollection elementsByWorkspaceTitle = $$x(workspaceTitleXpath).filter(visible);

        if (!elementsByWorkspaceTitle.isEmpty()) {
            LOG.info("Таблица найдена по заголовку рабочей области");
            return elementsByWorkspaceTitle;
        } else if (!$$x(tableTitleXpath).isEmpty()) {
            LOG.info("Таблица найдена по уникальному заголовку");
            return $$x(tableTitleXpath);
        } else {
            throw new NoSuchElementException("Не удалось найти таблицу по заголовку таблицы/рабочей области: " + title);
        }
    }

    /**
     * Метод выбора элемента select по id поля
     *
     * @param id - id целевого поля
     * @param value - значение целевого пункта выбора
     */
    public void selectFieldByIdFromValue(String id, String value) {
        SelenideElement fieldElement = $(By.id(id));
        String fieldLabel = extractElementName(fieldElement);
        LOG.info("Кликаем по селектбоксу: " + fieldLabel);
        fieldElement.$x(".//div[contains(@class, 'field__input')]").click();
        LOG.info("Выбираем в списке значение: " + value);
        fieldElement.$x("//div[contains(@class, 'dropdown__list-item')]//div[contains(@class, 'dropdown')"
                                + " and text() = '" + value + "' ]").click();
    }

    /**
     * Метод выбора элемента в модальном окне по id поля с поиском вхождения
     *
     * @param id - id целевого поля
     * @param subValue - субстрока значения целевого пункта выбора
     */
    public void selectItemInModalByIdFromPartValue(String id, String subValue) {
        SelenideElement fieldElement = $(By.id(id));
        String fieldLabel = extractElementName(fieldElement);
        LOG.info("Кликаем по селектбоксу: " + fieldLabel);
        fieldElement.$x(".//div[contains(@class, 'field__input')]").click();
        clickElementInModal(subValue, true);
        $(By.id("list-filter__submit-button")).click();
    }

    /**
     * Метод извлечения заголовка из элемента найденого по id
     *
     * @param fieldElement - элемент для которого ведется поиск имени
     */

    private String extractElementName(SelenideElement fieldElement) {
        String elementName = null;
        try {
            SelenideElement titleElement = fieldElement.$x(
                    ".//*[contains(@class, 'field__label') or contains(@class, 'button__text')]");
            if (!titleElement.exists()) {
                titleElement = fieldElement.$x(
                        "./ancestor::div[contains(@class, 'field ')]//*[contains(@class, 'field__label') or contains(@class, 'button__text')]");
            }
            elementName = titleElement.getText();
        } catch (Exception e) {
            LOG.error("Ошибка получения заголовка элемента ", e);
        }
        return elementName;
    }

    /**
     * Метод выбора элемента в модальном окне по id поля с поиском вхождения
     *
     * @param id - id целевого поля
     * @param value - субстрока значения целевого пункта выбора
     */
    public void selectItemInModalByIdFromValue(String id, String value) {
        SelenideElement fieldElement = $(By.id(id));
        fieldElement.$x(".//div[contains(@class, 'field__input')]").click();
        clickElementInModal(value, false);
        $(By.id("list-filter__submit-button")).click();
    }

    /**
     * Метод клика по элементу по id
     *
     * @param id - id целевого элемента
     */
    public void clickFieldById(String id) {
        SelenideElement element = $(By.id(id));
        String fieldLabel = extractElementName(element);
        LOG.info("Кликаем по элементу " + fieldLabel);
        element.$x(".//span").click();
        waitSpinner();
    }

    /**
     * Метод поиска элемента по имени с модальном окне
     *
     * @param fieldName - имя целевого элемента
     * @param subFlag - флаг поиска по подстроке
     */
    @Step("Переходим по ссылке/кнопке <{0}>")
    private void clickElementInModal(String fieldName, boolean subFlag) {
        String xpathExpression = "//div[contains(@class,'modal')]//*";
        if (subFlag) {
            xpathExpression += "[contains(text(), '" + fieldName + "')]";
        } else {
            xpathExpression += "[text()='" + fieldName + "']";
        }
        LOG.info("Кликаем по ссылке/кнопке : " + fieldName + " в модальном окне. xpath: " + xpathExpression);
        $x(xpathExpression).click();
        waitSpinner();
    }

    /**
     * Метод для получения текста всех выпадающих сообщений (notifications-wrapper)
     * После получения значения происходит нажатие на сообщения для их исчезновения
     */
    public String getNotificationsWrapperText() {
        waitSpinner();
        String notificationsWrapperXPath = "//div[contains(@class, 'notification-message')]";
        LOG.info("Получаем все появившиеся информационные сообщения. xpath: " + notificationsWrapperXPath);
        LOG.info("Количество информационных сообщений: " + $$x(notificationsWrapperXPath).size());

        String text = $$x(notificationsWrapperXPath).texts().stream().collect(Collectors.joining());
        LOG.info("Текст информационных сообщений: " + text);

        clickNotificationMessages();
        return text;
    }

    /** Метод используется как временное решение для закрытия ошибочных нотификационных окон на стендах */
    @Step("Закрываем нотификационные сообщения")
    public void clickNotificationMessages() {
        String xpathExpression = "//div[contains(@class,'notification-message')]";
        LOG.info("Закрываем нотификационные сообщения. xpath: " + xpathExpression);
        $$x(xpathExpression).forEach(SelenideElement::click);
    }

    /**
     * Метод для заполнения элемента по его атрибуту name
     *
     * @param fieldName - значение атрибута name  целевого элемента
     * @param value - значение которое будет установлено элементу
     */
    public void setInputValueByAtrName(String fieldName, String value) {
        SelenideElement fieldElement = $(By.name(fieldName));
        String fieldLabel = extractElementName(fieldElement);
        LOG.info(MessageFormat.format("Устанавливаем элементу '{0}' значение '{1}'", fieldLabel, value));
        fieldElement.$x(".//*[name() = 'input' or name() = 'textarea']").setValue(value);
    }

    /**
     * Метод для  выбора значения из selectbox по его атрибуту name
     *
     * @param fieldName - значение атрибута name  целевого элемента
     * @param value - значение которое будет установлено элементу
     */
    public void setSelectBoxValueByAtrName(String fieldName, String value) {
        SelenideElement fieldElement = $(By.name(fieldName));
        String fieldLabel = extractElementName(fieldElement);
        LOG.info(MessageFormat.format("Выбираем в элементу '{0}' значение '{1}'", fieldLabel, value));
        fieldElement.click();
        fieldElement.$x("//div[contains(@class, 'dropdown__list-item')]//div[contains(@class, 'dropdown')"
                                + " and text() = '" + value + "' ]").click();
    }

    public ElementsCollection getTableRowsByValueIntoColumn(String columnName, String value){
        String valueSearchString = ".//text()='" + value +"'";
        if (StringUtils.isBlank(value)) {
            valueSearchString = "not(.//text())";
        }
        String rowXPath = "//table[.//thead//tr//td[.//text()='"+  columnName
                + "']]//tr[.//td[position()= count(./ancestor::table//thead//tr//td[.//text()='"+
                columnName+ "']/preceding-sibling::td)+1 and " + valueSearchString + " ]]";
        return $$x(rowXPath);
    }

    public SelenideElement getTableRowByValueIntoColumn(String columnName, String value){
        return getTableRowsByValueIntoColumn(columnName, value).first();
    }
}