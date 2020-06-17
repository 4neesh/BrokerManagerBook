package com.cfbenchmarks.orderBookManager;

import static org.junit.Assert.assertEquals;

import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class GetOrdersAtLevelTest {

  private OrderBookManagerImpl orderBookManager;
  private Order buy1;
  private Order buy2;
  private Order buy3;
  private Order sell1;
  private Order sell2;
  private Order sell3;
  private Order buyOther;
  private Order sellOther;

  @Before
  public void setUp() {

    orderBookManager = new OrderBookManagerImpl();
    buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    buy2 = new Order("order2", "VOD.L", Side.BUY, 200, 10);
    buy3 = new Order("order3", "VOD.L", Side.BUY, 50, 10);
    sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 10);
    sell2 = new Order("order5", "VOD.L", Side.SELL, 200, 10);
    sell3 = new Order("order6", "VOD.L", Side.SELL, 50, 10);
    buyOther = new Order("order7", "APPL", Side.BUY, 200, 10);
    sellOther = new Order("order8", "APPL", Side.SELL, 200, 10);
  }

  @Test(expected = NullPointerException.class)
  public void emptyBidLevelReturnsNull() {

    assertEquals(
        "NullPointer not thrown for trying to access empty bid level",
        null,
        orderBookManager.getOrdersAtLevel("VOD.L", Side.BUY, 10L));
  }

  @Test(expected = NullPointerException.class)
  public void emptyAskLevelReturnsNull() {

    assertEquals(
            "NullPointer not thrown for trying to access empty ask level",
        null,
        orderBookManager.getOrdersAtLevel("VOD.L", Side.SELL, 10L));
  }

  @Test
  public void bidOrderAtLevelReturnsOrderWhenAdded() {

    orderBookManager.addOrder(buy1);
    List<Order> expected = new ArrayList<>();
    expected.add(buy1);

    assertEquals(
        "ordersAtLevel bid does not return single order",
        expected,
        orderBookManager.getOrdersAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askOrderAtLevelReturnsOrderWhenAdded() {

    orderBookManager.addOrder(sell1);
    List<Order> expected = new ArrayList<>();
    expected.add(sell1);

    assertEquals(
            "ordersAtLevel ask does not return single order",
        expected,
        orderBookManager.getOrdersAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }

  @Test
  public void bidReturnsMultipleOrdersAtLevelSequentiallyWhenAdded() {

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy2);
    List<Order> expected = new ArrayList<>();
    expected.add(buy1);
    expected.add(buy2);

    assertEquals(
        "orderAtLevel bid does not return multiple orders sequentially on level",
        expected,
        orderBookManager.getOrdersAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askReturnsMultipleOrdersAtLevelSequentiallyWhenAdded() {

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell2);
    List<Order> expected = new ArrayList<>();
    expected.add(sell1);
    expected.add(sell2);

    assertEquals(
            "orderAtLevel ask does not return multiple orders sequentially on level",
        expected,
        orderBookManager.getOrdersAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }

  @Test
  public void bidOrdersAtLevelCreatesSeparateListsForInstruments() {

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buyOther);
    List<Order> expected1 = new ArrayList<>();
    List<Order> expected2 = new ArrayList<>();
    expected1.add(buy1);
    expected2.add(buyOther);

    assertEquals(
        "ordersAtLevel bid does not create new list for different instruments",
        expected2,
        orderBookManager.getOrdersAtLevel(
            buyOther.getInstrument(), buyOther.getSide(), buyOther.getPrice()));
  }

  @Test
  public void askOrdersAtLevelCreatesSeparateListsForInstruments() {

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sellOther);
    List<Order> expected1 = new ArrayList<>();
    List<Order> expected2 = new ArrayList<>();
    expected1.add(sell1);
    expected2.add(sellOther);

    assertEquals(
            "ordersAtLevel ask does not create new list for different instruments",
        expected2,
        orderBookManager.getOrdersAtLevel(
            sellOther.getInstrument(), sellOther.getSide(), sellOther.getPrice()));
  }

  @Test
  public void bidOrdersAtLevelCreatesSeparateListsForPrices() {

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy3);
    List<Order> expected1 = new ArrayList<>();
    List<Order> expected2 = new ArrayList<>();
    expected1.add(buy1);
    expected2.add(buy3);

    assertEquals(
            "ordersAtLevel bid does not create new list for different prices",
        expected2,
        orderBookManager.getOrdersAtLevel(buy3.getInstrument(), buy3.getSide(), buy3.getPrice()));
  }

  @Test
  public void askOrdersAtLevelCreatesSeparateListsForPrices() {

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell3);
    List<Order> expected1 = new ArrayList<>();
    List<Order> expected2 = new ArrayList<>();
    expected1.add(sell1);
    expected2.add(sell3);

    assertEquals(
            "ordersAtLevel ask does not create new list for different prices",
        expected2,
        orderBookManager.getOrdersAtLevel(
            sell3.getInstrument(), sell3.getSide(), sell3.getPrice()));
  }

  @Test
  public void getOrdersAtLevelReturnsForDifferentSides() {

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(sell1);
    List<Order> expected1 = new ArrayList<>();
    List<Order> expected2 = new ArrayList<>();
    expected1.add(buy1);
    expected2.add(sell1);

    assertEquals(
        "getOrdersAtLevel returns different sides in same list",
        expected1,
        orderBookManager.getOrdersAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
    assertEquals(
        "getOrdersAtLevel returns different sides in same list",
        expected2,
        orderBookManager.getOrdersAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }
}
