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
  public void bidOrderIsAdded() {

    Order buy = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    String propertyKey = buy.getInstrument() + buy.getSide().toString();
    assertFalse(orderBookManager.orderHashMap.containsKey(buy.getOrderId()));
    assertTrue(orderBookManager.bidBookHashMap.isEmpty());
    assertFalse(orderBookManager.instrumentPropertyMap.containsKey(propertyKey));

    orderBookManager.addOrder(buy);

    assertEquals(
        "Bid does not exist in orderBookHashMap",
        orderBookManager.orderHashMap.containsKey(buy.getOrderId()),
        true);
    assertEquals(
        "Bid does not exist in bidBookHashMap",
        orderBookManager.bidBookHashMap.get(buy.getInstrument()).containsKey(buy.getPrice()),
        true);
    assertEquals(
        "Bid does not exist in instrumentPropertyMap",
        orderBookManager.instrumentPropertyMap.containsKey(propertyKey),
        true);
  }

  @Test
  public void askOrderIsAdded() {

    Order sell = new Order("order1", "VOD.L", Side.SELL, 200, 10);
    String propertyKey = sell.getInstrument() + sell.getSide().toString();

    assertFalse(orderBookManager.orderHashMap.containsKey(sell.getOrderId()));
    assertTrue(orderBookManager.askBookHashMap.isEmpty());
    assertFalse(orderBookManager.instrumentPropertyMap.containsKey(propertyKey));

    orderBookManager.addOrder(sell);

    assertEquals(
        "Bid does not exist in orderHashMap",
        orderBookManager.orderHashMap.containsKey(sell.getOrderId()),
        true);
    assertEquals(
        "Bid does not exist in askBookHashMap",
        orderBookManager.askBookHashMap.get(sell.getInstrument()).containsKey(sell.getPrice()),
        true);
    assertEquals(
        "Bid does not exist in instrumentPropertyMap",
        orderBookManager.instrumentPropertyMap.containsKey(propertyKey),
        true);
  }

  @Test
  public void cannotAddSameBidOrderMultipleTimes() {}

  @Test
  public void cannotAddSameAskOrderMultipleTimes() {}

  @Test
  public void multipleBidAtSameLevelLogged() {

    Order buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    Order buy2 = new Order("order2", "VOD.L", Side.BUY, 200, 10);
    assertEquals("buy1 and buy2 have different prices", buy1.getPrice(), buy2.getPrice());
    assertEquals(
        "buy1 and buy2 have different instruments", buy1.getInstrument(), buy2.getInstrument());
    assertEquals("buy1 and buy2 have different sides", buy1.getSide(), buy2.getSide());
    assertEquals("bidBookHashMap is not empty", orderBookManager.bidBookHashMap.isEmpty(), true);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy2);

    String expected1 =
        orderBookManager
            .bidBookHashMap
            .get(buy1.getInstrument())
            .get(buy1.getPrice())
            .head
            .order
            .getOrderId();
    String expected2 =
        orderBookManager
            .bidBookHashMap
            .get(buy2.getInstrument())
            .get(buy2.getPrice())
            .head
            .next
            .order
            .getOrderId();
    assertEquals("buy1 is not added to bidBookHashMap.", expected1, buy1.getOrderId());

    assertEquals("buy2 is not added to bidBookHashMap.", expected2, buy2.getOrderId());
  }

  @Test
  public void multipleAskAtSameLevelLogged() {

    Order sell1 = new Order("order1", "VOD.L", Side.SELL, 200, 10);
    Order sell2 = new Order("order2", "VOD.L", Side.SELL, 200, 10);
    assertTrue(sell1.getPrice() == sell2.getPrice());
    assertTrue(orderBookManager.askBookHashMap.isEmpty());

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell2);

    assertEquals(
        "sell1 is not added to askBookHashMap.",
        orderBookManager
            .askBookHashMap
            .get(sell1.getInstrument())
            .get(sell1.getPrice())
            .head
            .order
            .getOrderId(),
        sell1.getOrderId());
    assertEquals(
        "sell2 is not added to askBookHashMap.",
        orderBookManager
            .askBookHashMap
            .get(sell1.getInstrument())
            .get(sell1.getPrice())
            .head
            .next
            .order
            .getOrderId(),
        sell2.getOrderId());
  }
}
