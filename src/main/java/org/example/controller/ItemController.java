package org.example.controller;

import io.javalin.Javalin;
import org.example.model.Item;
import org.example.model.FeedbackMessage;
import org.example.service.ItemService;
import org.example.view.ItemView;

import org.example.exceptions.ItemInvalidoException;
import org.example.exceptions.ItemNaoEncontradoException;

import java.util.HashMap;
import java.util.Map;

public class ItemController {
    private final ItemService service = new ItemService();

    public ItemController(Javalin app) {
        // Rota para a raiz - redireciona para a lista de itens
        app.get("/", ctx -> ctx.redirect("/items"));
        
        // Lista de tarefas
        app.get("/items", ctx -> {
            String search = ctx.queryParam("search");
            String status = ctx.queryParam("status");
            String priority = ctx.queryParam("priority");
            String success = ctx.queryParam("success");
            String error = ctx.queryParam("error");
            
            var items = getFilteredItems(search, status, priority);
            
            FeedbackMessage feedback = null;
            if (success != null) {
                feedback = FeedbackMessage.success(success);
            } else if (error != null) {
                feedback = FeedbackMessage.error(error);
            }
            
            ctx.html(ItemView.renderList(items, feedback));
        });

        // Formulário para nova tarefa
        app.get("/items/new", ctx -> {
            Map<String, Object> model = new HashMap<>();
            ctx.html(ItemView.renderForm(model, null));
        });

        // Criar nova tarefa
        app.post("/items", ctx -> {
            try {
                String name = ctx.formParam("name");
                String description = ctx.formParam("description");
                String priority = ctx.formParam("priority");
                String status = ctx.formParam("status");
                
                service.addItem(name, description, priority, status);
                ctx.redirect("/items?success=Tarefa criada com sucesso!");
            } catch (ItemInvalidoException e) {
                Map<String, Object> model = new HashMap<>();
                model.put("name", ctx.formParam("name"));
                model.put("description", ctx.formParam("description"));
                model.put("priority", ctx.formParam("priority"));
                model.put("status", ctx.formParam("status"));
                
                ctx.html(ItemView.renderForm(model, FeedbackMessage.error(e.getMessage())));
            } catch (Exception e) {
                Map<String, Object> model = new HashMap<>();
                ctx.html(ItemView.renderForm(model, FeedbackMessage.error("Erro interno do servidor. Tente novamente.")));
            }
        });

        // Formulário para editar tarefa
        app.get("/items/edit/{id}", ctx -> {
            try {
                int id = ctx.pathParamAsClass("id", Integer.class).get();
                Item item = service.findById(id);
                
                Map<String, Object> model = new HashMap<>();
                model.put("id", item.getId());
                model.put("name", item.getName());
                model.put("description", item.getDescription());
                model.put("priority", item.getPriority());
                model.put("status", item.getStatus());
                
                ctx.html(ItemView.renderForm(model, null));
            } catch (ItemNaoEncontradoException e) {
                ctx.redirect("/items?error=Tarefa não encontrada.");
            } catch (NumberFormatException | ItemInvalidoException e) {
                ctx.redirect("/items?error=ID inválido.");
            }
        });

        // Atualizar tarefa
        app.post("/items/edit/{id}", ctx -> {
            try {
                int id = ctx.pathParamAsClass("id", Integer.class).get();
                String name = ctx.formParam("name");
                String description = ctx.formParam("description");
                String priority = ctx.formParam("priority");
                String status = ctx.formParam("status");
                
                service.updateItem(id, name, description, priority, status);
                ctx.redirect("/items?success=Tarefa atualizada com sucesso!");
            } catch (ItemNaoEncontradoException e) {
                ctx.redirect("/items?error=Tarefa não encontrada.");
            } catch (ItemInvalidoException e) {
                Map<String, Object> model = new HashMap<>();
                model.put("id", ctx.pathParamAsClass("id", Integer.class).get());
                model.put("name", ctx.formParam("name"));
                model.put("description", ctx.formParam("description"));
                model.put("priority", ctx.formParam("priority"));
                model.put("status", ctx.formParam("status"));
                
                ctx.html(ItemView.renderForm(model, FeedbackMessage.error(e.getMessage())));
            } catch (NumberFormatException e) {
                ctx.redirect("/items?error=ID inválido.");
            } catch (Exception e) {
                ctx.redirect("/items?error=Erro interno do servidor. Tente novamente.");
            }
        });

        // Excluir tarefa
        app.post("/items/delete/{id}", ctx -> {
            try {
                int id = ctx.pathParamAsClass("id", Integer.class).get();
                Item item = service.findById(id);
                service.deleteItem(id);
                ctx.redirect("/items?success=Tarefa '" + item.getName() + "' excluída com sucesso!");
            } catch (ItemNaoEncontradoException e) {
                ctx.redirect("/items?error=Tarefa não encontrada.");
            } catch (NumberFormatException | ItemInvalidoException e) {
                ctx.redirect("/items?error=ID inválido.");
            } catch (Exception e) {
                ctx.redirect("/items?error=Erro interno do servidor. Tente novamente.");
            }
        });
    }
    
    private java.util.List<Item> getFilteredItems(String search, String status, String priority) {
        var items = service.listar();
        
        if (search != null && !search.trim().isEmpty()) {
            items = service.buscarPorNome(search);
        }
        
        if (status != null && !status.trim().isEmpty()) {
            items = items.stream()
                    .filter(item -> item.getStatus().equalsIgnoreCase(status.trim()))
                    .toList();
        }
        
        if (priority != null && !priority.trim().isEmpty()) {
            items = items.stream()
                    .filter(item -> item.getPriority().equalsIgnoreCase(priority.trim()))
                    .toList();
        }
        
        return items;
    }
}