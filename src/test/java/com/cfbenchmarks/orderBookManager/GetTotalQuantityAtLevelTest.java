package com.cfbenchmarks.orderBookManager;

import static org.junit.Assert.assertEquals;

import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import org.junit.Before;
import org.junit.Test;

public class GetTotalQuantityAtLevelTest {

  private OrderBookManagerImpl orderBookManager;
  private Order buy1;
  private Order buy1SamePrice;
  private Order buy2;
  private Order buy3;
  private Order sell1;
  private Order sell1SamePrice;
  private Order sell2;
  private Order sell3;
  private Order buyOther;
  private Order sellOther;
  private Order buyUnique;
  private Order sellUnique;

  @Before
  public void setUp() {

    orderBookManager = new OrderBookManagerImpl();
    buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    buy1SamePrice = new Order("order9", "VOD.L", Side.BUY, 200, 10);
    buy2 = new Order("order2", "VOD.L", Side.BUY, 100, 10);
    buy3 = new Order("order3", "VOD.L", Side.BUY, 50, 10);
    sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 10);
    sell1SamePrice = new Order("order10", "VOD.L", Side.SELL, 200, 10);
    sell2 = new Order("order5", "VOD.L", Side.SELL, 100, 10);
    sell3 = new Order("order6", "VOD.L", Side.SELL, 50, 10);
    buyOther = new Order("order7", "APPL", Side.BUY, 50, 10);
    sellOther = new Order("order8", "APPL", Side.SELL, 50, 10);
  }

  @Test
  public void bidBookReturnsOrdersAfterAdding() {

    assertEquals(
        "Bid Book is not empty", orderBookManager.bidBookHashMap.get(buy1.getInstrument()), null);
    assertEquals("buy1 does not have 200 orders", buy1.getQuantity(), 10);

    orderBookManager.addOrder(buy1);

    assertEquals(
        "Empty Bid book level does not add orderQuantity with order",
        orderBookManager.getTotalQuantityAtLevel(
            buy1.getInstrument(), buy1.getSide(), buy1.getPrice()),
        buy1.getQuantity());
  }

  // to do ones below

  @Test
  public void askBookReturnsOrdersAfterAdding() {

    assertEquals(
        "Ask Book is not empty", orderBookManager.bidBookHashMap.get(sell1.getInstrument()), null);

    orderBookManager.addOrder(sell1);

    assertEquals(
        "Empty Ask book level does not increment with order",
        orderBookManager.getOrderNumAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()),
        1);
  }

  @Test
  public void bidBookReturnsAddedOrdersAfterAddingMultiple() {

    assertEquals(
        "Bid Book is not empty", orderBookManager.bidBookHashMap.get(buy1.getInstrument()), null);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy1SamePrice);

    assertEquals(
        "Empty Bid book level does not increment with order",
        orderBookManager.getOrderNumAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()),
        2);
  }

  @Test
  public void askBookReturnsAddedOrdersAfterAddingMultiple() {

    assertEquals(
        "Ask Book is not empty", orderBookManager.bidBookHashMap.get(sell1.getInstrument()), null);

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell1SamePrice);

    assertEquals(
        "Empty Ask book level does not increment with order",
        orderBookManager.getOrderNumAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()),
        2);
  }

  @Test
  public void bidBookOrdersReflectsLevel() {

    assertEquals(
        "Bid Book is not empty", orderBookManager.bidBookHashMap.get(buy1.getInstrument()), null);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy2);

    assertEquals(
        "Empty Bid book level does not increment with order",
        orderBookManager.getOrderNumAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()),
        1);
    assertEquals(
        "Empty Bid book level does not increment with order",
        orderBookManager.getOrderNumAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()),
        1);
  }

  @Test
  public void askBookOrdersReflectsLevel() {

    assertEquals(
        "Ask Book is not empty", orderBookManager.bidBookHashMap.get(sell1.getInstrument()), null);

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell2);

    assertEquals(
        "Empty Ask book level does not increment with order",
        orderBookManager.getOrderNumAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()),
        1);
    assertEquals(
        "Empty Ask book level does not increment with order",
        orderBookManager.getOrderNumAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()),
        1);
  }

  @Test
  public void bidBookOrdersReducesAfterOneDeleted() {

    assertEquals(
        "Bid Book is not empty", orderBookManager.bidBookHashMap.get(buy1.getInstrument()), null);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy1SamePrice);
    orderBookManager.deleteOrder(buy1.getOrderId());

    assertEquals(
        "Empty Bid book level does not increment with order",
        orderBookManager.getOrderNumAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()),
        1);
  }

  @Test
  public void askBookOrdersReduceAfterOneDeleted() {

    assertEquals(
        "Ask Book is not empty", orderBookManager.bidBookHashMap.get(sell1.getInstrument()), null);

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell1SamePrice);
    orderBookManager.deleteOrder(sell1.getOrderId());
    assertEquals(
        "Empty Ask book level does not increment with order",
        orderBookManager.getOrderNumAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()),
        1);
  }

  @Test
  public void bidBookOrdersReducesToZero() {

    assertEquals(
        "Bid Book is not empty", orderBookManager.bidBookHashMap.get(buy1.getInstrument()), null);

    orderBookManager.addOrder(buy1);
    orderBookManager.deleteOrder(buy1.getOrderId());

    assertEquals(
        "Empty Bid book level does not increment with order",
        0,
        orderBookManager.getOrderNumAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askBookOrdersReducesToZero() {

    assertEquals(
        "Ask Book is not empty", orderBookManager.bidBookHashMap.get(sell1.getInstrument()), null);

    orderBookManager.addOrder(sell1);
    orderBookManager.deleteOrder(sell1.getOrderId());
    assertEquals(
        "Empty Ask book level does not increment with order",
        0,
        orderBookManager.getOrderNumAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }
}
