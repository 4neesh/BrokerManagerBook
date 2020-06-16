package com.cfbenchmarks.orderBook;

import java.util.Comparator;
import java.util.TreeMap;

public class OrderBook extends TreeMap<Long, OrderLinkedList> {

  public OrderBook() {}

  public OrderBook(Comparator comparator) {
    super(comparator);
  }

  public void addOrder(Long key, OrderNode orderNode) {

    if (keyDoesNotExist(key)) {
      super.put(key, new OrderLinkedList(orderNode.getOrder()));
    } else {
      super.get(key).appendOrder(orderNode);
    }
  }

  public void removeOrder(String orderId, long orderPrice) {

    if (super.get(orderPrice).getHead().getNext() == null) {
      remove(orderPrice);

    } else {
      super.get(orderPrice).removeNode(orderId);
    }
  }

  public void modifyOrder(String orderId, long newQuantity, long price) {
    super.get(price).modifyOrder(orderId, newQuantity);
  }

  private boolean keyDoesNotExist(Long key) {
    return super.get(key) == null;
  }
}
