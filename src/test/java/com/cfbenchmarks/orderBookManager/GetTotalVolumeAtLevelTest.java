package com.cfbenchmarks.orderBookManager;

import static org.junit.Assert.assertEquals;

import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import org.junit.Before;
import org.junit.Test;

public class GetTotalVolumeAtLevelTest {

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
  private static final long BUY1_VOLUME = 2000;
  private static final long BUY2_VOLUME = 1100;
  private static final long BUY1OTHER_VOLUME = 600;
  private static final long BUY1SAMEPRICE_VOLUME = 1800;
  private static final long SELL1_VOLUME = 2200;
  private static final long SELL2_VOLUME = 1100;
  private static final long SELL1OTHER_VOLUME = 500;
  private static final long SELL1SAMEPRICE_VOLUME = 1600;

  @Before
  public void setUp() {

    orderBookManager = new OrderBookManagerImpl();
    buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    buy1SamePrice = new Order("order9", "VOD.L", Side.BUY, 200, 9);
    buy2 = new Order("order2", "VOD.L", Side.BUY, 100, 11);
    buy3 = new Order("order3", "VOD.L", Side.BUY, 50, 10);
    sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 11);
    sell1SamePrice = new Order("order10", "VOD.L", Side.SELL, 200, 8);
    sell2 = new Order("order5", "VOD.L", Side.SELL, 100, 11);
    sell3 = new Order("order6", "VOD.L", Side.SELL, 50, 10);
    buyOther = new Order("order7", "APPL", Side.BUY, 50, 12);
    sellOther = new Order("order8", "APPL", Side.SELL, 50, 10);
  }

  @Test
  public void bidBookReturnsOrdersAfterAdding() {

    assertEquals(
        "Bid Book for VOD.L is not empty",
        null,
        orderBookManager.bidBookHashMap.get(buy1.getInstrument()));
    assertEquals("buy1 does not have 10 orders", 10, buy1.getQuantity());
    assertEquals("buy1 does not have 200 price", 200, buy1.getPrice());

    orderBookManager.addOrder(buy1);

    assertEquals(
        "Empty Bid book level does not add orderVolume with first order",
        BUY1_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askBookReturnsOrdersAfterAdding() {

    assertEquals(
        "Ask Book for VOD.L is not empty",
        null,
        orderBookManager.askBookHashMap.get(buy1.getInstrument()));
    assertEquals("sell1 does not have 11 orders", 11, sell1.getQuantity());
    assertEquals("sell1 does not have 200 price", 200, sell1.getPrice());

    orderBookManager.addOrder(sell1);

    assertEquals(
        "Empty Ask book level does not add orderQuantity with first order",
        SELL1_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }

  @Test
  public void bidBookReturnsAddedOrdersAfterAddingMultiple() {

    assertEquals(
        "Bid Book is not empty", null, orderBookManager.bidBookHashMap.get(buy1.getInstrument()));
    assertEquals("buy1 does not have 10 orders", 10, buy1.getQuantity());
    assertEquals("buy1 does not have 200 price", 200, buy1.getPrice());
    assertEquals("buy1SamePrice does not have 9 orders", 9, buy1SamePrice.getQuantity());
    assertEquals("buy1SamePrice does not have 200 price", 200, buy1SamePrice.getPrice());

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy1SamePrice);

    assertEquals(
        "Empty Bid book quantity does not increases with multiple orders",
        BUY1_VOLUME + BUY1SAMEPRICE_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askBookReturnsAddedOrdersAfterAddingMultiple() {

    assertEquals(
        "Ask Book is not empty", orderBookManager.bidBookHashMap.get(sell1.getInstrument()), null);

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell1SamePrice);

    assertEquals(
        "Empty Ask book quantity does not increase with multiple orders",
        SELL1_VOLUME + SELL1SAMEPRICE_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }

  @Test
  public void bidBookOrdersReflectsLevel() {

    assertEquals(
        "Bid Book is not empty", orderBookManager.bidBookHashMap.get(buy1.getInstrument()), null);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buyOther);

    assertEquals(
        "Bid book quantity does not reflect on instrument",
        BUY1_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
    assertEquals(
        "Bid book quantity does not reflect on instrument",
        BUY1OTHER_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            buyOther.getInstrument(), buyOther.getSide(), buyOther.getPrice()));
  }

  @Test
  public void askBookOrdersReflectsLevel() {

    assertEquals(
        "Ask Book is not empty", orderBookManager.bidBookHashMap.get(sell1.getInstrument()), null);

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sellOther);

    assertEquals(
        "Ask book quantity does not reflect on instrument",
        SELL1_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
    assertEquals(
        "Ask book quantity does not reflect on instrument",
        SELL1OTHER_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            sellOther.getInstrument(), sellOther.getSide(), sellOther.getPrice()));
  }

  @Test
  public void bidBookOrdersReducesAfterOneDeleted() {

    assertEquals(
        "Bid Book is not empty", orderBookManager.bidBookHashMap.get(buy1.getInstrument()), null);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy1SamePrice);
    orderBookManager.deleteOrder(buy1.getOrderId());

    assertEquals(
        "Bid book level does not increment with order",
        BUY1SAMEPRICE_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askBookOrdersReduceAfterOneDeleted() {

    assertEquals(
        "Ask Book is not empty", orderBookManager.bidBookHashMap.get(sell1.getInstrument()), null);

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell1SamePrice);
    orderBookManager.deleteOrder(sell1.getOrderId());
    assertEquals(
        "Ask book quantity does not increment with order",
        SELL1SAMEPRICE_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }

  @Test
  public void bidBookOrdersReducesToZero() {

    assertEquals(
        "Bid Book is not empty", orderBookManager.bidBookHashMap.get(buy1.getInstrument()), null);

    orderBookManager.addOrder(buy1);
    orderBookManager.deleteOrder(buy1.getOrderId());

    assertEquals(
        "Empty Bid book quantity does not equal 0",
        0,
        orderBookManager.getTotalVolumeAtLevel(
            buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askBookOrdersReducesToZero() {

    assertEquals(
        "Ask Book is not empty", orderBookManager.bidBookHashMap.get(sell1.getInstrument()), null);

    orderBookManager.addOrder(sell1);
    orderBookManager.deleteOrder(sell1.getOrderId());
    assertEquals(
        "Empty Ask book quantity does not equal 0",
        0,
        orderBookManager.getTotalVolumeAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }
}
