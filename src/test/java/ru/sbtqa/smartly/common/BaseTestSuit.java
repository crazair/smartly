package ru.sbtqa.smartly.common;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.sbtqa.smartly.common.lifecycle.BeforeAfterExtension;
import ru.sbtqa.smartly.common.utils.AllureUtils;
import ru.sbtqa.smartly.common.utils.RunnerUtils;
import ru.sbtqa.smartly.common.utils.ShutdownHookUtils;
import ru.sbtqa.smartly.data.DataConstants;
import ru.sbtqa.smartly.pageobjects.*;
import ru.sbtqa.smartly.pageobjects.base.*;


import static com.codeborne.selenide.Selenide.close;
import static com.codeborne.selenide.Selenide.open;
import static ru.sbtqa.smartly.common.utils.PropertyUtils.prop;
import static ru.sbtqa.smartly.pageobjects.base.CommonPage.login;

@Tag("ALL")
@ExtendWith(BeforeAfterExtension.class)
public class BaseTestSuit implements DataConstants {

    /** Инициализируем Pages для реализации логики всех автотестов. Инициализация в одном месте. */
    protected BasePage                    basePage                    = new BasePage();
    protected ClientsPage                 clientsPage                 = new ClientsPage();
    protected CommonPage                  commonPage                  = new CommonPage();
    protected ConditionPage               conditionPage               = new ConditionPage();
    protected DebtsPage                   debtsPage                   = new DebtsPage();
    protected ExtraditionPage             extraditionPage             = new ExtraditionPage();
    protected FirstPage                   firstPage                   = new FirstPage();
    protected ProductCreationTemplatePage productCreationTemplatePage = new ProductCreationTemplatePage();
    protected ProductsPage                productsPage                = new ProductsPage();
    protected ServicesPage                servicesPage                = new ServicesPage();

    static {
            System.setProperty("selenide.browser", prop("selenide.browser"));
            Configuration.timeout = 8000;
            Configuration.browserSize = "1920x1080";

            if (!prop("selenide.browser").contains("SelenoidProvider")) {
                //System.setProperty("webdriver.chrome.driver", prop("webdriver.drivers.path"));  // В случае отсутствия интернета
                Configuration.startMaximized = true;
            }

            RunnerUtils.initDicts();                      // В случае необходимости инициализируем справочники...
            AllureUtils.createEnvironmentProperties();    // Создание environment.properties

            // Блок кода в ShutdownHookUtils.setHook выполняется толкьо 1 раз ПОСЛЕ выполнения всех тестов
            // независимо от параллельности, если она работает через junit.jupiter.execution.parallel
            ShutdownHookUtils.setHook(() -> {
                System.out.println("#################################");
                System.out.println("#Конец выполнения всех тестов!!!#");
                System.out.println("#################################");
            });
    }

    @BeforeAll
    public static void beforeTestSuite() {
        open(prop("url"));
        login();
    }

    @BeforeEach
    public void beforeEachTest() {
        open(prop("url"));
    }

    @AfterAll
    public static void afterTestSuite() {
        close();
    }
}
