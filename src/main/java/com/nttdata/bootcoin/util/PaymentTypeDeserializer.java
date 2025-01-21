package com.nttdata.bootcoin.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nttdata.bootcoin.model.enums.PaymentMethod;

import java.io.IOException;

public class PaymentTypeDeserializer extends JsonDeserializer<PaymentMethod> {
    @Override
    public PaymentMethod deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().toUpperCase();
        return PaymentMethod.valueOf(value);
    }
}
