package com.cfbenchmarks.interview;


import com.cfbenchmarks.orderBook.AskBook;
import com.cfbenchmarks.orderBook.BidBook;
import com.cfbenchmarks.orderBook.InstrumentProperty;
import com.cfbenchmarks.orderBook.OrderNode;

import java.util.*;

public class OrderBookManagerImpl implements OrderBookManager {

    HashMap<String, Long> askLookup = new HashMap<>();
    HashMap<String, Long> bidLookup = new HashMap<>();

    HashMap<String, InstrumentProperty> askInstruments = new HashMap<>();
    HashMap<String, InstrumentProperty> bidInstruments = new HashMap<>();


    AskBook askBook;
    BidBook bidBook;

    public OrderBookManagerImpl(AskBook askBook, BidBook bidBook){

        this.askBook = askBook;
        this.bidBook = bidBook;

    }


    public void addOrder(Order order) {

       if(order.getSide() == Side.BUY){
           bidLookup.put(order.getOrderId(), order.getPrice());
           bidBook.addOrder(order.getPrice(), new OrderNode(order));
       }
       else{
           askLookup.put(order.getOrderId(), order.getPrice());
           askBook.addOrder(order.getPrice(), new OrderNode(order));
       }

    }

    public boolean modifyOrder(String orderId, long newQuantity) {

        if(bidLookup.containsKey(orderId)){

            long orderPrice = bidLookup.get(orderId);
            bidBook.modifyOrder(orderId, newQuantity, orderPrice);

            return true;
        }
        else if(askLookup.containsKey(orderId)){

            long orderPrice = askLookup.get(orderId);
            askBook.modifyOrder(orderId, newQuantity, orderPrice);


            return true;

        }
        else{

            return false;
        }
    }

    public boolean deleteOrder(String orderId) {

        if(bidLookup.containsKey(orderId)){

            long orderPrice = bidLookup.get(orderId);
            bidBook.removeOrder(orderId, orderPrice);
            bidLookup.remove(orderId);

            return true;
        }
        else if(askLookup.containsKey(orderId)){

            long orderPrice = askLookup.get(orderId);
            askBook.removeOrder(orderId, orderPrice);
            askLookup.remove(orderId);

            return true;
        }
        else{
            return false;
        }
    }

    public Optional<Long> getBestPrice(String instrument, Side side) {


        return Optional.empty();
    }

    public long getOrderNumAtLevel(String instrument, Side side, long price) {



        return 0;
    }

    public long getTotalQuantityAtLevel(String instrument, Side side, long price) {


        return 0;
    }

    public long getTotalVolumeAtLevel(String instrument, Side side, long price) {


        return 0;
    }

    public List<Order> getOrdersAtLevel(String instrument, Side side, long price) {



        return null;
    }
}
