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

    OrderNode lastOrderNode = getLastOrderNode();
    order.previous = lastOrderNode;

    lastOrderNode.next = order;
    this.last = order;
  }

  public void modifyOrder(String orderId, long quantity) {

    OrderNode orderToModify = findOrderToModify(orderId);

    if (quantityIsIncreasing(quantity, orderToModify)) {

      moveOrderToEnd(orderToModify, quantity);

    } else {

      orderToModify.order.setQuantity(quantity);
    }
  }

  public void removeNode(String orderId) {

    if (orderIsHead(orderId)) {

      removeOrderFromHead();

    } else if (orderIsLast(orderId)) {

      removeOrderFromLast();

    } else {
      removeOrderFromMiddle(orderId);
    }
  }

  private OrderNode getLastOrderNode() {
    OrderNode current = this.head;

    while (current.next != null) {
      current = current.next;
    }
    return current;
  }

  private void removeOrderFromLast() {
    this.last = this.last.previous;
    this.last.next = null;
  }

  private void removeOrderFromHead() {
    this.head = this.head.next;
    this.head.previous = null;
  }

  private boolean quantityIsIncreasing(long quantity, OrderNode orderToModify) {
    return quantity > orderToModify.order.getQuantity();
  }

  private OrderNode findOrderToModify(String orderId) {
    OrderNode current = this.head;

    while (current.order.getOrderId() != orderId) {
      current = current.next;
    }
    return current;
  }

  private void removeOrderFromMiddle(String orderId) {
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

  private boolean orderIsLast(String orderId) {
    return this.last.order.getOrderId() == orderId;
  }

  private boolean orderIsHead(String orderId) {
    return this.head.order.getOrderId() == orderId;
  }

  private void moveOrderToEnd(OrderNode current, long quantity) {
    current.order.setQuantity(quantity);

    if (current != this.last) {

      OrderNode newOrder = current;
      this.removeNode(current.order.getOrderId());

      this.append(newOrder);
    }
  }
}
