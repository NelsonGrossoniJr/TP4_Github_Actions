package org.example.controller;

import io.github.bonigarcia.wdm.WebDriverManager;
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
        driver.get("http://localhost:8000/items");
    }

    @BeforeEach
    public void tearDown(){
        if(driver != null ){
            driver.quit();
        }
    }
}