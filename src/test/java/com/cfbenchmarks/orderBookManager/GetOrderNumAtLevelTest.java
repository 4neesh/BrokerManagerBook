package com.cfbenchmarks.orderBookManager;

import static org.junit.Assert.assertEquals;

import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import org.junit.Before;
import org.junit.Test;

public class GetOrderNumAtLevelTest {

  private OrderBookManagerImpl orderBookManager;
  private Order buy1;
  private Order buy1SamePrice;
  private Order buy2;
  private Order sell1;
  private Order sell1SamePrice;
  private Order sell2;

  @Before
  public void setUp() {

    orderBookManager = new OrderBookManagerImpl();
    buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    buy1SamePrice = new Order("order9", "VOD.L", Side.BUY, 200, 10);
    buy2 = new Order("order2", "VOD.L", Side.BUY, 100, 10);
    sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 10);
    sell1SamePrice = new Order("order10", "VOD.L", Side.SELL, 200, 10);
    sell2 = new Order("order5", "VOD.L", Side.SELL, 100, 10);
  }

  @Test
  public void bidBookReturnsOneAfterAdding() {

    orderBookManager.addOrder(buy1);

    assertEquals(
        "Empty Bid book level does not increment with order",
        1,
        orderBookManager.getOrderNumAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askBookReturnsOneAfterAdding() {

    orderBookManager.addOrder(sell1);

    assertEquals(
        "Empty Ask book level does not increment with order",
        1,
        orderBookManager.getOrderNumAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }

  @Test
  public void bidBookReturnsTwoAfterAddingMultiple() {

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy1SamePrice);

    assertEquals(
        "Empty Bid book level does not increment with order",
        2,
        orderBookManager.getOrderNumAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askBookReturnsTwoAfterAddingMultiple() {

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell1SamePrice);

    assertEquals(
        "Empty Ask book level does not increment with order",
        2,
        orderBookManager.getOrderNumAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }

  @Test
  public void bidBookNumberReflectsLevel() {

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy2);

    assertEquals(
        "Empty Bid book level does not increment with order",
        1,
        orderBookManager.getOrderNumAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
    assertEquals(
        "Empty Bid book level does not increment with order",
        1,
        orderBookManager.getOrderNumAtLevel(buy2.getInstrument(), buy2.getSide(), buy2.getPrice()));
  }

  @Test
  public void askBookNumberReflectsLevel() {

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell2);

    assertEquals(
        "Empty Ask book level does not increment with order",
        1,
        orderBookManager.getOrderNumAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
    assertEquals(
        "Empty Ask book level does not increment with order",
        1,
        orderBookManager.getOrderNumAtLevel(
            sell2.getInstrument(), sell2.getSide(), sell2.getPrice()));
  }

  @Test
  public void bidBookNumberReducesToOne() {

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy1SamePrice);
    orderBookManager.deleteOrder(buy1.getOrderId());

    assertEquals(
        "Empty Bid book level does not reduce with deleted order",
        1,
        orderBookManager.getOrderNumAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askBookNumberReducesToOne() {

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell1SamePrice);
    orderBookManager.deleteOrder(sell1.getOrderId());
    assertEquals(
        "Empty Ask book level does not reduce with deleted order",
        1,
        orderBookManager.getOrderNumAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }
}
