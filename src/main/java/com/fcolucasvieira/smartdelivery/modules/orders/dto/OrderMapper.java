package com.fcolucasvieira.smartdelivery.modules.orders.dto;

import com.fcolucasvieira.smartdelivery.modules.orders.OrderEntity;
import com.fcolucasvieira.smartdelivery.modules.products.ProductEntity;

import java.util.List;

public class OrderMapper {
    public static OrderEntity toEntity(CreateOrderRequest request){
            OrderEntity order = new OrderEntity();
            order.setCustomerId(request.customerId());

            List<ProductEntity> products = request.productsIds().stream()
                    .map(id -> {
                        ProductEntity product = new ProductEntity();
                        product.setId(id);
                        return product;
                    }).toList();

            order.setProducts(products);
            return order;
    }
}
