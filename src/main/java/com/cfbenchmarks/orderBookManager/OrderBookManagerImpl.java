package com.cfbenchmarks.orderBookManager;

import com.cfbenchmarks.instrumentProperty.InstrumentProperty;
import com.cfbenchmarks.instrumentProperty.LevelProperty;
import com.cfbenchmarks.levelOrders.OrderNode;
import com.cfbenchmarks.orderBook.*;
import java.util.*;

public class OrderBookManagerImpl implements OrderBookManager {

  HashMap<String, Long> askLookup = new HashMap<>();
  HashMap<String, Long> bidLookup = new HashMap<>();

  HashMap<String, String> askInstrumentLookup = new HashMap<>();
  HashMap<String, String> bidInstrumentLookup = new HashMap<>();

  HashMap<String, InstrumentProperty> instrumentPropertyMap = new HashMap<>();
  HashMap<String, AskBook> askBookHashMap = new HashMap<>();
  HashMap<String, BidBook> bidBookHashMap = new HashMap<>();

  public void addOrder(Order order) {

    String propertyKey = order.getInstrument() + order.getSide().toString();
    addOrderToInstrumentPropertyMap(order, propertyKey);

    if (bidBookHashMap.containsKey(order.getInstrument()) && order.getSide().equals(Side.BUY)) {

      addOrderToBookAndLookup(bidBookHashMap.get(order.getInstrument()), order);
      bidInstrumentLookup.put(order.getOrderId(), order.getInstrument());
    } else if (askBookHashMap.containsKey(order.getInstrument())
        && order.getSide().equals(Side.SELL)) {

      addOrderToBookAndLookup(askBookHashMap.get(order.getInstrument()), order);
      askInstrumentLookup.put(order.getOrderId(), order.getInstrument());

    } else {

      if (order.getSide().equals(Side.BUY)) {
        bidBookHashMap.put(order.getInstrument(), new BidBook());
        addOrderToBookAndLookup(bidBookHashMap.get(order.getInstrument()), order);
        bidInstrumentLookup.put(order.getOrderId(), order.getInstrument());

      } else {
        askBookHashMap.put(order.getInstrument(), new AskBook());
        addOrderToBookAndLookup(askBookHashMap.get(order.getInstrument()), order);
        askInstrumentLookup.put(order.getOrderId(), order.getInstrument());
      }
    }
  }

  private void addOrderToInstrumentPropertyMap(Order order, String propertyKey) {

    if (instrumentPropertyMap.containsKey(propertyKey)) {

      instrumentPropertyMap.get(propertyKey).updateProperties(order);

    } else {

      Optional<Long> bestPrice = Optional.of(order.getPrice());
      HashMap<String, LevelProperty> levelPropertiesHashMap = new HashMap<>();

      instrumentPropertyMap.put(
          propertyKey, new InstrumentProperty(bestPrice, levelPropertiesHashMap));
    }
  }

  private void addOrderToBookAndLookup(OrderBook orderBook, Order order) {
    if (order.getSide().equals(Side.BUY)) {

      bidLookup.put(order.getOrderId(), order.getPrice());
      orderBook.addOrder(order.getPrice(), new OrderNode(order));

    } else {
      askLookup.put(order.getOrderId(), order.getPrice());
      orderBook.addOrder(order.getPrice(), new OrderNode(order));
    }
  }

  public boolean modifyOrder(String orderId, long newQuantity) {

    if (bidLookup.containsKey(orderId)) {

      long orderPrice = bidLookup.get(orderId);
      bidBookHashMap
          .get(bidInstrumentLookup.get(orderId))
          .modifyOrder(orderId, newQuantity, orderPrice);

      return true;
    } else if (askLookup.containsKey(orderId)) {

      long orderPrice = askLookup.get(orderId);
      askBookHashMap
          .get(askInstrumentLookup.get(orderId))
          .modifyOrder(orderId, newQuantity, orderPrice);

      return true;

    } else {

      return false;
    }
  }

  public boolean deleteOrder(String orderId) {

    if (bidLookup.containsKey(orderId)) {

      long orderPrice = bidLookup.get(orderId);
      bidBookHashMap.get(bidInstrumentLookup.get(orderId)).removeOrder(orderId, orderPrice);
      bidLookup.remove(orderId);

      String propertiesKey = bidInstrumentLookup.get(orderId) + Side.BUY.toString();
      if (instrumentPropertyMap.get(propertiesKey).getBestPrice().get() == orderPrice
          && bidBookHashMap.get(bidInstrumentLookup.get(orderId)).get(orderPrice) == null) {
        Optional<Long> newBestPrice;
        if (bidBookHashMap.get(bidInstrumentLookup.get(orderId)).isEmpty()) {
          newBestPrice = Optional.empty();
        } else {
          newBestPrice =
              Optional.of(bidBookHashMap.get(bidInstrumentLookup.get(orderId)).firstKey());
        }

        instrumentPropertyMap.get(propertiesKey).setNextBestPrice(newBestPrice);
      }
      return true;
    } else if (askLookup.containsKey(orderId)) {

      long orderPrice = askLookup.get(orderId);
      askBookHashMap.get(askInstrumentLookup.get(orderId)).removeOrder(orderId, orderPrice);
      askLookup.remove(orderId);

      String propertiesKey = askInstrumentLookup.get(orderId) + Side.SELL.toString();
      if (instrumentPropertyMap.get(propertiesKey).getBestPrice().get() == orderPrice
          && askBookHashMap.get(askInstrumentLookup.get(orderId)).get(orderPrice) == null) {
        Optional<Long> newBestPrice;

        if (askBookHashMap.get(askInstrumentLookup.get(orderId)).isEmpty()) {
          newBestPrice = Optional.empty();
        } else {
          newBestPrice =
              Optional.of(askBookHashMap.get(askInstrumentLookup.get(orderId)).firstKey());
        }

        instrumentPropertyMap.get(propertiesKey).setNextBestPrice(newBestPrice);
      }

      return true;
    } else {
      return false;
    }
  }

  public Optional<Long> getBestPrice(String instrument, Side side) {

    String propertiesKey = instrument + side.toString();

    return instrumentPropertyMap.get(propertiesKey).getBestPrice();
  }

  public long getOrderNumAtLevel(String instrument, Side side, long price) {

    String propertiesKey = instrument + side.toString();
    String levelPropertiesKey = propertiesKey + price;
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

  public static void main(String[] args) {

    AskBook askBook = new AskBook();
    BidBook bidBook = new BidBook();
    HashMap<String, InstrumentProperty> instrumentPropertyHashMap = new HashMap<>();

    OrderBookManagerImpl orderBookManagerVodl = new OrderBookManagerImpl();
    Order buy1 = new Order("order1", "VOD.L", Side.BUY, 2100, 10);
    Order buy2 = new Order("order2", "VOD.L", Side.BUY, 100, 10);
    Order buy3 = new Order("order3", "VOD.L", Side.BUY, 40, 10);
    Order sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 10);
    Order sell2 = new Order("order5", "VOD.L", Side.SELL, 100, 10);
    Order sell3 = new Order("order6", "VOD.L", Side.SELL, 40, 10);

    orderBookManagerVodl.addOrder(buy1);
    orderBookManagerVodl.addOrder(buy2);
    orderBookManagerVodl.addOrder(buy3);
    orderBookManagerVodl.addOrder(sell1);
    orderBookManagerVodl.addOrder(sell2);
    orderBookManagerVodl.addOrder(sell3);
  }
}
