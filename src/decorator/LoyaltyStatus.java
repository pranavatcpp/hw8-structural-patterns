package decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class LoyaltyStatus {
    public enum Level {
        REGULAR("0.00"), SILVER("0.05"), GOLD("0.10");

        private final BigDecimal discountRate;

        Level(String discountRate) {
            this.discountRate = new BigDecimal(discountRate);
        }
    }

    private final Level level;

    public LoyaltyStatus(Level level) {
        this.level = Objects.requireNonNull(level, "level");
    }

    public Level getLevel() {
        return level;
    }

    public BigDecimal applyDiscount(BigDecimal total) {
        Objects.requireNonNull(total, "total");
        if (total.signum() < 0) {
            throw new IllegalArgumentException("Total cannot be negative");
        }
        return total.multiply(BigDecimal.ONE.subtract(level.discountRate))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
