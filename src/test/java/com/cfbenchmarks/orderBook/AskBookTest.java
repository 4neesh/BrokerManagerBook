package com.cfbenchmarks.orderBook;

import static org.junit.Assert.assertEquals;

import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import com.cfbenchmarks.orderBookManager.OrderBookManagerImpl;
import org.junit.Before;
import org.junit.Test;

public class AskBookTest {

  private OrderBookManagerImpl orderBookManager;
  private Order sell1;
  private Order sell3;

  @Before
  public void setUp() {

    orderBookManager = new OrderBookManagerImpl();
    sell1 = new Order("order4", "VOD.L", Side.SELL, 100, 10);
    sell3 = new Order("order6", "VOD.L", Side.SELL, 80, 10);
    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell3);
  }

  @Test
  public void askBookStoresOrdersInAscendingOrder() {

    assertEquals(
        "Ask book does not contain 2 levels",
        2,
        orderBookManager.getAskBookHashMap().get(sell1.getInstrument()).size());
    assertEquals(
        "sell1 price is not greater than sell3 price", true, sell1.getPrice() > sell3.getPrice());
    assertEquals(
        "sell1 and sell3 have different instruments",
        true,
        sell1.getInstrument().equals(sell3.getInstrument()));
    assertEquals(
        "sell1 and sell3 have different sides", true, sell1.getSide().equals(sell3.getSide()));

    AskBook askBook = orderBookManager.getAskBookHashMap().get(sell1.getInstrument());
    assertEquals(
        "Ask book does not sort levels in ascending order",
        true,
        askBook.firstEntry().getValue().getHead().getOrder().getPrice()
            < askBook.lastEntry().getValue().getHead().getOrder().getPrice());
    assertEquals(
        "Ask book first entry is not sell3",
        sell3.getOrderId(),
        askBook.firstEntry().getValue().getHead().getOrder().getOrderId());
    assertEquals(
        "Ask book last entry is not sell1",
        sell1.getOrderId(),
        askBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void ascendingOrderMaintainedWithMiddleOrder() {

    assertEquals(
        "Ask book does not contain 2 levels",
        2,
        orderBookManager.getAskBookHashMap().get(sell1.getInstrument()).size());

    Order sell2 = new Order("order5", "VOD.L", Side.SELL, 90, 10);
    assertEquals(
        "sell1 and sell2 have different instruments",
        true,
        sell1.getInstrument().equals(sell2.getInstrument()));
    assertEquals(
        "sell1 and sell2 have different sides", true, sell1.getSide().equals(sell2.getSide()));
    assertEquals("sell2 is not less than sell1", true, sell2.getPrice() < sell1.getPrice());
    assertEquals("sell2 is not more than sell3", true, sell2.getPrice() > sell3.getPrice());
    orderBookManager.addOrder(sell2);

    AskBook askBook = orderBookManager.getAskBookHashMap().get(sell1.getInstrument());
    assertEquals(
        "Ask book does not sort levels in ascending order",
        true,
        askBook.firstEntry().getValue().getHead().getOrder().getPrice()
            < askBook.lastEntry().getValue().getHead().getOrder().getPrice());
    assertEquals(
        "Ask book first entry is not sell3",
        sell3.getOrderId(),
        askBook.firstEntry().getValue().getHead().getOrder().getOrderId());
    assertEquals(
        "Ask book last entry is not sell1",
        sell1.getOrderId(),
        askBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void ascendingOrderMaintainedWithNewHeadOrder() {

    assertEquals(
        "Ask book does not contain 2 levels",
        2,
        orderBookManager.getAskBookHashMap().get(sell1.getInstrument()).size());

    Order sell4 = new Order("order5", "VOD.L", Side.SELL, 70, 10);
    assertEquals(
        "sell1 and sell4 have different instruments",
        true,
        sell1.getInstrument().equals(sell4.getInstrument()));
    assertEquals(
        "sell1 and sell4 have different sides", true, sell1.getSide().equals(sell4.getSide()));
    assertEquals("sell4 price is not less than sell1", true, sell4.getPrice() < sell1.getPrice());
    assertEquals("sell4 price is not less than sell3", true, sell4.getPrice() < sell3.getPrice());
    orderBookManager.addOrder(sell4);

    AskBook askBook = orderBookManager.getAskBookHashMap().get(sell1.getInstrument());
    assertEquals(
        "Ask book does not sort levels in ascending order",
        true,
        askBook.firstEntry().getValue().getHead().getOrder().getPrice()
            < askBook.lastEntry().getValue().getHead().getOrder().getPrice());
    assertEquals(
        "Ask book first entry is not sell4",
        sell4.getOrderId(),
        askBook.firstEntry().getValue().getHead().getOrder().getOrderId());
    assertEquals(
        "Ask book last entry is not sell1",
        sell1.getOrderId(),
        askBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void ascendingOrderMaintainedWithNewLastOrder() {

    assertEquals(
        "Ask book does not contain 2 levels",
        2,
        orderBookManager.getAskBookHashMap().get(sell1.getInstrument()).size());

    Order sell5 = new Order("order5", "VOD.L", Side.SELL, 110, 10);
    assertEquals(
        "sell1 and sell4 have different instruments",
        true,
        sell1.getInstrument().equals(sell5.getInstrument()));
    assertEquals(
        "sell1 and sell4 have different sides", true, sell1.getSide().equals(sell5.getSide()));
    assertEquals(
        "sell5 price is not greater than sell1", true, sell5.getPrice() > sell1.getPrice());
    assertEquals(
        "sell5 price is not greater than sell3", true, sell5.getPrice() > sell3.getPrice());
    orderBookManager.addOrder(sell5);

    AskBook askBook = orderBookManager.getAskBookHashMap().get(sell1.getInstrument());
    assertEquals(
        "Ask book does not sort levels in ascending order",
        true,
        askBook.firstEntry().getValue().getHead().getOrder().getPrice()
            < askBook.lastEntry().getValue().getHead().getOrder().getPrice());
    assertEquals(
        "Ask book first entry is not sell3",
        sell3.getOrderId(),
        askBook.firstEntry().getValue().getHead().getOrder().getOrderId());
    assertEquals(
        "Ask book last entry is not sell5",
        sell5.getOrderId(),
        askBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }
}
