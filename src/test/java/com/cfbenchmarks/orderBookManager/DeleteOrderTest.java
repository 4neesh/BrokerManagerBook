package com.cfbenchmarks.orderBookManager;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DeleteOrderTest {

  private static OrderBookManagerImpl orderBookManager;

  private static Order buy1;
  private static Order buy2;
  private static Order buy3;
  private static Order sell1;
  private static Order sell2;
  private static Order sell3;
  private static Order buyOther;
  private static Order sellOther;

  @Before
  public void setUp() {


    orderBookManager = new OrderBookManagerImpl();
    buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    buy2 = new Order("order2", "VOD.L", Side.BUY, 200, 10);
    buy3 = new Order("order3", "VOD.L", Side.BUY, 200, 10);

    sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 10);
    sell2 = new Order("order5", "VOD.L", Side.SELL, 200, 10);
    sell3 = new Order("order6", "VOD.L", Side.SELL, 200, 10);

    buyOther = new Order("order7", "AAPL", Side.BUY, 200, 10);
    sellOther = new Order("order8", "AAPL", Side.SELL, 200, 10);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy2);
    orderBookManager.addOrder(buy3);
    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell2);
    orderBookManager.addOrder(sell3);
    orderBookManager.addOrder(buyOther);
    orderBookManager.addOrder(sellOther);
  }

  @Test
  public void bidMustExistInBookToDelete() {

    Order buy4 = new Order("order7", "VOD.L", Side.BUY, 200, 10);

    assertFalse(orderBookManager.bidLookup.containsKey(buy4.getOrderId()));

    assertEquals(
        buy4.getOrderId() + " does not exist in bid book.",
        orderBookManager.deleteOrder(buy4.getOrderId()),
        false);
  }

  @Test
  public void askMustExistInBookToDelete() {

    Order sell4 = new Order("order7", "VOD.L", Side.SELL, 200, 10);

    assertFalse(orderBookManager.bidLookup.containsKey(sell4.getOrderId()));

    assertEquals(
        sell4.getOrderId() + " does not exist in ask book",
        orderBookManager.deleteOrder(sell4.getOrderId()),
        false);
  }

  @Test
  public void deleteFromHeadInBid() {

    assertTrue(
        orderBookManager.bidBookHashMap.get(buy1.getInstrument()).get(buy1.getPrice()).head.order
            == buy1);
    assertTrue(
        orderBookManager
                .bidBookHashMap
                .get(buy1.getInstrument())
                .get(buy1.getPrice())
                .head
                .next
                .order
            == buy2);
    assertTrue(orderBookManager.bidLookup.containsKey(buy1.getOrderId()));

    orderBookManager.deleteOrder(buy1.getOrderId());

    assertFalse(orderBookManager.bidLookup.containsKey(buy1.getOrderId()));
    assertEquals(
        orderBookManager.bidBookHashMap.get(buy2.getInstrument()).get(buy2.getPrice()).head.order,
        buy2);
  }

  @Test
  public void deleteFromHeadInAsk() {

    assertTrue(
        orderBookManager.askBookHashMap.get(sell1.getInstrument()).get(sell1.getPrice()).head.order
            == sell1);
    assertTrue(
        orderBookManager
                .askBookHashMap
                .get(sell1.getInstrument())
                .get(sell1.getPrice())
                .head
                .next
                .order
            == sell2);
    assertTrue(orderBookManager.askLookup.containsKey(sell1.getOrderId()));

    orderBookManager.deleteOrder(sell1.getOrderId());

    assertFalse(orderBookManager.askLookup.containsKey(sell1.getOrderId()));
    assertEquals(
        orderBookManager.askBookHashMap.get(sell2.getInstrument()).get(sell2.getPrice()).head.order,
        sell2);
  }

  @Test
  public void deleteFromBodyInBid() {

    assertTrue(
        orderBookManager
                .bidBookHashMap
                .get(buy1.getInstrument())
                .get(buy1.getPrice())
                .head
                .next
                .order
            == buy2);
    assertTrue(
        orderBookManager
                .bidBookHashMap
                .get(buy1.getInstrument())
                .get(buy1.getPrice())
                .head
                .next
                .next
                .order
            == buy3);
    assertTrue(orderBookManager.bidLookup.containsKey(buy1.getOrderId()));

    orderBookManager.deleteOrder(buy2.getOrderId());

    assertFalse(orderBookManager.bidLookup.containsKey(buy2.getOrderId()));
    assertEquals(
        orderBookManager
            .bidBookHashMap
            .get(buy2.getInstrument())
            .get(buy1.getPrice())
            .head
            .next
            .order,
        buy3);
  }

  @Test
  public void deleteFromBodyInAsk() {

    assertTrue(
        orderBookManager
                .askBookHashMap
                .get(sell1.getInstrument())
                .get(sell1.getPrice())
                .head
                .next
                .order
            == sell2);
    assertTrue(
        orderBookManager
                .askBookHashMap
                .get(sell1.getInstrument())
                .get(sell1.getPrice())
                .head
                .next
                .next
                .order
            == sell3);
    assertTrue(orderBookManager.askLookup.containsKey(sell2.getOrderId()));

    orderBookManager.deleteOrder(sell2.getOrderId());

    assertFalse(orderBookManager.askLookup.containsKey(sell2.getOrderId()));
    assertEquals(
        orderBookManager
            .askBookHashMap
            .get(sell1.getInstrument())
            .get(sell1.getPrice())
            .head
            .next
            .order,
        sell3);
  }

  @Test
  public void deleteFromEndInBid() {

    assertEquals(
        orderBookManager.bidBookHashMap.get(buy1.getInstrument()).get(buy1.getPrice()).last.order,buy3);
    assertTrue(
        orderBookManager
                .bidBookHashMap
                .get(buy1.getInstrument())
                .get(buy1.getPrice())
                .last
                .previous
                .order
            == buy2);
    assertTrue(orderBookManager.bidLookup.containsKey(buy3.getOrderId()));

    orderBookManager.deleteOrder(buy3.getOrderId());

    assertFalse(orderBookManager.bidLookup.containsKey(buy3.getOrderId()));
    assertEquals(
        orderBookManager.bidBookHashMap.get(buy2.getInstrument()).get(buy2.getPrice()).last.order,
        buy2);
  }

  @Test
  public void deleteFromEndInAsk() {

    assertTrue(
        orderBookManager.askBookHashMap.get(sell1.getInstrument()).get(sell1.getPrice()).last.order.equals(sell3));
    assertTrue(
        orderBookManager
                .askBookHashMap
                .get(sell1.getInstrument())
                .get(sell1.getPrice())
                .last
                .previous
                .order
            == sell2);
    assertTrue(orderBookManager.askLookup.containsKey(sell3.getOrderId()));

    orderBookManager.deleteOrder(sell3.getOrderId());

    assertFalse(orderBookManager.askLookup.containsKey(sell3.getOrderId()));
    assertEquals(
        orderBookManager.askBookHashMap.get(sell1.getInstrument()).get(sell2.getPrice()).last.order,
        sell2);
  }

  @Test
  public void removeFromBookForSingleBid() {

    assertEquals("Head and Last Node are not equal for single order in book",
        orderBookManager.bidBookHashMap.get(buyOther.getInstrument()).get(buyOther.getPrice()).last.order
            ,orderBookManager
                .bidBookHashMap
                .get(buyOther.getInstrument())
                .get(buyOther.getPrice())
                .head.order);
    assertEquals("Head is not buyOther",orderBookManager.bidBookHashMap.get(buyOther.getInstrument()).get(buyOther.getPrice()).head, buyOther );

    orderBookManager.deleteOrder(buyOther.getOrderId());

    assertEquals("Removing single entry from level does not remove the level from bidBook",
        orderBookManager.bidBookHashMap.get(buyOther.getInstrument()).get(buyOther.getPrice()),
        null);
  }

  @Test
  public void removeFromBookForSingleAsk() {}
}
