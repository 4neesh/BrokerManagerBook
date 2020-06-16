package com.cfbenchmarks.orderBookManager;

import com.cfbenchmarks.instrumentProperty.InstrumentProperty;
import com.cfbenchmarks.instrumentProperty.LevelProperty;
import com.cfbenchmarks.levelOrders.OrderLinkedList;
import com.cfbenchmarks.levelOrders.OrderNode;
import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import com.cfbenchmarks.orderBook.*;
import java.util.*;

public class OrderBookManagerImpl implements OrderBookManager {

  private HashMap<String, Order> orderHashMap;
  private HashMap<String, InstrumentProperty> instrumentPropertyMap;
  private HashMap<String, OrderBook> orderBookHashMap;

  public OrderBookManagerImpl() {
    this.orderHashMap = new HashMap<>();
    this.instrumentPropertyMap = new HashMap<>();
    this.orderBookHashMap = new HashMap<>();
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

  public boolean deleteOrder(String orderId) {

    if (orderExists(orderId)) {
      HashMap<String, ? extends OrderBook> orderBook = getOrderBook(orderId);

      removeOrderFromBook(orderId, orderBook);

      reviewNextBestPriceInBook(orderId, orderBook);

      removeOrderFromPropertyMap(orderId);

      orderHashMap.remove(orderId);

      return true;
    } else {
      return false;
    }
  }

  public List<Order> getOrdersAtLevel(String instrument, Side side, long price) {

    OrderLinkedList orders = orderBookHashMap.get(instrument + side.toString()).get(price);
    return orders.getListOfOrders();
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

  public HashMap<String, Order> getOrderHashMap() {
    return orderHashMap;
  }

  public HashMap<String, InstrumentProperty> getInstrumentPropertyMap() {
    return instrumentPropertyMap;
  }

  private void removeOrderFromPropertyMap(String orderId) {

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

    if (bestPriceRequiresUpdate(orderBook, orderId)) {

      Optional<Long> newBestPrice;

      if (orderBookIsEmpty(orderBook, orderId)) {
        newBestPrice = Optional.empty();
      } else {
        newBestPrice = Optional.of(getNextBestPrice(orderBook, orderId));
      }

      instrumentPropertyMap.get(getPropertiesKey(orderId)).setNextBestPrice(newBestPrice);
    }
  }

  private Long getNextBestPrice(HashMap<String, ? extends OrderBook> orderBook, String orderId) {
    return orderBook
        .get(
            orderHashMap.get(orderId).getInstrument()
                + orderHashMap.get(orderId).getSide().toString())
        .firstKey();
  }

  private boolean orderBookIsEmpty(HashMap<String, ? extends OrderBook> orderBook, String orderId) {
    return orderBook
        .get(
            orderHashMap.get(orderId).getInstrument()
                + orderHashMap.get(orderId).getSide().toString())
        .isEmpty();
  }

  private boolean bestPriceRequiresUpdate(
      HashMap<String, ? extends OrderBook> orderBook, String orderId) {

    return orderHasBestPrice(orderId) && orderLevelIsEmpty(orderBook, orderId);
  }

  private boolean orderLevelIsEmpty(
      HashMap<String, ? extends OrderBook> orderBook, String orderId) {
    return orderBook
            .get(
                orderHashMap.get(orderId).getInstrument()
                    + orderHashMap.get(orderId).getSide().toString())
            .get(getOrderPrice(orderId))
        == null;
  }

  private boolean orderHasBestPrice(String orderId) {
    return instrumentPropertyMap.get(getPropertiesKey(orderId)).getBestPrice().get()
        == getOrderPrice(orderId);
  }

  private String getPropertiesKey(String orderId) {
    return orderHashMap.get(orderId).getInstrument()
        + orderHashMap.get(orderId).getSide().toString();
  }

  private void modifyOrderInBook(
      HashMap<String, ? extends OrderBook> orderBook, String orderId, long newQuantity) {

    long orderPrice = orderHashMap.get(orderId).getPrice();

    orderBook
        .get(
            orderHashMap.get(orderId).getInstrument()
                + orderHashMap.get(orderId).getSide().toString())
        .modifyOrder(orderId, newQuantity, orderPrice);
  }

  private void removeOrderFromBook(String orderId, HashMap<String, ? extends OrderBook> orderBook) {
    orderBook
        .get(
            orderHashMap.get(orderId).getInstrument()
                + orderHashMap.get(orderId).getSide().toString())
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
      return orderBookHashMap;
    } else {
      return null;
    }
  }

  private void createNewBookForOrder(Order order) {

    if (order.getSide().equals(Side.BUY)) {
      orderBookHashMap.put(
          order.getInstrument() + order.getSide().toString(),
          new OrderBook(Comparator.reverseOrder()));

    } else {
      orderBookHashMap.put(order.getInstrument() + order.getSide().toString(), new OrderBook());
    }
    addOrderToBook(orderBookHashMap.get(order.getInstrument() + order.getSide().toString()), order);
  }

  private boolean orderBookForOrderExists(Order order) {
    return orderBookHashMap.containsKey(order.getInstrument() + order.getSide().toString());
  }

  private void addOrderToInstrumentPropertyMap(Order order) {
    String propertyKey = order.getInstrument() + order.getSide().toString();

    if (instrumentPropertyMap.containsKey(propertyKey)) {

      instrumentPropertyMap.get(propertyKey).updateProperties(order);

    } else {

      addNewInstrumentProperty(propertyKey, order);
    }
  }

  private void addNewInstrumentProperty(String propertyKey, Order order) {

    Optional<Long> bestPrice = Optional.of(order.getPrice());
    HashMap<String, LevelProperty> levelPropertiesHashMap = new HashMap<>();

    instrumentPropertyMap.put(
        propertyKey, new InstrumentProperty(bestPrice, levelPropertiesHashMap));
    instrumentPropertyMap.get(propertyKey).updateProperties(order);
  }

  public HashMap<String, OrderBook> getOrderBookHashMap() {
    return orderBookHashMap;
  }

  private void addOrderToRespectiveBook(Order order) {

    if (orderBookForOrderExists(order)) {

      addOrderToBook(
          orderBookHashMap.get(order.getInstrument() + order.getSide().toString()), order);

    } else {

      createNewBookForOrder(order);
    }
  }

  private void addOrderToBook(OrderBook orderBook, Order order) {

    orderBook.addOrder(order.getPrice(), new OrderNode(order));
  }
}
