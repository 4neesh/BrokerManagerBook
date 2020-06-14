package com.cfbenchmarks.interview;

import static org.junit.Assert.*;

import com.cfbenchmarks.orderBook.AskBook;
import com.cfbenchmarks.orderBook.BidBook;
import com.cfbenchmarks.orderBook.InstrumentProperty;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

public class SampleTest {

  private static OrderBookManagerImpl orderBookManager;
  private static BidBook bidBook;
  private static AskBook askBook;
  private static HashMap<String, InstrumentProperty> instrumentPropertyHashMap;

  @Before
  public void setUp() {

    askBook = new AskBook();
    bidBook = new BidBook();
    instrumentPropertyHashMap = new HashMap<>();

    orderBookManager = new OrderBookManagerImpl(askBook, bidBook, instrumentPropertyHashMap);
  }

  @Test
  public void bidOrderIsAdded() {

    Order buy = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    assertFalse(orderBookManager.bidLookup.containsKey(buy.getOrderId()));
    assertFalse(orderBookManager.bidBook.containsKey(buy.getPrice()));

    orderBookManager.addOrder(buy);

    assertTrue(orderBookManager.bidLookup.containsKey(buy.getOrderId()));
    assertTrue(orderBookManager.bidBook.containsKey(buy.getPrice()));
  }

  @Test
  public void askOrderIsAdded() {

    Order sell = new Order("order1", "VOD.L", Side.SELL, 200, 10);

    assertFalse(orderBookManager.askLookup.containsKey(sell.getOrderId()));
    assertFalse(orderBookManager.askBook.containsKey(sell.getPrice()));

    orderBookManager.addOrder(sell);

    assertTrue(orderBookManager.askLookup.containsKey(sell.getOrderId()));
    assertTrue(orderBookManager.askBook.containsKey(sell.getPrice()));
  }

  @Test
  public void multipleBidAtSameLevelLogged() {

    Order buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    Order buy2 = new Order("order2", "VOD.L", Side.BUY, 200, 10);
    assertTrue(buy1.getPrice() == buy2.getPrice());
    assertTrue(orderBookManager.bidBook.get(buy1.getPrice()) == null);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy2);

    assertEquals(
        orderBookManager.bidBook.get(buy1.getPrice()).head.order.getOrderId(), buy1.getOrderId());
    assertEquals(
        orderBookManager.bidBook.get(buy1.getPrice()).head.next.order.getOrderId(),
        buy2.getOrderId());
  }

  @Test
  public void multipleAskAtSameLevelLogged() {

    Order sell1 = new Order("order1", "VOD.L", Side.SELL, 200, 10);
    Order sell2 = new Order("order2", "VOD.L", Side.SELL, 200, 10);
    assertTrue(sell1.getPrice() == sell2.getPrice());
    assertTrue(orderBookManager.askBook.get(sell1.getPrice()) == null);

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell2);

    assertEquals(
        orderBookManager.askBook.get(sell1.getPrice()).head.order.getOrderId(), sell1.getOrderId());
    assertEquals(
        orderBookManager.askBook.get(sell1.getPrice()).head.next.order.getOrderId(),
        sell2.getOrderId());
  }

  //  @Test
  //  public void testBestBidPrice() {
  //    // create order book
  //    OrderBookManager orderBookManager = new OrderBookManagerImpl();
  //
  //    // create order
  //    Order buy = new Order("order1", "VOD.L", Side.BUY, 200, 10);
  //
  //    // send order
  //    orderBookManager.addOrder(buy);
  //
  //    // check that best price is 200
  //    Optional<Long> expectedPrice = Optional.of(200L);
  //    Optional<Long> actualPrice = orderBookManager.getBestPrice("VOD.L", Side.BUY);
  //    assertEquals("Best bid price is 200", expectedPrice, actualPrice);
  //  }

}
