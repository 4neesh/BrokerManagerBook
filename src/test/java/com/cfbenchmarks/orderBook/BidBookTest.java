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
  private OrderBook bidBook;

  @Before
  public void setUp() {

    orderBookManager = new OrderBookManagerImpl();
    buy1 = new Order("order4", "VOD.L", Side.BUY, 100, 10);
    buy3 = new Order("order6", "VOD.L", Side.BUY, 80, 10);
    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy3);
    bidBook =
        orderBookManager
            .getOrderBookHashMap()
            .get(buy1.getInstrument() + buy1.getSide().toString());
  }

  @Test
  public void bidBookStoresOrdersInDescendingOrder() {

    assertEquals(
        "Bid book does not sort levels in descending order",
        true,
        bidBook.firstEntry().getValue().getHead().getOrder().getPrice()
            > bidBook.lastEntry().getValue().getHead().getOrder().getPrice());
  }

  @Test
  public void headRemainsWithMiddleOrder() {

    Order buy2 = new Order("order5", "VOD.L", Side.BUY, 90, 10);
    orderBookManager.addOrder(buy2);

    assertEquals(
        "Bid book first entry is not buy1",
        buy1.getOrderId(),
        bidBook.firstEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void lastRemainsWithMiddleOrder() {

    Order buy2 = new Order("order5", "VOD.L", Side.BUY, 90, 10);
    orderBookManager.addOrder(buy2);

    assertEquals(
        "Bid book last entry is not buy3",
        buy3.getOrderId(),
        bidBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void headIsUpdatedWithNewHigherPriceOrder() {

    Order buy4 = new Order("order5", "VOD.L", Side.BUY, 110, 10);
    orderBookManager.addOrder(buy4);

    assertEquals(
        "Bid book first entry is not buy4",
        buy4.getOrderId(),
        bidBook.firstEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void lastIsMaintainedWithNewHigherPriceOrder() {

    Order buy4 = new Order("order5", "VOD.L", Side.BUY, 110, 10);
    orderBookManager.addOrder(buy4);

    assertEquals(
        "Bid book last entry is not buy3",
        buy3.getOrderId(),
        bidBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void headIsMaintainedWithNewLowPriceOrder() {

    Order buy5 = new Order("order5", "VOD.L", Side.BUY, 70, 10);
    orderBookManager.addOrder(buy5);

    assertEquals(
        "Bid book first entry is not buy1",
        buy1.getOrderId(),
        bidBook.firstEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void lastIsUpdatedWithNewLowPriceOrder() {

    Order buy5 = new Order("order5", "VOD.L", Side.BUY, 70, 10);
    orderBookManager.addOrder(buy5);

    assertEquals(
        "Bid book last entry is not buy5",
        buy5.getOrderId(),
        bidBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }
}
