package com.cfbenchmarks.orderBookManager;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AddOrderTest {

    private static OrderBookManagerImpl orderBookManager;

    @Before
    public void setUp() {

        orderBookManager = new OrderBookManagerImpl();
    }

    @Test
    public void bidOrderIsAdded() {

        Order buy = new Order("order1", "VOD.L", Side.BUY, 200, 10);
        String propertyKey = buy.getInstrument() + buy.getSide().toString();
        assertFalse(orderBookManager.bidLookup.containsKey(buy.getOrderId()));
        assertTrue(orderBookManager.bidBookHashMap.isEmpty());
        assertFalse(orderBookManager.instrumentPropertyMap.containsKey(propertyKey));

        orderBookManager.addOrder(buy);

        assertEquals("Bid does not exist in bidLookup",
                orderBookManager.bidLookup.containsKey(buy.getOrderId()),
                true);
        assertEquals(
                "Bid does not exist in bidBookHashMap",
                orderBookManager.bidBookHashMap.get(buy.getInstrument()).containsKey(buy.getPrice()),
                true);
        assertEquals(
                "Bid does not exist in instrumentPropertyMap",
                orderBookManager.instrumentPropertyMap.containsKey(propertyKey),
                true);
    }

    @Test
    public void askOrderIsAdded() {

        Order sell = new Order("order1", "VOD.L", Side.SELL, 200, 10);
        String propertyKey = sell.getInstrument() + sell.getSide().toString();

        assertFalse(orderBookManager.askLookup.containsKey(sell.getOrderId()));
        assertTrue(orderBookManager.askBookHashMap.isEmpty());
        assertFalse(orderBookManager.instrumentPropertyMap.containsKey(propertyKey));

        orderBookManager.addOrder(sell);

        assertEquals("Bid does not exist in askLookup",
                orderBookManager.askLookup.containsKey(sell.getOrderId()),
                true);
        assertEquals(
                "Bid does not exist in askBookHashMap",
                orderBookManager.askBookHashMap.get(sell.getInstrument()).containsKey(sell.getPrice()),
                true);
        assertEquals(
                "Bid does not exist in instrumentPropertyMap",
                orderBookManager.instrumentPropertyMap.containsKey(propertyKey),
                true);
    }

    @Test
    public void multipleBidAtSameLevelLogged() {

        Order buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
        Order buy2 = new Order("order2", "VOD.L", Side.BUY, 200, 10);
        assertTrue(buy1.getPrice() == buy2.getPrice());
        assertTrue(orderBookManager.bidBookHashMap.isEmpty());

        orderBookManager.addOrder(buy1);
        orderBookManager.addOrder(buy2);

        assertEquals("buy1 is not added to bidBookHashMap.",
                orderBookManager
                        .bidBookHashMap
                        .get(buy1.getInstrument())
                        .get(buy1.getPrice())
                        .head
                        .order
                        .getOrderId(),
                buy1.getOrderId());
        assertEquals("buy2 is not added to bidBookHashMap.",
                orderBookManager
                        .bidBookHashMap
                        .get(buy1.getInstrument())
                        .get(buy1.getPrice())
                        .head
                        .next
                        .order
                        .getOrderId(),
                buy2.getOrderId());
    }

    @Test
    public void multipleAskAtSameLevelLogged() {

        Order sell1 = new Order("order1", "VOD.L", Side.SELL, 200, 10);
        Order sell2 = new Order("order2", "VOD.L", Side.SELL, 200, 10);
        assertTrue(sell1.getPrice() == sell2.getPrice());
        assertTrue(orderBookManager.askBookHashMap.isEmpty());

        orderBookManager.addOrder(sell1);
        orderBookManager.addOrder(sell2);

        assertEquals("sell1 is not added to askBookHashMap.",
                orderBookManager
                        .askBookHashMap
                        .get(sell1.getInstrument())
                        .get(sell1.getPrice())
                        .head
                        .order
                        .getOrderId(),
                sell1.getOrderId());
        assertEquals("sell2 is not added to askBookHashMap.",
                orderBookManager
                        .askBookHashMap
                        .get(sell1.getInstrument())
                        .get(sell1.getPrice())
                        .head
                        .next
                        .order
                        .getOrderId(),
                sell2.getOrderId());
    }
}
