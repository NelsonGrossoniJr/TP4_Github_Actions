package org.example.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes pós-deploy para validação do sistema em produção.
 * Reutiliza a lógica dos testes Selenium existentes, mas adaptado para validar
 * o ambiente de produção após o deploy.
 */
public class PostDeployProductionTest extends BaseTest {

    @DisplayName("Deve validar que a página principal está acessível em produção")
    @Test
    void validarPaginaPrincipalEmProducao() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        // Verifica se a página carregou corretamente
        WebElement titulo = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));
        assertEquals("Gerenciamento de Tarefas", titulo.getText(), 
            "❌ PRODUÇÃO: O título da página não corresponde ao esperado.");
        
        System.out.println("✅ PRODUÇÃO: Página principal carregada com sucesso");
    }

    @DisplayName("Deve validar que a lista de tarefas está sendo exibida corretamente em produção")
    @Test
    void validarListaDeTarefasEmProducao() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        // Verifica se existe pelo menos uma tarefa na lista
        List<WebElement> cards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".card.task-card")));
        assertFalse(cards.isEmpty(), 
            "❌ PRODUÇÃO: Nenhuma tarefa foi encontrada na lista.");
        
        System.out.println("✅ PRODUÇÃO: Lista de tarefas exibida corretamente (" + cards.size() + " tarefas encontradas)");
    }

    @DisplayName("Deve validar que o formulário de nova tarefa está funcionando em produção")
    @Test
    void validarFormularioNovaTarefaEmProducao() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        // Clica no botão "Nova Tarefa"
        WebElement botaoNovaTarefa = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Nova Tarefa")));
        try {
            botaoNovaTarefa.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botaoNovaTarefa);
        }

        // Verifica se foi redirecionado para o formulário
        WebElement tituloFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h4")));
        assertEquals("Nova Tarefa", tituloFormulario.getText(), 
            "❌ PRODUÇÃO: Não foi redirecionado para o formulário de nova tarefa.");
        
        // Verifica se os campos estão presentes
        WebElement campoName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
        WebElement campoDescription = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("description")));
        WebElement campoPriority = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("priority")));
        WebElement campoStatus = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("status")));
        
        assertTrue(campoName.isDisplayed(), "❌ PRODUÇÃO: Campo 'name' não está visível");
        assertTrue(campoDescription.isDisplayed(), "❌ PRODUÇÃO: Campo 'description' não está visível");
        assertTrue(campoPriority.isDisplayed(), "❌ PRODUÇÃO: Campo 'priority' não está visível");
        assertTrue(campoStatus.isDisplayed(), "❌ PRODUÇÃO: Campo 'status' não está visível");
        
        System.out.println("✅ PRODUÇÃO: Formulário de nova tarefa está funcionando corretamente");
    }

    @DisplayName("Deve validar criação de tarefa em produção (teste completo)")
    @Test
    void validarCriacaoDeTarefaEmProducao() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        // Navega para o formulário
        WebElement botaoNovaTarefa = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Nova Tarefa")));
        try {
            botaoNovaTarefa.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botaoNovaTarefa);
        }

        // Aguarda formulário carregar
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h4")));
        Thread.sleep(500);

        // Preenche o formulário com dados de teste
        String nomeTarefa = "Teste Pós-Deploy Produção " + System.currentTimeMillis();
        
        WebElement campoName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
        campoName.sendKeys(nomeTarefa);
        
        WebElement campoDescription = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("description")));
        campoDescription.sendKeys("Tarefa criada automaticamente pelo teste pós-deploy em produção");
        
        WebElement campoPriority = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("priority")));
        campoPriority.sendKeys("Alta");
        
        WebElement campoStatus = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("status")));
        campoStatus.sendKeys("Em Andamento");
        
        Thread.sleep(500);

        // Submete o formulário
        WebElement botaoSubmit = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", botaoSubmit);
        Thread.sleep(300);
        
        try {
            botaoSubmit.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botaoSubmit);
        }

        // Verifica se foi redirecionado com mensagem de sucesso
        wait.until(ExpectedConditions.urlContains("success="));
        assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("success="), 
            "❌ PRODUÇÃO: Não foi exibida mensagem de sucesso após criar tarefa.");
        
        // Verifica se a nova tarefa aparece na lista
        WebElement novaTarefa = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h6[contains(text(), '" + nomeTarefa + "')]")));
        assertEquals(nomeTarefa, novaTarefa.getText(), 
            "❌ PRODUÇÃO: A nova tarefa não foi encontrada na lista.");
        
        System.out.println("✅ PRODUÇÃO: Criação de tarefa funcionando corretamente");
    }

    @DisplayName("Deve validar que a aplicação está respondendo corretamente em produção")
    @Test
    void validarAplicacaoRespondendoEmProducao() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        
        // Verifica se a página carregou
        WebElement titulo = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));
        assertNotNull(titulo, "❌ PRODUÇÃO: Página não carregou");
        
        // Verifica se a URL está correta
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("/items"), 
            "❌ PRODUÇÃO: URL incorreta. Esperado: contém '/items', obtido: " + currentUrl);
        
        // Verifica se o título da página está correto
        assertEquals("Gerenciamento de Tarefas", titulo.getText(), 
            "❌ PRODUÇÃO: Título da página incorreto");
        
        System.out.println("✅ PRODUÇÃO: Aplicação está respondendo corretamente");
        System.out.println("   URL: " + currentUrl);
        System.out.println("   Título: " + titulo.getText());
    }

    @DisplayName("Deve executar validação completa do sistema em produção")
    @Test
    void validarSistemaCompletoEmProducao() throws InterruptedException {
        System.out.println("🚀 Iniciando validação completa do sistema em produção...");
        
        // 1. Valida página principal
        validarPaginaPrincipalEmProducao();
        Thread.sleep(1000);
        
        // 2. Valida lista de tarefas
        validarListaDeTarefasEmProducao();
        Thread.sleep(1000);
        
        // 3. Valida formulário
        validarFormularioNovaTarefaEmProducao();
        Thread.sleep(1000);
        
        // 4. Valida criação de tarefa
        validarCriacaoDeTarefaEmProducao();
        
        System.out.println("✅ PRODUÇÃO: Validação completa concluída com sucesso!");
    }
}

