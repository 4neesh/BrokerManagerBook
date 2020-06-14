package com.cfbenchmarks.orderBook;

import com.cfbenchmarks.interview.Order;

import java.util.List;

public class LevelProperty {


    public LevelProperty(long numberOfOrders, long quantity, long volume, List<Order> ordersAtLevel) {
        this.numberOfOrders = numberOfOrders;
        this.quantity = quantity;
        this.volume = volume;
        this.ordersAtLevel = ordersAtLevel;
    }

    private long numberOfOrders;

    private long quantity;

    private long volume;

    private List<Order> ordersAtLevel;


    public long getNumberOfOrders() {
        return numberOfOrders;
    }

    public long getQuantity() {
        return quantity;
    }

    public long getVolume() {
        return volume;
    }

    public List<Order> getOrdersAtLevel() {
        return ordersAtLevel;
    }

    public void updateLevelProperties(Order order){
        this.numberOfOrders += 1;
        this.ordersAtLevel.add(order);
        this.quantity += order.getQuantity();
        this.volume += (order.getQuantity() + order.getPrice());
    }


}
