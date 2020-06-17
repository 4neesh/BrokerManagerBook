package com.cfbenchmarks.orderBookManager;

import static org.junit.Assert.*;

import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import org.junit.Before;
import org.junit.Test;

public class AddOrderTest {

  private static OrderBookManagerImpl orderBookManager;

  @Before
  public void setUp() {
    orderBookManager = new OrderBookManagerImpl();
  }

  @Test
  public void bidOrderIsAddedToOrderHashMap() {

    Order buy = new Order("order1", "VOD.L", Side.BUY, 200, 10);

    orderBookManager.addOrder(buy);

    assertEquals(
        "Bid does not exist in orderHashMap after being added",
        true,
        orderBookManager.getOrderHashMap().containsKey(buy.getOrderId()));
  }

  @Test
  public void bidOrderIsAddedToOrderBookHashMap() {

    Order buy = new Order("order1", "VOD.L", Side.BUY, 200, 10);

    orderBookManager.addOrder(buy);

    assertEquals(
        "Bid does not exist in orderBookHashMap after being added",
        orderBookManager
            .getOrderBookHashMap()
            .get(buy.getInstrument() + buy.getSide().toString())
            .containsKey(buy.getPrice()),
        true);
  }

  @Test
  public void bidOrderIsAddedToPropertyMap() {

    Order buy = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    String propertyKey = buy.getInstrument() + buy.getSide().toString();

    orderBookManager.addOrder(buy);

    assertEquals(
        "Bid does not exist in instrumentPropertyMap after being added",
        true,
        orderBookManager.getInstrumentPropertyHashMap().containsKey(propertyKey));
  }

  @Test
  public void askOrderIsAddedToPropertyMap() {

    Order sell = new Order("order1", "VOD.L", Side.SELL, 200, 10);
    String propertyKey = sell.getInstrument() + sell.getSide().toString();

    orderBookManager.addOrder(sell);

    assertEquals(
        "Ask does not exist in instrumentPropertyMap after being added",
        true,
        orderBookManager.getInstrumentPropertyHashMap().containsKey(propertyKey));
  }

  @Test
  public void askOrderIsAddedToOrderMap() {

    Order sell = new Order("order1", "VOD.L", Side.SELL, 200, 10);

    orderBookManager.addOrder(sell);

    assertEquals(
        "Ask does not exist in orderHashMap after being added",
        true,
        orderBookManager.getOrderHashMap().containsKey(sell.getOrderId()));
  }

  @Test
  public void askOrderIsAddedToOrderHashMap() {

    Order sell = new Order("order1", "VOD.L", Side.SELL, 200, 10);

    orderBookManager.addOrder(sell);

    assertEquals(
        "Ask does not exist in orderBookHashMap after being added",
        orderBookManager
            .getOrderBookHashMap()
            .get(sell.getInstrument() + sell.getSide().toString())
            .containsKey(sell.getPrice()),
        true);
  }

  @Test(expected = RuntimeException.class)
  public void cannotAddSameBidOrderMultipleTimes() {

    Order buy = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    orderBookManager.addOrder(buy);
    orderBookManager.addOrder(buy);
  }

  @Test(expected = RuntimeException.class)
  public void cannotAddSameAskOrderMultipleTimes() {
    Order sell = new Order("order1", "VOD.L", Side.SELL, 200, 10);
    orderBookManager.addOrder(sell);
    orderBookManager.addOrder(sell);
  }

  @Test
  public void multipleBidAtSameLevelLogged() {

    Order buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    Order buy2 = new Order("order2", "VOD.L", Side.BUY, 200, 10);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy2);

    String expected1 =
        orderBookManager
            .getOrderBookHashMap()
            .get(buy1.getInstrument() + buy1.getSide().toString())
            .get(buy1.getPrice())
            .getHead()
            .getOrder()
            .getOrderId();
    String expected2 =
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getHead()
            .getNext()
            .getOrder()
            .getOrderId();
    assertEquals("buy1 is not added to orderBookHashMap.", expected1, buy1.getOrderId());
    assertEquals("buy2 is not added to orderBookHashMap.", expected2, buy2.getOrderId());
  }

  @Test
  public void multipleAskAtSameLevelLogged() {

    Order sell1 = new Order("order1", "VOD.L", Side.SELL, 200, 10);
    Order sell2 = new Order("order2", "VOD.L", Side.SELL, 200, 10);

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell2);

    String expected1 =
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString())
            .get(sell1.getPrice())
            .getHead()
            .getOrder()
            .getOrderId();

    String expected2 =
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString())
            .get(sell1.getPrice())
            .getHead()
            .getNext()
            .getOrder()
            .getOrderId();

    assertEquals("sell1 is not added to orderBookHashMap.", sell1.getOrderId(), expected1);

    assertEquals("sell2 is not added to orderBookHashMap.", expected2, sell2.getOrderId());
  }

  @Test
  public void askBookContainsTwoLevelsWhenTwoOrdersAdded() {

    Order sell1 = new Order("order4", "VOD.L", Side.SELL, 100, 10);
    Order sell3 = new Order("order6", "VOD.L", Side.SELL, 80, 10);
    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell3);

    assertEquals(
        "Ask book does not contain 2 levels",
        2,
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString())
            .size());
  }

  @Test
  public void bidBookContainsTwoLevelsWhenTwoOrdersAdded() {

    Order buy1 = new Order("order4", "VOD.L", Side.BUY, 100, 10);
    Order buy3 = new Order("order6", "VOD.L", Side.BUY, 80, 10);
    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy3);

    assertEquals(
        "Bid book does not contain 2 levels",
        2,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy1.getInstrument() + buy1.getSide().toString())
            .size());
  }
}
