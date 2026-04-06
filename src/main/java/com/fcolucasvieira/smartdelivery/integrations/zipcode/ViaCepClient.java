package com.fcolucasvieira.smartdelivery.integrations.zipcode;

import com.fcolucasvieira.smartdelivery.modules.customers.dto.ViaCepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// https://viacep.com.br/ws/ + zipCode + "/json/"
@FeignClient(name = "viacep-service", url = "${feign.client.config.viacep-service.url}")
public interface ViaCepClient {
    @GetMapping("/{zipCode}/json")
    ViaCepResponse findZipCode(@PathVariable String zipCode);
}
