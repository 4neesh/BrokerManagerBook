package com.cfbenchmarks.orderBook;

import com.cfbenchmarks.interview.Order;

import java.util.Comparator;
import java.util.TreeMap;

public class BidBook extends TreeMap<Long, OrderLinkedList> {


    @Override
    public Comparator<? super Long> comparator() {
        return super.comparator();
    }



    public void addOrder(Long key, OrderNode order){

        if(super.get(key) == null){
            super.put(key, new OrderLinkedList(order.order));
        }
        else{
            super.get(key).append(order);
        }


    }

    @Override
    public OrderLinkedList remove(Object key) {
        return super.remove(key);
    }


    public void removeOrder(String orderId, long orderPrice) {

        if(super.get(orderPrice).head.next == null){
            remove(orderId);
        }
        else{
            super.get(orderPrice).removeNode(orderId);
        }

    }

//    public void removeOrder(String orderId, Long price) {
//
//        if(super.get(orderId).head.next == null){
//            remove(orderId);
//        }
//        else{
//            super.get(orderId).removeNode(orderId);
//        }
//
//
//    }


}
