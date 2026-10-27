package bridge;

public class BitcoinPayment extends PaymentNotification {
    public BitcoinPayment(NotificationChannel channel) {
        super(channel);
    }

    @Override
    protected String createMessage() {
        return "Your Bitcoin payment was received. Thank you for your order!";
    }
}
