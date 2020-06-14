package com.cfbenchmarks.orderBook;

import com.cfbenchmarks.levelOrders.OrderNode;

public interface OrderBook {

  void addOrder(Long key, OrderNode order);

  void removeOrder(String orderId, long orderPrice);

  void modifyOrder(String orderId, long newQuantity, long price);
}
