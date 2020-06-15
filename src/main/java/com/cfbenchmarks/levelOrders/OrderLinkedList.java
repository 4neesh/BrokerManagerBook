package com.cfbenchmarks.levelOrders;

import com.cfbenchmarks.order.Order;

public class OrderLinkedList {

  public OrderNode head;
  public OrderNode last;

  public OrderLinkedList(Order order) {
    this.head = new OrderNode(order);
    this.last = new OrderNode(order);
  }

  public void append(OrderNode order) {

    OrderNode current = this.head;

    while (current.next != null) {
      current = current.next;
    }

    order.previous = current;

    current.next = order;
    this.last = order;
  }

  public void modifyOrder(String orderId, long quantity) {

    OrderNode current = this.head;

    while (current.order.getOrderId() != orderId) {

      current = current.next;
    }

    if (quantity > current.order.getQuantity()) {

      current.order.setQuantity(quantity);

      if (current != this.last) {

        OrderNode newOrder = current;
        this.removeNode(current.order.getOrderId());

        this.append(newOrder);
      }

    } else {

      current.order.setQuantity(quantity);
    }
  }

  public void removeNode(String orderId) {

    if (this.head.order.getOrderId() == orderId) {

      this.head = this.head.next;
      this.head.previous = null;

    } else if (this.last.order.getOrderId() == orderId) {

      this.last = this.last.previous;
      this.last.next = null;

    } else {
      OrderNode previous = this.head;
      OrderNode current = this.head.next;
      while (current.next != null) {
        if (current.order.getOrderId().equals(orderId)) {
          break;
        }
        current = current.next;
        previous = previous.next;
      }
      previous.next = current.next;
      current.next.previous = previous;
    }
  }
}
