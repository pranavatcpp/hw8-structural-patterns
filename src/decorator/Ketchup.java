package decorator;

import java.math.BigDecimal;

public class Ketchup extends ToppingDecorator {
    public Ketchup(FoodItem foodItem) {
        super(foodItem);
    }

    @Override
    protected String getToppingName() {
        return "Ketchup";
    }

    @Override
    protected BigDecimal getToppingCost() {
        return new BigDecimal("0.25");
    }
}
