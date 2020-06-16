package com.cfbenchmarks.instrumentProperty;

import com.cfbenchmarks.order.Order;
import java.util.List;

public class LevelProperty {

  private long numberOfOrders;
  private long quantity;
  private long volume;

  public LevelProperty(long numberOfOrders, long quantity, long volume, List<Order> ordersAtLevel) {
    this.numberOfOrders = numberOfOrders;
    this.quantity = quantity;
    this.volume = volume;
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

  public void updateLevelProperties(Order order) {
    this.numberOfOrders += 1;
    this.quantity += order.getQuantity();
    this.volume += (order.getQuantity() * order.getPrice());
  }

  public void removeOrderFromLevelProperties(Order order) {

    this.numberOfOrders -= 1;
    this.quantity -= order.getQuantity();
    this.volume -= (order.getQuantity() * order.getPrice());
  }
}
