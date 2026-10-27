package decorator;

import java.math.BigDecimal;

public class Onions extends ToppingDecorator {
    public Onions(FoodItem foodItem) {
        super(foodItem);
    }

    @Override
    protected String getToppingName() {
        return "Onions";
    }

    @Override
    protected BigDecimal getToppingCost() {
        return new BigDecimal("0.50");
    }
}
