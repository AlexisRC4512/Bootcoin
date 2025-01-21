package com.nttdata.bootcoin.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nttdata.bootcoin.model.enums.TypeDocument;

import java.io.IOException;

public class DocumentTypeDeserializer extends JsonDeserializer<TypeDocument> {
    @Override
    public TypeDocument deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().toUpperCase();
        return TypeDocument.valueOf(value);
    }
}
