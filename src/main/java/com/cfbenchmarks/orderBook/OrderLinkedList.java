package com.cfbenchmarks.orderBook;

import com.cfbenchmarks.order.Order;
import java.util.ArrayList;
import java.util.List;

public class OrderLinkedList {

  private OrderNode head;
  private OrderNode last;

  public OrderNode getHead() {
    return this.head;
  }

  public OrderNode getLast() {
    return this.last;
  }

  public OrderLinkedList(Order order) {
    this.head = new OrderNode(order);
    this.last = new OrderNode(order);
  }

  public void appendOrder(OrderNode order) {
    if (headEqualsLast()) {
      this.head.setNext(order);
      order.setPrevious(this.head);
    } else {
      OrderNode lastOrderNode = this.last;
      order.setPrevious(lastOrderNode);
      lastOrderNode.setNext(order);
    }
    this.last = order;
  }

  public void modifyOrder(String orderId, long quantity) {

    OrderNode orderToModify = findOrder(orderId);

    if (quantityIsIncreasing(quantity, orderToModify)) {

      moveOrderToEnd(orderToModify, quantity);

    } else {

      orderToModify.getOrder().setQuantity(quantity);
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

  public List<Order> getListOfOrders() {
    List<Order> orderAtLevel = new ArrayList<>();
    OrderNode current = this.head;

    if (current != null) {
      while (current != null) {

        orderAtLevel.add(current.getOrder());
        current = current.getNext();
      }

      return orderAtLevel;
    } else {
      return null;
    }
  }

  private boolean headEqualsLast() {
    return this.head.getOrder().equals(this.last.getOrder());
  }

  private void removeOrderFromLast() {
    this.last = this.last.getPrevious();
    this.last.setNext(null);
  }

  private void removeOrderFromHead() {
    this.head = this.head.getNext();
    this.head.setPrevious(null);
  }

  private boolean quantityIsIncreasing(long quantity, OrderNode orderToModify) {
    return quantity > orderToModify.getOrder().getQuantity();
  }

  private OrderNode findOrder(String orderId) {
    OrderNode current = this.head;

    while (current.getOrder().getOrderId() != orderId) {
      current = current.getNext();
    }
    return current;
  }

  private void removeOrderFromMiddle(String orderId) {
    OrderNode previous = this.head;
    OrderNode current = this.head.getNext();
    while (current.getNext() != null) {
      if (current.getOrder().getOrderId().equals(orderId)) {
        break;
      }
      current = current.getNext();
      previous = previous.getNext();
    }
    previous.setNext(current.getNext());
    current.getNext().setPrevious(previous);
  }

  private boolean orderIsLast(String orderId) {
    return this.last.getOrder().getOrderId() == orderId;
  }

  private boolean orderIsHead(String orderId) {
    return this.head.getOrder().getOrderId() == orderId;
  }

  private void moveOrderToEnd(OrderNode current, long quantity) {
    current.getOrder().setQuantity(quantity);

    if (current != this.last) {

      OrderNode newOrder = current;
      this.removeNode(current.getOrder().getOrderId());

      this.appendOrder(newOrder);
    }
  }
}
