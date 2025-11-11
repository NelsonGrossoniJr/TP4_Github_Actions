package org.example.service;

import org.example.model.Item;
import org.example.exceptions.ItemNaoEncontradoException;
import org.example.exceptions.ItemInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceTest {

    private ItemService itemService;

    @BeforeEach
    void setUp() {
        itemService = new ItemService();
    }

    @Test
    void testeAddItem() {
        int tamanhoInicial = itemService.listar().size();
        Item novoItem = itemService.addItem("Novo Item", "Descrição do novo item");

        assertEquals(tamanhoInicial + 1, itemService.listar().size());
        assertNotNull(novoItem);
        assertEquals("Novo Item", novoItem.getName());
        assertEquals("Descrição do novo item", novoItem.getDescription());
        assertEquals("Pendente", novoItem.getStatus());
        assertEquals("Média", novoItem.getPriority());
    }

    @Test
    void testeAddItemComPrioridadeEStatus() {
        int tamanhoInicial = itemService.listar().size();
        Item novoItem = itemService.addItem("Item com Prioridade", "Descrição", "Alta", "Em Andamento");

        assertEquals(tamanhoInicial + 1, itemService.listar().size());
        assertNotNull(novoItem);
        assertEquals("Item com Prioridade", novoItem.getName());
        assertEquals("Alta", novoItem.getPriority());
        assertEquals("Em Andamento", novoItem.getStatus());
    }

    @Test
    void testeFindById() {
        // Testa busca por item existente
        Item itemAchado = itemService.findById(1);
        assertNotNull(itemAchado);
        assertEquals("Implementar autenticação", itemAchado.getName());
        assertEquals("Desenvolver sistema de login e registro de usuários", itemAchado.getDescription());

        // Testa busca por item não existente
        assertThrows(ItemNaoEncontradoException.class, () -> itemService.findById(99));
    }

    @Test
    void testeUpdateItem() {
        // Testa atualização de item existente
        Item itemAtualizado = itemService.updateItem(1, "Autenticação Atualizada", "Sistema de login melhorado");
        assertNotNull(itemAtualizado);
        assertEquals("Autenticação Atualizada", itemAtualizado.getName());
        assertEquals("Sistema de login melhorado", itemAtualizado.getDescription());

        // Verifica se a atualização foi persistida
        Item itemVerificacao = itemService.findById(1);
        assertEquals("Autenticação Atualizada", itemVerificacao.getName());
        assertEquals("Sistema de login melhorado", itemVerificacao.getDescription());

        // Testa atualização de item não existente
        assertThrows(ItemNaoEncontradoException.class, () -> itemService.updateItem(99, "Item Inexistente", "Descrição"));
    }

    @Test
    void testeUpdateItemComPrioridadeEStatus() {
        Item itemAtualizado = itemService.updateItem(2, "Documentação Atualizada", "API documentada", "Urgente", "Concluída");
        assertNotNull(itemAtualizado);
        assertEquals("Documentação Atualizada", itemAtualizado.getName());
        assertEquals("Urgente", itemAtualizado.getPriority());
        assertEquals("Concluída", itemAtualizado.getStatus());
    }

    @Test
    void testDeleteItem() {
        int tamanhoInicial = itemService.listar().size();

        // Testa exclusão de item existente
        boolean resultado = itemService.deleteItem(5);
        assertTrue(resultado);
        assertEquals(tamanhoInicial - 1, itemService.listar().size());

        // Verifica se o item foi realmente removido
        assertThrows(ItemNaoEncontradoException.class, () -> itemService.findById(5));

        // Testa exclusão de item não existente
        assertThrows(ItemNaoEncontradoException.class, () -> itemService.deleteItem(99));
    }

    @Test
    void testListar() {
        List<Item> itens = itemService.listar();
        assertNotNull(itens);
        assertEquals(5, itens.size()); // 5 itens iniciais

        // Verifica se os itens iniciais estão presentes
        assertTrue(itens.stream().anyMatch(item -> item.getName().equals("Implementar autenticação")));
        assertTrue(itens.stream().anyMatch(item -> item.getName().equals("Criar documentação da API")));
        assertTrue(itens.stream().anyMatch(item -> item.getName().equals("Configurar CI/CD")));
        assertTrue(itens.stream().anyMatch(item -> item.getName().equals("Testes unitários")));
        assertTrue(itens.stream().anyMatch(item -> item.getName().equals("Otimizar performance")));
    }

    @Test
    void testeConstrutorTamanhoInicial() {
        assertEquals(5, itemService.listar().size()); // 5 itens iniciais
        assertNotNull(itemService.findById(1));
        assertNotNull(itemService.findById(2));
        assertNotNull(itemService.findById(3));
        assertNotNull(itemService.findById(4));
        assertNotNull(itemService.findById(5));
    }

    @Test
    void testeBuscarPorNome() {
        List<Item> resultados = itemService.buscarPorNome("autenticação");
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("Implementar autenticação", resultados.getFirst().getName());

        // Testa busca por termo que não existe
        List<Item> semResultados = itemService.buscarPorNome("xyz123");
        assertTrue(semResultados.isEmpty());
    }

    @Test
    void testeListarPorStatus() {
        List<Item> pendentes = itemService.listarPorStatus("Pendente");
        assertNotNull(pendentes);
        assertTrue(pendentes.size() >= 3); // Pelo menos 3 itens pendentes

        List<Item> emAndamento = itemService.listarPorStatus("Em Andamento");
        assertNotNull(emAndamento);
        assertEquals(1, emAndamento.size()); // 1 item em andamento
        assertEquals("Implementar autenticação", emAndamento.getFirst().getName());
    }

    @Test
    void testeListarPorPrioridade() {
        List<Item> altaPrioridade = itemService.listarPorPrioridade("Alta");
        assertNotNull(altaPrioridade);
        assertTrue(altaPrioridade.size() >= 2); // Pelo menos 2 itens com prioridade alta

        List<Item> baixaPrioridade = itemService.listarPorPrioridade("Baixa");
        assertNotNull(baixaPrioridade);
        assertEquals(1, baixaPrioridade.size()); // 1 item com prioridade baixa
        assertEquals("Otimizar performance", baixaPrioridade.getFirst().getName());
    }

    @Test
    void testeValidacaoItemInvalido() {
        // Testa nome vazio
        assertThrows(ItemInvalidoException.class, () -> itemService.addItem("", "Descrição válida"));

        // Testa descrição vazia
        assertThrows(ItemInvalidoException.class, () -> itemService.addItem("Nome válido", ""));

        // Testa nome muito longo
        String nomeLongo = "a".repeat(101);
        assertThrows(ItemInvalidoException.class, () -> itemService.addItem(nomeLongo, "Descrição válida"));

        // Testa descrição muito longa
        String descricaoLonga = "a".repeat(101);
        assertThrows(ItemInvalidoException.class, () -> itemService.addItem("Nome válido", descricaoLonga));
    }

    @Test
    void testeEstatisticas() {
        assertEquals(5, itemService.getTotalTarefas());
        assertEquals(1, itemService.getTarefasPorStatus("Em Andamento"));
        assertEquals(1, itemService.getTarefasPorStatus("Concluída"));

        List<String> statusDisponiveis = itemService.getStatusDisponiveis();
        assertEquals(4, statusDisponiveis.size());
        assertTrue(statusDisponiveis.contains("Pendente"));
        assertTrue(statusDisponiveis.contains("Em Andamento"));
        assertTrue(statusDisponiveis.contains("Concluída"));
        assertTrue(statusDisponiveis.contains("Cancelada"));

        List<String> prioridadesDisponiveis = itemService.getPrioridadesDisponiveis();
        assertEquals(4, prioridadesDisponiveis.size());
        assertTrue(prioridadesDisponiveis.contains("Baixa"));
        assertTrue(prioridadesDisponiveis.contains("Média"));
        assertTrue(prioridadesDisponiveis.contains("Alta"));
        assertTrue(prioridadesDisponiveis.contains("Urgente"));
    }

    // ========== TESTES PARAMETRIZADOS ==========

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n", "\r\n"})
    void testeValidacaoNomeInvalidoParametrizado(String nomeInvalido) {
        assertThrows(ItemInvalidoException.class, () -> itemService.addItem(nomeInvalido, "Descrição válida"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n", "\r\n"})
    void testeValidacaoDescricaoInvalidaParametrizado(String descricaoInvalida) {
        assertThrows(ItemInvalidoException.class, () -> itemService.addItem("Nome válido", descricaoInvalida));
    }

    @ParameterizedTest
    @MethodSource("fornecerNomesLongos")
    void testeValidacaoNomeMuitoLongoParametrizado(String nomeLongo) {
        assertThrows(ItemInvalidoException.class, () -> itemService.addItem(nomeLongo, "Descrição válida"));
    }

    @ParameterizedTest
    @MethodSource("fornecerDescricoesLongas")
    void testeValidacaoDescricaoMuitoLongaParametrizado(String descricaoLonga) {
        assertThrows(ItemInvalidoException.class, () -> itemService.addItem("Nome válido", descricaoLonga));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Pendente", "Em Andamento", "Concluída", "Cancelada"})
    void testeStatusValidosParametrizado(String status) {
        Item item = itemService.addItem("Teste Status", "Descrição", "Média", status);
        assertEquals(status, item.getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Baixa", "Média", "Alta", "Urgente"})
    void testePrioridadesValidasParametrizado(String prioridade) {
        Item item = itemService.addItem("Teste Prioridade", "Descrição", prioridade, "Pendente");
        assertEquals(prioridade, item.getPriority());
    }

    @ParameterizedTest
    @CsvSource({
            "1, Implementar autenticação, Desenvolver sistema de login e registro de usuários",
            "2, Criar documentação da API, Documentar todos os endpoints da API REST",
            "3, Configurar CI/CD, Implementar pipeline de integração contínua",
            "4, Testes unitários, Escrever testes para todas as classes de serviço",
            "5, Otimizar performance, Melhorar tempo de resposta das consultas"
    })
    void testeDadosIniciaisParametrizado(int id, String nomeEsperado, String descricaoEsperada) {
        Item item = itemService.findById(id);
        assertEquals(nomeEsperado, item.getName());
        assertEquals(descricaoEsperada, item.getDescription());
    }

    @ParameterizedTest
    @CsvSource({
            "autenticação, 1, Implementar autenticação",
            "documentação, 1, Criar documentação da API",
            "CI/CD, 1, Configurar CI/CD",
            "testes, 1, Testes unitários",
            "performance, 1, Otimizar performance"
    })
    void testeBuscaPorNomeParametrizado(String termoBusca, int quantidadeEsperada, String nomeEsperado) {
        List<Item> resultados = itemService.buscarPorNome(termoBusca);
        assertEquals(quantidadeEsperada, resultados.size());
        assertEquals(nomeEsperado, resultados.getFirst().getName());
    }

    @ParameterizedTest
    @CsvSource({
            "Pendente, 3",
            "Em Andamento, 1",
            "Concluída, 1",
            "Cancelada, 0"
    })
    void testeListarPorStatusParametrizado(String status, int quantidadeEsperada) {
        List<Item> resultados = itemService.listarPorStatus(status);
        assertEquals(quantidadeEsperada, resultados.size());
    }

    @ParameterizedTest
    @CsvSource({
            "Baixa, 1",
            "Média, 2",
            "Alta, 2",
            "Urgente, 0"
    })
    void testeListarPorPrioridadeParametrizado(String prioridade, int quantidadeEsperada) {
        List<Item> resultados = itemService.listarPorPrioridade(prioridade);
        assertEquals(quantidadeEsperada, resultados.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100, Integer.MIN_VALUE})
    void testeIdInvalidoParametrizado(int idInvalido) {
        assertThrows(ItemInvalidoException.class, () -> itemService.findById(idInvalido));
    }

    @ParameterizedTest
    @ValueSource(ints = {99, 100, 1000, Integer.MAX_VALUE})
    void testeIdInexistenteParametrizado(int idInexistente) {
        assertThrows(ItemNaoEncontradoException.class, () -> itemService.findById(idInexistente));
    }

    @ParameterizedTest
    @MethodSource("fornecerDadosParaTesteFuzz")
    void testeFuzzInputsParametrizado(String nome, String descricao, boolean deveFalhar) {
        if (deveFalhar) {
            assertThrows(ItemInvalidoException.class, () -> itemService.addItem(nome, descricao));
        } else {
            assertDoesNotThrow(() -> {
                itemService.addItem(nome, descricao);
            });
        }
    }

    static Stream<String> fornecerNomesLongos() {
        return Stream.of(
                "a".repeat(101), // Exatamente 101 caracteres
                "b".repeat(150), // 150 caracteres
                "c".repeat(1000), // 1000 caracteres
                "d".repeat(10000) // 10000 caracteres
        );
    }

    static Stream<String> fornecerDescricoesLongas() {
        return Stream.of(
                "a".repeat(101), // Exatamente 101 caracteres
                "b".repeat(150), // 150 caracteres
                "c".repeat(1000), // 1000 caracteres
                "d".repeat(10000) // 10000 caracteres
        );
    }

    static Stream<Arguments> fornecerDadosParaTesteFuzz() {
        return Stream.of(
                // Casos que devem falhar
                Arguments.of("", "Descrição válida", true),
                Arguments.of("Nome válido", "", true),
                Arguments.of(null, "Descrição válida", true),
                Arguments.of("Nome válido", null, true),
                Arguments.of("   ", "Descrição válida", true),
                Arguments.of("Nome válido", "   ", true),
                Arguments.of("a".repeat(101), "Descrição válida", true),
                Arguments.of("Nome válido", "a".repeat(101), true),

                // Tentativas de SQL Injection
                Arguments.of("'; DROP TABLE items; --", "Descrição válida", false), // Nome válido, mas suspeito
                Arguments.of("Nome válido", "'; DROP TABLE items; --", false), // Descrição válida, mas suspeita

                // Tentativas de XSS
                Arguments.of("<script>alert('xss')</script>", "Descrição válida", false), // Nome válido, mas suspeito
                Arguments.of("Nome válido", "<script>alert('xss')</script>", false), // Descrição válida, mas suspeita

                // Caracteres especiais
                Arguments.of("Nome com ção", "Descrição com ãção", false),
                Arguments.of("Nome com emoji 🚀", "Descrição com emoji 📝", false),
                Arguments.of("Nome com símbolos !@#$%", "Descrição com símbolos &*()", false),

                // Casos válidos
                Arguments.of("Nome válido", "Descrição válida", false),
                Arguments.of("a", "b", false), // Mínimo possível
                Arguments.of("a".repeat(100), "b".repeat(100), false), // Máximo possível
                Arguments.of("Nome com espaços", "Descrição com espaços", false)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1, Nome Atualizado 1, Descrição Atualizada 1",
            "2, Nome Atualizado 2, Descrição Atualizada 2",
            "3, Nome Atualizado 3, Descrição Atualizada 3",
            "4, Nome Atualizado 4, Descrição Atualizada 4",
            "5, Nome Atualizado 5, Descrição Atualizada 5"
    })
    void testeUpdateItemParametrizado(int id, String novoNome, String novaDescricao) {
        Item itemAtualizado = itemService.updateItem(id, novoNome, novaDescricao);
        assertEquals(novoNome, itemAtualizado.getName());
        assertEquals(novaDescricao, itemAtualizado.getDescription());

        // Verifica persistência
        Item itemVerificacao = itemService.findById(id);
        assertEquals(novoNome, itemVerificacao.getName());
        assertEquals(novaDescricao, itemVerificacao.getDescription());
    }

    @ParameterizedTest
    @CsvSource({
            "1, Alta, Em Andamento",
            "2, Média, Concluída",
            "3, Baixa, Cancelada",
            "4, Urgente, Pendente"
    })
    void testeUpdateItemComPrioridadeEStatusParametrizado(int id, String novaPrioridade, String novoStatus) {
        Item itemAtualizado = itemService.updateItem(id, "Nome Atualizado", "Descrição Atualizada", novaPrioridade, novoStatus);
        assertEquals(novaPrioridade, itemAtualizado.getPriority());
        assertEquals(novoStatus, itemAtualizado.getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "termo inexistente",
            "xyz123",
            "abc def ghi",
            "123456",
            "!@#$%^&*()",
            "SELECT * FROM",
            "<script>",
            "DROP TABLE"
    })
    void testeBuscaTermosInexistentesParametrizado(String termoInexistente) {
        List<Item> resultados = itemService.buscarPorNome(termoInexistente);
        assertTrue(resultados.isEmpty(), "Busca por termo inexistente deve retornar lista vazia");
    }
}