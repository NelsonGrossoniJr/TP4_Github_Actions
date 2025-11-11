package org.example;

import io.javalin.Javalin;
import org.example.controller.ItemController;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8000);

        new ItemController(app);
    }
}
