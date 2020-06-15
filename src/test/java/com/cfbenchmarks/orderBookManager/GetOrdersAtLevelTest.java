package com.cfbenchmarks.orderBookManager;

import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GetOrdersAtLevelTest {


    private OrderBookManagerImpl orderBookManager;
    private Order buy1;
    private Order buy1SamePrice;
    private Order buy2;
    private Order buy3;
    private Order sell1;
    private Order sell1SamePrice;
    private Order sell2;
    private Order sell3;
    private Order buyOther;
    private Order sellOther;
    private Order buyUnique;
    private Order sellUnique;

    @Before
    public void setUp() {

        orderBookManager = new OrderBookManagerImpl();
        buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
        buy1SamePrice = new Order("order9", "VOD.L", Side.BUY, 200, 10);
        buy2 = new Order("order2", "VOD.L", Side.BUY, 100, 10);
        buy3 = new Order("order3", "VOD.L", Side.BUY, 50, 10);
        sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 10);
        sell1SamePrice = new Order("order10", "VOD.L", Side.SELL, 200, 10);
        sell2 = new Order("order5", "VOD.L", Side.SELL, 100, 10);
        sell3 = new Order("order6", "VOD.L", Side.SELL, 50, 10);
        buyOther = new Order("order7", "APPL", Side.BUY, 50, 10);
        sellOther = new Order("order8", "APPL", Side.SELL, 50, 10);
    }

    @Test(expected = NullPointerException.class)
    public void emptyBidLevelReturnsNull(){

        assertEquals("Bid book level is not empty", null, orderBookManager.getOrdersAtLevel("VOD.L", Side.BUY, 10L));

    }

    @Test(expected = NullPointerException.class)
    public void emptyAskLevelReturnsNull(){

        assertEquals("Ask book level is not empty", null, orderBookManager.getOrdersAtLevel("VOD.L", Side.SELL, 10L));

    }


    @Test
    public void bidReturnsOrderWhenAdded(){

        orderBookManager.addOrder(buy1);
        List<Order> expected = new ArrayList<>();
        expected.add(buy1);

        assertEquals("Bid book does not return orders on level", expected, orderBookManager.getOrdersAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));

    }

    @Test
    public void askReturnsOrderWhenAdded(){

        orderBookManager.addOrder(sell1);
        List<Order> expected = new ArrayList<>();
        expected.add(sell1);

        assertEquals("Ask book does not return orders on level", expected, orderBookManager.getOrdersAtLevel(sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));

    }

    //adding new ones will respect the order
    @Test
    public void bidReturnsMultipleOrdersWhenAdded(){

        orderBookManager.addOrder(buy1);
        List<Order> expected = new ArrayList<>();
        expected.add(buy1);

        assertEquals("Bid book does not return orders on level", expected, orderBookManager.getOrdersAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));

    }

    @Test
    public void askReturnsMultipleOrdersWhenAdded(){

        orderBookManager.addOrder(sell1);
        List<Order> expected = new ArrayList<>();
        expected.add(sell1);

        assertEquals("Ask book does not return orders on level", expected, orderBookManager.getOrdersAtLevel(sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));

    }

    //deleting will be reflected


    //deleting even though there are two of the high ones will keep order




}
