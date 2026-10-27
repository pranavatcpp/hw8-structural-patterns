package bridge;

public class CashOnDeliveryPayment extends PaymentNotification {
    public CashOnDeliveryPayment(NotificationChannel channel) {
        super(channel);
    }

    @Override
    protected String createMessage() {
        return "Your cash-on-delivery order is confirmed. Please pay in cash upon delivery.";
    }
}
