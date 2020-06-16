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
    buy2 = new Order("order2", "VOD.L", Side.BUY, 200, 10);
    buy3 = new Order("order3", "VOD.L", Side.BUY, 50, 10);
    sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 10);
    sell1SamePrice = new Order("order10", "VOD.L", Side.SELL, 200, 10);
    sell2 = new Order("order5", "VOD.L", Side.SELL, 200, 10);
    sell3 = new Order("order6", "VOD.L", Side.SELL, 50, 10);
    buyOther = new Order("order7", "APPL", Side.BUY, 200, 10);
    sellOther = new Order("order8", "APPL", Side.SELL, 200, 10);
  }

  @Test(expected = NullPointerException.class)
  public void emptyBidLevelReturnsNull() {

    assertEquals(
        "Bid book level is not empty",
        null,
        orderBookManager.getOrdersAtLevel("VOD.L", Side.BUY, 10L));
  }

  @Test(expected = NullPointerException.class)
  public void emptyAskLevelReturnsNull() {

    assertEquals(
        "Ask book level is not empty",
        null,
        orderBookManager.getOrdersAtLevel("VOD.L", Side.SELL, 10L));
  }

  @Test
  public void bidReturnsOrderWhenAdded() {

    orderBookManager.addOrder(buy1);
    List<Order> expected = new ArrayList<>();
    expected.add(buy1);

    assertEquals(
        "Bid book does not return orders on level",
        expected,
        orderBookManager.getOrdersAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askReturnsOrderWhenAdded() {

    orderBookManager.addOrder(sell1);
    List<Order> expected = new ArrayList<>();
    expected.add(sell1);

    assertEquals(
        "Ask book does not return orders on level",
        expected,
        orderBookManager.getOrdersAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }

  @Test
  public void bidReturnsMultipleOrdersSequentiallyWhenAdded() {

    assertEquals("buy1 and buy2 have different prices", buy1.getPrice(), buy2.getPrice());
    assertEquals(
        "buy1 and buy2 have different instrument", buy1.getInstrument(), buy2.getInstrument());
    assertEquals("buy1 and buy2 have different side", buy1.getSide(), buy2.getSide());

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy2);
    List<Order> expected = new ArrayList<>();
    expected.add(buy1);
    expected.add(buy2);

    assertEquals(
        "Bid book does not return multiple orders on level",
        expected,
        orderBookManager.getOrdersAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
  }

  @Test
  public void askReturnsMultipleOrdersWhenAdded() {

    assertEquals("sell1 and sell2 have different prices", sell1.getPrice(), sell2.getPrice());
    assertEquals(
        "sell1 and sell2 have different instrument", sell1.getInstrument(), sell2.getInstrument());
    assertEquals("sell1 and sell2 have different side", sell1.getSide(), sell2.getSide());

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell2);
    List<Order> expected = new ArrayList<>();
    expected.add(sell1);
    expected.add(sell2);

    assertEquals(
        "Ask book does not return multiple orders on level",
        expected,
        orderBookManager.getOrdersAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }

  @Test
  public void bidCreatesSeparateListsForInstruments() {

    assertEquals("buy1 and buyOther have different prices", buy1.getPrice(), buyOther.getPrice());
    assertEquals(
        "buy1 and buyOther have same instrument",
        buy1.getInstrument().equals(buyOther.getInstrument()),
        false);
    assertEquals("buy1 and buyOther have different side", buy1.getSide(), buyOther.getSide());

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buyOther);
    List<Order> expected1 = new ArrayList<>();
    List<Order> expected2 = new ArrayList<>();
    expected1.add(buy1);
    expected2.add(buyOther);

    assertEquals(
        "Bid book returns different instruments in same list",
        expected1,
        orderBookManager.getOrdersAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
    assertEquals(
        "Bid book returns different instruments in same list",
        expected2,
        orderBookManager.getOrdersAtLevel(
            buyOther.getInstrument(), buyOther.getSide(), buyOther.getPrice()));
  }

  @Test
  public void askCreatesSeparateListsForInstruments() {

    assertEquals("sell1 and sell2 have different prices", sell1.getPrice(), sellOther.getPrice());
    assertEquals(
        "sell1 and sell2 have same instrument",
        sell1.getInstrument().equals(sellOther.getInstrument()),
        false);
    assertEquals("sell1 and sell2 have different side", sell1.getSide(), sellOther.getSide());

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sellOther);
    List<Order> expected1 = new ArrayList<>();
    List<Order> expected2 = new ArrayList<>();
    expected1.add(sell1);
    expected2.add(sellOther);

    assertEquals(
        "Ask book returns different instruments in same list",
        expected1,
        orderBookManager.getOrdersAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
    assertEquals(
        "Ask book returns different instruments in same list",
        expected2,
        orderBookManager.getOrdersAtLevel(
            sellOther.getInstrument(), sellOther.getSide(), sellOther.getPrice()));
  }

  @Test
  public void bidCreatesSeparateListsForPrices() {

    assertEquals("buy1 and buy3 have same prices", buy1.getPrice() == buy3.getPrice(), false);
    assertEquals(
        "buy1 and buy3 have different instruments", buy1.getInstrument(), buy3.getInstrument());
    assertEquals("buy1 and buy3 have different sides", buy1.getSide(), buy3.getSide());

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy3);
    List<Order> expected1 = new ArrayList<>();
    List<Order> expected2 = new ArrayList<>();
    expected1.add(buy1);
    expected2.add(buy3);

    assertEquals(
        "Bid book returns different prices in same list",
        expected1,
        orderBookManager.getOrdersAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
    assertEquals(
        "Bid book returns different prices in same list",
        expected2,
        orderBookManager.getOrdersAtLevel(buy3.getInstrument(), buy3.getSide(), buy3.getPrice()));
  }

  @Test
  public void askCreatesSeparateListsForPrices() {

    assertEquals("sell1 and sell3 have same prices", sell1.getPrice() == sell3.getPrice(), false);
    assertEquals(
        "sell1 and sell3 have different instruments", sell1.getInstrument(), sell3.getInstrument());
    assertEquals("sell1 and sell3 have different sides", sell1.getSide(), sell3.getSide());

    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell3);
    List<Order> expected1 = new ArrayList<>();
    List<Order> expected2 = new ArrayList<>();
    expected1.add(sell1);
    expected2.add(sell3);

    assertEquals(
        "Ask book returns different prices in same list",
        expected1,
        orderBookManager.getOrdersAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
    assertEquals(
        "Ask book returns different prices in same list",
        expected2,
        orderBookManager.getOrdersAtLevel(
            sell3.getInstrument(), sell3.getSide(), sell3.getPrice()));
  }

  @Test
  public void getOrdersAtLevelReturnsForPrices() {
    assertEquals("buy1 and sell1 have different prices", buy1.getPrice(), sell1.getPrice());
    assertEquals(
        "buy1 and sell1 have different instrument",
        buy1.getInstrument().equals(sell1.getInstrument()),
        true);
    assertEquals("buy1 and sell1 have same side", buy1.getSide().equals(sell1.getSide()), false);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(sell1);
    List<Order> expected1 = new ArrayList<>();
    List<Order> expected2 = new ArrayList<>();
    expected1.add(buy1);
    expected2.add(sell1);

    assertEquals(
        "Ask book returns different sides in same list",
        expected1,
        orderBookManager.getOrdersAtLevel(buy1.getInstrument(), buy1.getSide(), buy1.getPrice()));
    assertEquals(
        "Ask book returns different sides in same list",
        expected2,
        orderBookManager.getOrdersAtLevel(
            sell1.getInstrument(), sell1.getSide(), sell1.getPrice()));
  }
}
