package com.fcolucasvieira.smartdelivery.integrations.zipcode;

import com.fcolucasvieira.smartdelivery.ViaCepDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacep-service", url = "${feign.client.config.viacep-service.url}")
public interface ViaCEPClient {
    // String url = "https://viacep.com.br/ws/"+createCustomerRequest.zipCode()+"/json/"

    @GetMapping("/{zipCode}/json")
    ViaCepDTO findZipCode(@PathVariable("zipCode") String zipCode);

}
