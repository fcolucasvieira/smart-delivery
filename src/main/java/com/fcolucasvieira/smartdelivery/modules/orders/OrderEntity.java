package com.fcolucasvieira.smartdelivery.modules.orders;

import com.fcolucasvieira.smartdelivery.modules.customers.CustomerEntity;
import com.fcolucasvieira.smartdelivery.modules.deliveryman.DeliveryManEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private CustomerEntity customer;

    @Column(name = "delivery_man_id")
    private UUID deliveryManId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_man_id", insertable = false, updatable = false)
    private DeliveryManEntity deliveryMan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrder status = StatusOrder.CRIADO;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();

    public OrderEntity(UUID customerId, CustomerEntity customer, StatusOrder status) {
        this.customerId = customerId;
        this.customer = customer;
        this.status = status;
    }
}
