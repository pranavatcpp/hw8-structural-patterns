package bridge;

/** Delivery mechanism shared by all payment notification types. */
public interface NotificationChannel {
    void send(String message);
}
