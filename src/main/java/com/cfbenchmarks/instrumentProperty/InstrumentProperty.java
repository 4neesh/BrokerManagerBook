package com.cfbenchmarks.instrumentProperty;

import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class InstrumentProperty {

  private Optional<Long> bestPrice;
  private HashMap<String, LevelProperty> levelPropertiesHashMap;

  public InstrumentProperty(
      Optional<Long> bestPrice, HashMap<String, LevelProperty> levelPropertiesHashMap) {

    this.bestPrice = bestPrice;
    this.levelPropertiesHashMap = levelPropertiesHashMap;
  }

  public void updateProperties(Order order) {

    updatePropertiesAtLevel(order);

    reviewBestPrice(order);
  }

  private void reviewBestPrice(Order order) {

    Optional<Long> orderPrice = Optional.of(order.getPrice());

    if (this.getBestPrice().equals(Optional.empty())) {
      this.bestPrice = orderPrice;
    } else if (orderIsBidAndHigherThanBest(order)) {
      this.bestPrice = orderPrice;
    } else if (orderIsAskAndLessThanBest(order)) {
      this.bestPrice = orderPrice;
    }
  }

  private boolean orderIsAskAndLessThanBest(Order order) {
    return order.getSide().equals(Side.SELL) && this.bestPrice.get() > order.getPrice();
  }

  private boolean orderIsBidAndHigherThanBest(Order order) {
    return order.getSide().equals(Side.BUY) && this.bestPrice.get() < order.getPrice();
  }

  private void updatePropertiesAtLevel(Order order) {
    String levelPropertiesKey =
        order.getInstrument() + order.getSide().toString() + order.getPrice();

    if (levelExists(levelPropertiesKey)) {
      levelPropertiesHashMap.get(levelPropertiesKey).updateLevelProperties(order);
    } else {
      List<Order> orderList = new ArrayList<>();
      orderList.add(order);
      levelPropertiesHashMap.put(
          levelPropertiesKey,
          new LevelProperty(
              1, order.getQuantity(), (order.getQuantity() * order.getPrice()), orderList));
    }
  }

  private boolean levelExists(String levelPropertiesKey) {
    return levelPropertiesHashMap.containsKey(levelPropertiesKey);
  }

  public void deleteFromLevel(String instrumentPropertyKey, Order order) {
    levelPropertiesHashMap.get(instrumentPropertyKey).removeOrderFromLevelProperties(order);
  }

  public Optional<Long> getBestPrice() {
    return bestPrice;
  }

  public void setNextBestPrice(Optional<Long> newBestPrice) {
    this.bestPrice = newBestPrice;
  }

  public HashMap<String, LevelProperty> getLevelPropertiesHashMap() {
    return levelPropertiesHashMap;
  }
}
