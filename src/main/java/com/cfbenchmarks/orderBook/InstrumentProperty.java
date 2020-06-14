package com.cfbenchmarks.orderBook;

import com.cfbenchmarks.interview.Order;
import com.cfbenchmarks.interview.Side;
import java.util.List;
import java.util.Optional;

public class InstrumentProperty {

  public InstrumentProperty(
      Optional<Long> bestPrice,
      long numberOfOrders,
      long quantity,
      long volume,
      List<Order> ordersAtLevel) {
    this.bestPrice = bestPrice;
    this.numberOfOrders = numberOfOrders;
    this.quantity = quantity;
    this.volume = volume;
    this.ordersAtLevel = ordersAtLevel;
  }

  private Optional<Long> bestPrice;

  private long numberOfOrders;

  private long quantity;

  private long volume;

  private List<Order> ordersAtLevel;

  public void updateProperties(Order order) {

    Optional<Long> orderPrice = Optional.of(order.getPrice());

    this.numberOfOrders += 1;
    this.ordersAtLevel.add(order);
    this.quantity += order.getQuantity();
    this.volume += (order.getQuantity() + order.getPrice());

    if (this.getBestPrice().equals(Optional.empty())) {
      this.bestPrice = orderPrice;
    } else if (order.getSide() == Side.BUY && this.bestPrice.get() < orderPrice.get()) {
      this.bestPrice = orderPrice;
    } else if (this.bestPrice.get() > orderPrice.get()) {
      this.bestPrice = orderPrice;
    }
  }

  public Optional<Long> getBestPrice() {
    return bestPrice;
  }

  public long getNumberOfOrders() {
    return numberOfOrders;
  }

  public long getQuantity() {
    return quantity;
  }

  public long getVolume() {
    return volume;
  }

  public List<Order> getOrdersAtLevel() {
    return ordersAtLevel;
  }
}
