package com.cfbenchmarks.orderBook;

import com.cfbenchmarks.order.Order;

public class OrderNode {

  private OrderNode previous;
  private OrderNode next;
  private Order order;

  public OrderNode getPrevious() {
    return previous;
  }

  public void setPrevious(OrderNode previous) {
    this.previous = previous;
  }

  public void setNext(OrderNode next) {
    this.next = next;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public OrderNode getNext() {
    return next;
  }

  public Order getOrder() {
    return order;
  }

  public OrderNode(Order order) {
    this.order = order;
  }
}
