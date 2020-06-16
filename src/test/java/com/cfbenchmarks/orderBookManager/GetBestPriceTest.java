package com.cfbenchmarks.orderBookManager;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class GetBestPriceTest {

  private static OrderBookManagerImpl orderBookManager;
  private static Order buy1;
  private static Order buy2;
  private static Order buy3;
  private static Order sell1;
  private static Order sell2;
  private static Order sell3;
  private static Order buyOther;
  private static Order sellOther;

  @Before
  public void setUp() {

    orderBookManager = new OrderBookManagerImpl();
    buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    buy2 = new Order("order2", "VOD.L", Side.BUY, 100, 10);
    buy3 = new Order("order3", "VOD.L", Side.BUY, 50, 10);
    sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 10);
    sell2 = new Order("order5", "VOD.L", Side.SELL, 100, 10);
    sell3 = new Order("order6", "VOD.L", Side.SELL, 50, 10);
    buyOther = new Order("order7", "APPL", Side.BUY, 50, 10);
    sellOther = new Order("order8", "APPL", Side.SELL, 50, 10);

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
  public void bestBidPriceIsHighest() {

    long bestBidPriceOfVodL =
        orderBookManager.getBestPrice(buy1.getInstrument(), buy1.getSide()).get();

    assertEquals(
        "Best price of bid is not highest",
        bestBidPriceOfVodL,
        orderBookManager.getOrderHashMap().get(buy1.getOrderId()).getPrice());
  }

  @Test
  public void bestAskPriceIsLowest() {

    long bestAskPriceOfVodL =
        orderBookManager.getBestPrice(sell3.getInstrument(), sell3.getSide()).get();

    assertEquals(
        "Best price of ask is not lowest",
        bestAskPriceOfVodL,
        orderBookManager.getOrderHashMap().get(sell3.getOrderId()).getPrice());
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
    assertEquals(
        "Best ask price is changed after order of equal price added", expectedPrice, actualPrice);
  }

  @Test
  public void bestBidPriceIsMovedToNextAfterDeleteHighest() {

    orderBookManager.deleteOrder(buy1.getOrderId());

    Long expectedPrice = buy2.getPrice();
    Long actualPrice = orderBookManager.getBestPrice(buy1.getInstrument(), buy1.getSide()).get();
    assertEquals(
        "Best bid price is not changed after highest is deleted", expectedPrice, actualPrice);
  }

  @Test
  public void bestBidPriceStillHighestAfterDeleteNonHighest() {

    assertEquals(
        "buy2 is incorrectly the best price bid",
        orderBookManager.getBestPrice(buy2.getInstrument(), buy2.getSide()).equals(buy2.getPrice()),
        false);
    orderBookManager.deleteOrder(buy2.getOrderId());

    Long expectedPrice = buy1.getPrice();
    Long actualPrice = orderBookManager.getBestPrice(buy2.getInstrument(), buy2.getSide()).get();
    assertEquals(
        "Best bid price has changed after non-highest is deleted", expectedPrice, actualPrice);
  }

  @Test
  public void bestAskPriceIsMovedToNextAfterDeleteHighest() {

    orderBookManager.deleteOrder(sell3.getOrderId());

    Long expectedPrice = sell2.getPrice();
    Long actualPrice = orderBookManager.getBestPrice(sell3.getInstrument(), sell3.getSide()).get();
    assertEquals(
        "Best ask price is not changed after highest is deleted", expectedPrice, actualPrice);
  }

  @Test
  public void bestAskPriceStillHighestAfterDeleteNonHighest() {

    assertEquals(
        "sell2 is incorrectly the best price ask",
        orderBookManager
            .getBestPrice(sell2.getInstrument(), sell2.getSide())
            .equals(sell2.getPrice()),
        false);
    orderBookManager.deleteOrder(sell2.getOrderId());

    Long expectedPrice = sell3.getPrice();
    Long actualPrice = orderBookManager.getBestPrice(sell2.getInstrument(), sell2.getSide()).get();
    assertEquals(
        "Best ask price has changed after non-highest is deleted", expectedPrice, actualPrice);
  }

  @Test
  public void bestBidIsOptionalEmptyWhenOnlyOrderIsDeleted() {

    assertEquals(
        "buyOther is not the best price bid",
        orderBookManager
            .getBestPrice(buyOther.getInstrument(), buyOther.getSide())
            .get()
            .equals(buyOther.getPrice()),
        true);
    orderBookManager.deleteOrder(buyOther.getOrderId());

    Optional<Long> expectedPrice = Optional.empty();
    Optional<Long> actualPrice =
        orderBookManager.getBestPrice(buyOther.getInstrument(), buyOther.getSide());
    assertEquals(
        "Best bid price has changed to Optional.empty when only order is deleted",
        expectedPrice,
        actualPrice);
  }

  @Test
  public void bestAskIsOptionalEmptyWhenOnlyOrderIsDeleted() {

    assertEquals(
        "sellOther is not the best price ask",
        orderBookManager
            .getBestPrice(sell2.getInstrument(), sell2.getSide())
            .equals(sell2.getPrice()),
        false);
    orderBookManager.deleteOrder(sell2.getOrderId());

    Long expectedPrice = sell3.getPrice();
    Long actualPrice = orderBookManager.getBestPrice(sell2.getInstrument(), sell2.getSide()).get();
    assertEquals(
        "Best ask price has changed after non-highest is deleted", expectedPrice, actualPrice);
  }
}
