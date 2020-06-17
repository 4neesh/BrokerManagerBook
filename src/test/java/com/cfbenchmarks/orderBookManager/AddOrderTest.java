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
  public void bidOrderIsAddedToHeadInOrderLinkedList() {

    Order buy = new Order("order1", "VOD.L", Side.BUY, 200, 10);

    orderBookManager.addOrder(buy);

    assertEquals(
        "Bid does not exist as head in OrderLinkedList after being added as first entry",
        buy,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy.getInstrument() + buy.getSide().toString())
            .get(buy.getPrice())
            .getHead()
            .getOrder());
  }

  @Test
  public void bidOrderIsAddedToLastInOrderLinkedList() {

    Order buy = new Order("order1", "VOD.L", Side.BUY, 200, 10);

    orderBookManager.addOrder(buy);

    assertEquals(
        "Bid does not exist as last in OrderLinkedList after being added as first entry",
        buy,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy.getInstrument() + buy.getSide().toString())
            .get(buy.getPrice())
            .getLast()
            .getOrder());
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
  public void bidOrderIsAddedToPropertyHashMap() {

    Order buy = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    String propertyKey = buy.getInstrument() + buy.getSide().toString();

    orderBookManager.addOrder(buy);

    assertEquals(
        "Bid does not exist in instrumentPropertyMap after being added",
        true,
        orderBookManager.getInstrumentPropertyHashMap().containsKey(propertyKey));
  }

  @Test
  public void askOrderIsAddedToPropertyHashMap() {

    Order sell = new Order("order1", "VOD.L", Side.SELL, 200, 10);
    String propertyKey = sell.getInstrument() + sell.getSide().toString();

    orderBookManager.addOrder(sell);

    assertEquals(
        "Ask does not exist in instrumentPropertyMap after being added",
        true,
        orderBookManager.getInstrumentPropertyHashMap().containsKey(propertyKey));
  }

  @Test
  public void askOrderIsAddedToOrderHashMap() {

    Order sell = new Order("order1", "VOD.L", Side.SELL, 200, 10);

    orderBookManager.addOrder(sell);

    assertEquals(
        "Ask does not exist in orderHashMap after being added",
        true,
        orderBookManager.getOrderHashMap().containsKey(sell.getOrderId()));
  }

  @Test
  public void askOrderIsAddedToOrderBookHashMap() {

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

  @Test
  public void askOrderIsAddedToHeadInOrderLinkedList() {

    Order sell = new Order("order1", "VOD.L", Side.SELL, 200, 10);

    orderBookManager.addOrder(sell);

    assertEquals(
        "Ask does not exist as head in OrderLinkedList after being added as first entry",
        sell,
        orderBookManager
            .getOrderBookHashMap()
            .get(sell.getInstrument() + sell.getSide().toString())
            .get(sell.getPrice())
            .getHead()
            .getOrder());
  }

  @Test
  public void askOrderIsAddedToLastInOrderLinkedList() {

    Order sell = new Order("order1", "VOD.L", Side.SELL, 200, 10);

    orderBookManager.addOrder(sell);

    assertEquals(
        "Ask does not exist as last in OrderLinkedList after being added as first entry",
        sell,
        orderBookManager
            .getOrderBookHashMap()
            .get(sell.getInstrument() + sell.getSide().toString())
            .get(sell.getPrice())
            .getLast()
            .getOrder());
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
  public void multipleBidAtSameLevelLoggedInOrderBookHashMap() {

    Order buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    Order buy2 = new Order("order2", "VOD.L", Side.BUY, 200, 10);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy2);

    String expected2 =
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getHead()
            .getNext()
            .getOrder()
            .getOrderId();

    assertEquals(
        "OrderBookHashMap is not updated for multiple bid orders", expected2, buy2.getOrderId());
  }

  @Test
  public void multipleAskAtSameLevelLoggedInOrderBookHashMap() {

    Order sell1 = new Order("order1", "VOD.L", Side.SELL, 200, 10);
    Order sell2 = new Order("order2", "VOD.L", Side.SELL, 200, 10);

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell2);

    String expected2 =
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString())
            .get(sell1.getPrice())
            .getHead()
            .getNext()
            .getOrder()
            .getOrderId();

    assertEquals(
        "OrderBookHashMap is not updated for multiple ask orders.", expected2, sell2.getOrderId());
  }

  @Test
  public void askBookContainsTwoLevelsWhenTwoPriceOrdersAdded() {

    Order sell1 = new Order("order4", "VOD.L", Side.SELL, 100, 10);
    Order sell3 = new Order("order6", "VOD.L", Side.SELL, 80, 10);
    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell3);

    assertEquals(
        "Ask book does not reflect multiple levels for multiple price orders",
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
        "Bid book does not reflect multiple levels for multiple price orders",
        2,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy1.getInstrument() + buy1.getSide().toString())
            .size());
  }
}
