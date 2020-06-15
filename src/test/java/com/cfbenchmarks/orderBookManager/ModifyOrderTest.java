package com.cfbenchmarks.orderBookManager;

import static org.junit.Assert.*;

import com.cfbenchmarks.instrumentProperty.InstrumentProperty;
import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;

public class ModifyOrderTest {

  private static OrderBookManagerImpl orderBookManager;
  private static HashMap<String, InstrumentProperty> instrumentPropertyHashMap;

  private static Order buy1;
  private static Order buy2;
  private static Order buy3;
  private static Order sell1;
  private static Order sell2;
  private static Order sell3;

  @Before
  public void setUp() {

    instrumentPropertyHashMap = new HashMap<>();

    orderBookManager = new OrderBookManagerImpl();
    buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
    buy2 = new Order("order2", "VOD.L", Side.BUY, 200, 10);
    buy3 = new Order("order3", "VOD.L", Side.BUY, 200, 10);
    sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 10);
    sell2 = new Order("order5", "VOD.L", Side.SELL, 200, 10);
    sell3 = new Order("order6", "VOD.L", Side.SELL, 200, 10);

    orderBookManager.addOrder(buy1);
    orderBookManager.addOrder(buy2);
    orderBookManager.addOrder(buy3);
    orderBookManager.addOrder(sell1);
    orderBookManager.addOrder(sell2);
    orderBookManager.addOrder(sell3);
  }

  @Test
  public void bidMustExistInBookToModify() {

    Order buy4 = new Order("order7", "VOD.L", Side.BUY, 200, 10);

    assertFalse(orderBookManager.orderHashMap.containsKey(buy4.getOrderId()));

    assertFalse(orderBookManager.modifyOrder(buy4.getOrderId(), 10));
  }

  @Test
  public void askMustExistInBookToModify() {

    Order sell4 = new Order("order7", "VOD.L", Side.BUY, 200, 10);

    assertFalse(orderBookManager.orderHashMap.containsKey(sell4.getOrderId()));

    assertFalse(orderBookManager.modifyOrder(sell4.getOrderId(), 10));
  }

  @Test
  public void modifyBidBeforeEndToLower() {

    long newQuantity = 7;
    assertTrue(
        orderBookManager
                .bidBookHashMap
                .get(buy2.getInstrument())
                .get(buy2.getPrice())
                .head
                .next
                .order
                .getQuantity()
            > newQuantity);
    assertTrue(
        orderBookManager.bidBookHashMap.get(buy2.getInstrument()).get(buy2.getPrice()).last.order
            == buy3);

    orderBookManager.modifyOrder(buy2.getOrderId(), newQuantity);

    assertEquals(
        orderBookManager.bidBookHashMap.get(buy2.getInstrument()).get(buy2.getPrice()).last.order,
        buy3);
    assertEquals(
        orderBookManager
            .bidBookHashMap
            .get(buy2.getInstrument())
            .get(buy2.getPrice())
            .head
            .next
            .order
            .getQuantity(),
        newQuantity);
  }

  @Test
  public void modifyBidAtEndToLower() {

    long newQuantity = 7;
    assertTrue(
        orderBookManager
                .bidBookHashMap
                .get(buy2.getInstrument())
                .get(buy2.getPrice())
                .last
                .order
                .getQuantity()
            > newQuantity);
    assertTrue(
        orderBookManager.bidBookHashMap.get(buy2.getInstrument()).get(buy2.getPrice()).last.order
            == buy3);

    orderBookManager.modifyOrder(buy3.getOrderId(), newQuantity);

    assertEquals(
        orderBookManager.bidBookHashMap.get(buy2.getInstrument()).get(buy2.getPrice()).last.order,
        buy3);
    assertEquals(
        orderBookManager
            .bidBookHashMap
            .get(buy2.getInstrument())
            .get(buy2.getPrice())
            .last
            .order
            .getQuantity(),
        newQuantity);
  }

  @Test
  public void modifyBidBeforeEndToHigher() {

    long newQuantity = 13;
    assertTrue(
        orderBookManager
                .bidBookHashMap
                .get(buy2.getInstrument())
                .get(buy2.getPrice())
                .head
                .next
                .order
                .getQuantity()
            < newQuantity);
    assertTrue(
        orderBookManager.bidBookHashMap.get(buy2.getInstrument()).get(buy2.getPrice()).last.order
            != buy2);

    orderBookManager.modifyOrder(buy2.getOrderId(), newQuantity);

    assertEquals(
        orderBookManager.bidBookHashMap.get(buy2.getInstrument()).get(buy2.getPrice()).last.order,
        buy2);
    assertEquals(
        orderBookManager
            .bidBookHashMap
            .get(buy2.getInstrument())
            .get(buy2.getPrice())
            .last
            .order
            .getQuantity(),
        newQuantity);
  }

  @Test
  public void modifyBidAtEndToHigher() {

    long newQuantity = 13;
    // need to assert that there are 3 items in the level
    assertTrue(
        orderBookManager
                .bidBookHashMap
                .get(buy2.getInstrument())
                .get(buy2.getPrice())
                .last
                .order
                .getQuantity()
            < newQuantity);
    assertTrue(
        orderBookManager.bidBookHashMap.get(buy2.getInstrument()).get(buy2.getPrice()).last.order
            == buy3);

    orderBookManager.modifyOrder(buy3.getOrderId(), newQuantity);

    assertEquals(
        orderBookManager
            .bidBookHashMap
            .get(buy2.getInstrument())
            .get(buy2.getPrice())
            .head
            .next
            .order,
        buy2);
    assertEquals(
        orderBookManager
            .bidBookHashMap
            .get(buy2.getInstrument())
            .get(buy2.getPrice())
            .last
            .order
            .getQuantity(),
        newQuantity);
  }

  @Test
  public void modifyAskBeforeEndToLower() {

    long newQuantity = 7;
    assertTrue(
        orderBookManager
                .askBookHashMap
                .get(sell2.getInstrument())
                .get(sell2.getPrice())
                .head
                .next
                .order
                .getQuantity()
            > newQuantity);
    assertTrue(
        orderBookManager.askBookHashMap.get(sell2.getInstrument()).get(sell2.getPrice()).last.order
            == sell3);

    orderBookManager.modifyOrder(sell2.getOrderId(), newQuantity);

    assertEquals(
        orderBookManager.askBookHashMap.get(sell2.getInstrument()).get(sell2.getPrice()).last.order,
        sell3);
    assertEquals(
        orderBookManager
            .askBookHashMap
            .get(sell2.getInstrument())
            .get(sell2.getPrice())
            .head
            .next
            .order
            .getQuantity(),
        newQuantity);
  }

  @Test
  public void modifyAskAtEndToLower() {

    long newQuantity = 7;
    assertTrue(
        orderBookManager
                .askBookHashMap
                .get(sell2.getInstrument())
                .get(sell2.getPrice())
                .last
                .order
                .getQuantity()
            > newQuantity);
    assertTrue(
        orderBookManager.askBookHashMap.get(sell2.getInstrument()).get(sell2.getPrice()).last.order
            == sell3);

    orderBookManager.modifyOrder(sell3.getOrderId(), newQuantity);

    assertEquals(
        orderBookManager.askBookHashMap.get(sell2.getInstrument()).get(sell2.getPrice()).last.order,
        sell3);
    assertEquals(
        orderBookManager
            .askBookHashMap
            .get(sell2.getInstrument())
            .get(sell2.getPrice())
            .last
            .order
            .getQuantity(),
        newQuantity);
  }

  @Test
  public void modifyAskBeforeEndToHigher() {

    long newQuantity = 13;
    assertTrue(
        orderBookManager
                .askBookHashMap
                .get(sell2.getInstrument())
                .get(sell2.getPrice())
                .head
                .next
                .order
                .getQuantity()
            < newQuantity);
    assertTrue(
        orderBookManager.askBookHashMap.get(sell2.getInstrument()).get(sell2.getPrice()).last.order
            != sell2);

    orderBookManager.modifyOrder(sell2.getOrderId(), newQuantity);

    assertEquals(
        orderBookManager.askBookHashMap.get(sell2.getInstrument()).get(sell2.getPrice()).last.order,
        sell2);
    assertEquals(
        orderBookManager
            .askBookHashMap
            .get(sell2.getInstrument())
            .get(sell2.getPrice())
            .last
            .order
            .getQuantity(),
        newQuantity);
  }

  @Test
  public void modifyAskAtEndToHigher() {

    long newQuantity = 13;
    // need to assert that there are 3 items in the level
    assertTrue(
        orderBookManager
                .askBookHashMap
                .get(sell2.getInstrument())
                .get(sell2.getPrice())
                .last
                .order
                .getQuantity()
            < newQuantity);
    assertTrue(
        orderBookManager.askBookHashMap.get(sell2.getInstrument()).get(sell2.getPrice()).last.order
            == sell3);

    orderBookManager.modifyOrder(sell3.getOrderId(), newQuantity);

    assertEquals(
        orderBookManager
            .askBookHashMap
            .get(sell2.getInstrument())
            .get(sell2.getPrice())
            .last
            .order
            .getQuantity(),
        newQuantity);
  }
}
