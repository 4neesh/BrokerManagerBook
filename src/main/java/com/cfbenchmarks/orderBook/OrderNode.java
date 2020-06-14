package com.cfbenchmarks.orderBook;

import com.cfbenchmarks.interview.Order;

public class OrderNode {

  public OrderNode previous;
  public OrderNode next;
  public Order order;

  public OrderNode(Order order) {
    this.order = order;
  }
}
