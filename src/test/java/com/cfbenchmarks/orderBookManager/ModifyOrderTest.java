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
  public void orderMustExistInOrderLinkedListToModify() {

    Order buy4 = new Order("order7", "VOD.L", Side.BUY, 200, 10);

    assertFalse(
        "Returning modification for order not in the orderBook",
        orderBookManager.modifyOrder(buy4.getOrderId(), 10));
  }

  @Test
  public void lastRemainsAfterModifyOrderBeforeEndToLower() {

    long newQuantity = 7;

    orderBookManager.modifyOrder(buy2.getOrderId(), newQuantity);

    assertEquals(
        "Last is changed when bid before end is lowered",
        buy3,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getLast()
            .getOrder());
  }

  @Test
  public void quantityUpdatedAfterModifyOrderBeforeEndToLower() {

    long newQuantity = 7;

    orderBookManager.modifyOrder(buy2.getOrderId(), newQuantity);

    assertEquals(
        "New quantity is not updated when bid before end is lowered",
        newQuantity,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getHead()
            .getNext()
            .getOrder()
            .getQuantity());
  }

  @Test
  public void lastRemainsWhenOrderAtEndModifiedToLower() {

    long newQuantity = 7;

    orderBookManager.modifyOrder(buy3.getOrderId(), newQuantity);

    assertEquals(
        "Last in OrderLinkedList is changed when bid at end modified to lower",
        buy3,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getLast()
            .getOrder());
  }

  @Test
  public void quantityUpdatedWhenOrderAtEndModifiedToLower() {

    long newQuantity = 7;

    orderBookManager.modifyOrder(buy3.getOrderId(), newQuantity);

    assertEquals(
        "Quantity is not updated when bid at end of OrderLinkedList is changed",
        newQuantity,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getLast()
            .getOrder()
            .getQuantity());
  }

  @Test
  public void lastUpdatedWhenOrderBeforeEndModifiedToHigher() {

    long newQuantity = 13;

    orderBookManager.modifyOrder(buy2.getOrderId(), newQuantity);

    assertEquals(
        "Order in body of OrderLinkedList not moved to end when quantity increased",
        buy2,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getLast()
            .getOrder());
  }

  @Test
  public void quantityUpdatedWhenOrderBeforeEndModifiedToHigher() {

    long newQuantity = 13;

    orderBookManager.modifyOrder(buy2.getOrderId(), newQuantity);

    assertEquals(
        "Quantity is not updated when body node in OrderLinkedList is modified to higher",
        newQuantity,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getLast()
            .getOrder()
            .getQuantity());
  }

  @Test
  public void lastUpdatedWhenOrderAtEndModifiedToHigher() {

    long newQuantity = 13;

    orderBookManager.modifyOrder(buy3.getOrderId(), newQuantity);

    assertEquals(
        "Order in body of OrderLinkedList not moved to end when quantity increased",
        buy3,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getLast()
            .getOrder());
  }

  @Test
  public void quantityUpdatedWhenOrderAtEndModifiedToHigher() {

    long newQuantity = 13;

    orderBookManager.modifyOrder(buy3.getOrderId(), newQuantity);

    assertEquals(
        "Quantity is not updated when end node in OrderLinkedList is modified to higher",
        newQuantity,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getLast()
            .getOrder()
            .getQuantity());
  }
}
