package com.cfbenchmarks.orderBookManager;

import static org.junit.Assert.*;

import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
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

    Order buy4 = new Order("order9", "VOD.L", Side.BUY, 200, 10);

    assertFalse(orderBookManager.getOrderHashMap().containsKey(buy4.getOrderId()));

    assertEquals(
        "buy4 does not exist in bidBook.", orderBookManager.deleteOrder(buy4.getOrderId()), false);
  }

  @Test
  public void askMustExistInBookToDelete() {

    Order sell4 = new Order("order10", "VOD.L", Side.SELL, 200, 10);

    assertFalse(orderBookManager.getOrderHashMap().containsKey(sell4.getOrderId()));

    assertEquals(
        sell4.getOrderId() + " does not exist in ask book",
        orderBookManager.deleteOrder(sell4.getOrderId()),
        false);
  }

  @Test
  public void deleteFromHeadInBid() {

    assertTrue(
        orderBookManager
                .getOrderBookHashMap()
                .get(buy1.getInstrument() + buy1.getSide().toString())
                .get(buy1.getPrice())
                .getHead()
                .getOrder()
            == buy1);
    assertTrue(
        orderBookManager
                .getOrderBookHashMap()
                .get(buy1.getInstrument() + buy1.getSide().toString())
                .get(buy1.getPrice())
                .getHead()
                .getNext()
                .getOrder()
            == buy2);
    assertTrue(orderBookManager.getOrderHashMap().containsKey(buy1.getOrderId()));

    orderBookManager.deleteOrder(buy1.getOrderId());

    assertFalse(orderBookManager.getOrderHashMap().containsKey(buy1.getOrderId()));
    assertEquals(
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getHead()
            .getOrder(),
        buy2);
  }

  @Test
  public void deleteFromHeadInAsk() {

    assertTrue(
        orderBookManager
                .getOrderBookHashMap()
                .get(sell1.getInstrument() + sell1.getSide().toString())
                .get(sell1.getPrice())
                .getHead()
                .getOrder()
            == sell1);
    assertTrue(
        orderBookManager
                .getOrderBookHashMap()
                .get(sell1.getInstrument() + sell1.getSide().toString())
                .get(sell1.getPrice())
                .getHead()
                .getNext()
                .getOrder()
            == sell2);
    assertTrue(orderBookManager.getOrderHashMap().containsKey(sell1.getOrderId()));

    orderBookManager.deleteOrder(sell1.getOrderId());

    assertFalse(orderBookManager.getOrderHashMap().containsKey(sell1.getOrderId()));
    assertEquals(
        orderBookManager
            .getOrderBookHashMap()
            .get(sell2.getInstrument() + sell2.getSide().toString())
            .get(sell2.getPrice())
            .getHead()
            .getOrder(),
        sell2);
  }

  @Test
  public void deleteFromBodyInBid() {

    assertTrue(
        orderBookManager
                .getOrderBookHashMap()
                .get(buy1.getInstrument() + buy1.getSide().toString())
                .get(buy1.getPrice())
                .getHead()
                .getNext()
                .getOrder()
            == buy2);
    assertTrue(
        orderBookManager
                .getOrderBookHashMap()
                .get(buy1.getInstrument() + buy1.getSide().toString())
                .get(buy1.getPrice())
                .getHead()
                .getNext()
                .getNext()
                .getOrder()
            == buy3);
    assertTrue(orderBookManager.getOrderHashMap().containsKey(buy1.getOrderId()));

    orderBookManager.deleteOrder(buy2.getOrderId());

    assertFalse(orderBookManager.getOrderHashMap().containsKey(buy2.getOrderId()));
    assertEquals(
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy1.getPrice())
            .getHead()
            .getNext()
            .getOrder(),
        buy3);
  }

  @Test
  public void deleteFromBodyInAsk() {

    assertTrue(
        orderBookManager
                .getOrderBookHashMap()
                .get(sell1.getInstrument() + sell1.getSide().toString())
                .get(sell1.getPrice())
                .getHead()
                .getNext()
                .getOrder()
            == sell2);
    assertTrue(
        orderBookManager
                .getOrderBookHashMap()
                .get(sell1.getInstrument() + sell1.getSide().toString())
                .get(sell1.getPrice())
                .getHead()
                .getNext()
                .getNext()
                .getOrder()
            == sell3);
    assertTrue(orderBookManager.getOrderHashMap().containsKey(sell2.getOrderId()));

    orderBookManager.deleteOrder(sell2.getOrderId());

    assertFalse(orderBookManager.getOrderHashMap().containsKey(sell2.getOrderId()));
    assertEquals(
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString())
            .get(sell1.getPrice())
            .getHead()
            .getNext()
            .getOrder(),
        sell3);
  }

  @Test
  public void deleteFromEndInBid() {

    assertEquals(
        orderBookManager
            .getOrderBookHashMap()
            .get(buy1.getInstrument() + buy1.getSide().toString())
            .get(buy1.getPrice())
            .getLast()
            .getOrder(),
        buy3);
    assertTrue(
        orderBookManager
                .getOrderBookHashMap()
                .get(buy1.getInstrument() + buy1.getSide().toString())
                .get(buy1.getPrice())
                .getLast()
                .getPrevious()
                .getOrder()
            == buy2);
    assertTrue(orderBookManager.getOrderHashMap().containsKey(buy3.getOrderId()));

    orderBookManager.deleteOrder(buy3.getOrderId());

    assertFalse(orderBookManager.getOrderHashMap().containsKey(buy3.getOrderId()));
    assertEquals(
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getLast()
            .getOrder(),
        buy2);
  }

  @Test
  public void deleteFromEndInAsk() {

    assertTrue(
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString())
            .get(sell1.getPrice())
            .getLast()
            .getOrder()
            .equals(sell3));
    assertTrue(
        orderBookManager
                .getOrderBookHashMap()
                .get(sell1.getInstrument() + sell1.getSide().toString())
                .get(sell1.getPrice())
                .getLast()
                .getPrevious()
                .getOrder()
            == sell2);
    assertTrue(orderBookManager.getOrderHashMap().containsKey(sell3.getOrderId()));

    orderBookManager.deleteOrder(sell3.getOrderId());

    assertFalse(orderBookManager.getOrderHashMap().containsKey(sell3.getOrderId()));
    assertEquals(
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString())
            .get(sell2.getPrice())
            .getLast()
            .getOrder(),
        sell2);
  }

  @Test
  public void removeFromBookForSingleBid() {

    assertEquals(
        "Head and Last Node are not equal for single order in bid book",
        orderBookManager
            .getOrderBookHashMap()
            .get(buyOther.getInstrument() + buyOther.getSide().toString())
            .get(buyOther.getPrice())
            .getLast()
            .getOrder(),
        orderBookManager
            .getOrderBookHashMap()
            .get(buyOther.getInstrument() + buyOther.getSide().toString())
            .get(buyOther.getPrice())
            .getHead()
            .getOrder());
    assertEquals(
        "Head is not buyOther in bidBook",
        orderBookManager
            .getOrderBookHashMap()
            .get(buyOther.getInstrument() + buyOther.getSide())
            .get(buyOther.getPrice())
            .getHead()
            .getOrder(),
        buyOther);

    orderBookManager.deleteOrder(buyOther.getOrderId());

    assertEquals(
        "Removing single entry from level does not remove the level from bidBook",
        orderBookManager
            .getOrderBookHashMap()
            .get(buyOther.getInstrument() + buyOther.getSide().toString())
            .get(buyOther.getPrice()),
        null);
  }

  @Test
  public void removeFromBookForSingleAsk() {
    assertEquals(
        "Head and Last Node are not equal for single order in ask book",
        orderBookManager
            .getOrderBookHashMap()
            .get(sellOther.getInstrument() + sellOther.getSide().toString())
            .get(sellOther.getPrice())
            .getLast()
            .getOrder(),
        orderBookManager
            .getOrderBookHashMap()
            .get(sellOther.getInstrument() + sellOther.getSide().toString())
            .get(sellOther.getPrice())
            .getHead()
            .getOrder());
    assertEquals(
        "Head is not buyOther in askBook",
        orderBookManager
            .getOrderBookHashMap()
            .get(sellOther.getInstrument() + sellOther.getSide().toString())
            .get(sellOther.getPrice())
            .getHead()
            .getOrder(),
        sellOther);

    orderBookManager.deleteOrder(sellOther.getOrderId());

    assertEquals(
        "Removing single entry from level does not remove the level from askBook",
        orderBookManager
            .getOrderBookHashMap()
            .get(sellOther.getInstrument() + sellOther.getSide().toString())
            .get(sellOther.getPrice()),
        null);
  }
}
