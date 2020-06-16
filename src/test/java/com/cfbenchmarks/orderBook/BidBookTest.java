package com.cfbenchmarks.orderBook;

import static org.junit.Assert.assertEquals;

import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import com.cfbenchmarks.orderBookManager.OrderBookManagerImpl;
import org.junit.Before;
import org.junit.Test;

public class BidBookTest {

  private OrderBookManagerImpl orderBookManager;
  private Order buy1;
  private Order buy3;

  @Before
  public void setUp() {

    orderBookManager = new OrderBookManagerImpl();
    buy1 = new Order("order4", "VOD.L", Side.BUY, 100, 10);
    buy3 = new Order("order6", "VOD.L", Side.BUY, 80, 10);
    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy3);
  }

  @Test
  public void bidBookStoresOrdersInDescendingOrder() {

    assertEquals(
        "Bid book does not contain 2 levels",
        2,
        orderBookManager.bidBookHashMap.get(buy1.getInstrument()).size());
    assertEquals(
        "buy1 price is not greater than buy3 price", true, buy1.getPrice() > buy3.getPrice());
    assertEquals(
        "buy1 and buy3 have different instruments",
        true,
        buy1.getInstrument().equals(buy3.getInstrument()));
    assertEquals("buy1 and buy3 have different sides", true, buy1.getSide().equals(buy3.getSide()));

    BidBook bidBook = orderBookManager.bidBookHashMap.get(buy1.getInstrument());
    assertEquals(
        "Bid book does not sort levels in descending order",
        true,
        bidBook.firstEntry().getValue().head.order.getPrice()
            > bidBook.lastEntry().getValue().head.order.getPrice());
    assertEquals(
        "Bid book first entry is not buy1",
        buy1.getOrderId(),
        bidBook.firstEntry().getValue().head.order.getOrderId());
    assertEquals(
        "Bid book last entry is not buy3",
        buy3.getOrderId(),
        bidBook.lastEntry().getValue().head.order.getOrderId());
  }

  @Test
  public void descendingOrderMaintainedWithMiddleOrder() {

    assertEquals(
        "Bid book does not contain 2 levels",
        2,
        orderBookManager.bidBookHashMap.get(buy1.getInstrument()).size());

    Order buy2 = new Order("order5", "VOD.L", Side.BUY, 90, 10);
    assertEquals(
        "buy1 and buy2 have different instruments",
        true,
        buy1.getInstrument().equals(buy2.getInstrument()));
    assertEquals("buy1 and buy2 have different sides", true, buy1.getSide().equals(buy2.getSide()));
    assertEquals("buy2 is not less than buy1", true, buy2.getPrice() < buy1.getPrice());
    assertEquals("buy2 is not more than buy3", true, buy2.getPrice() > buy3.getPrice());
    orderBookManager.addOrder(buy2);

    BidBook bidBook = orderBookManager.bidBookHashMap.get(buy1.getInstrument());
    assertEquals(
        "Bid book does not sort levels in descending order",
        true,
        bidBook.firstEntry().getValue().head.order.getPrice()
            > bidBook.lastEntry().getValue().head.order.getPrice());
    assertEquals(
        "Bid book first entry is not buy1",
        buy1.getOrderId(),
        bidBook.firstEntry().getValue().head.order.getOrderId());
    assertEquals(
        "Bid book last entry is not buy3",
        buy3.getOrderId(),
        bidBook.lastEntry().getValue().head.order.getOrderId());
  }

  @Test
  public void descendingOrderMaintainedWithNewHeadOrder() {

    assertEquals(
        "Bid book does not contain 2 levels",
        2,
        orderBookManager.bidBookHashMap.get(buy1.getInstrument()).size());

    Order buy4 = new Order("order5", "VOD.L", Side.BUY, 110, 10);
    assertEquals(
        "buy1 and buy4 have different instruments",
        true,
        buy1.getInstrument().equals(buy4.getInstrument()));
    assertEquals("buy1 and buy4 have different sides", true, buy1.getSide().equals(buy4.getSide()));
    assertEquals("buy4 price is not greater than buy1", true, buy4.getPrice() > buy1.getPrice());
    assertEquals("buy4 price is not greater than buy3", true, buy4.getPrice() > buy3.getPrice());
    orderBookManager.addOrder(buy4);

    BidBook bidBook = orderBookManager.bidBookHashMap.get(buy1.getInstrument());
    assertEquals(
        "Bid book does not sort levels in descending order",
        true,
        bidBook.firstEntry().getValue().head.order.getPrice()
            > bidBook.lastEntry().getValue().head.order.getPrice());
    assertEquals(
        "Bid book first entry is not buy4",
        buy4.getOrderId(),
        bidBook.firstEntry().getValue().head.order.getOrderId());
    assertEquals(
        "Bid book last entry is not buy3",
        buy3.getOrderId(),
        bidBook.lastEntry().getValue().head.order.getOrderId());
  }

  @Test
  public void descendingOrderMaintainedWithNewLastOrder() {

    assertEquals(
        "Bid book does not contain 2 levels",
        2,
        orderBookManager.bidBookHashMap.get(buy1.getInstrument()).size());

    Order buy5 = new Order("order5", "VOD.L", Side.BUY, 70, 10);
    assertEquals(
        "buy1 and sell4 have different instruments",
        true,
        buy1.getInstrument().equals(buy5.getInstrument()));
    assertEquals(
        "buy1 and sell4 have different sides", true, buy1.getSide().equals(buy5.getSide()));
    assertEquals("buy5 price is not less than buy1", true, buy5.getPrice() < buy1.getPrice());
    assertEquals("buy5 price is not less than buy3", true, buy5.getPrice() < buy3.getPrice());
    orderBookManager.addOrder(buy5);

    BidBook bidBook = orderBookManager.bidBookHashMap.get(buy1.getInstrument());
    assertEquals(
        "Bid book does not sort levels in descending order",
        true,
        bidBook.firstEntry().getValue().head.order.getPrice()
            > bidBook.lastEntry().getValue().head.order.getPrice());
    assertEquals(
        "Bid book first entry is not buy1",
        buy1.getOrderId(),
        bidBook.firstEntry().getValue().head.order.getOrderId());
    assertEquals(
        "Bid book last entry is not buy5",
        buy5.getOrderId(),
        bidBook.lastEntry().getValue().head.order.getOrderId());
  }
}
