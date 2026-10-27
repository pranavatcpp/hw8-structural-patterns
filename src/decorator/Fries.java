package decorator;

import java.math.BigDecimal;

public class Fries implements FoodItem {
    @Override
    public String getDescription() {
        return "Fries";
    }

    @Override
    public BigDecimal getCost() {
        return new BigDecimal("2.50");
    }
}
