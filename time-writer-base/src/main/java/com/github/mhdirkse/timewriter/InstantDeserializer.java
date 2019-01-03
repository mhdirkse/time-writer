package com.github.mhdirkse.timewriter;

import java.io.IOException;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class InstantDeserializer extends StdDeserializer<Instant> {
    private static final long serialVersionUID = 3308547371074562769L;

    public InstantDeserializer() {
        super(Instant.class);
    }

    @Override
    public Instant deserialize(JsonParser parser, DeserializationContext ctx)
            throws IOException, JsonProcessingException {
        String valueStringSecond = parser.getValueAsString();
        long millis = 1000 * Long.valueOf(valueStringSecond).longValue();
        return Instant.ofEpochMilli(millis);
    }
}
