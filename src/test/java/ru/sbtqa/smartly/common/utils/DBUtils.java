package ru.sbtqa.smartly.common.utils;

import com.codeborne.selenide.Selenide;
import ru.sbtqa.smartly.common.elements.table.Table;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import oracle.jdbc.driver.OracleDriver;

import java.io.BufferedWriter;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import static com.codeborne.selenide.Selenide.title;
import static ru.sbtqa.smartly.common.utils.PropertyUtils.getProperty;
import static ru.sbtqa.smartly.common.utils.StringUtils.getAmountWithDelimiter;
import static org.junit.jupiter.api.Assertions.*;


public class DBUtils {

    public static final Logger LOG = Logger.getRootLogger();
    private static final long TIMEOUT = 300000; // 5 minutes in milliseconds
    private static final long PROCESS_TIMEOUT = TIMEOUT * 3; // 15 minutes in milliseconds

    private static final String URL = "jdbc:oracle:thin:@" + getProperty("db.url");
    private static final String LOGIN = getProperty("db.login");
    private static final String PASSWORD = getProperty("db.pass");

    static {
        try {
            DriverManager.registerDriver(new OracleDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для получения значения из БД
     *
     * @param statement - SQL-запрос
     **/
    public static String executeStatement(String statement) {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             Statement sqlStatement = connection.createStatement();
             ResultSet resultSet = sqlStatement.executeQuery(statement)) {

            if (resultSet.next()) return resultSet.getString(1) == null ? "" : resultSet.getString(1);
        } catch (SQLException e) {
            LOG.error("DBUtils executeStatement SQLException: ", e);
        }
        return "";
    }

    /**
     * Метод ожидает указанного статуса в базе
     *
     * @param productNumber - номер договора
     * @param state         - ожидаемый статус
     **/
    public static void waitDocStateIn(String productNumber, String state) {
        long elapsedTime = System.currentTimeMillis() + TIMEOUT;
        String sqlStatement = "select C_NAME from z#COM_STATUS_PRD where ID = " +
                "(select C_COM_STATUS from z#PRODUCT WHERE C_NUM_DOG = '" + productNumber + "' AND CLASS_ID = 'KRED_CORP')";

        while (!Objects.equals(executeStatement(sqlStatement), state)) {
            LOG.info("Ждем, пока документ примет статус \"" + state + "\"...");
            sleep(5);

            if (System.currentTimeMillis() > elapsedTime) {
                throw new IllegalStateException("Превышено время ожидания статуса " + state + " для продукта " + productNumber
                        + "\n Текущий статус: " + executeStatement(sqlStatement));
            }
        }
    }

    /**
     * Метод ожидает появления запроса на создание КД в таблице входящих запросов
     *
     * @param productNumber - номер договора
     **/
    private static void waitCreateRequest(String productNumber) {
        long elapsedTime = System.currentTimeMillis() + TIMEOUT;

        LOG.info("Ждем появления записи в таблице входящих запросов ЕКС...");
        while (executeStatement("select count(r.id) from Z#PCL_IN_REQUEST r, Z#PCL_PROD_DATA p, Z#PCL_CREDIT cr " +
                "where r.c_Data#Cls = 'PCL_CREDIT' AND p.c_num_dog = '" + productNumber + "' and r.c_Data#Obj = p.id and p.id = cr.id").equals("0")) {
            sleep(3);
            if (System.currentTimeMillis() > elapsedTime) {
                throw new IllegalStateException("Запрос на создание продукта " + productNumber + " отсутствует в таблице входящих запросов ЕКС");
            }
        }
    }

    /**
     * Метод ожидает появления сервиса в таблице входящих запросов ЕКС
     *
     * @param rquid - Уникальный идентификатор запроса
     **/
    private static void waitServiceRequest(String rquid) {
        long elapsedTime = System.currentTimeMillis() + TIMEOUT;
        String sqlStatement = "SELECT COUNT(*) FROM Z#PCL_IN_REQUEST WHERE C_RQUID = '" + rquid + "'";

        LOG.info("Ждем появления сервиса в таблице входящих запросов ЕКС...");
        while (executeStatement(sqlStatement).equals("0")) {
            sleep(3);
            if (System.currentTimeMillis() > elapsedTime) {
                throw new IllegalStateException("Сервис с " + rquid + " отсутствует в таблице входящих запросов ЕКС");
            }
        }
    }

    /**
     * Метод возвращает операционный день
     **/
    public static String getOperDay(String branchCode) {
        return LocalDate.parse(executeStatement("SELECT C_OP_DATE FROM Z#BRANCH WHERE C_CODE = " + branchCode),
                DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    /**
     * Метод запускает процедуру обработки над указанным договором
     *
     * @param productNumber - номер договора
     * @return eksId - метод возвращает ID
     **/
    @Step("Запускаем обработчик для продукта")
    public static String processProduct(String productNumber) {
        waitCreateRequest(productNumber);
        LOG.info("Запускаем обработчик для продукта " + productNumber);
        String id = executeStatement("select min(r.id)from Z#PCL_IN_REQUEST r, Z#PCL_PROD_DATA p, Z#PCL_CREDIT cr " +
                "where r.c_Data#Cls = 'PCL_CREDIT' AND p.c_num_dog = '" + productNumber + "' and r.c_Data#Obj = p.id and p.id = cr.id");
        String rquId = executeStatement("SELECT C_RQUID FROM Z#PCL_IN_REQUEST WHERE ID = '" + id + "'");

        executeScript("processService.sql", "<rquid>", rquId);
        String eksId = executeStatement("SELECT C_PRODUCT#EKS#OBJ FROM Z#PCL_IN_REQUEST WHERE ID = '" + id + "'");

        assertAll(
                () -> assertEquals("SUCCESS",
                        executeStatement("SELECT STATE_ID FROM Z#PCL_IN_REQUEST WHERE ID = '" + id + "'"),
                        "Создание продукта в ЕКС завершилось неудачей. Ошибка: \n" +
                                executeStatement("SELECT C_STATUS_DESC FROM Z#PCL_IN_REQUEST WHERE ID = '" + id + "'")),
                () -> assertFalse(eksId.isEmpty())
        );

        LOG.info("ID продукта в ЕКС: " + eksId);
        return eksId;
    }

    /**
     * Метод запускает процедуру обработки записи в таблице входящих запросов ЕКС по указанному rquid
     *
     * @param rquid - Уникальный идентификатор запроса
     */
    public static void processService(String rquid) {
        waitServiceRequest(rquid);
        LOG.info("Запускаем обработчик для записи с rquid " + rquid);
        executeScript("processService.sql", "<rquid>", rquid);

        String id = executeStatement("select min(id) from Z#PCL_IN_REQUEST where c_rquid = '" + rquid + "'");

        assertEquals("SUCCESS",
                executeStatement("SELECT STATE_ID FROM Z#PCL_IN_REQUEST WHERE ID = '" + id + "'"),
                "Создание продукта в ЕКС завершилось неудачей. Ошибка: \n" +
                        executeStatement("SELECT C_STATUS_DESC FROM Z#PCL_IN_REQUEST WHERE ID = '" + id + "'"));
    }

    /**
     * Метод запускает процедуру обработки записи "Установка/удаление блокировки на модификации в закрытых оперднях"
     * в таблице входящих запросов ЕКС (Передача МС в ЕКС)
     *
     * @param sourceId - идентификатор договора в ЕКС
     */
    public static void processMS(String sourceId) {
        long elapsedTime = System.currentTimeMillis() + PROCESS_TIMEOUT;
        LOG.info("Ждем появления запроса на обработку МС в таблице входящих запросов ЕКС...");
        while (executeStatement("SELECT count(*) FROM Z#PCL_IN_REQUEST WHERE STATE_ID = 'FORM' AND C_PRODUCT#EKS#OBJ = '" +
                sourceId + "' AND C_BP_NAME = 'SetStateOperDateEKSRq'").equals("0")) {
            sleep(3);
            if (System.currentTimeMillis() > elapsedTime) {
                throw new IllegalStateException("Запись на обработку МС отсутствует в таблице входящих запросов ЕКС");
            }
        }
        processService(executeStatement("SELECT C_RQUID FROM Z#PCL_IN_REQUEST WHERE STATE_ID = 'FORM' AND C_PRODUCT#EKS#OBJ = '" +
                sourceId + "' AND C_BP_NAME = 'SetStateOperDateEKSRq'"));
    }

    /**
     * @param scriptName  - имя файла скрипта в каталоге scripts/
     * @param target      - атибут, который заменяем в скрипте
     * @param replacement - чем заменяем
     */
    private static void executeScript(String scriptName, String target, String replacement) {
        // первый поток ждет окончание работы обработчика
        CompletableFuture future = CompletableFuture.runAsync(() -> {
            try {
                String body = FileUtils.readFileToString(
                        new File(DBUtils.class.getClassLoader().getResource("scripts/" + scriptName).toURI()), "UTF-8")
                        .replace(target, replacement);
                executeStatement(body);
            } catch (Exception e) {
                LOG.error("Ошибка обработки: ", e);
            }
        });

        LOG.info("Ждем завершения работы обработчика...");
        long elapsedTime = System.currentTimeMillis() + PROCESS_TIMEOUT;
        while (!future.isDone()) { // второй поток продлевает сессию браузера
            sleep(3);
            if (System.currentTimeMillis() > elapsedTime) {
                future.completeExceptionally(new IllegalStateException("Ожидание завершения работы обработчика превысило " +
                        PROCESS_TIMEOUT / 60000 + " минут"));
            }
        }
    }

    /**
     * Метод спит в течение seconds секунд, продлевает сессию браузера, чтобы не закрылась по таймауту
     *
     * @param seconds - время сна в секундах
     **/
    private static void sleep(int seconds) {
        Selenide.sleep(seconds * 1000L);
        title();
    }

    /**
     * Метод создает в текущем каталоге html таблицу с ошибками нормализации и прикрепляет ее к Аллюр отчету
     *
     * @param eksId - ЕКС id
     * @return - массив byte для аллюр отчета
     */
    @Attachment(type = "plain/text", value = "MigrationErrors.html")
    public static byte[] takeNormalizationErrorsLog(String eksId) {
        try {
            String body = FileUtils.readFileToString(
                    new File(DBUtils.class.getClassLoader().getResource("scripts/eks/SQLScripts/" + "getNormalizationErrors.sql").toURI()), "UTF-8")
                    .replace("<eksId>", eksId);

            Table table = getTable(body, getProperty("buf_db.url"), getProperty("buf_db.login"), getProperty("buf_db.pass"));
            if (table == null) {
                return new byte[0];
            }
            BufferedWriter bw = Files.newBufferedWriter(Paths.get("MigrationErrors.html"));
            // Формируем страничку с ошибками нормализации
            bw.write("<html><body><table border=\"1\"><thead><tr bgcolor=\"silver\">");
            for (String heading : table.getHeadings()) {
                bw.write("<td>" + heading + "</td>");
            }

            for (List<String> row : table.getRows()) {
                bw.write("<tr>");
                for (String cell : row) {
                    bw.write("<td>" + cell + "</td>");
                }
                bw.write("</tr>");
            }
            bw.write("</tr></thead><tbody>");
            bw.write("</tbody></table></body></html>");
            bw.close();

            return FileUtils.readFileToString(new File("MigrationErrors.html"), "UTF-8").getBytes();
        } catch (Exception e) {
            LOG.error("Ошибка при получении лога ошибок нормализации: ", e);
        }
        return new byte[0];
    }

    /**
     * Метод возвращает результат запроса к БД в виде объекта Table (заголовки и строки)
     *
     * @param statement - SQL-запрос
     * @return объект Table
     */
    private static Table getTable(String statement, String url, String login, String password) {
        List<String> headings = new ArrayList<>();
        List<List<String>> rows = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@" + url, login, password);
             Statement sqlStatement = connection.createStatement();
             ResultSet resultSet = sqlStatement.executeQuery(statement)) {

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            String dbHeading;

            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                dbHeading = resultSet.getMetaData().getColumnName(i);
                headings.add(dbHeading);
            }

            while (resultSet.next()) {
                ArrayList<String> row = new ArrayList<>();
                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    row.add(resultSet.getString(i));
                }
                rows.add(row);
            }

            if (rows.isEmpty()) {
                LOG.error("Таблица пуста");
                return null;
            }
        } catch (SQLException e) {
            LOG.error("DBUtils executeStatement SQLException: ", e);
        }
        return new Table(headings, rows);
    }

    /**
     * @param statement   - SQL-запрос, получающий нужную таблицу
     * @param isNumbered  - таблица пронумерована в UI? (Если true - метод вернет пронумерованную таблицу)
     * @param headingsMap - объект Map, в котором ключ - название столбца в БД, значение - название столбца в UI
     * @return объект Table
     */
    public static Table createTable(String statement, boolean isNumbered, Map<String, String> headingsMap) {
        return createTable(statement, isNumbered, headingsMap, true);
    }

    /**
     * @param statement    - SQL-запрос, получающий нужную таблицу
     * @param isNumbered   - таблица пронумерована в UI? (Если true - метод вернет пронумерованную таблицу)
     * @param headingsMap  - объект Map, в котором ключ - название столбца в БД, значение - название столбца в UI
     * @param formatDigits - приводить числа из БД к формату интерфейса (# ###,##)?
     * @return объект Table
     */
    public static Table createTable(String statement, boolean isNumbered, Map<String, String> headingsMap, boolean formatDigits) {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             Statement sqlStatement = connection.createStatement();
             ResultSet resultSet = sqlStatement.executeQuery(statement)) {

            List<String> UIHeadings = new ArrayList<>();
            List<String> DBHeadings = new ArrayList<>();
            List<List<String>> rows = new ArrayList<>();
            int rowNumber = 0;
            String dbHeading;

            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                dbHeading = resultSet.getMetaData().getColumnName(i);
                UIHeadings.add(headingsMap.get(dbHeading));
                DBHeadings.add(dbHeading);
            }

            if (isNumbered) {
                // Если таблица нумерована, добавляем в заголовок "№"
                UIHeadings.add(0, "№");
            }

            // Заполняем строки
            while (resultSet.next()) {
                List<Object> row = new ArrayList<>();
                if (isNumbered) {
                    // Если таблица нумерована, добавляем в строку порядковый номер
                    row.add(++rowNumber);
                }
                for (String DBHeading : DBHeadings) {
                    row.add(resultSet.getObject(DBHeading));
                }
                rows.add(convertRowToUITableFormat(row, formatDigits));
            }
            return new Table(UIHeadings, rows);
        } catch (Exception e) {
            throw new IllegalStateException("Ошибка при создании таблицы", e);
        }
    }

    /**
     * @param row - неформатированная строка таблицы БД ЕКС
     * @return приведенная к формату интерфейса ППРБ строка таблицы
     */
    private static List<String> convertRowToUITableFormat(List<Object> row, boolean formatDigits) {
        List<String> newRow = new ArrayList<>();
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        row.forEach(element -> {
            if (element instanceof Date) {
                newRow.add(formatter.format(element));
            } else if (element instanceof BigDecimal && formatDigits) {
                newRow.add(getAmountWithDelimiter(element.toString()));
            } else newRow.add(element.toString());
        });
        return newRow;
    }

}
