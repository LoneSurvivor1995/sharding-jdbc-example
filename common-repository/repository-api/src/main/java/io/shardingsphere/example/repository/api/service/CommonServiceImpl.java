package io.shardingsphere.example.repository.api.service;

import io.shardingsphere.example.repository.api.entity.Order;
import io.shardingsphere.example.repository.api.entity.OrderItem;
import io.shardingsphere.example.repository.api.repository.OrderItemRepository;
import io.shardingsphere.example.repository.api.repository.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonServiceImpl implements CommonService {
    
    @Override
    public void initEnvironment() {
        getOrderRepository().createTableIfNotExists();
        getOrderItemRepository().createTableIfNotExists();
        getOrderRepository().truncateTable();
        getOrderItemRepository().truncateTable();
    }
    
    @Override
    public void cleanEnvironment() {
        getOrderItemRepository().dropTable();
        getOrderItemRepository().dropTable();
    }
    
    @Transactional
    @Override
    public void processSuccess() {
        System.out.println("-------------- Process Success Begin ---------------");
        List<Long> orderIds = insertData();
        printData();
        deleteData(orderIds);
        printData();
        System.out.println("-------------- Process Success Finish --------------");
    }
    
    @Transactional
    @Override
    public void processFailure() {
        System.out.println("-------------- Process Failure Begin ---------------");
        insertData();
        System.out.println("-------------- Process Failure Finish --------------");
        throw new RuntimeException("Exception occur for transaction test.");
    }
    
    private List<Long> insertData() {
        System.out.println("---------------------------- Insert Data ----------------------------");
        List<Long> result = new ArrayList<>(10);
        for (int i = 1; i <= 20; i++) {
            Order order = newOrder();
            order.setOrderId(i);
            order.setUserId(i);
            order.setStatus("INSERT_TEST");
            System.out.println(order);
            getOrderRepository().insert(order);
//            OrderItem item = newOrderItem();
//            item.setOrderId(order.getOrderId());
//            item.setUserId(i);
//            item.setStatus("INSERT_TEST");
            result.add(order.getOrderId());
        }

        for (int i = 1; i <= 20; i++) {
            Order order = newOrder();
            order.setOrderId(i+1);
            order.setUserId(i);
            order.setStatus("INSERT_TEST");
            System.out.println(order);
            getOrderRepository().insert(order);
//            OrderItem item = newOrderItem();
//            item.setOrderId(order.getOrderId());
//            item.setUserId(i);
//            item.setStatus("INSERT_TEST");
            result.add(order.getOrderId());
        }
        return result;
    }
    
    private void deleteData(final List<Long> orderIds) {
        System.out.println("---------------------------- Delete Data ----------------------------");
        for (Long each : orderIds) {
            getOrderRepository().delete(each);
//            getOrderItemRepository().delete(each);
        }
    }
    
    @Override
    public void printData() {
        System.out.println("---------------------------- Print Order Data -----------------------");
        System.out.println(getOrderRepository().selectAll());
//        System.out.println("---------------------------- Print OrderItem Data -------------------");
//        System.out.println(getOrderItemRepository().selectAll());
    }
    
    protected abstract OrderRepository getOrderRepository();
    
    protected abstract OrderItemRepository getOrderItemRepository();
    
    protected abstract Order newOrder();
    
    protected abstract OrderItem newOrderItem();
}
