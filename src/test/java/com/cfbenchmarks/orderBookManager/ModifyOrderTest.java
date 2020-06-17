package com.cfbenchmarks.orderBookManager;

import static org.junit.Assert.*;

import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import org.junit.Before;
import org.junit.Test;

public class ModifyOrderTest {

  private static OrderBookManagerImpl orderBookManager;

  private static Order buy1;
  private static Order buy2;
  private static Order buy3;
  private static Order sell1;
  private static Order sell2;
  private static Order sell3;

  @Before
  public void setUp() {

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
  public void bidMustExistInOrderBookToModify() {

    Order buy4 = new Order("order7", "VOD.L", Side.BUY, 200, 10);

    assertFalse(
        "modifyOrder returns true for bid not in the order book",
        orderBookManager.modifyOrder(buy4.getOrderId(), 10));
  }

  @Test
  public void lastRemainsAfterModifyBidBeforeEndToLower() {

    long newQuantity = 7;

    orderBookManager.modifyOrder(buy2.getOrderId(), newQuantity);

    assertEquals(
        "Last in OrderLinkedList is changed when bid before end is lowered",
        buy3,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getLast()
            .getOrder());
  }

  @Test
  public void quantityUpdatedAfterModifyBidBeforeEndToLower() {

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
  public void lastRemainsWhenBidAtEndModifiedToLower() {

    long newQuantity = 7;

    orderBookManager.modifyOrder(buy3.getOrderId(), newQuantity);

    assertEquals(
        "Last order in OrderLinkedList is changed when bid quantity at end modified to lower",
        buy3,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getLast()
            .getOrder());
  }

  @Test
  public void quantityUpdatedWhenBidAtEndModifiedToLower() {

    long newQuantity = 7;

    orderBookManager.modifyOrder(buy3.getOrderId(), newQuantity);

    assertEquals(
        "Quantity is not updated when bid at end of OrderLinkedList is lowered",
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
  public void lastUpdatedWhenBidBeforeEndModifiedToHigher() {

    long newQuantity = 13;

    orderBookManager.modifyOrder(buy2.getOrderId(), newQuantity);

    assertEquals(
        "bid in body of OrderLinkedList not moved to end when quantity increased",
        buy2,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getLast()
            .getOrder());
  }

  @Test
  public void quantityUpdatedWhenBidBeforeEndModifiedToHigher() {

    long newQuantity = 13;

    orderBookManager.modifyOrder(buy2.getOrderId(), newQuantity);

    assertEquals(
        "Quantity is not updated when bid in body OrderLinkedList is modified to higher",
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
  public void lastUpdatedWhenBidAtEndModifiedToHigher() {

    long newQuantity = 13;

    orderBookManager.modifyOrder(buy3.getOrderId(), newQuantity);

    assertEquals(
        "bid in body of OrderLinkedList not moved to end when quantity increased",
        buy3,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getLast()
            .getOrder());
  }

  @Test
  public void quantityUpdatedWhenBidAtEndModifiedToHigher() {

    long newQuantity = 13;

    orderBookManager.modifyOrder(buy3.getOrderId(), newQuantity);

    assertEquals(
        "Quantity is not updated when end bid node in OrderLinkedList is modified to higher",
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
  public void askMustExistInOrderBookToModify() {

    Order sell4 = new Order("order7", "VOD.L", Side.SELL, 200, 10);

    assertFalse(
        "modifyOrder returns true for ask not in the order book",
        orderBookManager.modifyOrder(sell4.getOrderId(), 10));
  }

  @Test
  public void lastRemainsAfterModifyAskBeforeEndToLower() {

    long newQuantity = 7;

    orderBookManager.modifyOrder(sell2.getOrderId(), newQuantity);

    assertEquals(
        "Last in OrderLinkedList is changed when ask before end is lowered",
        sell3,
        orderBookManager
            .getOrderBookHashMap()
            .get(sell2.getInstrument() + sell2.getSide().toString())
            .get(sell2.getPrice())
            .getLast()
            .getOrder());
  }

  @Test
  public void quantityUpdatedAfterModifyAskBeforeEndToLower() {

    long newQuantity = 7;

    orderBookManager.modifyOrder(sell2.getOrderId(), newQuantity);

    assertEquals(
        "New quantity is not updated when ask before end is lowered",
        newQuantity,
        orderBookManager
            .getOrderBookHashMap()
            .get(sell2.getInstrument() + sell2.getSide().toString())
            .get(sell2.getPrice())
            .getHead()
            .getNext()
            .getOrder()
            .getQuantity());
  }

  @Test
  public void lastRemainsWhenAskAtEndModifiedToLower() {

    long newQuantity = 7;

    orderBookManager.modifyOrder(sell3.getOrderId(), newQuantity);

    assertEquals(
        "Last order in OrderLinkedList is changed when ask quantity at end modified to lower",
        sell3,
        orderBookManager
            .getOrderBookHashMap()
            .get(sell2.getInstrument() + sell2.getSide().toString())
            .get(sell2.getPrice())
            .getLast()
            .getOrder());
  }

  @Test
  public void quantityUpdatedWhenAskAtEndModifiedToLower() {

    long newQuantity = 7;

    orderBookManager.modifyOrder(sell3.getOrderId(), newQuantity);

    assertEquals(
        "Quantity is not updated when ask at end of OrderLinkedList is lowered",
        newQuantity,
        orderBookManager
            .getOrderBookHashMap()
            .get(sell2.getInstrument() + sell2.getSide().toString())
            .get(sell2.getPrice())
            .getLast()
            .getOrder()
            .getQuantity());
  }

  @Test
  public void lastUpdatedWhenAskBeforeEndModifiedToHigher() {

    long newQuantity = 13;

    orderBookManager.modifyOrder(sell2.getOrderId(), newQuantity);

    assertEquals(
        "ask in body of OrderLinkedList not moved to end when quantity increased",
        sell2,
        orderBookManager
            .getOrderBookHashMap()
            .get(sell2.getInstrument() + sell2.getSide().toString())
            .get(sell2.getPrice())
            .getLast()
            .getOrder());
  }

  @Test
  public void quantityUpdatedWhenAskBeforeEndModifiedToHigher() {

    long newQuantity = 13;

    orderBookManager.modifyOrder(sell2.getOrderId(), newQuantity);

    assertEquals(
        "Quantity is not updated when ask in body OrderLinkedList is modified to higher",
        newQuantity,
        orderBookManager
            .getOrderBookHashMap()
            .get(sell2.getInstrument() + sell2.getSide().toString())
            .get(sell2.getPrice())
            .getLast()
            .getOrder()
            .getQuantity());
  }

  @Test
  public void lastUpdatedWhenAskAtEndModifiedToHigher() {

    long newQuantity = 13;

    orderBookManager.modifyOrder(sell3.getOrderId(), newQuantity);

    assertEquals(
        "ask in body of OrderLinkedList not moved to end when quantity increased",
        sell3,
        orderBookManager
            .getOrderBookHashMap()
            .get(sell2.getInstrument() + sell2.getSide().toString())
            .get(sell2.getPrice())
            .getLast()
            .getOrder());
  }

  @Test
  public void quantityUpdatedWhenAskAtEndModifiedToHigher() {

    long newQuantity = 13;

    orderBookManager.modifyOrder(sell3.getOrderId(), newQuantity);

    assertEquals(
        "Quantity is not updated when end ask node in OrderLinkedList is modified to higher",
        newQuantity,
        orderBookManager
            .getOrderBookHashMap()
            .get(sell2.getInstrument() + sell2.getSide().toString())
            .get(sell2.getPrice())
            .getLast()
            .getOrder()
            .getQuantity());
  }
}
