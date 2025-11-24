package org.example.controller;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BaseTest {
    protected WebDriver driver;

    @BeforeEach
    public void setUp(){
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--incognito");
        
        // Verifica se está rodando em ambiente CI (GitHub Actions, etc)
        String ci = System.getenv("CI");
        if (ci != null && ci.equals("true")) {
            // Argumentos necessários para rodar Chrome em ambiente CI sem display
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.addArguments("--disable-gpu");
        } else {
            // Em ambiente local, maximiza a janela
            chromeOptions.addArguments("--start-maximized");
        }

        driver = new ChromeDriver(chromeOptions);
        
        // Permite configurar a URL via variável de ambiente (útil para testes pós-deploy em produção)
        String baseUrl = System.getenv("TEST_BASE_URL");
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = "http://localhost:8000";
        }
        driver.get(baseUrl + "/items");
    }

    @AfterEach
    public void tearDown(){
        if(driver != null ){
            driver.quit();
        }
    }
}