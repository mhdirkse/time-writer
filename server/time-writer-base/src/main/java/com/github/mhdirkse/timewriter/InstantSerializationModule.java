package com.github.mhdirkse.timewriter;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

@Component
public class InstantSerializationModule extends SimpleModule {
    private static final long serialVersionUID = -2531195235346345821L;

    public InstantSerializationModule() {
        super("InstantSerializationModule");
    }

    @Override
    public void setupModule(SetupContext context) {
        SimpleSerializers simpleSerializers = new SimpleSerializers();
        simpleSerializers.addSerializer(new InstantSerializer());
        context.addSerializers(simpleSerializers);
        SimpleDeserializers simpleDeserializers = new SimpleDeserializers();
        simpleDeserializers.addDeserializer(Instant.class, new InstantDeserializer());
        context.addDeserializers(simpleDeserializers);
    }
}
