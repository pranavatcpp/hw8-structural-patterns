# HW8 Structural Design Patterns

Java implementations for CS5800 Assignment 8.

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

From this directory, with JDK 8 or newer:

```sh
mkdir -p build
javac -d build src/bridge/*.java src/decorator/*.java
java -cp build bridge.BridgeDriver
java -cp build decorator.DecoratorDriver
```

In an IDE, mark `src` as the source directory and run `bridge.BridgeDriver` or
`decorator.DecoratorDriver`.
