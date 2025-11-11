package org.example.view;

import org.example.model.Item;
import org.example.model.FeedbackMessage;

import java.util.List;
import java.util.Map;

public class ItemView {

    public static String renderList(List<Item> items, FeedbackMessage feedback) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"pt\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>Sistema de Gerenciamento de Tarefas</title>\n");
        html.append("    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n");
        html.append("    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css\" rel=\"stylesheet\">\n");
        html.append("    <style>\n");
        html.append("        .task-card { transition: transform 0.2s; }\n");
        html.append("        .task-card:hover { transform: translateY(-2px); }\n");
        html.append("        .priority-urgent { border-left: 4px solid #dc3545; }\n");
        html.append("        .priority-high { border-left: 4px solid #fd7e14; }\n");
        html.append("        .priority-medium { border-left: 4px solid #ffc107; }\n");
        html.append("        .priority-low { border-left: 4px solid #198754; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body class=\"bg-light\">\n");
        html.append("    <nav class=\"navbar navbar-expand-lg navbar-dark bg-primary\">\n");
        html.append("        <div class=\"container\">\n");
        html.append("            <a class=\"navbar-brand\" href=\"/\">\n");
        html.append("                <i class=\"bi bi-check2-square me-2\"></i>Sistema de Tarefas\n");
        html.append("            </a>\n");
        html.append("        </div>\n");
        html.append("    </nav>\n");
        html.append("    \n");
        html.append("    <div class=\"container mt-4\">\n");
        html.append("        <div class=\"row\">\n");
        html.append("            <div class=\"col-12\">\n");
        html.append("                <div class=\"d-flex justify-content-between align-items-center mb-4\">\n");
        html.append("                    <h1><i class=\"bi bi-list-task me-2\"></i>Gerenciamento de Tarefas</h1>\n");
        html.append("                    <a href=\"/items/new\" class=\"btn btn-primary\">\n");
        html.append("                        <i class=\"bi bi-plus-circle me-2\"></i>Nova Tarefa\n");
        html.append("                    </a>\n");
        html.append("                </div>\n");
        
        // Exibir mensagem de feedback se existir
        if (feedback != null) {
            html.append("                <div class=\"alert ").append(feedback.getCssClass()).append(" alert-dismissible fade show\" role=\"alert\">\n");
            html.append("                    <i class=\"bi bi-info-circle me-2\"></i>").append(escapeHtml(feedback.message())).append("\n");
            html.append("                    <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\"></button>\n");
            html.append("                </div>\n");
        }
        
        // Estatísticas
        html.append("                <div class=\"row mb-4\">\n");
        html.append("                    <div class=\"col-md-3\">\n");
        html.append("                        <div class=\"card bg-primary text-white\">\n");
        html.append("                            <div class=\"card-body\">\n");
        html.append("                                <div class=\"d-flex justify-content-between\">\n");
        html.append("                                    <div>\n");
        html.append("                                        <h6 class=\"card-title\">Total</h6>\n");
        html.append("                                        <h3>").append(items.size()).append("</h3>\n");
        html.append("                                    </div>\n");
        html.append("                                    <i class=\"bi bi-list-ul fs-1\"></i>\n");
        html.append("                                </div>\n");
        html.append("                            </div>\n");
        html.append("                        </div>\n");
        html.append("                    </div>\n");
        html.append("                    <div class=\"col-md-3\">\n");
        html.append("                        <div class=\"card bg-warning text-white\">\n");
        html.append("                            <div class=\"card-body\">\n");
        html.append("                                <div class=\"d-flex justify-content-between\">\n");
        html.append("                                    <div>\n");
        html.append("                                        <h6 class=\"card-title\">Pendentes</h6>\n");
        html.append("                                        <h3>").append(countByStatus(items, "Pendente")).append("</h3>\n");
        html.append("                                    </div>\n");
        html.append("                                    <i class=\"bi bi-clock fs-1\"></i>\n");
        html.append("                                </div>\n");
        html.append("                            </div>\n");
        html.append("                        </div>\n");
        html.append("                    </div>\n");
        html.append("                    <div class=\"col-md-3\">\n");
        html.append("                        <div class=\"card bg-info text-white\">\n");
        html.append("                            <div class=\"card-body\">\n");
        html.append("                                <div class=\"d-flex justify-content-between\">\n");
        html.append("                                    <div>\n");
        html.append("                                        <h6 class=\"card-title\">Em Andamento</h6>\n");
        html.append("                                        <h3>").append(countByStatus(items, "Em Andamento")).append("</h3>\n");
        html.append("                                    </div>\n");
        html.append("                                    <i class=\"bi bi-arrow-repeat fs-1\"></i>\n");
        html.append("                                </div>\n");
        html.append("                            </div>\n");
        html.append("                        </div>\n");
        html.append("                    </div>\n");
        html.append("                    <div class=\"col-md-3\">\n");
        html.append("                        <div class=\"card bg-success text-white\">\n");
        html.append("                            <div class=\"card-body\">\n");
        html.append("                                <div class=\"d-flex justify-content-between\">\n");
        html.append("                                    <div>\n");
        html.append("                                        <h6 class=\"card-title\">Concluídas</h6>\n");
        html.append("                                        <h3>").append(countByStatus(items, "Concluída")).append("</h3>\n");
        html.append("                                    </div>\n");
        html.append("                                    <i class=\"bi bi-check-circle fs-1\"></i>\n");
        html.append("                                </div>\n");
        html.append("                            </div>\n");
        html.append("                        </div>\n");
        html.append("                    </div>\n");
        html.append("                </div>\n");
        
        // Lista de tarefas
        if (items.isEmpty()) {
            html.append("                <div class=\"text-center py-5\">\n");
            html.append("                    <i class=\"bi bi-inbox fs-1 text-muted\"></i>\n");
            html.append("                    <h3 class=\"text-muted mt-3\">Nenhuma tarefa encontrada</h3>\n");
            html.append("                    <p class=\"text-muted\">Comece criando sua primeira tarefa!</p>\n");
            html.append("                    <a href=\"/items/new\" class=\"btn btn-primary\">\n");
            html.append("                        <i class=\"bi bi-plus-circle me-2\"></i>Criar Primeira Tarefa\n");
            html.append("                    </a>\n");
            html.append("                </div>\n");
        } else {
            html.append("                <div class=\"row\">\n");
            
        for (Item item : items) {
                String priorityClass = getPriorityClass(item.getPriority());
                
                html.append("                    <div class=\"col-md-6 col-lg-4 mb-3\">\n");
                html.append("                        <div class=\"card task-card ").append(priorityClass).append(" h-100\">\n");
                html.append("                            <div class=\"card-body\">\n");
                html.append("                                <div class=\"d-flex justify-content-between align-items-start mb-2\">\n");
                html.append("                                    <h6 class=\"card-title mb-0\">").append(escapeHtml(item.getName())).append("</h6>\n");
                html.append("                                    <span class=\"").append(item.getStatusBadgeClass()).append("\">").append(item.getStatus()).append("</span>\n");
                html.append("                                </div>\n");
                html.append("                                <p class=\"card-text text-muted small\">").append(escapeHtml(item.getDescription())).append("</p>\n");
                html.append("                                <div class=\"d-flex justify-content-between align-items-center\">\n");
                html.append("                                    <span class=\"").append(item.getPriorityBadgeClass()).append("\">").append(item.getPriority()).append("</span>\n");
                html.append("                                    <div class=\"btn-group btn-group-sm\">\n");
                html.append("                                        <a href=\"/items/edit/").append(item.getId()).append("\" class=\"btn btn-outline-primary\" title=\"Editar\">\n");
                html.append("                                            <i class=\"bi bi-pencil\"></i>\n");
                html.append("                                        </a>\n");
                html.append("                                        <button type=\"button\" class=\"btn btn-outline-danger\" \n");
                html.append("                                                onclick=\"confirmDelete(").append(item.getId()).append(", '").append(escapeHtml(item.getName())).append("')\" title=\"Excluir\">\n");
                html.append("                                            <i class=\"bi bi-trash\"></i>\n");
                html.append("                                        </button>\n");
                html.append("                                    </div>\n");
                html.append("                                </div>\n");
                html.append("                            </div>\n");
                html.append("                        </div>\n");
                html.append("                    </div>\n");
            }
            
            html.append("                </div>\n");
        }
        
        html.append("            </div>\n");
        html.append("        </div>\n");
        html.append("    </div>\n");
        html.append("    \n");
        html.append("    <!-- Modal de Confirmação -->\n");
        html.append("    <div class=\"modal fade\" id=\"deleteModal\" tabindex=\"-1\">\n");
        html.append("        <div class=\"modal-dialog\">\n");
        html.append("            <div class=\"modal-content\">\n");
        html.append("                <div class=\"modal-header\">\n");
        html.append("                    <h5 class=\"modal-title\">Confirmar Exclusão</h5>\n");
        html.append("                    <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\"></button>\n");
        html.append("                </div>\n");
        html.append("                <div class=\"modal-body\">\n");
        html.append("                    <p>Tem certeza que deseja excluir a tarefa <strong id=\"taskName\"></strong>?</p>\n");
        html.append("                    <p class=\"text-muted small\">Esta ação não pode ser desfeita.</p>\n");
        html.append("                </div>\n");
        html.append("                <div class=\"modal-footer\">\n");
        html.append("                    <button type=\"button\" class=\"btn btn-secondary\" data-bs-dismiss=\"modal\">Cancelar</button>\n");
        html.append("                    <form id=\"deleteForm\" method=\"post\" style=\"display: inline;\">\n");
        html.append("                        <button type=\"submit\" class=\"btn btn-danger\">Excluir</button>\n");
        html.append("                    </form>\n");
        html.append("                </div>\n");
        html.append("            </div>\n");
        html.append("        </div>\n");
        html.append("    </div>\n");
        html.append("    \n");
        html.append("    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js\"></script>\n");
        html.append("    <script>\n");
        html.append("        function confirmDelete(id, name) {\n");
        html.append("            document.getElementById('taskName').textContent = name;\n");
        html.append("            document.getElementById('deleteForm').action = '/items/delete/' + id;\n");
        html.append("            new bootstrap.Modal(document.getElementById('deleteModal')).show();\n");
        html.append("        }\n");
        html.append("    </script>\n");
        html.append("</body>\n");
        html.append("</html>\n");
        
        return html.toString();
    }

    public static String renderForm(Map<String, Object> model, FeedbackMessage feedback) {
        Object id = model.get("id");
        String action = id != null ? "/items/edit/" + id : "/items";
        String title = id != null ? "Editar Tarefa" : "Nova Tarefa";
        String name = (String) model.getOrDefault("name", "");
        String description = (String) model.getOrDefault("description", "");
        String status = (String) model.getOrDefault("status", "Pendente");
        String priority = (String) model.getOrDefault("priority", "Média");

        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"pt\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>").append(title).append("</title>\n");
        html.append("    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n");
        html.append("    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css\" rel=\"stylesheet\">\n");
        html.append("</head>\n");
        html.append("<body class=\"bg-light\">\n");
        html.append("    <nav class=\"navbar navbar-expand-lg navbar-dark bg-primary\">\n");
        html.append("        <div class=\"container\">\n");
        html.append("            <a class=\"navbar-brand\" href=\"/\">\n");
        html.append("                <i class=\"bi bi-check2-square me-2\"></i>Sistema de Tarefas\n");
        html.append("            </a>\n");
        html.append("        </div>\n");
        html.append("    </nav>\n");
        html.append("    \n");
        html.append("    <div class=\"container mt-4\">\n");
        html.append("        <div class=\"row justify-content-center\">\n");
        html.append("            <div class=\"col-md-8\">\n");
        html.append("                <div class=\"card\">\n");
        html.append("                    <div class=\"card-header\">\n");
        html.append("                        <h4 class=\"mb-0\"><i class=\"bi bi-pencil-square me-2\"></i>").append(title).append("</h4>\n");
        html.append("                    </div>\n");
        html.append("                    <div class=\"card-body\">\n");
        
        // Exibir mensagem de feedback se existir
        if (feedback != null) {
            html.append("                        <div class=\"alert ").append(feedback.getCssClass()).append(" alert-dismissible fade show\" role=\"alert\">\n");
            html.append("                            <i class=\"bi bi-info-circle me-2\"></i>").append(escapeHtml(feedback.message())).append("\n");
            html.append("                            <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\"></button>\n");
            html.append("                        </div>\n");
        }
        
        html.append("                        <form action=\"").append(action).append("\" method=\"post\">\n");
        html.append("                            <div class=\"mb-3\">\n");
        html.append("                                <label for=\"name\" class=\"form-label\">Nome da Tarefa *</label>\n");
        html.append("                                <input type=\"text\" class=\"form-control\" id=\"name\" name=\"name\" \n");
        html.append("                                       value=\"").append(escapeHtml(name)).append("\" required maxlength=\"100\" \n");
        html.append("                                       placeholder=\"Digite o nome da tarefa\">\n");
        html.append("                                <div class=\"form-text\">Máximo 100 caracteres</div>\n");
        html.append("                            </div>\n");
        html.append("                            \n");
        html.append("                            <div class=\"mb-3\">\n");
        html.append("                                <label for=\"description\" class=\"form-label\">Descrição *</label>\n");
        html.append("                                <textarea class=\"form-control\" id=\"description\" name=\"description\" \n");
        html.append("                                          rows=\"3\" required maxlength=\"100\" \n");
        html.append("                                          placeholder=\"Descreva a tarefa\">").append(escapeHtml(description)).append("</textarea>\n");
        html.append("                                <div class=\"form-text\">Máximo 100 caracteres</div>\n");
        html.append("                            </div>\n");
        html.append("                            \n");
        html.append("                            <div class=\"row\">\n");
        html.append("                                <div class=\"col-md-6\">\n");
        html.append("                                    <div class=\"mb-3\">\n");
        html.append("                                        <label for=\"priority\" class=\"form-label\">Prioridade</label>\n");
        html.append("                                        <select class=\"form-select\" id=\"priority\" name=\"priority\">\n");
        
        // Opções de prioridade
        String[] priorities = {"Baixa", "Média", "Alta", "Urgente"};
        for (String p : priorities) {
            String selected = p.equals(priority) ? " selected" : "";
            html.append("                                            <option value=\"").append(p).append("\"").append(selected).append(">").append(p).append("</option>\n");
        }
        
        html.append("                                        </select>\n");
        html.append("                                    </div>\n");
        html.append("                                </div>\n");
        html.append("                                \n");
        html.append("                                <div class=\"col-md-6\">\n");
        html.append("                                    <div class=\"mb-3\">\n");
        html.append("                                        <label for=\"status\" class=\"form-label\">Status</label>\n");
        html.append("                                        <select class=\"form-select\" id=\"status\" name=\"status\">\n");
        
        // Opções de status
        String[] statuses = {"Pendente", "Em Andamento", "Concluída", "Cancelada"};
        for (String s : statuses) {
            String selected = s.equals(status) ? " selected" : "";
            html.append("                                            <option value=\"").append(s).append("\"").append(selected).append(">").append(s).append("</option>\n");
        }
        
        html.append("                                        </select>\n");
        html.append("                                    </div>\n");
        html.append("                                </div>\n");
        html.append("                            </div>\n");
        html.append("                            \n");
        html.append("                            <div class=\"d-flex gap-2\">\n");
        html.append("                                <button type=\"submit\" class=\"btn btn-success\">\n");
        html.append("                                    <i class=\"bi bi-check-circle me-2\"></i>Salvar\n");
        html.append("                                </button>\n");
        html.append("                                <a href=\"/items\" class=\"btn btn-secondary\">\n");
        html.append("                                    <i class=\"bi bi-arrow-left me-2\"></i>Cancelar\n");
        html.append("                                </a>\n");
        html.append("                            </div>\n");
        html.append("                        </form>\n");
        html.append("                    </div>\n");
        html.append("                </div>\n");
        html.append("            </div>\n");
        html.append("        </div>\n");
        html.append("    </div>\n");
        html.append("    \n");
        html.append("    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js\"></script>\n");
        html.append("</body>\n");
        html.append("</html>\n");
        
        return html.toString();
    }
    
    private static String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
    
    private static int countByStatus(List<Item> items, String status) {
        return (int) items.stream()
                .filter(item -> item.getStatus().equals(status))
                .count();
    }
    
    private static String getPriorityClass(String priority) {
        return switch (priority) {
            case "Urgente" -> "priority-urgent";
            case "Alta" -> "priority-high";
            case "Média" -> "priority-medium";
            case "Baixa" -> "priority-low";
            default -> "";
        };
    }
}