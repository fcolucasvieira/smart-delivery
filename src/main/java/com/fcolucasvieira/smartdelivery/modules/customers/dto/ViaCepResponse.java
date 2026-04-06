package com.fcolucasvieira.smartdelivery.modules.customers.dto;

public record ViaCepResponse(
        String cep,
        String logradouro,
        Boolean erro
) { }
