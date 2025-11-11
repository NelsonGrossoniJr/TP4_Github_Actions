package org.example.service;

import org.example.model.Item;
import org.example.exceptions.ItemInvalidoException;
import org.example.exceptions.ItemNaoEncontradoException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemService {
    private final List<Item> items = new ArrayList<>();
    private int nextId = 1;

    public ItemService() {
        // Dados de exemplo mais realistas para um sistema de tarefas
        addItem("Implementar autenticação", "Desenvolver sistema de login e registro de usuários", "Alta", "Em Andamento");
        addItem("Criar documentação da API", "Documentar todos os endpoints da API REST", "Média", "Pendente");
        addItem("Configurar CI/CD", "Implementar pipeline de integração contínua", "Alta", "Pendente");
        addItem("Testes unitários", "Escrever testes para todas as classes de serviço", "Média", "Pendente");
        addItem("Otimizar performance", "Melhorar tempo de resposta das consultas", "Baixa", "Concluída");
    }

    public Item addItem(String name, String description) {
        return addItem(name, description, "Média", "Pendente");
    }
    
    public Item addItem(String name, String description, String priority, String status) {
        validateId(nextId);
        Item item = new Item(nextId++, name, description, status, priority);
        items.add(item);
        return item;
    }

    public Item findById(int id) {
        validateId(id);
        return items.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ItemNaoEncontradoException("Tarefa com ID " + id + " não encontrada."));
    }

    public Item updateItem(int id, String name, String description) {
        return updateItem(id, name, description, null, null);
    }
    
    public Item updateItem(int id, String name, String description, String priority, String status) {
        Item item = findById(id);
        item.setName(name);
        item.setDescription(description);
        if (priority != null) {
            item.setPriority(priority);
        }
        if (status != null) {
            item.setStatus(status);
        }
        return item;
    }

    public boolean deleteItem(int id) {
        validateId(id);
        boolean removed = items.removeIf(item -> item.getId() == id);
        if (!removed) {
            throw new ItemNaoEncontradoException("Tarefa com ID " + id + " não encontrada para exclusão.");
        }
        return true;
    }

    public List<Item> listar() {
        return new ArrayList<>(items);
    }
    
    public List<Item> listarPorStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return listar();
        }
        return items.stream()
                .filter(item -> item.getStatus().equalsIgnoreCase(status.trim()))
                .collect(Collectors.toList());
    }
    
    public List<Item> listarPorPrioridade(String priority) {
        if (priority == null || priority.trim().isEmpty()) {
            return listar();
        }
        return items.stream()
                .filter(item -> item.getPriority().equalsIgnoreCase(priority.trim()))
                .collect(Collectors.toList());
    }
    
    public List<Item> buscarPorNome(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return listar();
        }
        String term = searchTerm.trim().toLowerCase();
        return items.stream()
                .filter(item -> item.getName().toLowerCase().contains(term) || 
                               item.getDescription().toLowerCase().contains(term))
                .collect(Collectors.toList());
    }
    
    public int getTotalTarefas() {
        return items.size();
    }
    
    public int getTarefasPorStatus(String status) {
        return (int) items.stream()
                .filter(item -> item.getStatus().equals(status))
                .count();
    }
    
    public List<String> getStatusDisponiveis() {
        return List.of("Pendente", "Em Andamento", "Concluída", "Cancelada");
    }
    
    public List<String> getPrioridadesDisponiveis() {
        return List.of("Baixa", "Média", "Alta", "Urgente");
    }
    
    private void validateId(int id) {
        if (id <= 0) {
            throw new ItemInvalidoException("O ID deve ser um número positivo.");
        }
    }
}
