package com.cfbenchmarks.orderBook;

import static org.junit.Assert.assertEquals;

import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import com.cfbenchmarks.orderBookManager.OrderBookManagerImpl;
import org.junit.Before;
import org.junit.Test;

public class ComparatorTest {

  private OrderBookManagerImpl orderBookManager;
  private Order buy1;
  private Order buy3;
  private OrderBook bidBook;
  private Order sell1;
  private Order sell3;
  private OrderBook askBook;

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

    sell1 = new Order("order8", "VOD.L", Side.SELL, 100, 10);
    sell3 = new Order("order10", "VOD.L", Side.SELL, 80, 10);
    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell3);
    askBook =
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString());
  }

  @Test
  public void bidBookStoresLevelsInDescendingOrder() {

    assertEquals(
        "Bid book does not sort levels in descending order",
        true,
        bidBook.firstEntry().getValue().getHead().getOrder().getPrice()
            > bidBook.lastEntry().getValue().getHead().getOrder().getPrice());
  }

  @Test
  public void headBidRemainsWithNewMiddlePriceOrder() {

    Order buy2 = new Order("order5", "VOD.L", Side.BUY, 90, 10);
    orderBookManager.addOrder(buy2);

    assertEquals(
        "OrderLinkedList head is changed with new middle value bid",
        buy1.getOrderId(),
        bidBook.firstEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void lastBidRemainsWithNewMiddlePriceOrder() {

    Order buy2 = new Order("order5", "VOD.L", Side.BUY, 90, 10);
    orderBookManager.addOrder(buy2);

    assertEquals(
        "OrderLinkedList last is changed with new middle value bid",
        buy3.getOrderId(),
        bidBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void headBidUpdatedWithNewHigherPriceOrder() {

    Order buy4 = new Order("order5", "VOD.L", Side.BUY, 110, 10);
    orderBookManager.addOrder(buy4);

    assertEquals(
        "OrderLinkedList head is not changed with new high value bid",
        buy4.getOrderId(),
        bidBook.firstEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void lastBidRemainsWithNewHigherPriceOrder() {

    Order buy4 = new Order("order5", "VOD.L", Side.BUY, 110, 10);
    orderBookManager.addOrder(buy4);

    assertEquals(
        "OrderLinkedList last is changed with new high value bid",
        buy3.getOrderId(),
        bidBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void headBidRemainsWithNewLowPriceOrder() {

    Order buy5 = new Order("order5", "VOD.L", Side.BUY, 70, 10);
    orderBookManager.addOrder(buy5);

    assertEquals(
        "OrderLinkedList head is changed with new low value bid",
        buy1.getOrderId(),
        bidBook.firstEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void lastBidUpdatedWithNewLowerPriceOrder() {

    Order buy5 = new Order("order5", "VOD.L", Side.BUY, 70, 10);
    orderBookManager.addOrder(buy5);

    assertEquals(
        "OrderLinkedList last is not changed with new low value bid",
        buy5.getOrderId(),
        bidBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void askBookStoresLevelsInAscendingOrder() {

    assertEquals(
        "Ask book does not sort levels in ascending order",
        true,
        askBook.firstEntry().getValue().getHead().getOrder().getPrice()
            < askBook.lastEntry().getValue().getHead().getOrder().getPrice());
  }

  @Test
  public void headAskRemainsWithNewMiddlePriceOrder() {

    Order sell2 = new Order("order5", "VOD.L", Side.SELL, 90, 10);
    orderBookManager.addOrder(sell2);

    assertEquals(
        "OrderLinkedList head is changed with new middle value ask",
        sell3.getOrderId(),
        askBook.firstEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void lastAskRemainsWithNewMiddlePriceOrder() {

    Order sell2 = new Order("order5", "VOD.L", Side.SELL, 90, 10);
    orderBookManager.addOrder(sell2);

    assertEquals(
        "OrderLinkedList last is changed with new middle value ask",
        sell1.getOrderId(),
        askBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void headAskUpdatedWithNewLowerPriceOrder() {

    Order sell4 = new Order("order5", "VOD.L", Side.SELL, 70, 10);
    orderBookManager.addOrder(sell4);

    assertEquals(
        "OrderLinkedList head is not changed with new low value ask",
        sell4.getOrderId(),
        askBook.firstEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void lastAskRemainsWithNewLowerPriceOrder() {

    Order sell4 = new Order("order5", "VOD.L", Side.SELL, 70, 10);
    orderBookManager.addOrder(sell4);

    assertEquals(
        "OrderLinkedList last is changed with new low value ask",
        sell1.getOrderId(),
        askBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void headAskRemainsWithNewHigherPriceOrder() {

    Order sell5 = new Order("order5", "VOD.L", Side.SELL, 110, 10);

    orderBookManager.addOrder(sell5);
    assertEquals(
        "OrderLinkedList head is changed with new high value ask",
        sell3.getOrderId(),
        askBook.firstEntry().getValue().getHead().getOrder().getOrderId());
  }

  @Test
  public void lastAskUpdatedWithNewHigherPriceOrder() {

    Order sell5 = new Order("order5", "VOD.L", Side.SELL, 110, 10);
    orderBookManager.addOrder(sell5);

    assertEquals(
        "OrderLinkedList last is not changed with new high value ask",
        sell5.getOrderId(),
        askBook.lastEntry().getValue().getHead().getOrder().getOrderId());
  }
}
