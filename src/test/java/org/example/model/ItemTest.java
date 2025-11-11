package org.example.model;

import org.example.exceptions.ItemInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void testeConstrutorCompleto() {
        Item item = new Item(1, "Teste Item", "Descrição do teste", "Em Andamento", "Alta");

        assertEquals(1, item.getId());
        assertEquals("Teste Item", item.getName());
        assertEquals("Descrição do teste", item.getDescription());
        assertEquals("Em Andamento", item.getStatus());
        assertEquals("Alta", item.getPriority());
    }

    @Test
    void testeConstrutorSimplificado() {
        Item item = new Item(1, "Teste Item", "Descrição do teste");

        assertEquals(1, item.getId());
        assertEquals("Teste Item", item.getName());
        assertEquals("Descrição do teste", item.getDescription());
        assertEquals("Pendente", item.getStatus()); // Valor padrão
        assertEquals("Média", item.getPriority()); // Valor padrão
    }

    @Test
    void testeValidacaoNomeVazio() {
        assertThrows(ItemInvalidoException.class, () -> new Item(1, "", "Descrição válida"));
    }

    @Test
    void testeValidacaoNomeNulo() {
        assertThrows(ItemInvalidoException.class, () -> new Item(1, null, "Descrição válida"));
    }

    @Test
    void testeValidacaoNomeApenasEspacos() {
        assertThrows(ItemInvalidoException.class, () -> new Item(1, "   ", "Descrição válida"));
    }

    @Test
    void testeValidacaoNomeMuitoLongo() {
        String nomeLongo = "a".repeat(101);
        assertThrows(ItemInvalidoException.class, () -> new Item(1, nomeLongo, "Descrição válida"));
    }

    @Test
    void testeValidacaoDescricaoVazia() {
        assertThrows(ItemInvalidoException.class, () -> new Item(1, "Nome válido", ""));
    }

    @Test
    void testeValidacaoDescricaoNula() {
        assertThrows(ItemInvalidoException.class, () -> new Item(1, "Nome válido", null));
    }

    @Test
    void testeValidacaoDescricaoApenasEspacos() {
        assertThrows(ItemInvalidoException.class, () -> new Item(1, "Nome válido", "   "));
    }

    @Test
    void testeValidacaoDescricaoMuitoLonga() {
        String descricaoLonga = "a".repeat(101);
        assertThrows(ItemInvalidoException.class, () -> new Item(1, "Nome válido", descricaoLonga));
    }

    @Test
    void testeValidacaoStatusValido() {
        Item item1 = new Item(1, "Teste", "Descrição", "Pendente", "Média");
        assertEquals("Pendente", item1.getStatus());

        Item item2 = new Item(2, "Teste", "Descrição", "Em Andamento", "Média");
        assertEquals("Em Andamento", item2.getStatus());

        Item item3 = new Item(3, "Teste", "Descrição", "Concluída", "Média");
        assertEquals("Concluída", item3.getStatus());

        Item item4 = new Item(4, "Teste", "Descrição", "Cancelada", "Média");
        assertEquals("Cancelada", item4.getStatus());
    }

    @Test
    void testeValidacaoStatusInvalido() {
        assertThrows(ItemInvalidoException.class, () -> new Item(1, "Teste", "Descrição", "Status Inválido", "Média"));
    }

    @Test
    void testeStatusNuloUsaPadrao() {
        Item item = new Item(1, "Teste", "Descrição", null, "Média");
        assertEquals("Pendente", item.getStatus());
    }

    @Test
    void testeStatusVazioUsaPadrao() {
        Item item = new Item(1, "Teste", "Descrição", "", "Média");
        assertEquals("Pendente", item.getStatus());
    }

    @Test
    void testeValidacaoPrioridadeValida() {
        Item item1 = new Item(1, "Teste", "Descrição", "Pendente", "Baixa");
        assertEquals("Baixa", item1.getPriority());

        Item item2 = new Item(2, "Teste", "Descrição", "Pendente", "Média");
        assertEquals("Média", item2.getPriority());

        Item item3 = new Item(3, "Teste", "Descrição", "Pendente", "Alta");
        assertEquals("Alta", item3.getPriority());

        Item item4 = new Item(4, "Teste", "Descrição", "Pendente", "Urgente");
        assertEquals("Urgente", item4.getPriority());
    }

    @Test
    void testeValidacaoPrioridadeInvalida() {
        assertThrows(ItemInvalidoException.class, () -> new Item(1, "Teste", "Descrição", "Pendente", "Prioridade Inválida"));
    }

    @Test
    void testePrioridadeNulaUsaPadrao() {
        Item item = new Item(1, "Teste", "Descrição", "Pendente", null);
        assertEquals("Média", item.getPriority());
    }

    @Test
    void testePrioridadeVaziaUsaPadrao() {
        Item item = new Item(1, "Teste", "Descrição", "Pendente", "");
        assertEquals("Média", item.getPriority());
    }

    @Test
    void testeTrimNome() {
        Item item = new Item(1, "  Nome com espaços  ", "Descrição");
        assertEquals("Nome com espaços", item.getName());
    }

    @Test
    void testeTrimDescricao() {
        Item item = new Item(1, "Nome", "  Descrição com espaços  ");
        assertEquals("Descrição com espaços", item.getDescription());
    }

    @Test
    void testeTrimStatus() {
        Item item = new Item(1, "Nome", "Descrição", "  Em Andamento  ", "Média");
        assertEquals("Em Andamento", item.getStatus());
    }

    @Test
    void testeTrimPrioridade() {
        Item item = new Item(1, "Nome", "Descrição", "Pendente", "  Alta  ");
        assertEquals("Alta", item.getPriority());
    }

    @Test
    void testeSetters() {
        Item item = new Item(1, "Nome Inicial", "Descrição Inicial");

        item.setName("Nome Atualizado");
        assertEquals("Nome Atualizado", item.getName());

        item.setDescription("Descrição Atualizada");
        assertEquals("Descrição Atualizada", item.getDescription());

        item.setStatus("Concluída");
        assertEquals("Concluída", item.getStatus());

        item.setPriority("Urgente");
        assertEquals("Urgente", item.getPriority());

        item.setId(999);
        assertEquals(999, item.getId());
    }

    @Test
    void testeSetterValidacaoNome() {
        Item item = new Item(1, "Nome Válido", "Descrição");

        assertThrows(ItemInvalidoException.class, () -> item.setName(""));

        assertThrows(ItemInvalidoException.class, () -> item.setName(null));

        assertThrows(ItemInvalidoException.class, () -> item.setName("a".repeat(101)));
    }

    @Test
    void testeSetterValidacaoDescricao() {
        Item item = new Item(1, "Nome", "Descrição Válida");

        assertThrows(ItemInvalidoException.class, () -> item.setDescription(""));

        assertThrows(ItemInvalidoException.class, () -> item.setDescription(null));

        assertThrows(ItemInvalidoException.class, () -> item.setDescription("a".repeat(101)));
    }

    @Test
    void testeSetterValidacaoStatus() {
        Item item = new Item(1, "Nome", "Descrição");

        item.setStatus("Em Andamento");
        assertEquals("Em Andamento", item.getStatus());

        assertThrows(ItemInvalidoException.class, () -> item.setStatus("Status Inválido"));
    }

    @Test
    void testeSetterValidacaoPrioridade() {
        Item item = new Item(1, "Nome", "Descrição");

        item.setPriority("Alta");
        assertEquals("Alta", item.getPriority());

        assertThrows(ItemInvalidoException.class, () -> item.setPriority("Prioridade Inválida"));
    }

    @Test
    void testeStatusBadgeClass() {
        Item item1 = new Item(1, "Teste", "Descrição", "Pendente", "Média");
        assertEquals("badge bg-warning", item1.getStatusBadgeClass());

        Item item2 = new Item(2, "Teste", "Descrição", "Em Andamento", "Média");
        assertEquals("badge bg-primary", item2.getStatusBadgeClass());

        Item item3 = new Item(3, "Teste", "Descrição", "Concluída", "Média");
        assertEquals("badge bg-success", item3.getStatusBadgeClass());

        Item item4 = new Item(4, "Teste", "Descrição", "Cancelada", "Média");
        assertEquals("badge bg-danger", item4.getStatusBadgeClass());
    }

    @Test
    void testePriorityBadgeClass() {
        Item item1 = new Item(1, "Teste", "Descrição", "Pendente", "Baixa");
        assertEquals("badge bg-success", item1.getPriorityBadgeClass());

        Item item2 = new Item(2, "Teste", "Descrição", "Pendente", "Média");
        assertEquals("badge bg-warning", item2.getPriorityBadgeClass());

        Item item3 = new Item(3, "Teste", "Descrição", "Pendente", "Alta");
        assertEquals("badge bg-danger", item3.getPriorityBadgeClass());

        Item item4 = new Item(4, "Teste", "Descrição", "Pendente", "Urgente");
        assertEquals("badge bg-dark", item4.getPriorityBadgeClass());
    }

    @Test
    void testeLimiteExatoCaracteres() {
        String nomeLimite = "a".repeat(100);
        String descricaoLimite = "b".repeat(100);

        Item item = new Item(1, nomeLimite, descricaoLimite);

        assertEquals(nomeLimite, item.getName());
        assertEquals(descricaoLimite, item.getDescription());
    }

    @Test
    void testeMensagensDeErroEspecificas() {
        try {
            new Item(1, "", "Descrição");
            fail("Deveria ter lançado exceção para nome vazio");
        } catch (ItemInvalidoException e) {
            assertTrue(e.getMessage().contains("nome"));
            assertTrue(e.getMessage().contains("não pode ser vazio"));
        }

        try {
            new Item(1, "Nome", "");
            fail("Deveria ter lançado exceção para descrição vazia");
        } catch (ItemInvalidoException e) {
            assertTrue(e.getMessage().contains("descrição"));
            assertTrue(e.getMessage().contains("não pode ser vazio"));
        }

        try {
            new Item(1, "Nome", "Descrição", "Status Inválido", "Média");
            fail("Deveria ter lançado exceção para status inválido");
        } catch (ItemInvalidoException e) {
            assertTrue(e.getMessage().contains("Status inválido"));
        }

        try {
            new Item(1, "Nome", "Descrição", "Pendente", "Prioridade Inválida");
            fail("Deveria ter lançado exceção para prioridade inválida");
        } catch (ItemInvalidoException e) {
            assertTrue(e.getMessage().contains("Prioridade inválida"));
        }
    }
}
