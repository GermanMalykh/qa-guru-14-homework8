package com.lenta.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.junit5.BrowserPerTestStrategyExtension;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.lenta.data.ConfigurationData;
import com.lenta.helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.remote.DesiredCapabilities;

@ExtendWith({BrowserPerTestStrategyExtension.class})
public class TestBase {

    @BeforeAll
    static void configure() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        DesiredCapabilities capabilities = new DesiredCapabilities();

        Configuration.browserCapabilities = capabilities;
        Configuration.baseUrl = ConfigurationData.baseUrl;
        Configuration.browser = ConfigurationData.browserName;
        Configuration.browserVersion = ConfigurationData.browserVersion;
        Configuration.browserSize = ConfigurationData.browserSize;

        if (ConfigurationData.browserVersion != null) {
            Configuration.browserVersion = ConfigurationData.browserVersion;
        }
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
    }
}