package bridge;

import java.util.Objects;

public class OnlinePaymentNotification extends PaymentNotification {
    public enum Method {
        CREDIT_CARD("credit card"), PAYPAL("PayPal");

        private final String label;

        Method(String label) {
            this.label = label;
        }
    }

    private final Method method;

    public OnlinePaymentNotification(NotificationChannel channel, Method method) {
        super(channel);
        this.method = Objects.requireNonNull(method, "method");
    }

    @Override
    protected String createMessage() {
        return "Your online payment via " + method.label
                + " was received. Thank you for your order!";
    }
}
