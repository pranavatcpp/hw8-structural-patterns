package decorator;

import java.math.BigDecimal;

public class Burger implements FoodItem {
    @Override
    public String getDescription() {
        return "Burger";
    }

    @Override
    public BigDecimal getCost() {
        return new BigDecimal("5.00");
    }
}
