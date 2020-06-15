package com.cfbenchmarks.orderBookManager;

import com.cfbenchmarks.instrumentProperty.InstrumentProperty;
import com.cfbenchmarks.instrumentProperty.LevelProperty;
import com.cfbenchmarks.levelOrders.OrderNode;
import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import com.cfbenchmarks.orderBook.*;
import java.util.*;

public class OrderBookManagerImpl implements OrderBookManager {

  HashMap<String, Order> orderHashMap;
  HashMap<String, InstrumentProperty> instrumentPropertyMap;
  HashMap<String, AskBook> askBookHashMap;
  HashMap<String, BidBook> bidBookHashMap;

  public OrderBookManagerImpl() {
    this.orderHashMap = new HashMap<>();
    this.instrumentPropertyMap = new HashMap<>();
    this.askBookHashMap = new HashMap<>();
    this.bidBookHashMap = new HashMap<>();
  }

  public void addOrder(Order order) {

    orderHashMap.putIfAbsent(order.getOrderId(), order);

    addOrderToInstrumentPropertyMap(order);

    addOrderToRespectiveBook(order);
  }

  public boolean modifyOrder(String orderId, long newQuantity) {

    if (orderExists(orderId)) {

      HashMap<String, ? extends OrderBook> orderBook = getOrderBook(orderId);

      modifyOrderInBook(orderBook, orderId, newQuantity);

      return true;
    }

    return false;
  }

  private void modifyOrderInBook(
      HashMap<String, ? extends OrderBook> orderBook, String orderId, long newQuantity) {

    long orderPrice = orderHashMap.get(orderId).getPrice();

    orderBook
        .get(orderHashMap.get(orderId).getInstrument())
        .modifyOrder(orderId, newQuantity, orderPrice);
  }

  public boolean deleteOrder(String orderId) {

    HashMap<String, ? extends OrderBook> orderBook = getOrderBook(orderId);

    if (orderExists(orderId)) {

      removeOrderFromBook(orderId, orderBook);

      reviewNextBestPriceInBook(orderId, orderBook);

      removeOrderFromPropertyMap(orderId);

      orderHashMap.remove(orderId);

      return true;
    } else {
      return false;
    }
  }

  public void removeOrderFromPropertyMap(String orderId) {

    String propertiesKey = getPropertiesKey(orderId);

    Order order = orderHashMap.get(orderId);
    String levelPropertiesKey = getLevelPropertiesKey(order);

    instrumentPropertyMap.get(propertiesKey).deleteFromLevel(levelPropertiesKey, order);
  }

  private String getLevelPropertiesKey(Order order) {
    return order.getInstrument() + order.getSide().toString() + order.getPrice();
  }

  private void reviewNextBestPriceInBook(
      String orderId, HashMap<String, ? extends OrderBook> orderBook) {
    String propertiesKey = getPropertiesKey(orderId);

    if (instrumentPropertyMap.get(propertiesKey).getBestPrice().get() == getOrderPrice(orderId)
        && orderBook.get(orderHashMap.get(orderId).getInstrument()).get(getOrderPrice(orderId))
            == null) {
      Optional<Long> newBestPrice;

      if (orderBook.get(orderHashMap.get(orderId).getInstrument()).isEmpty()) {
        newBestPrice = Optional.empty();
      } else {
        newBestPrice =
            Optional.of(orderBook.get(orderHashMap.get(orderId).getInstrument()).firstKey());
      }

      instrumentPropertyMap.get(propertiesKey).setNextBestPrice(newBestPrice);
    }
  }

  private String getPropertiesKey(String orderId) {
    return orderHashMap.get(orderId).getInstrument()
        + orderHashMap.get(orderId).getSide().toString();
  }

  private void removeOrderFromBook(String orderId, HashMap<String, ? extends OrderBook> orderBook) {

    orderBook
        .get(orderHashMap.get(orderId).getInstrument())
        .removeOrder(orderId, getOrderPrice(orderId));
  }

  private long getOrderPrice(String orderId) {
    return orderHashMap.get(orderId).getPrice();
  }

  private boolean orderExists(String orderId) {
    return orderHashMap.containsKey(orderId);
  }

  private HashMap<String, ? extends OrderBook> getOrderBook(String orderId) {

    if (orderExists(orderId)) {
      if (orderHashMap.get(orderId).getSide().equals(Side.BUY)) {
        return bidBookHashMap;
      } else {
        return askBookHashMap;
      }
    } else {
      return null;
    }
  }

  public Optional<Long> getBestPrice(String instrument, Side side) {

    String propertiesKey = instrument + side.toString();

    return instrumentPropertyMap.get(propertiesKey).getBestPrice();
  }

  public long getOrderNumAtLevel(String instrument, Side side, long price) {

    String propertiesKey = instrument + side.toString();
    String levelPropertiesKey = instrument + side.toString() + price;
    return instrumentPropertyMap
        .get(propertiesKey)
        .getLevelPropertiesHashMap()
        .get(levelPropertiesKey)
        .getNumberOfOrders();
  }

  public long getTotalQuantityAtLevel(String instrument, Side side, long price) {
    String propertiesKey = instrument + side.toString();
    String levelPropertiesKey = propertiesKey + price;
    return instrumentPropertyMap
        .get(propertiesKey)
        .getLevelPropertiesHashMap()
        .get(levelPropertiesKey)
        .getQuantity();
  }

  public long getTotalVolumeAtLevel(String instrument, Side side, long price) {

    String propertiesKey = instrument + side.toString();
    String levelPropertiesKey = propertiesKey + price;
    return instrumentPropertyMap
        .get(propertiesKey)
        .getLevelPropertiesHashMap()
        .get(levelPropertiesKey)
        .getVolume();
  }

  public List<Order> getOrdersAtLevel(String instrument, Side side, long price) {

    String propertiesKey = instrument + side.toString();
    String levelPropertiesKey = propertiesKey + price;
    return instrumentPropertyMap
        .get(propertiesKey)
        .getLevelPropertiesHashMap()
        .get(levelPropertiesKey)
        .getOrdersAtLevel();
  }

  private void createNewBookForOrder(Order order) {

    if (order.getSide().equals(Side.BUY)) {
      bidBookHashMap.put(order.getInstrument(), new BidBook());
      addOrderToBook(bidBookHashMap.get(order.getInstrument()), order);

    } else {
      askBookHashMap.put(order.getInstrument(), new AskBook());
      addOrderToBook(askBookHashMap.get(order.getInstrument()), order);
    }
  }

  private boolean askBookForOrderExists(Order order) {
    return askBookHashMap.containsKey(order.getInstrument()) && order.getSide().equals(Side.SELL);
  }

  private boolean bidBookForOrderExists(Order order) {
    return bidBookHashMap.containsKey(order.getInstrument()) && order.getSide().equals(Side.BUY);
  }

  private void addOrderToInstrumentPropertyMap(Order order) {
    String propertyKey = order.getInstrument() + order.getSide().toString();

    if (instrumentPropertyMap.containsKey(propertyKey)) {

      instrumentPropertyMap.get(propertyKey).updateProperties(order);

    } else {

      Optional<Long> bestPrice = Optional.of(order.getPrice());
      HashMap<String, LevelProperty> levelPropertiesHashMap = new HashMap<>();

      instrumentPropertyMap.put(
          propertyKey, new InstrumentProperty(bestPrice, levelPropertiesHashMap));
      instrumentPropertyMap.get(propertyKey).updateProperties(order);
    }
  }

  private void addOrderToRespectiveBook(Order order) {

    if (bidBookForOrderExists(order)) {

      addOrderToBook(bidBookHashMap.get(order.getInstrument()), order);

    } else if (askBookForOrderExists(order)) {

      addOrderToBook(askBookHashMap.get(order.getInstrument()), order);

    } else {

      createNewBookForOrder(order);
    }
  }

  private void addOrderToBook(OrderBook orderBook, Order order) {

    if (order.getSide().equals(Side.BUY)) {

      orderBook.addOrder(order.getPrice(), new OrderNode(order));

    } else {
      orderBook.addOrder(order.getPrice(), new OrderNode(order));
    }
  }
}
