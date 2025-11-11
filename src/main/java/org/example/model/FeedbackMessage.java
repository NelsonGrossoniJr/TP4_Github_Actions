package org.example.model;

public record FeedbackMessage(String message, MessageType type) {
    public enum MessageType {
        SUCCESS("alert-success"),
        ERROR("alert-danger"),
        WARNING("alert-warning"),
        INFO("alert-info");

        private final String cssClass;

        MessageType(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getCssClass() {
            return cssClass;
        }
    }

    public String getCssClass() {
        return type.getCssClass();
    }

    public static FeedbackMessage success(String message) {
        return new FeedbackMessage(message, MessageType.SUCCESS);
    }

    public static FeedbackMessage error(String message) {
        return new FeedbackMessage(message, MessageType.ERROR);
    }

    public static FeedbackMessage warning(String message) {
        return new FeedbackMessage(message, MessageType.WARNING);
    }

    public static FeedbackMessage info(String message) {
        return new FeedbackMessage(message, MessageType.INFO);
    }
}

