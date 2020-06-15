package com.cfbenchmarks.orderBook;

import java.util.Comparator;

public class BidBook extends OrderBook {

  public BidBook() {

    super(Comparator.reverseOrder());
  }
}
