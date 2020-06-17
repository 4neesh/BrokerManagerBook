package com.cfbenchmarks.order;

import org.junit.Test;

public class OrderConstructorTest {

  @Test(expected = NullPointerException.class)
  public void orderIdCannotBeNull() {

    Order order1 = new Order(null, "VOD.L", Side.BUY, 200, 10);
  }

  @Test(expected = NullPointerException.class)
  public void instrumentCannotBeNull() {

    Order order1 = new Order("001", null, Side.BUY, 200, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void priceMustBeGreaterThanZero() {

    Order order1 = new Order("001", "VOD.L", Side.BUY, -200, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void quantityMustBeGreaterThanZero() {

    Order order1 = new Order("001", "VOD.L", Side.BUY, 200, -10);
  }
}
