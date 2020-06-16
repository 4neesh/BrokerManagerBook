package com.cfbenchmarks.orderBookManager;

import static org.junit.Assert.assertEquals;

import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import org.junit.Before;
import org.junit.Test;

public class GetTotalQuantityAtLevelTest {

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
    buy2 = new Order("order2", "VOD.L", Side.BUY, 100, 11);
    buy3 = new Order("order3", "VOD.L", Side.BUY, 50, 10);
    sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 11);
    sell1SamePrice = new Order("order10", "VOD.L", Side.SELL, 200, 10);
    sell2 = new Order("order5", "VOD.L", Side.SELL, 100, 11);
    sell3 = new Order("order6", "VOD.L", Side.SELL, 50, 10);
    buyOther = new Order("order7", "APPL", Side.BUY, 50, 12);
    sellOther = new Order("order8", "APPL", Side.SELL, 50, 12);
  }

  @Test
  public void bidBookReturnsOrdersAfterAdding() {

    orderBookManager.addOrder(buy1);

    assertEquals(
        "Empty Bid book level does not add orderQuantity with first order",
        buy1.getQuantity(),
        orderBookManager.getTotalQuantityAtLevel(
            buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askBookReturnsOrdersAfterAdding() {

    orderBookManager.addOrder(sell1);

    assertEquals(
        "Empty Ask book level does not add orderQuantity with first order",
        sell1.getQuantity(),
        orderBookManager.getTotalQuantityAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }

  @Test
  public void bidBookReturnsAddedOrdersAfterAddingMultiple() {

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy1SamePrice);

    assertEquals(
        "Empty Bid book quantity does not increases with multiple orders",
        20,
        orderBookManager.getTotalQuantityAtLevel(
            buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askBookReturnsAddedOrdersAfterAddingMultiple() {

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell1SamePrice);

    assertEquals(
        "Empty Ask book quantity does not increase with multiple orders",
        21,
        orderBookManager.getTotalQuantityAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }

  @Test
  public void bidBookOrdersReflectsLevel() {

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buyOther);

    assertEquals(
        "Bid book quantity does not reflect on instrument",
        10,
        orderBookManager.getTotalQuantityAtLevel(
            buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
    assertEquals(
        "Bid book quantity does not reflect on instrument",
        12,
        orderBookManager.getTotalQuantityAtLevel(
            buyOther.getInstrument(), buyOther.getSide(), buyOther.getPrice()));
  }

  @Test
  public void askBookOrdersReflectsLevel() {

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sellOther);

    assertEquals(
        "Ask book quantity does not reflect on instrument",
        11,
        orderBookManager.getTotalQuantityAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
    assertEquals(
        "Ask book quantity does not reflect on instrument",
        12,
        orderBookManager.getTotalQuantityAtLevel(
            sellOther.getInstrument(), sellOther.getSide(), sellOther.getPrice()));
  }

  @Test
  public void bidBookOrdersReducesAfterOneDeleted() {

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy1SamePrice);
    orderBookManager.deleteOrder(buy1.getOrderId());

    assertEquals(
        "Bid book level does not increment with order",
        orderBookManager.getTotalQuantityAtLevel(
            buy1.getInstrument(), buy1.getSide(), buy1.getPrice()),
        10);
  }

  @Test
  public void askBookOrdersReduceAfterOneDeleted() {

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell1SamePrice);
    orderBookManager.deleteOrder(sell1.getOrderId());
    assertEquals(
        "Ask book quantity does not increment with order",
        10,
        orderBookManager.getTotalQuantityAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }
}
