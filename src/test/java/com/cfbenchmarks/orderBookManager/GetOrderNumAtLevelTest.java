package com.cfbenchmarks.orderBookManager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GetOrderNumAtLevelTest {

    private OrderBookManagerImpl orderBookManager;
    private  Order buy1;
    private  Order buy2;
    private  Order buy3;
    private  Order sell1;
    private  Order sell2;
    private  Order sell3;
    private  Order buyOther;
    private  Order sellOther;
    private  Order buyUnique;
    private  Order sellUnique;

    @Before
    public void setUp() {

        orderBookManager = new OrderBookManagerImpl();
        buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
        buy2 = new Order("order2", "VOD.L", Side.BUY, 100, 10);
        buy3 = new Order("order3", "VOD.L", Side.BUY, 50, 10);
        sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 10);
        sell2 = new Order("order5", "VOD.L", Side.SELL, 100, 10);
        sell3 = new Order("order6", "VOD.L", Side.SELL, 50, 10);
        buyUnique = new Order("order7", "GSK", Side.BUY, 50, 10);
        buyOther = new Order("order8", "APPL", Side.BUY, 50, 10);
        sellUnique= new Order("order9", "GSK", Side.BUY, 50, 10);
        sellOther = new Order("order10", "APPL", Side.SELL, 50, 10);



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
    public void bidBookReturnsZeroWithNoEntries(){

        assertEquals("Bid Book suggests it contains orders without any added", orderBookManager.bidBookHashMap.get(buyUnique.getInstrument()), null);

        assertEquals("Empty Bid book level does not return 0 orders", orderBookManager.getOrderNumAtLevel(buyUnique.getInstrument(), buyUnique.getSide(), buyUnique.getPrice()), 0);

    }



    @Test
    public void askBookReturnsZeroWithNoEntries(){

        assertEquals("Ask Book suggests askUnique exists when not added", orderBookManager.askBookHashMap.get(sellUnique.getInstrument()), null);

        assertEquals("Empty Ask book level does not return 0 orders", orderBookManager.getOrderNumAtLevel(sellUnique.getInstrument(), sellUnique.getSide(), sellUnique.getPrice()), 0);

    }

}
