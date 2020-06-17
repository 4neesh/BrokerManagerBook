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
  private Order sell1;
  private Order sell1SamePrice;
  private Order buyOther;
  private Order sellOther;
  private static final long BUY1_VOLUME = 2000;
  private static final long BUY1OTHER_VOLUME = 600;
  private static final long BUY1SAMEPRICE_VOLUME = 1800;
  private static final long SELL1_VOLUME = 2200;
  private static final long SELL1OTHER_VOLUME = 500;
  private static final long SELL1SAMEPRICE_VOLUME = 1600;

  @Before
  public void setUp() {

    orderBookManager = new OrderBookManagerImpl();
    buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    buyOther = new Order("order7", "APPL", Side.BUY, 60, 10);

    buy1SamePrice = new Order("order9", "VOD.L", Side.BUY, 200, 9);
    sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 11);
    sell1SamePrice = new Order("order10", "VOD.L", Side.SELL, 200, 8);
    sellOther = new Order("order8", "APPL", Side.SELL, 50, 10);
  }

  @Test
  public void bidOrderVolumeReflectedForSingleOrder() {

    orderBookManager.addOrder(buy1);

    assertEquals(
        "bid orderVolume not reflected with first order",
        BUY1_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askOrderVolumeReflectedForSingleOrder() {

    orderBookManager.addOrder(sell1);

    assertEquals(
            "ask orderVolume not reflected with first order",
        SELL1_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }

  @Test
  public void bidBookReturnsOrderVolumeAfterAddingMultiple() {

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy1SamePrice);

    assertEquals(
            "bid orderVolume not reflected with multiple orders",
        BUY1_VOLUME + BUY1SAMEPRICE_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askBookReturnsOrderVolumeAfterAddingMultiple() {

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell1SamePrice);

    assertEquals(
            "ask orderVolume not reflected with multiple orders",
        SELL1_VOLUME + SELL1SAMEPRICE_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }

  @Test
  public void bidBookReturnsOrderVolumeForDifferentInstruments() {

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buyOther);

    assertEquals(
        "Bid book orderVolume does not reflect on instrument",
        BUY1OTHER_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            buyOther.getInstrument(), buyOther.getSide(), buyOther.getPrice()));
  }

  @Test
  public void askBookReturnsOrderVolumeForDifferentInstruments() {

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sellOther);

    assertEquals(
        "Ask book orderVolume does not reflect on instrument",
        SELL1OTHER_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            sellOther.getInstrument(), sellOther.getSide(), sellOther.getPrice()));
  }

  @Test
  public void bidBookOrderVolumeReducesAfterOneDeleted() {

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy1SamePrice);
    orderBookManager.deleteOrder(buy1.getOrderId());

    assertEquals(
        "Bid book orderVolume does not reduce when order is deleted",
        BUY1SAMEPRICE_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askBookOrderVolumeReducesAfterOneDeleted() {

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell1SamePrice);
    orderBookManager.deleteOrder(sell1.getOrderId());
    assertEquals(
            "Ask book orderVolume does not reduce when order is deleted",
        SELL1SAMEPRICE_VOLUME,
        orderBookManager.getTotalVolumeAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }

  @Test
  public void bidBookOrdersReducesToZero() {

    orderBookManager.addOrder(buy1);
    orderBookManager.deleteOrder(buy1.getOrderId());

    assertEquals(
        "Bid book orderVolume does not equal 0 when same order added and removed",
        0,
        orderBookManager.getTotalVolumeAtLevel(
            buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askBookOrdersReducesToZero() {

    orderBookManager.addOrder(sell1);
    orderBookManager.deleteOrder(sell1.getOrderId());
    assertEquals(
            "Bid book orderVolume does not equal 0 when same order added and removed",
        0,
        orderBookManager.getTotalVolumeAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }
}
