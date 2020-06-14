package com.cfbenchmarks.orderBookManager;

import com.cfbenchmarks.instrumentProperty.InstrumentProperty;
import com.cfbenchmarks.orderBook.AskBook;
import com.cfbenchmarks.orderBook.BidBook;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetBestPriceTest {

    private static OrderBookManagerImpl orderBookManager;
    private static BidBook bidBook;
    private static AskBook askBook;
    private static HashMap<String, InstrumentProperty> instrumentPropertyHashMap;
    private static Order buy1;
    private static Order buy2;
    private static Order buy3;
    private static Order sell1;
    private static Order sell2;
    private static Order sell3;

    @Before
    public void setUp() {

        askBook = new AskBook();
        bidBook = new BidBook();
        instrumentPropertyHashMap = new HashMap<>();

        orderBookManager = new OrderBookManagerImpl(askBook, bidBook, instrumentPropertyHashMap);
        buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
        buy2 = new Order("order2", "VOD.L", Side.BUY, 100, 10);
        buy3 = new Order("order3", "VOD.L", Side.BUY, 50, 10);
        sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 10);
        sell2 = new Order("order5", "VOD.L", Side.SELL, 100, 10);
        sell3 = new Order("order6", "VOD.L", Side.SELL, 50, 10);

        orderBookManager.addOrder(buy1);
        orderBookManager.addOrder(buy2);
        orderBookManager.addOrder(buy3);
        orderBookManager.addOrder(sell1);
        orderBookManager.addOrder(sell2);
        orderBookManager.addOrder(sell3);
    }

    @Test
    public void bestBidPriceIsHighest() {

        Long bestBidPriceOfVodL = orderBookManager.getBestPrice(buy1.getInstrument(), buy1.getSide()).get();

        assertEquals("Best price of bid is not highest", bestBidPriceOfVodL, orderBookManager.bidLookup.get(buy1.getOrderId()));

    }

    @Test
    public void bestAskPriceIsLowest() {

        Long bestAskPriceOfVodL = orderBookManager.getBestPrice(sell3.getInstrument(), sell3.getSide()).get();

        assertEquals("Best price of ask is not lowest", bestAskPriceOfVodL, orderBookManager.askLookup.get(sell3.getOrderId()));


    }

    @Test
    public void bestBidPriceIsHighestAfterHigherAdd() {

        Order buy4 = new Order("order7", "VOD.L", Side.BUY, 300, 10);
        assertTrue(buy4.getPrice() > buy1.getPrice());
        assertTrue(buy4.getInstrument() == buy1.getInstrument());
        assertTrue(buy4.getSide() == buy1.getSide());
        orderBookManager.addOrder(buy4);

        Long expectedPrice = buy4.getPrice();
        Long actualPrice = orderBookManager.getBestPrice(buy1.getInstrument(), buy1.getSide()).get();
        assertEquals("Best bid price is not changed to higher value", expectedPrice, actualPrice);
    }

    @Test
    public void bestBidPriceIsHighestAfterLowerAdd() {

        Order buy4 = new Order("order7", "VOD.L", Side.BUY, 100, 10);
        assertTrue(buy4.getPrice() < buy1.getPrice());
        assertTrue(buy4.getInstrument() == buy1.getInstrument());
        assertTrue(buy4.getSide() == buy1.getSide());
        orderBookManager.addOrder(buy4);

        Long expectedPrice = buy1.getPrice();
        Long actualPrice = orderBookManager.getBestPrice(buy1.getInstrument(), buy1.getSide()).get();
        assertEquals("Best bid price is changed to lower value", expectedPrice, actualPrice);
    }

    @Test
    public void bestBidPriceIsHighestAfterEqualAdd() {

        Order buy4 = new Order("order7", "VOD.L", Side.BUY, 200, 10);
        assertTrue(buy4.getPrice() == buy1.getPrice());
        assertTrue(buy4.getInstrument() == buy1.getInstrument());
        assertTrue(buy4.getSide() == buy1.getSide());
        orderBookManager.addOrder(buy4);

        Long expectedPrice = buy1.getPrice();
        Long actualPrice = orderBookManager.getBestPrice(buy1.getInstrument(), buy1.getSide()).get();
        assertEquals("Best bid price is changed to lower value", expectedPrice, actualPrice);
    }

    @Test
    public void bestAskPriceIsLowestAfterHigherAdd() {

        Order sell4 = new Order("order7", "VOD.L", Side.SELL, 300, 10);
        assertTrue(sell4.getPrice() > sell3.getPrice());
        assertTrue(sell4.getInstrument() == sell3.getInstrument());
        assertTrue(sell4.getSide() == sell3.getSide());
        orderBookManager.addOrder(sell4);

        Long expectedPrice = sell3.getPrice();
        Long actualPrice = orderBookManager.getBestPrice(sell3.getInstrument(), sell3.getSide()).get();
        assertEquals("Best ask price is changed to higher value", expectedPrice, actualPrice);
    }

    @Test
    public void bestAskPriceIsLowestAfterLowerAdd() {

        Order sell4 = new Order("order7", "VOD.L", Side.SELL, 10, 10);
        assertTrue(sell4.getPrice() < sell3.getPrice());
        assertTrue(sell4.getInstrument() == sell3.getInstrument());
        assertTrue(sell4.getSide() == sell3.getSide());
        orderBookManager.addOrder(sell4);

        Long expectedPrice = sell4.getPrice();
        Long actualPrice = orderBookManager.getBestPrice(sell3.getInstrument(), sell3.getSide()).get();
        assertEquals("Best ask price is not changed to lower value", expectedPrice, actualPrice);
    }

    @Test
    public void bestAskPriceIsLowestAfterEqualAdd() {

        Order sell4 = new Order("order7", "VOD.L", Side.SELL, 50, 10);
        assertTrue(sell4.getPrice() == sell3.getPrice());
        assertTrue(sell4.getInstrument() == sell3.getInstrument());
        assertTrue(sell4.getSide() == sell3.getSide());
        orderBookManager.addOrder(sell4);

        Long expectedPrice = sell3.getPrice();
        Long actualPrice = orderBookManager.getBestPrice(sell3.getInstrument(), sell3.getSide()).get();
        assertEquals("Best ask price is changed after order of equal price added", expectedPrice, actualPrice);
    }


}
