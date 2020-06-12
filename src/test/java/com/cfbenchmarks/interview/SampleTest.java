package com.cfbenchmarks.interview;

import static org.junit.Assert.*;


import com.cfbenchmarks.orderBook.AskBook;
import com.cfbenchmarks.orderBook.BidBook;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SampleTest {

  private static OrderBookManagerImpl orderBookManager;
  private static BidBook bidBook;
  private static AskBook askBook;

  @Before
  public  void setUp(){

    askBook = new AskBook();
    bidBook = new BidBook();
    orderBookManager = new OrderBookManagerImpl(askBook, bidBook);


  }


  @Test
  public void bidOrderIsAddedToLookup(){

    Order buy = new Order("order1", "VOD.L", Side.BUY, 200, 10);

    orderBookManager.addOrder(buy);

    assertTrue(orderBookManager.bidLookup.containsKey(buy.getOrderId()));

  }

  @Test
  public void askOrderIsAddedToLookup(){

    Order sell = new Order("order1", "VOD.L", Side.SELL, 200, 10);

    orderBookManager.addOrder(sell);

    assertTrue(orderBookManager.askLookup.containsKey(sell.getOrderId()));

  }

  @Test
  public void bidOrderIsAddedToBook(){

    Order buy = new Order("order1", "VOD.L", Side.BUY, 200, 10);

    orderBookManager.addOrder(buy);

    assertTrue(orderBookManager.bidBook.containsKey(buy.getPrice()));

  }

  @Test
  public void askOrderIsAddedToBook(){

    Order sell = new Order("order1", "VOD.L", Side.SELL, 200, 10);

    orderBookManager.addOrder(sell);

    assertTrue(orderBookManager.askBook.containsKey(sell.getPrice()));

  }

  @Test
  public void multipleBidAtSameLevelLogged(){

    Order buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    Order buy2 = new Order("order2", "VOD.L", Side.BUY, 200, 10);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy2);


    assertEquals(orderBookManager.bidBook.get(buy1.getPrice()).head.next.order.getOrderId() , buy2.getOrderId());
  }

  @Test
  public void multipleAskAtSameLevelLogged(){

    Order sell1 = new Order("order1", "VOD.L", Side.SELL, 200, 10);
    Order sell2 = new Order("order2", "VOD.L", Side.SELL, 200, 10);

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell2);


    assertEquals(orderBookManager.askBook.get(sell1.getPrice()).head.next.order.getOrderId() , sell2.getOrderId());
  }

  @Test
  public void deleteFromHeadInBid(){

    Order buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    Order buy2 = new Order("order2", "VOD.L", Side.BUY, 200, 10);
    Order buy3 = new Order("order3", "VOD.L", Side.BUY, 200, 10);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy2);
    orderBookManager.addOrder(buy3);

    orderBookManager.deleteOrder(buy1.getOrderId());

    assertEquals(orderBookManager.bidBook.get(buy2.getPrice()).head.order, buy2);

  }

  @Test
  public void deleteFromHeadInAsk(){

    Order sell1 = new Order("order1", "VOD.L", Side.SELL, 200, 10);
    Order sell2 = new Order("order2", "VOD.L", Side.SELL, 200, 10);
    Order sell3 = new Order("order3", "VOD.L", Side.SELL, 200, 10);

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell2);
    orderBookManager.addOrder(sell3);

    orderBookManager.deleteOrder(sell1.getOrderId());

    assertEquals(orderBookManager.askBook.get(sell2.getPrice()).head.order, sell2);
  }

  @Test
  public void deleteFromBodyInBid(){

    Order buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    Order buy2 = new Order("order2", "VOD.L", Side.BUY, 200, 10);
    Order buy3 = new Order("order3", "VOD.L", Side.BUY, 200, 10);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy2);
    orderBookManager.addOrder(buy3);

    orderBookManager.deleteOrder(buy2.getOrderId());

    assertEquals(orderBookManager.bidBook.get(buy1.getPrice()).head.next.order, buy3);

  }

  @Test
  public void deleteFromBodyInAsk(){

    Order sell1 = new Order("order1", "VOD.L", Side.SELL, 200, 10);
    Order sell2 = new Order("order2", "VOD.L", Side.SELL, 200, 10);
    Order sell3 = new Order("order3", "VOD.L", Side.SELL, 200, 10);

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell2);
    orderBookManager.addOrder(sell3);

    orderBookManager.deleteOrder(sell2.getOrderId());

    assertEquals(orderBookManager.askBook.get(sell1.getPrice()).head.next.order, sell3);

  }

  @Test
  public void deleteFromEndInBid(){

    Order buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    Order buy2 = new Order("order2", "VOD.L", Side.BUY, 200, 10);
    Order buy3 = new Order("order3", "VOD.L", Side.BUY, 200, 10);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy2);
    orderBookManager.addOrder(buy3);

    orderBookManager.deleteOrder(buy3.getOrderId());

    assertEquals(orderBookManager.bidBook.get(buy2.getPrice()).last.order, buy2);

  }

  @Test
  public void deleteFromEndInAsk(){

    Order sell1 = new Order("order1", "VOD.L", Side.SELL, 200, 10);
    Order sell2 = new Order("order2", "VOD.L", Side.SELL, 200, 10);
    Order sell3 = new Order("order3", "VOD.L", Side.SELL, 200, 10);

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell2);
    orderBookManager.addOrder(sell3);

    orderBookManager.deleteOrder(sell3.getOrderId());

    assertEquals(orderBookManager.askBook.get(sell2.getPrice()).last.order, sell2);


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
