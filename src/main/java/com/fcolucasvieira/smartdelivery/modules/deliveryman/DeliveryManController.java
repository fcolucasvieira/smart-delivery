package com.fcolucasvieira.smartdelivery.modules.deliveryman;

import com.fcolucasvieira.smartdelivery.modules.deliveryman.dto.CreateDeliveryManRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/deliveryman")
public class DeliveryManController {
    private CreateDeliveryManUseCase createDeliveryManUseCase;

    public DeliveryManController(CreateDeliveryManUseCase createDeliveryManUseCase) {
        this.createDeliveryManUseCase = createDeliveryManUseCase;
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public UUID create(@RequestBody CreateDeliveryManRequest createDeliveryManRequest){
        return this.createDeliveryManUseCase.execute(createDeliveryManRequest);
    }
}
