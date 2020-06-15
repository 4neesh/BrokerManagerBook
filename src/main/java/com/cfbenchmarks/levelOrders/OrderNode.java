package com.cfbenchmarks.levelOrders;

import com.cfbenchmarks.order.Order;

public class OrderNode {

  public OrderNode previous;
  public OrderNode next;
  public Order order;

  public OrderNode(Order order) {
    this.order = order;
  }
}
