package com.cfbenchmarks.orderBookManager;

import static org.junit.Assert.*;

import com.cfbenchmarks.order.Order;
import com.cfbenchmarks.order.Side;
import org.junit.Before;
import org.junit.Test;

public class DeleteOrderTest {

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
    buy2 = new Order("order2", "VOD.L", Side.BUY, 200, 10);
    buy3 = new Order("order3", "VOD.L", Side.BUY, 200, 10);

    sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 10);
    sell2 = new Order("order5", "VOD.L", Side.SELL, 200, 10);
    sell3 = new Order("order6", "VOD.L", Side.SELL, 200, 10);

    buyOther = new Order("order7", "AAPL", Side.BUY, 200, 10);
    sellOther = new Order("order8", "AAPL", Side.SELL, 200, 10);

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
  public void bidMustExistInOrderHashMapToDelete() {

    Order buy4 = new Order("order9", "VOD.L", Side.BUY, 200, 10);

    assertFalse(
        "Delete returns true for bid that does not exist in orderHashMap",
        orderBookManager.deleteOrder(buy4.getOrderId()));
  }

  @Test
  public void askMustExistInOrderHashMapToDelete() {

    Order sell4 = new Order("order10", "VOD.L", Side.SELL, 200, 10);

    assertFalse(
        "Delete returns true for ask that does not exist in orderHashMap",
        orderBookManager.deleteOrder(sell4.getOrderId()));
  }

  @Test
  public void deleteBidHeadRemovesFromOrderHashMap() {

    orderBookManager.deleteOrder(buy1.getOrderId());

    assertFalse(
        "Delete a bid from the head does not remove reference in orderHashMap",
        orderBookManager.getOrderHashMap().containsKey(buy1.getOrderId()));
  }

  @Test
  public void deleteBidHeadRemovesFromHeadOrderLinkedList() {

    orderBookManager.deleteOrder(buy1.getOrderId());

    assertEquals(
        "Delete a bid from the head does not update reference in the OrderLinkedList",
        buy2,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getHead()
            .getOrder());
  }

  @Test
  public void deleteAskHeadRemovesFromOrderHashMap() {

    orderBookManager.deleteOrder(sell1.getOrderId());

    assertFalse(
        "Delete an ask from the head does not remove reference in orderHashMap",
        orderBookManager.getOrderHashMap().containsKey(sell1.getOrderId()));
  }

  @Test
  public void deleteAskHeadRemovesFromHeadOrderLinkedList() {

    orderBookManager.deleteOrder(sell1.getOrderId());

    assertEquals(
        "Delete an ask from the head does not remove in the OrderLinkedList",
        sell2,
        orderBookManager
            .getOrderBookHashMap()
            .get(sell2.getInstrument() + sell2.getSide().toString())
            .get(sell2.getPrice())
            .getHead()
            .getOrder());
  }

  @Test
  public void deleteBidBodyRemovesFromOrderHashMap() {

    orderBookManager.deleteOrder(buy2.getOrderId());

    assertFalse(
        "Delete a bid from the body does not remove reference in orderHashMap",
        orderBookManager.getOrderHashMap().containsKey(buy2.getOrderId()));
  }

  @Test
  public void deleteBidBodyRemovesFromOrderLinkedList() {

    orderBookManager.deleteOrder(buy2.getOrderId());

    assertEquals(
        "Delete a bid from the body does not remove in the OrderLinkedList",
        buy3,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy1.getPrice())
            .getHead()
            .getNext()
            .getOrder());
  }

  @Test
  public void deleteAskBodyRemovesFromOrderHashMap() {

    orderBookManager.deleteOrder(sell2.getOrderId());

    assertFalse(
        "Delete an ask from the body does not remove reference in orderHashMap",
        orderBookManager.getOrderHashMap().containsKey(sell2.getOrderId()));
  }

  @Test
  public void deleteAskBodyRemovesFromOrderLinkedList() {

    orderBookManager.deleteOrder(sell2.getOrderId());

    assertEquals(
        "Delete an ask from the body does not remove in the OrderLinkedList",
        sell3,
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString())
            .get(sell1.getPrice())
            .getHead()
            .getNext()
            .getOrder());
  }

  @Test
  public void deleteBidLastRemovesFromOrderHashMap() {

    orderBookManager.deleteOrder(buy3.getOrderId());

    assertFalse(
        "Delete a bid from last does not remove reference in orderHashMap",
        orderBookManager.getOrderHashMap().containsKey(buy3.getOrderId()));
  }

  @Test
  public void deleteBidLastRemovesFromOrderLinkedList() {

    orderBookManager.deleteOrder(buy3.getOrderId());

    assertEquals(
        "Delete a bid from last does not remove in the OrderLinkedList",
        buy2,
        orderBookManager
            .getOrderBookHashMap()
            .get(buy2.getInstrument() + buy2.getSide().toString())
            .get(buy2.getPrice())
            .getLast()
            .getOrder());
  }

  @Test
  public void deleteAskLastRemovesFromOrderHashMap() {

    orderBookManager.deleteOrder(sell3.getOrderId());

    assertFalse(
        "Delete an ask from last does not remove reference in orderHashMap",
        orderBookManager.getOrderHashMap().containsKey(sell3.getOrderId()));
  }

  @Test
  public void deleteAskLastRemovesFromOrderLinkedList() {

    orderBookManager.deleteOrder(sell3.getOrderId());

    assertEquals(
        "Delete an ask from last does not remove in the OrderLinkedList",
        sell2,
        orderBookManager
            .getOrderBookHashMap()
            .get(sell1.getInstrument() + sell1.getSide().toString())
            .get(sell2.getPrice())
            .getLast()
            .getOrder());
  }

  @Test
  public void deleteBidFromLevelWithOneOrderWillRemoveLevelFromOrderBook() {

    orderBookManager.deleteOrder(buyOther.getOrderId());

    assertEquals(
        "Bid level exists without orders when its only order was deleted",
        null,
        orderBookManager
            .getOrderBookHashMap()
            .get(buyOther.getInstrument() + buyOther.getSide().toString())
            .get(buyOther.getPrice()));
  }

  @Test
  public void deleteAskFromLevelWithOneOrderWillRemoveLevelFromOrderBook() {

    orderBookManager.deleteOrder(sellOther.getOrderId());

    assertEquals(
        "Ask level exists without orders when its only order was deleted",
        orderBookManager
            .getOrderBookHashMap()
            .get(sellOther.getInstrument() + sellOther.getSide().toString())
            .get(sellOther.getPrice()),
        null);
  }
}
