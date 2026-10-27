package bridge;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BridgeTest {
    private String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (PrintStream output = new PrintStream(bytes)) {
            System.setOut(output);
            action.run();
        } finally {
            System.setOut(original);
        }
        return bytes.toString().replace("\r\n", "\n");
    }

    @Test
    @DisplayName("NotificationChannel.send: implementations work through the interface")
    void notificationChannelSend() {
        NotificationChannel[] channels = {
            new EmailChannel(), new SmsChannel(), new PushChannel()
        };
        String[] prefixes = {"Email", "SMS", "Push"};
        for (int i = 0; i < channels.length; i++) {
            NotificationChannel channel = channels[i];
            assertEquals(prefixes[i] + ": Payment received.\n",
                    captureOutput(() -> channel.send("Payment received.")));
        }
    }

    @Test
    @DisplayName("EmailChannel.send: prints the Email prefix and message")
    void emailChannelSend() {
        assertEquals("Email: Order #42 paid.\n",
                captureOutput(() -> new EmailChannel().send("Order #42 paid.")));
    }

    @Test
    @DisplayName("SmsChannel.send: prints the SMS prefix and message")
    void smsChannelSend() {
        assertEquals("SMS: Order #42 paid.\n",
                captureOutput(() -> new SmsChannel().send("Order #42 paid.")));
    }

    @Test
    @DisplayName("PushChannel.send: prints the Push prefix and message")
    void pushChannelSend() {
        assertEquals("Push: Order #42 paid.\n",
                captureOutput(() -> new PushChannel().send("Order #42 paid.")));
    }

    @Test
    @DisplayName("PaymentNotification.notifyCustomer: forwards one generated message per call")
    void paymentNotificationNotifyCustomer() {
        List<String> received = new ArrayList<>();
        int[] generated = {0};
        PaymentNotification payment = new PaymentNotification(received::add) {
            @Override
            protected String createMessage() {
                return "Payment " + (++generated[0]);
            }
        };
        payment.notifyCustomer();
        assertEquals(Arrays.asList("Payment 1"), received);
        payment.notifyCustomer();
        assertEquals(Arrays.asList("Payment 1", "Payment 2"), received);
        assertEquals(2, generated[0]);
    }

    @Test
    @DisplayName("PaymentNotification.createMessage: supports a new payment implementation")
    void paymentNotificationCreateMessage() {
        // An abstract method has no body: exercise its contract with a new subtype.
        PaymentNotification giftCard = new PaymentNotification(new EmailChannel()) {
            @Override
            protected String createMessage() {
                return "Gift card payment received.";
            }
        };
        assertEquals("Gift card payment received.", giftCard.createMessage());
        assertEquals("Email: Gift card payment received.\n",
                captureOutput(giftCard::notifyCustomer));
    }

    @Test
    @DisplayName("OnlinePaymentNotification.createMessage: distinguishes credit card and PayPal")
    void onlinePaymentCreateMessage() {
        OnlinePaymentNotification credit = new OnlinePaymentNotification(
                new EmailChannel(), OnlinePaymentNotification.Method.CREDIT_CARD);
        OnlinePaymentNotification paypal = new OnlinePaymentNotification(
                new EmailChannel(), OnlinePaymentNotification.Method.PAYPAL);
        assertAll(
                () -> assertEquals("Your online payment via credit card was received. Thank you for your order!",
                        credit.createMessage()),
                () -> assertEquals("Your online payment via PayPal was received. Thank you for your order!",
                        paypal.createMessage()));
    }

    @Test
    @DisplayName("CashOnDeliveryPayment.createMessage: requests cash upon delivery")
    void cashOnDeliveryCreateMessage() {
        assertEquals("Your cash-on-delivery order is confirmed. Please pay in cash upon delivery.",
                new CashOnDeliveryPayment(new SmsChannel()).createMessage());
    }

    @Test
    @DisplayName("BitcoinPayment.createMessage: confirms Bitcoin payment")
    void bitcoinCreateMessage() {
        assertEquals("Your Bitcoin payment was received. Thank you for your order!",
                new BitcoinPayment(new EmailChannel()).createMessage());
    }

    @Test
    @DisplayName("BridgeDriver.main: prints all eight required scenarios in order")
    void bridgeDriverMain() {
        String expected = String.join("\n",
                "1. Online payment with Email",
                "Email: Your online payment via credit card was received. Thank you for your order!",
                "",
                "2. Online payment with SMS",
                "SMS: Your online payment via PayPal was received. Thank you for your order!",
                "",
                "3. Cash on delivery with Email",
                "Email: Your cash-on-delivery order is confirmed. Please pay in cash upon delivery.",
                "",
                "4. Cash on delivery with SMS",
                "SMS: Your cash-on-delivery order is confirmed. Please pay in cash upon delivery.",
                "",
                "5. Bitcoin payment with Email",
                "Email: Your Bitcoin payment was received. Thank you for your order!",
                "",
                "6. Bitcoin payment with SMS",
                "SMS: Your Bitcoin payment was received. Thank you for your order!",
                "",
                "7. Online payment with Push",
                "Push: Your online payment via credit card was received. Thank you for your order!",
                "",
                "8. Cash on delivery with Push",
                "Push: Your cash-on-delivery order is confirmed. Please pay in cash upon delivery.") + "\n";
        assertEquals(expected, captureOutput(() -> BridgeDriver.main(new String[0])));
    }
}
