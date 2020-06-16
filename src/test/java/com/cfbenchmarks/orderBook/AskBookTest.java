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
  public void askBookStoresLevelsInAscendingOrder() {

    OrderBook askBook =
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString());
    assertEquals(
        "Ask book does not sort levels in ascending order",
        true,
        askBook.firstEntry().getValue().getHead().getOrder().getPrice()
            < askBook.lastEntry().getValue().getHead().getOrder().getPrice());
  }

  @Test
  public void headRemainsInOrderWhenMiddleOrderAdded() {

    Order sell2 = new Order("order5", "VOD.L", Side.SELL, 90, 10);
    OrderBook askBook =
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString());

    orderBookManager.addOrder(sell2);

    assertEquals(
        "Ask book first entry is not sell3",
        sell3.getOrderId(),
        askBook.firstEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void lastRemainsInOrderWhenMiddleOrderAdded() {

    Order sell2 = new Order("order5", "VOD.L", Side.SELL, 90, 10);
    OrderBook askBook =
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString());

    orderBookManager.addOrder(sell2);

    assertEquals(
        "Ask book last entry is not sell1",
        sell1.getOrderId(),
        askBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void headIsUpdatedWithNewLowerPriceOrder() {

    Order sell4 = new Order("order5", "VOD.L", Side.SELL, 70, 10);
    OrderBook askBook =
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString());

    orderBookManager.addOrder(sell4);

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
  public void lastRemainsWithNewLowerPriceOrder() {

    Order sell4 = new Order("order5", "VOD.L", Side.SELL, 70, 10);
    OrderBook askBook =
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString());

    orderBookManager.addOrder(sell4);

    assertEquals(
        "Ask book last entry is not sell1",
        sell1.getOrderId(),
        askBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void headRemainsWithNewHighestPriceOrder() {

    Order sell5 = new Order("order5", "VOD.L", Side.SELL, 110, 10);

    orderBookManager.addOrder(sell5);

    OrderBook askBook =
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString());

    assertEquals(
        "Ask book first entry is not sell3",
        sell3.getOrderId(),
        askBook.firstEntry().getValue().getHead().getOrder().getOrderId());
    assertEquals(
        "Ask book last entry is not sell5",
        sell5.getOrderId(),
        askBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void lastIsUpdatedWithNewHighestPriceOrder() {

    Order sell5 = new Order("order5", "VOD.L", Side.SELL, 110, 10);
    orderBookManager.addOrder(sell5);

    OrderBook askBook =
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString());

    assertEquals(
        "Ask book last entry is not sell5",
        sell5.getOrderId(),
        askBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }
}
