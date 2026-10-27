package decorator;

import java.math.BigDecimal;
import java.util.Objects;

/** Wraps a food item, which may already have other toppings. */
public abstract class ToppingDecorator implements FoodItem {
    private final FoodItem foodItem;

    protected ToppingDecorator(FoodItem foodItem) {
        this.foodItem = Objects.requireNonNull(foodItem, "foodItem");
    }

    protected abstract String getToppingName();
    protected abstract BigDecimal getToppingCost();

    @Override
    public String getDescription() {
        return foodItem.getDescription() + ", " + getToppingName();
    }

    @Override
    public BigDecimal getCost() {
        return foodItem.getCost().add(getToppingCost());
    }
}
