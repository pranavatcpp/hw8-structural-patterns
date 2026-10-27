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

## Compile and run

From this directory, with JDK 8 or newer:

```sh
mkdir -p build
javac -d build src/bridge/*.java
java -cp build bridge.BridgeDriver
```

In an IDE, mark `src` as the source directory and run `bridge.BridgeDriver`.

The Decorator example will be added in subsequent commits.
