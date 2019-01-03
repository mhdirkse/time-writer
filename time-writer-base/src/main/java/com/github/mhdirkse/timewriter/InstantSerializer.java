package com.github.mhdirkse.timewriter;

import java.io.IOException;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class InstantSerializer extends StdSerializer<Instant> {
    private static final long serialVersionUID = 1236232443159069848L;

    public InstantSerializer() {
        super(Instant.class);
    }

    @Override
    public void serialize(
            Instant toSerialize,
            JsonGenerator jsonGenerator,
            SerializerProvider serializeProvider) throws IOException {
        long epochSecond = toSerialize.getEpochSecond();
        String text = Long.valueOf(epochSecond).toString();
        jsonGenerator.writeString(text);
    }
}
