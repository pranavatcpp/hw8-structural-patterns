package decorator;

import java.math.BigDecimal;

public class HotDog implements FoodItem {
    @Override
    public String getDescription() {
        return "Hot dog";
    }

    @Override
    public BigDecimal getCost() {
        return new BigDecimal("3.50");
    }
}
