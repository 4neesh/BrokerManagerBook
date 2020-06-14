package com.cfbenchmarks.orderBook;

import com.cfbenchmarks.interview.Order;
import com.cfbenchmarks.interview.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InstrumentProperty {

    public InstrumentProperty(Optional<Long> bestPrice, long numberOfOrders, long quantity, long volume, List<Order> ordersAtLevel) {
        this.bestPrice = bestPrice;
        this.numberOfOrders = numberOfOrders;
        this.quantity = quantity;
        this.volume = volume;
        this.ordersAtLevel = ordersAtLevel;
    }

    private Optional<Long> bestPrice;

    private long numberOfOrders ;

    private long quantity;

    private long volume ;

    private List<Order> ordersAtLevel;

    public void updateProperties(Order order){

        if(order.getSide() == Side.BUY){
            
        }

    }

    public Optional<Long> getBestPrice() {
        return bestPrice;
    }

    public void setBestPrice(Optional<Long> bestPrice) {
        this.bestPrice = bestPrice;
    }

    public long getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(long numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public List<Order> getOrdersAtLevel() {
        return ordersAtLevel;
    }

    public void setOrdersAtLevel(List<Order> ordersAtLevel) {
        this.ordersAtLevel = ordersAtLevel;
    }
}
