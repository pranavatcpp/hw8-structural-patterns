package decorator;

public class DecoratorDriver {
    public static void main(String[] args) {
        Order order = new Order();
        order.addItem(new Onions(new Cheese(new Burger())));
        order.addItem(new Ketchup(new Fries()));
        order.addItem(new Cheese(new Ketchup(new HotDog())));

        System.out.println("Restaurant order");
        for (FoodItem item : order.getItems()) {
            System.out.println(item.getDescription() + ": $" + item.getCost());
        }
        System.out.println("Subtotal: $" + order.getSubtotal());

        System.out.println("\nTotals by loyalty status");
        for (LoyaltyStatus.Level level : LoyaltyStatus.Level.values()) {
            LoyaltyStatus status = new LoyaltyStatus(level);
            System.out.println(status.getLevel() + ": $" + order.getTotal(status));
        }
    }
}
