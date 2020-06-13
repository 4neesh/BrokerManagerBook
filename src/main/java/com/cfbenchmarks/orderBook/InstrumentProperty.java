package com.cfbenchmarks.orderBook;

import com.cfbenchmarks.interview.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InstrumentProperty {

    Optional<Long> bestPrice;

    long numberOfOrders = 0;

    long quantity = 0;

    long volume = 0;

    List<Order> ordersAtLevel = new ArrayList<>();


}
