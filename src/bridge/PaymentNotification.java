package bridge;

import java.util.Objects;

/** The abstraction holds a channel instead of inheriting channel behavior. */
public abstract class PaymentNotification {
    private final NotificationChannel channel;

    protected PaymentNotification(NotificationChannel channel) {
        this.channel = Objects.requireNonNull(channel, "channel");
    }

    public final void notifyCustomer() {
        channel.send(createMessage());
    }

    protected abstract String createMessage();
}
