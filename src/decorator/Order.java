package decorator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {
    private final List<FoodItem> items = new ArrayList<>();

    public void addItem(FoodItem item) {
        items.add(Objects.requireNonNull(item, "item"));
    }

    public List<FoodItem> getItems() {
        return Collections.unmodifiableList(new ArrayList<>(items));
    }

    public BigDecimal getSubtotal() {
        BigDecimal total = new BigDecimal("0.00");
        for (FoodItem item : items) {
            total = total.add(item.getCost());
        }
        return total;
    }

    public BigDecimal getTotal(LoyaltyStatus loyaltyStatus) {
        return Objects.requireNonNull(loyaltyStatus, "loyaltyStatus")
                .applyDiscount(getSubtotal());
    }
}
