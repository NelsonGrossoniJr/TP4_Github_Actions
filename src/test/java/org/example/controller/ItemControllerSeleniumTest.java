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

public class ItemControllerSeleniumTest extends BaseTest{

    @DisplayName("Deve testar a entrada na página principal e visualizar as tarefas existentes")
    private void entrarNaPaginaEVisualizar() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Verifica se a página carregou corretamente
        WebElement titulo = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));
        assertEquals("Gerenciamento de Tarefas", titulo.getText(), "O título da página não corresponde ao esperado.");

        // Verifica se existe pelo menos uma tarefa na lista
        List<WebElement> cards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".card.task-card")));
        assertFalse(cards.isEmpty(), "Nenhuma tarefa foi encontrada na lista.");

        // Verifica se a primeira tarefa é "Implementar autenticação"
        WebElement primeiraTarefa = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".card.task-card h6")));
        assertEquals("Implementar autenticação", primeiraTarefa.getText(), "A primeira tarefa não corresponde ao esperado.");
    }

    @DisplayName("Deve testar a adição de novos itens na lista de tarefas")
    private void adicionarNovoItem() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Clica no botão "Nova Tarefa" - aguarda estar clicável
        WebElement botaoNovaTarefa = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Nova Tarefa")));
        // Tenta clicar normalmente, se falhar usa JavaScript click
        try {
            botaoNovaTarefa.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botaoNovaTarefa);
        }

        // Verifica se foi redirecionado para o formulário
        WebElement tituloFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h4")));
        assertEquals("Nova Tarefa", tituloFormulario.getText(), "Não foi redirecionado para o formulário de nova tarefa.");

        // Preenche o formulário - aguarda campos estarem presentes
        WebElement campoName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
        campoName.sendKeys("Teste Selenium Automatizado");
        
        WebElement campoDescription = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("description")));
        campoDescription.sendKeys("Tarefa criada automaticamente pelo teste Selenium");

        // Seleciona prioridade "Alta"
        WebElement campoPriority = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("priority")));
        campoPriority.sendKeys("Alta");

        // Seleciona status "Em Andamento"
        WebElement campoStatus = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("status")));
        campoStatus.sendKeys("Em Andamento");

        // Aguarda um pouco para garantir que os campos foram preenchidos
        Thread.sleep(500);

        // Submete o formulário - aguarda botão estar clicável antes de clicar
        WebElement botaoSubmit = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        // Rola a página até o botão para garantir que está visível
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", botaoSubmit);
        Thread.sleep(300);
        // Tenta clicar normalmente, se falhar usa JavaScript click
        try {
            botaoSubmit.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botaoSubmit);
        }

        // Verifica se foi redirecionado para a lista com mensagem de sucesso
        wait.until(ExpectedConditions.urlContains("success="));
        assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("success="), "Não foi exibida mensagem de sucesso.");

        // Verifica se a nova tarefa aparece na lista - aguarda elemento estar presente
        WebElement novaTarefa = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h6[contains(text(), 'Teste Selenium Automatizado')]")));
        assertEquals("Teste Selenium Automatizado", novaTarefa.getText(), "A nova tarefa não foi encontrada na lista.");
    }

    @DisplayName("Deve testar a atualização de itens já existentes na lista de tarefas")
    private void atualizarItem() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Encontra e clica no botão de editar da tarefa criada anteriormente
        WebElement tarefaParaEditar = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h6[contains(text(), 'Teste Selenium Automatizado')]/ancestor::div[contains(@class, 'card')]")));
        // Rola até a tarefa estar visível
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tarefaParaEditar);
        Thread.sleep(500);
        
        WebElement botaoEditar = wait.until(ExpectedConditions.elementToBeClickable(tarefaParaEditar.findElement(By.cssSelector("a[href*='/items/edit/']"))));
        // Tenta clicar normalmente, se falhar usa JavaScript click
        try {
            botaoEditar.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            // Se o clique normal falhar, usa JavaScript click como fallback
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botaoEditar);
        }

        // Verifica se foi redirecionado para o formulário de edição
        WebElement tituloFormulario = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h4")));
        assertEquals("Editar Tarefa", tituloFormulario.getText(), "Não foi redirecionado para o formulário de edição.");

        // Limpa e atualiza os campos
        WebElement inputName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
        inputName.clear();
        inputName.sendKeys("Teste Selenium Atualizado");

        WebElement inputDescription = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("description")));
        inputDescription.clear();
        inputDescription.sendKeys("Tarefa atualizada automaticamente pelo teste Selenium");

        // Muda o status para "Concluída"
        WebElement campoStatus = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("status")));
        campoStatus.sendKeys("Concluída");

        Thread.sleep(500);

        // Submete o formulário - aguarda botão estar clicável
        WebElement botaoSubmit = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        // Rola a página até o botão para garantir que está visível
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", botaoSubmit);
        Thread.sleep(300);
        // Tenta clicar normalmente, se falhar usa JavaScript click
        try {
            botaoSubmit.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botaoSubmit);
        }

        // Verifica se foi redirecionado para a lista com mensagem de sucesso
        wait.until(ExpectedConditions.urlContains("success="));
        assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("success="), "Não foi exibida mensagem de sucesso na atualização.");

        // Verifica se a tarefa foi atualizada na lista
        WebElement tarefaAtualizada = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h6[contains(text(), 'Teste Selenium Atualizado')]")));
        assertEquals("Teste Selenium Atualizado", tarefaAtualizada.getText(), "A tarefa não foi atualizada corretamente.");

        // Verifica se o status foi alterado para "Concluída"
        WebElement statusBadge = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h6[contains(text(), 'Teste Selenium Atualizado')]/ancestor::div[contains(@class, 'card')]//span[contains(@class, 'badge')]")));
        assertEquals("Concluída", statusBadge.getText(), "O status da tarefa não foi atualizado corretamente.");
    }

    @DisplayName("Deve testar a remoção de itens já existentes na lista de tarefas")
    private void deletarItem() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Encontra a tarefa que foi criada e atualizada
        WebElement tarefaParaDeletar = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h6[contains(text(), 'Teste Selenium Atualizado')]/ancestor::div[contains(@class, 'card')]")));
        // Rola até a tarefa estar visível
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", tarefaParaDeletar);
        Thread.sleep(500);
        
        WebElement botaoDeletar = wait.until(ExpectedConditions.elementToBeClickable(tarefaParaDeletar.findElement(By.cssSelector("button[onclick*='confirmDelete']"))));

        // Clica no botão de deletar - tenta clicar normalmente, se falhar usa JavaScript click
        try {
            botaoDeletar.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botaoDeletar);
        }

        // Verifica se o modal de confirmação foi exibido
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteModal")));
        assertTrue(modal.isDisplayed(), "O modal de confirmação não foi exibido.");

        // Verifica se o nome da tarefa aparece no modal
        WebElement nomeTarefaModal = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("taskName")));
        assertEquals("Teste Selenium Atualizado", nomeTarefaModal.getText(), "O nome da tarefa não aparece corretamente no modal.");

        // Confirma a exclusão - aguarda botão estar clicável
        WebElement botaoConfirmarExclusao = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Excluir')]")));
        // Tenta clicar normalmente, se falhar usa JavaScript click
        try {
            botaoConfirmarExclusao.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", botaoConfirmarExclusao);
        }

        // Verifica se foi redirecionado para a lista com mensagem de sucesso
        wait.until(ExpectedConditions.urlContains("success="));
        assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("success="), "Não foi exibida mensagem de sucesso na exclusão.");

        // Verifica se a tarefa foi removida da lista - aguarda um pouco para garantir que foi removida
        Thread.sleep(1000);
        List<WebElement> tarefaRemovida = driver.findElements(By.xpath("//h6[contains(text(), 'Teste Selenium Atualizado')]"));
        assertTrue(tarefaRemovida.isEmpty(), "A tarefa ainda está presente na lista, mas deveria ter sido removida.");
    }

    @Test
    @DisplayName("Deve executar todos os testes relacionados ao ItemController com Selenium")
    void testarFluxoCompletoDeGerenciamentoDeTarefas() throws InterruptedException {
        // Testa visualização da página inicial
        entrarNaPaginaEVisualizar();
        Thread.sleep(1000);

        // Testa criação de nova tarefa
        adicionarNovoItem();
        Thread.sleep(2000);

        // Testa atualização da tarefa criada
        atualizarItem();
        Thread.sleep(2000);

        // Testa exclusão da tarefa
        deletarItem();
    }
}










