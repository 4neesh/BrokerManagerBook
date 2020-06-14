package com.cfbenchmarks.interview;

import com.cfbenchmarks.orderBook.*;

import java.util.*;

public class OrderBookManagerImpl implements OrderBookManager {

    HashMap<String, Long> askLookup = new HashMap<>();
    HashMap<String, Long> bidLookup = new HashMap<>();

    HashMap<String, InstrumentProperty> instrumentPropertyMap;

    AskBook askBook;
    BidBook bidBook;

    public OrderBookManagerImpl(
            AskBook askBook, BidBook bidBook, HashMap<String, InstrumentProperty> instrumentPropertyMap) {

        this.askBook = askBook;
        this.bidBook = bidBook;
        this.instrumentPropertyMap = instrumentPropertyMap;
    }

    public void addOrder(Order order) {

        String propertyKey;
        if (order.getSide() == Side.BUY) {
            bidLookup.put(order.getOrderId(), order.getPrice());
            bidBook.addOrder(order.getPrice(), new OrderNode(order));

            propertyKey = order.getInstrument() + Side.BUY.toString();



        } else {
            askLookup.put(order.getOrderId(), order.getPrice());
            askBook.addOrder(order.getPrice(), new OrderNode(order));
            propertyKey = order.getInstrument() + Side.SELL.toString();
        }

        if (instrumentPropertyMap.containsKey(propertyKey)) {

            instrumentPropertyMap.get(propertyKey).updateProperties(order);

        } else {
            List<Order> newOrderList = new ArrayList<>();
            newOrderList.add(order);
            Optional<Long> bestPrice = Optional.of(order.getPrice());
            HashMap<String, LevelProperties> levelPropertiesHashMap = new HashMap<>();

            instrumentPropertyMap.put(
                    propertyKey,
                    new InstrumentProperty(bestPrice, levelPropertiesHashMap));
        }

    }

    public boolean modifyOrder(String orderId, long newQuantity) {

        if (bidLookup.containsKey(orderId)) {

            long orderPrice = bidLookup.get(orderId);
            bidBook.modifyOrder(orderId, newQuantity, orderPrice);

            return true;
        } else if (askLookup.containsKey(orderId)) {

            long orderPrice = askLookup.get(orderId);
            askBook.modifyOrder(orderId, newQuantity, orderPrice);

            return true;

        } else {

            return false;
        }
    }

    public boolean deleteOrder(String orderId) {

        if (bidLookup.containsKey(orderId)) {

            long orderPrice = bidLookup.get(orderId);
            bidBook.removeOrder(orderId, orderPrice);
            bidLookup.remove(orderId);

            return true;
        } else if (askLookup.containsKey(orderId)) {

            long orderPrice = askLookup.get(orderId);
            askBook.removeOrder(orderId, orderPrice);
            askLookup.remove(orderId);

            return true;
        } else {
            return false;
        }
    }

    public Optional<Long> getBestPrice(String instrument, Side side) {

        String propertiesKey = instrument + side.toString();

        return instrumentPropertyMap.get(propertiesKey).getBestPrice();
    }

    public long getOrderNumAtLevel(String instrument, Side side, long price) {

        String propertiesKey = instrument + side.toString();
        String levelPropertiesKey = propertiesKey + price;
        return instrumentPropertyMap.get(propertiesKey).getLevelPropertiesHashMap().get(levelPropertiesKey).getNumberOfOrders();
    }

    public long getTotalQuantityAtLevel(String instrument, Side side, long price) {
        String propertiesKey = instrument + side.toString();
        String levelPropertiesKey = propertiesKey + price;
        return instrumentPropertyMap.get(propertiesKey).getLevelPropertiesHashMap().get(levelPropertiesKey).getQuantity();

    }

    public long getTotalVolumeAtLevel(String instrument, Side side, long price) {

        String propertiesKey = instrument + side.toString();
        String levelPropertiesKey = propertiesKey + price;
        return instrumentPropertyMap.get(propertiesKey).getLevelPropertiesHashMap().get(levelPropertiesKey).getVolume();

    }

    public List<Order> getOrdersAtLevel(String instrument, Side side, long price) {

        String propertiesKey = instrument + side.toString();
        String levelPropertiesKey = propertiesKey + price;
        return instrumentPropertyMap.get(propertiesKey).getLevelPropertiesHashMap().get(levelPropertiesKey).getOrdersAtLevel();

    }

    public static void main(String[] args) {

        AskBook askBook = new AskBook();
        BidBook bidBook = new BidBook();
        HashMap<String, InstrumentProperty> instrumentPropertyHashMap = new HashMap<>();

        OrderBookManagerImpl orderBookManager =
                new OrderBookManagerImpl(askBook, bidBook, instrumentPropertyHashMap);
        Order buy1 = new Order("order1", "VOD.L", Side.BUY, 200, 10);
        Order buy2 = new Order("order2", "VOD.L", Side.BUY, 100, 10);
        Order buy3 = new Order("order3", "VOD.L", Side.BUY, 40, 10);
        Order sell1 = new Order("order4", "VOD.L", Side.SELL, 200, 10);
        Order sell2 = new Order("order5", "VOD.L", Side.SELL, 100, 10);
        Order sell3 = new Order("order6", "VOD.L", Side.SELL, 40, 10);

        orderBookManager.addOrder(buy1);
        orderBookManager.addOrder(buy2);
        orderBookManager.addOrder(buy3);
        orderBookManager.addOrder(sell1);
        orderBookManager.addOrder(sell2);
        orderBookManager.addOrder(sell3);

        List<OrderLinkedList> askListOfLevels = new ArrayList<>(orderBookManager.askBook.values());
        List<OrderLinkedList> bidListOfLevels = new ArrayList<>(orderBookManager.bidBook.values());

        System.out.println("ask levels:");
        for (OrderLinkedList level : askListOfLevels) {
            System.out.println(level.head.order.getPrice());
        }

        System.out.println("\nbid levels:");
        for (OrderLinkedList level : bidListOfLevels) {
            System.out.println(level.head.order.getPrice());
        }
    }
}
