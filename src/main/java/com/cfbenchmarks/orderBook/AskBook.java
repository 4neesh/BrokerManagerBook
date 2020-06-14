package com.cfbenchmarks.orderBook;

import com.cfbenchmarks.levelOrders.OrderLinkedList;
import com.cfbenchmarks.levelOrders.OrderNode;
import java.util.TreeMap;

public class AskBook extends TreeMap<Long, OrderLinkedList> implements OrderBook {

  public void addOrder(Long key, OrderNode order) {

    if (super.get(key) == null) {
      super.put(key, new OrderLinkedList(order.order));
    } else {
      super.get(key).append(order);
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
