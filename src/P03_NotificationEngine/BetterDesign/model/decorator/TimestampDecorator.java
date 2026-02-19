package P03_NotificationEngine.BetterDesign.model.decorator;

import P03_NotificationEngine.BetterDesign.model.INotification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimestampDecorator extends NotificationDecorator {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TimestampDecorator(INotification notification) {
        super(notification);
    }

    @Override
    public String getContents() {
        return "[" + LocalDateTime.now().format(FORMATTER) + "]  " + notification.getContents();
    }
}
