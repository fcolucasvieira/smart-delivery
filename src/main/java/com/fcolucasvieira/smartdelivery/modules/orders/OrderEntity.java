package com.fcolucasvieira.smartdelivery.modules.orders;

import com.fcolucasvieira.smartdelivery.modules.customers.CustomerEntity;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.DeliveryManEntity;
import com.fcolucasvieira.smartdelivery.modules.products.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Table(name = "orders")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "customer_id")
    private UUID customerId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false, insertable = false, updatable = false)
    private CustomerEntity customer;

    @ManyToMany
    @JoinTable(
            name = "orders_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "products_id")
    )
    private List<ProductEntity> products;

    @Column(name = "deliveryMan_id")
    private UUID deliveryManId;

    @ManyToOne
    @JoinColumn(name = "deliveryMan_id", insertable = false, updatable = false)
    private DeliveryManEntity deliveryMan;

    @Enumerated(EnumType.STRING)
    private StatusOrder status = StatusOrder.CRIADO;

    public OrderEntity(UUID customerId, CustomerEntity customer, List<ProductEntity> products, StatusOrder status) {
        this.customerId = customerId;
        this.customer = customer;
        this.products = products;
        this.status = status;
    }
}
