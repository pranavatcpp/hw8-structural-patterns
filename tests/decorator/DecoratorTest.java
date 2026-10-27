package decorator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DecoratorTest {
    private Order sampleOrder() {
        Order order = new Order();
        order.addItem(new Onions(new Cheese(new Burger())));
        order.addItem(new Ketchup(new Fries()));
        order.addItem(new Cheese(new Ketchup(new HotDog())));
        return order;
    }

    @Test
    @DisplayName("FoodItem.getDescription: base and decorated items share the interface")
    void foodItemGetDescription() {
        FoodItem plain = new Burger();
        FoodItem decorated = new Cheese(plain);
        assertEquals("Burger", plain.getDescription());
        assertEquals("Burger, Cheese", decorated.getDescription());
    }

    @Test
    @DisplayName("FoodItem.getCost: base and decorated items share the interface")
    void foodItemGetCost() {
        FoodItem plain = new Fries();
        FoodItem decorated = new Ketchup(plain);
        assertEquals(new BigDecimal("2.50"), plain.getCost());
        assertEquals(new BigDecimal("2.75"), decorated.getCost());
    }

    @Test
    @DisplayName("ToppingDecorator.getToppingName: supports a new topping implementation")
    void toppingDecoratorGetToppingName() {
        ToppingDecorator mustard = new ToppingDecorator(new HotDog()) {
            protected String getToppingName() { return "Mustard"; }
            protected BigDecimal getToppingCost() { return new BigDecimal("0.30"); }
        };
        assertEquals("Mustard", mustard.getToppingName());
        assertEquals("Hot dog, Mustard", mustard.getDescription());
    }

    @Test
    @DisplayName("ToppingDecorator.getToppingCost: supports a new topping price")
    void toppingDecoratorGetToppingCost() {
        ToppingDecorator mustard = new ToppingDecorator(new HotDog()) {
            protected String getToppingName() { return "Mustard"; }
            protected BigDecimal getToppingCost() { return new BigDecimal("0.30"); }
        };
        assertEquals(new BigDecimal("0.30"), mustard.getToppingCost());
        assertEquals(new BigDecimal("3.80"), mustard.getCost());
    }

    @Test
    @DisplayName("ToppingDecorator.getDescription: preserves stacked and repeated toppings")
    void toppingDecoratorGetDescription() {
        FoodItem plain = new Burger();
        FoodItem topped = new Onions(new Cheese(new Cheese(new Ketchup(plain))));
        assertEquals("Burger, Ketchup, Cheese, Cheese, Onions", topped.getDescription());
        assertEquals("Burger", plain.getDescription());
    }

    @Test
    @DisplayName("ToppingDecorator.getCost: adds every topping without changing the base")
    void toppingDecoratorGetCost() {
        FoodItem plain = new Burger();
        assertEquals(new BigDecimal("7.25"),
                new Onions(new Cheese(new Cheese(new Ketchup(plain)))).getCost());
        assertEquals(new BigDecimal("5.00"), plain.getCost());
        for (FoodItem base : new FoodItem[] {new Burger(), new Fries(), new HotDog()}) {
            assertEquals(base.getCost().add(new BigDecimal("1.50")),
                    new Onions(new Cheese(new Ketchup(base))).getCost());
        }
    }

    @Test
    @DisplayName("Order.addItem: keeps repeated purchases and rejects null")
    void orderAddItem() {
        Order order = new Order();
        FoodItem burger = new Burger();
        order.addItem(burger);
        order.addItem(burger);
        assertEquals(2, order.getItems().size());
        assertEquals(new BigDecimal("10.00"), order.getSubtotal());
        assertThrows(NullPointerException.class, () -> order.addItem(null));
        assertEquals(2, order.getItems().size());
    }

    @Test
    @DisplayName("Order.getItems: returns an immutable snapshot in insertion order")
    void orderGetItems() {
        Order order = new Order();
        FoodItem burger = new Burger();
        FoodItem fries = new Fries();
        order.addItem(burger);
        order.addItem(fries);
        List<FoodItem> snapshot = order.getItems();
        assertSame(burger, snapshot.get(0));
        assertSame(fries, snapshot.get(1));
        assertThrows(UnsupportedOperationException.class, snapshot::clear);
        order.addItem(new HotDog());
        assertEquals(2, snapshot.size());
        assertEquals(3, order.getItems().size());
    }

    @Test
    @DisplayName("Order.getSubtotal: handles empty and decorated orders without double counting")
    void orderGetSubtotal() {
        assertEquals(new BigDecimal("0.00"), new Order().getSubtotal());
        Order order = sampleOrder();
        assertEquals(new BigDecimal("13.50"), order.getSubtotal());
        order.addItem(new Fries());
        assertEquals(new BigDecimal("16.00"), order.getSubtotal());
    }

    @Test
    @DisplayName("Order.getTotal: applies each loyalty level without cumulative discounts")
    void orderGetTotal() {
        Order order = sampleOrder();
        assertEquals(new BigDecimal("13.50"), order.getTotal(new LoyaltyStatus(LoyaltyStatus.Level.REGULAR)));
        assertEquals(new BigDecimal("12.83"), order.getTotal(new LoyaltyStatus(LoyaltyStatus.Level.SILVER)));
        LoyaltyStatus gold = new LoyaltyStatus(LoyaltyStatus.Level.GOLD);
        assertEquals(new BigDecimal("12.15"), order.getTotal(gold));
        assertEquals(new BigDecimal("12.15"), order.getTotal(gold));
        assertEquals(new BigDecimal("13.50"), order.getSubtotal());
        assertEquals(new BigDecimal("0.00"), new Order().getTotal(gold));
        assertThrows(NullPointerException.class, () -> order.getTotal(null));
    }

    @Test
    @DisplayName("LoyaltyStatus.getLevel: returns the selected level")
    void loyaltyStatusGetLevel() {
        for (LoyaltyStatus.Level level : LoyaltyStatus.Level.values()) {
            assertEquals(level, new LoyaltyStatus(level).getLevel());
        }
    }

    @Test
    @DisplayName("LoyaltyStatus.applyDiscount: discounts, rounds half up, and validates totals")
    void loyaltyStatusApplyDiscount() {
        LoyaltyStatus regular = new LoyaltyStatus(LoyaltyStatus.Level.REGULAR);
        LoyaltyStatus silver = new LoyaltyStatus(LoyaltyStatus.Level.SILVER);
        LoyaltyStatus gold = new LoyaltyStatus(LoyaltyStatus.Level.GOLD);
        assertEquals(new BigDecimal("100.00"), regular.applyDiscount(new BigDecimal("100.00")));
        assertEquals(new BigDecimal("95.00"), silver.applyDiscount(new BigDecimal("100.00")));
        assertEquals(new BigDecimal("90.00"), gold.applyDiscount(new BigDecimal("100.00")));
        assertEquals(new BigDecimal("0.10"), silver.applyDiscount(new BigDecimal("0.10")));
        assertEquals(new BigDecimal("0.09"), silver.applyDiscount(new BigDecimal("0.09")));
        assertEquals(new BigDecimal("0.00"), gold.applyDiscount(BigDecimal.ZERO));
        assertThrows(NullPointerException.class, () -> gold.applyDiscount(null));
        assertThrows(IllegalArgumentException.class, () -> gold.applyDiscount(new BigDecimal("-0.01")));
    }

    @Test
    @DisplayName("DecoratorDriver.main: prints decorated items, subtotal, and loyalty totals")
    void decoratorDriverMain() {
        PrintStream original = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (PrintStream output = new PrintStream(bytes)) {
            System.setOut(output);
            DecoratorDriver.main(new String[0]);
        } finally {
            System.setOut(original);
        }
        String expected = String.join("\n",
                "Restaurant order",
                "Burger, Cheese, Onions: $6.25",
                "Fries, Ketchup: $2.75",
                "Hot dog, Ketchup, Cheese: $4.50",
                "Subtotal: $13.50", "",
                "Totals by loyalty status",
                "REGULAR: $13.50", "SILVER: $12.83", "GOLD: $12.15") + "\n";
        assertEquals(expected, bytes.toString().replace("\r\n", "\n"));
    }

    @Test
    @DisplayName("Burger.getDescription: returns the expected value")
    void burgerGetDescription() {
        assertEquals("Burger", new Burger().getDescription());
    }

    @Test
    @DisplayName("Burger.getCost: returns the expected value")
    void burgerGetCost() {
        assertEquals(new BigDecimal("5.00"), new Burger().getCost());
    }

    @Test
    @DisplayName("Fries.getDescription: returns the expected value")
    void friesGetDescription() {
        assertEquals("Fries", new Fries().getDescription());
    }

    @Test
    @DisplayName("Fries.getCost: returns the expected value")
    void friesGetCost() {
        assertEquals(new BigDecimal("2.50"), new Fries().getCost());
    }

    @Test
    @DisplayName("HotDog.getDescription: returns the expected value")
    void hotDogGetDescription() {
        assertEquals("Hot dog", new HotDog().getDescription());
    }

    @Test
    @DisplayName("HotDog.getCost: returns the expected value")
    void hotDogGetCost() {
        assertEquals(new BigDecimal("3.50"), new HotDog().getCost());
    }

    @Test
    @DisplayName("Ketchup.getToppingName: returns the expected value")
    void ketchupGetToppingName() {
        assertEquals("Ketchup", new Ketchup(new Burger()).getToppingName());
    }

    @Test
    @DisplayName("Ketchup.getToppingCost: returns the expected value")
    void ketchupGetToppingCost() {
        assertEquals(new BigDecimal("0.25"), new Ketchup(new Burger()).getToppingCost());
    }

    @Test
    @DisplayName("Cheese.getToppingName: returns the expected value")
    void cheeseGetToppingName() {
        assertEquals("Cheese", new Cheese(new Burger()).getToppingName());
    }

    @Test
    @DisplayName("Cheese.getToppingCost: returns the expected value")
    void cheeseGetToppingCost() {
        assertEquals(new BigDecimal("0.75"), new Cheese(new Burger()).getToppingCost());
    }

    @Test
    @DisplayName("Onions.getToppingName: returns the expected value")
    void onionsGetToppingName() {
        assertEquals("Onions", new Onions(new Burger()).getToppingName());
    }

    @Test
    @DisplayName("Onions.getToppingCost: returns the expected value")
    void onionsGetToppingCost() {
        assertEquals(new BigDecimal("0.50"), new Onions(new Burger()).getToppingCost());
    }
}
