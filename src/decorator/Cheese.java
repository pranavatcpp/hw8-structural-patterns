package decorator;

import java.math.BigDecimal;

public class Cheese extends ToppingDecorator {
    public Cheese(FoodItem foodItem) {
        super(foodItem);
    }

    @Override
    protected String getToppingName() {
        return "Cheese";
    }

    @Override
    protected BigDecimal getToppingCost() {
        return new BigDecimal("0.75");
    }
}
