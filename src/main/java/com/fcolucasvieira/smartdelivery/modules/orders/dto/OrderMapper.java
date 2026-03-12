package com.fcolucasvieira.smartdelivery.modules.orders.dto;

import com.fcolucasvieira.smartdelivery.modules.orders.OrderEntity;
import com.fcolucasvieira.smartdelivery.modules.products.ProductEntity;

import java.util.List;
import java.util.UUID;

public class OrderMapper {
    public static OrderEntity toEntity(CreateOrderRequest request, UUID customerId){
            OrderEntity order = new OrderEntity();
            order.setCustomerId(customerId);

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
