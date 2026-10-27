package bridge;

public class BridgeDriver {
    public static void main(String[] args) {
        NotificationChannel email = new EmailChannel();
        NotificationChannel sms = new SmsChannel();

        System.out.println("1. Online payment with Email");
        new OnlinePaymentNotification(email,
                OnlinePaymentNotification.Method.CREDIT_CARD).notifyCustomer();

        System.out.println("\n2. Online payment with SMS");
        new OnlinePaymentNotification(sms,
                OnlinePaymentNotification.Method.PAYPAL).notifyCustomer();

        System.out.println("\n3. Cash on delivery with Email");
        new CashOnDeliveryPayment(email).notifyCustomer();

        System.out.println("\n4. Cash on delivery with SMS");
        new CashOnDeliveryPayment(sms).notifyCustomer();

        System.out.println("\n5. Bitcoin payment with Email");
        new BitcoinPayment(email).notifyCustomer();

        System.out.println("\n6. Bitcoin payment with SMS");
        new BitcoinPayment(sms).notifyCustomer();
    }
}
