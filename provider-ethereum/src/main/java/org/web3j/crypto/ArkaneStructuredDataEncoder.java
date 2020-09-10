package org.web3j.crypto;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ArkaneStructuredDataEncoder extends StructuredDataEncoder {
    public ArkaneStructuredDataEncoder(String jsonMessageInString) throws IOException, RuntimeException {
        super(jsonMessageInString);
    }

    @Override
    public StructuredData.EIP712Message parseJSONMessage(String jsonMessageInString) throws IOException, RuntimeException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        StructuredData.EIP712Message tempJSONMessageObject = (StructuredData.EIP712Message) mapper.readValue(jsonMessageInString, StructuredData.EIP712Message.class);
        this.validateStructuredData(tempJSONMessageObject);
        return tempJSONMessageObject;
    }
}
