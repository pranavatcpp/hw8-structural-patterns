package bridge;

public class EmailChannel implements NotificationChannel {
    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}
