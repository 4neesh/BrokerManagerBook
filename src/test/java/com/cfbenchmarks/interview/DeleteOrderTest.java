package com.cfbenchmarks.interview;

import com.cfbenchmarks.orderBook.AskBook;
import com.cfbenchmarks.orderBook.BidBook;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeleteOrderTest {

    private static OrderBookManagerImpl orderBookManager;
    private static BidBook bidBook;
    private static AskBook askBook;
    private static Order buy1;
    private static Order buy2;
    private static Order buy3;
    private static Order sell1;
    private static Order sell2;
    private static Order sell3;

    @Before
    public  void setUp(){

        askBook = new AskBook();
        bidBook = new BidBook();
        orderBookManager = new OrderBookManagerImpl(askBook, bidBook);
        buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
        buy2 = new Order("order2", "VOD.L", Side.BUY, 200, 10);
        buy3 = new Order("order3", "VOD.L", Side.BUY, 200, 10);
        sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 10);
        sell2 = new Order("order5", "VOD.L", Side.SELL, 200, 10);
        sell3 = new Order("order6", "VOD.L", Side.SELL, 200, 10);

        orderBookManager.addOrder(buy1);
        orderBookManager.addOrder(buy2);
        orderBookManager.addOrder(buy3);
        orderBookManager.addOrder(sell1);
        orderBookManager.addOrder(sell2);
        orderBookManager.addOrder(sell3);

    }


    @Test
    public void deleteFromHeadInBid(){

        assertTrue(orderBookManager.bidBook.get(buy1.getPrice()).head.order == buy1);
        assertTrue(orderBookManager.bidBook.get(buy1.getPrice()).head.next.order == buy2);
        assertTrue(orderBookManager.bidLookup.containsKey(buy1.getOrderId()));

        orderBookManager.deleteOrder(buy1.getOrderId());

        assertFalse(orderBookManager.bidLookup.containsKey(buy1.getOrderId()));
        assertEquals(orderBookManager.bidBook.get(buy2.getPrice()).head.order, buy2);

    }

    @Test
    public void deleteFromHeadInAsk(){

        assertTrue(orderBookManager.askBook.get(sell1.getPrice()).head.order == sell1);
        assertTrue(orderBookManager.askBook.get(sell1.getPrice()).head.next.order == sell2);
        assertTrue(orderBookManager.askLookup.containsKey(sell1.getOrderId()));

        orderBookManager.deleteOrder(sell1.getOrderId());

        assertFalse(orderBookManager.askLookup.containsKey(sell1.getOrderId()));
        assertEquals(orderBookManager.askBook.get(sell2.getPrice()).head.order, sell2);

    }

    @Test
    public void deleteFromBodyInBid(){

        assertTrue(orderBookManager.bidBook.get(buy1.getPrice()).head.next.order == buy2);
        assertTrue(orderBookManager.bidBook.get(buy1.getPrice()).head.next.next.order == buy3);
        assertTrue(orderBookManager.bidLookup.containsKey(buy1.getOrderId()));


        orderBookManager.deleteOrder(buy2.getOrderId());

        assertFalse(orderBookManager.bidLookup.containsKey(buy2.getOrderId()));
        assertEquals(orderBookManager.bidBook.get(buy1.getPrice()).head.next.order, buy3);

    }

    @Test
    public void deleteFromBodyInAsk(){

        assertTrue(orderBookManager.askBook.get(sell1.getPrice()).head.next.order == sell2);
        assertTrue(orderBookManager.askBook.get(sell1.getPrice()).head.next.next.order == sell3);
        assertTrue(orderBookManager.askLookup.containsKey(sell2.getOrderId()));

        orderBookManager.deleteOrder(sell2.getOrderId());

        assertFalse(orderBookManager.askLookup.containsKey(sell2.getOrderId()));
        assertEquals(orderBookManager.askBook.get(sell1.getPrice()).head.next.order, sell3);

    }

    @Test
    public void deleteFromEndInBid(){

        assertTrue(orderBookManager.bidBook.get(buy1.getPrice()).last.order == buy3);
        assertTrue(orderBookManager.bidBook.get(buy1.getPrice()).last.previous.order == buy2);
        assertTrue(orderBookManager.bidLookup.containsKey(buy3.getOrderId()));

        orderBookManager.deleteOrder(buy3.getOrderId());

        assertFalse(orderBookManager.bidLookup.containsKey(buy3.getOrderId()));
        assertEquals(orderBookManager.bidBook.get(buy2.getPrice()).last.order, buy2);

    }

    @Test
    public void deleteFromEndInAsk(){

        assertTrue(orderBookManager.askBook.get(sell1.getPrice()).last.order == sell3);
        assertTrue(orderBookManager.askBook.get(sell1.getPrice()).last.previous.order == sell2);
        assertTrue(orderBookManager.askLookup.containsKey(sell3.getOrderId()));

        orderBookManager.deleteOrder(sell3.getOrderId());

        assertFalse(orderBookManager.askLookup.containsKey(sell3.getOrderId()));
        assertEquals(orderBookManager.askBook.get(sell2.getPrice()).last.order, sell2);


    }



}
