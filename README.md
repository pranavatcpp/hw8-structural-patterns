# HW8 Structural Design Patterns

Java implementations for CS5800 Assignment 8: Bridge payment notifications and
Decorator restaurant orders. Both examples use only the Java standard library.

## Bridge Part 1

`PaymentNotification` is the abstraction and holds a `NotificationChannel`, the
implementation interface. `OnlinePaymentNotification` and `CashOnDeliveryPayment`
create payment-specific messages. `EmailChannel` and `SmsChannel` deliver those
messages by printing them to the console for this demonstration.

Payment types depend only on the channel interface. A new payment type can extend
`PaymentNotification` without editing the channels, and a new channel can implement
`NotificationChannel` without editing payment types.

The driver demonstrates online payment with Email, online payment with SMS, cash
on delivery with Email, and cash on delivery with SMS. The online examples cover
both credit card and PayPal payments. Cash-on-delivery messages request payment
upon delivery rather than claiming that payment has already been received.

## Bridge Part 2

`BitcoinPayment` extends `PaymentNotification` and provides a Bitcoin-specific
message. The driver adds Bitcoin payments with Email and SMS using the existing
channels without modifying them.

## Bridge Part 3

`PushChannel` implements `NotificationChannel` and prints Push messages to the
console. The driver adds online payment and cash-on-delivery scenarios with Push
without modifying any payment classes.

## Decorator food items and toppings

`FoodItem` defines `getDescription()` and `getCost()`. `Burger`, `Fries`, and
`HotDog` implement this interface. `ToppingDecorator` also implements `FoodItem`
and wraps another food item, adding a topping's description and cost.
`Ketchup`, `Cheese`, and `Onions` extend the decorator, so toppings can be stacked
or repeated without changing the food classes.

Example: `new Onions(new Cheese(new Burger()))` describes a burger with cheese
and onions and costs $6.25. Wrapping a food item does not modify the original.
Prices use `BigDecimal` for exact decimal arithmetic.

These example prices are chosen for the demonstration; the assignment does not
specify prices.

| Item or topping | Price |
| --- | --- |
| Burger | $5.00 |
| Fries | $2.50 |
| Hot dog | $3.50 |
| Ketchup | $0.25 |
| Cheese | $0.75 |
| Onions | $0.50 |

## Orders and loyalty discounts

`Order` stores a list of `FoodItem` objects, including any wrapped toppings, and
sums their costs. Each decorated item is added once, so toppings are included
without being counted separately. `getTotal()` applies a `LoyaltyStatus` discount
to the subtotal. Reading or discounting an order does not change its items.

The demonstration uses Regular (0%), Silver (5%), and Gold (10%) loyalty levels.
These rates are example choices because the assignment does not specify them.
The final discounted total is rounded to two decimal places using `HALF_UP`.

`DecoratorDriver` creates a burger with cheese and onions ($6.25), fries with
ketchup ($2.75), and a hot dog with ketchup and cheese ($4.50). The subtotal is
$13.50; Regular, Silver, and Gold totals are $13.50, $12.83, and $12.15.

## Compile and run

With JDK 8 or newer, run both examples from this directory:

```sh
sh run.sh
```

The script compiles the sources and stops if compilation or either driver fails.
It uses the project directory even when called from another directory.

To compile and run each driver manually:

```sh
mkdir -p build
javac -d build src/bridge/*.java src/decorator/*.java
java -cp build bridge.BridgeDriver
java -cp build decorator.DecoratorDriver
```

In an IDE, mark `src` as the source directory and run `bridge.BridgeDriver` or
`decorator.DecoratorDriver`.

## Sample output and verification

[Sample output](output/sample-output.txt) contains the actual output of `sh run.sh`.
The project was compiled and run with JDK 24 with no compiler warnings.

The Bridge driver demonstrates all eight required scenarios:

| Part | Payment | Channel |
| --- | --- | --- |
| 1 | Online (credit card) | Email |
| 1 | Online (PayPal) | SMS |
| 1 | Cash on delivery | Email |
| 1 | Cash on delivery | SMS |
| 2 | Bitcoin | Email |
| 2 | Bitcoin | SMS |
| 3 | Online (credit card) | Push |
| 3 | Cash on delivery | Push |

The Decorator output was checked against the item prices: $6.25 + $2.75 + $4.50
= $13.50. Silver applies 5% off ($12.825 rounded to $12.83); Gold applies 10%
off ($12.15). Regular leaves the subtotal unchanged at $13.50.

Email, SMS, and Push delivery are simulated with console output.

## JUnit tests

Run all 35 tests (10 Bridge and 25 Decorator):

```sh
./mvnw test
```

The Apache Maven wrapper downloads Maven 3.9.11 when needed; a separate Maven
installation is not required. Maven manages JUnit Jupiter 5.13.4 and runs tests
with Surefire. The first run requires internet access. On Windows, use
`mvnw.cmd test`. `sh test.sh` is also a shortcut on macOS/Linux.

`pom.xml` preserves the existing `src/` and `tests/` directories. Maven writes
compiled classes and test reports to `target/`, which is excluded from Git.
The Java 8 compilation target may produce an obsolete-target warning on newer
JDKs; this does not indicate a test failure.

`tests/bridge/BridgeTest.java` has one named test for each of the 10 Bridge method
declarations. The interface `NotificationChannel.send()` and abstract
`PaymentNotification.createMessage()` have no method bodies, so their tests
exercise the contracts through implementations. The other tests cover each
channel, notification delegation, both online payment methods, cash on delivery,
Bitcoin, and all eight driver scenarios. Tests share the `bridge` package to
access protected message methods without changing production visibility.

`tests/decorator/DecoratorTest.java` adds one named test for each of the 25
Decorator method declarations. Interface and abstract declarations are exercised
through implementations. Tests cover base prices and descriptions, stacked and
repeated toppings, immutable order snapshots, totals, loyalty levels, rounding,
invalid inputs, and the complete Decorator driver output.

## Project files

- `src/bridge/`: payment notifications, channels, and `BridgeDriver`.
- `src/decorator/`: food items, topping decorators, orders, loyalty status, and
  `DecoratorDriver`.
- `run.sh`: compiles the project and runs both drivers.
- `test.sh` and `tests/`: Maven test shortcut and Bridge/Decorator JUnit tests.
- `pom.xml`, `mvnw`, `mvnw.cmd`, and `.mvn/`: Maven build and wrapper configuration.
- `output/sample-output.txt`: captured output of both examples.
- `build/`: generated Java classes, excluded from Git.
