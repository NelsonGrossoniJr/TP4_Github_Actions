package org.example.model;

import org.example.exceptions.ItemInvalidoException;

public class Item {
    private int id;
    private String name;
    private String description;
    private String status;
    private String priority;

    public Item(int id, String name, String description) {
        this(id, name, description, "Pendente", "Média");
    }
    
    public Item(int id, String name, String description, String status, String priority) {
        this.id = id;
        this.name = validateAndTrim(name, "nome");
        this.description = validateAndTrim(description, "descrição");
        this.status = validateStatus(status);
        this.priority = validatePriority(priority);
    }

    private String validateAndTrim(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ItemInvalidoException("O " + fieldName + " não pode ser vazio.");
        }
        String trimmed = value.trim();
        if (trimmed.length() > 100) {
            throw new ItemInvalidoException("O " + fieldName + " não pode ter mais de 100 caracteres.");
        }
        return trimmed;
    }
    
    private String validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return "Pendente";
        }
        String validStatus = status.trim();
        if (!validStatus.matches("Pendente|Em Andamento|Concluída|Cancelada")) {
            throw new ItemInvalidoException("Status inválido. Use: Pendente, Em Andamento, Concluída ou Cancelada.");
        }
        return validStatus;
    }
    
    private String validatePriority(String priority) {
        if (priority == null || priority.trim().isEmpty()) {
            return "Média";
        }
        String validPriority = priority.trim();
        if (!validPriority.matches("Baixa|Média|Alta|Urgente")) {
            throw new ItemInvalidoException("Prioridade inválida. Use: Baixa, Média, Alta ou Urgente.");
        }
        return validPriority;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getPriority() {
        return priority;
    }

    public void setDescription(String description) {
        this.description = validateAndTrim(description, "descrição");
    }

    public void setName(String name) {
        this.name = validateAndTrim(name, "nome");
    }
    
    public void setStatus(String status) {
        this.status = validateStatus(status);
    }
    
    public void setPriority(String priority) {
        this.priority = validatePriority(priority);
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getStatusBadgeClass() {
        return switch (status) {
            case "Pendente" -> "badge bg-warning";
            case "Em Andamento" -> "badge bg-primary";
            case "Concluída" -> "badge bg-success";
            case "Cancelada" -> "badge bg-danger";
            default -> "badge bg-secondary";
        };
    }
    
    public String getPriorityBadgeClass() {
        return switch (priority) {
            case "Baixa" -> "badge bg-success";
            case "Média" -> "badge bg-warning";
            case "Alta" -> "badge bg-danger";
            case "Urgente" -> "badge bg-dark";
            default -> "badge bg-secondary";
        };
    }
}
