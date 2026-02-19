package P03_NotificationEngine.BetterDesign.model;

/**
 * Immutable notification object constructed via the Builder pattern.
 * Carries type, priority and recipient metadata to enable routing and filtering.
 */
public class Notification implements INotification {

    private final String contents;
    private final NotificationType type;
    private final NotificationPriority priority;
    private final String recipient;

    private Notification(Builder builder) {
        this.contents = builder.contents;
        this.type = builder.type;
        this.priority = builder.priority;
        this.recipient = builder.recipient;
    }

    @Override
    public String getContents() { return contents; }

    @Override
    public NotificationType getType() { return type; }

    @Override
    public NotificationPriority getPriority() { return priority; }

    @Override
    public String getRecipient() { return recipient; }

    @Override
    public String toString() {
        return "Notification{type=" + type + ", priority=" + priority
                + ", recipient='" + recipient + "', contents='" + contents + "'}";
    }

    public static class Builder {
        private final String contents;
        private NotificationType type = NotificationType.INFO;
        private NotificationPriority priority = NotificationPriority.MEDIUM;
        private String recipient;

        public Builder(String contents) {
            if (contents == null || contents.isBlank()) {
                throw new IllegalArgumentException("Notification contents cannot be empty");
            }
            this.contents = contents;
        }

        public Builder type(NotificationType type) {
            this.type = type;
            return this;
        }

        public Builder priority(NotificationPriority priority) {
            this.priority = priority;
            return this;
        }

        public Builder recipient(String recipient) {
            this.recipient = recipient;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }
}
