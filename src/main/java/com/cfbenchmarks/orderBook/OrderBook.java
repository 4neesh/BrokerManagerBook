package com.cfbenchmarks.orderBook;

import com.cfbenchmarks.levelOrders.OrderLinkedList;
import com.cfbenchmarks.levelOrders.OrderNode;
import java.util.Comparator;
import java.util.TreeMap;

public class OrderBook extends TreeMap<Long, OrderLinkedList> {

  public OrderBook() {}

  public OrderBook(Comparator comparator) {
    super(comparator);
  }

  public void addOrder(Long key, OrderNode orderNode) {

    if (super.get(key) == null) {
      super.put(key, new OrderLinkedList(orderNode.order));
    } else {
      super.get(key).append(orderNode);
    }
  }

  public void removeOrder(String orderId, long orderPrice) {

    if (super.get(orderPrice).head.next == null) {
      remove(orderPrice);

    } else {
      super.get(orderPrice).removeNode(orderId);
    }
  }

  public void modifyOrder(String orderId, long newQuantity, long price) {
    super.get(price).modifyOrder(orderId, newQuantity);
  }
}
