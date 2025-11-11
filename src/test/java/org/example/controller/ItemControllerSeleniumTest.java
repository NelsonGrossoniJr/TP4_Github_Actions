package org.example.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
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
        // Verifica se a página carregou corretamente
        WebElement titulo = driver.findElement(By.tagName("h1"));
        assertEquals("Gerenciamento de Tarefas", titulo.getText(), "O título da página não corresponde ao esperado.");

        // Verifica se existe pelo menos uma tarefa na lista
        List<WebElement> cards = driver.findElements(By.cssSelector(".card.task-card"));
        assertFalse(cards.isEmpty(), "Nenhuma tarefa foi encontrada na lista.");

        // Verifica se a primeira tarefa é "Implementar autenticação"
        WebElement primeiraTarefa = driver.findElement(By.cssSelector(".card.task-card h6"));
        assertEquals("Implementar autenticação", primeiraTarefa.getText(), "A primeira tarefa não corresponde ao esperado.");
    }

    @DisplayName("Deve testar a adição de novos itens na lista de tarefas")
    private void adicionarNovoItem() throws InterruptedException {
        // Clica no botão "Nova Tarefa"
        driver.findElement(By.linkText("Nova Tarefa")).click();

        // Verifica se foi redirecionado para o formulário
        WebElement tituloFormulario = driver.findElement(By.tagName("h4"));
        assertEquals("Nova Tarefa", tituloFormulario.getText(), "Não foi redirecionado para o formulário de nova tarefa.");

        // Preenche o formulário
        driver.findElement(By.id("name")).sendKeys("Teste Selenium Automatizado");
        driver.findElement(By.id("description")).sendKeys("Tarefa criada automaticamente pelo teste Selenium");

        // Seleciona prioridade "Alta"
        driver.findElement(By.id("priority")).sendKeys("Alta");

        // Seleciona status "Em Andamento"
        driver.findElement(By.id("status")).sendKeys("Em Andamento");

        Thread.sleep(1000);

        // Submete o formulário
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verifica se foi redirecionado para a lista com mensagem de sucesso
        assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("success="), "Não foi exibida mensagem de sucesso.");

        // Verifica se a nova tarefa aparece na lista
        WebElement novaTarefa = driver.findElement(By.xpath("//h6[contains(text(), 'Teste Selenium Automatizado')]"));
        assertEquals("Teste Selenium Automatizado", novaTarefa.getText(), "A nova tarefa não foi encontrada na lista.");
    }

    @DisplayName("Deve testar a atualização de itens já existentes na lista de tarefas")
    private void atualizarItem() throws InterruptedException {
        // Encontra e clica no botão de editar da tarefa criada anteriormente
        WebElement tarefaParaEditar = driver.findElement(By.xpath("//h6[contains(text(), 'Teste Selenium Automatizado')]/ancestor::div[contains(@class, 'card')]"));
        WebElement botaoEditar = tarefaParaEditar.findElement(By.cssSelector("a[href*='/items/edit/']"));
        botaoEditar.click();

        Thread.sleep(1000);

        // Verifica se foi redirecionado para o formulário de edição
        WebElement tituloFormulario = driver.findElement(By.tagName("h4"));
        assertEquals("Editar Tarefa", tituloFormulario.getText(), "Não foi redirecionado para o formulário de edição.");

        // Limpa e atualiza os campos
        WebElement inputName = driver.findElement(By.id("name"));
        inputName.clear();
        inputName.sendKeys("Teste Selenium Atualizado");

        WebElement inputDescription = driver.findElement(By.id("description"));
        inputDescription.clear();
        inputDescription.sendKeys("Tarefa atualizada automaticamente pelo teste Selenium");

        // Muda o status para "Concluída"
        driver.findElement(By.id("status")).sendKeys("Concluída");

        Thread.sleep(1000);

        // Submete o formulário
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verifica se foi redirecionado para a lista com mensagem de sucesso
        assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("success="), "Não foi exibida mensagem de sucesso na atualização.");

        // Verifica se a tarefa foi atualizada na lista
        WebElement tarefaAtualizada = driver.findElement(By.xpath("//h6[contains(text(), 'Teste Selenium Atualizado')]"));
        assertEquals("Teste Selenium Atualizado", tarefaAtualizada.getText(), "A tarefa não foi atualizada corretamente.");

        // Verifica se o status foi alterado para "Concluída"
        WebElement statusBadge = driver.findElement(By.xpath("//h6[contains(text(), 'Teste Selenium Atualizado')]/ancestor::div[contains(@class, 'card')]//span[contains(@class, 'badge')]"));
        assertEquals("Concluída", statusBadge.getText(), "O status da tarefa não foi atualizado corretamente.");
    }

    @DisplayName("Deve testar a remoção de itens já existentes na lista de tarefas")
    private void deletarItem() throws InterruptedException {
        // Encontra a tarefa que foi criada e atualizada
        WebElement tarefaParaDeletar = driver.findElement(By.xpath("//h6[contains(text(), 'Teste Selenium Atualizado')]/ancestor::div[contains(@class, 'card')]"));
        WebElement botaoDeletar = tarefaParaDeletar.findElement(By.cssSelector("button[onclick*='confirmDelete']"));

        // Clica no botão de deletar
        botaoDeletar.click();

        Thread.sleep(1000);

        // Verifica se o modal de confirmação foi exibido
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteModal")));
        assertTrue(modal.isDisplayed(), "O modal de confirmação não foi exibido.");

        // Verifica se o nome da tarefa aparece no modal
        WebElement nomeTarefaModal = driver.findElement(By.id("taskName"));
        assertEquals("Teste Selenium Atualizado", nomeTarefaModal.getText(), "O nome da tarefa não aparece corretamente no modal.");

        // Confirma a exclusão
        WebElement botaoConfirmarExclusao = driver.findElement(By.xpath("//button[contains(text(), 'Excluir')]"));
        botaoConfirmarExclusao.click();

        Thread.sleep(2000);

        // Verifica se foi redirecionado para a lista com mensagem de sucesso
        assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("success="), "Não foi exibida mensagem de sucesso na exclusão.");

        // Verifica se a tarefa foi removida da lista
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










