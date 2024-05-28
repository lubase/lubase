package com.lubase.starter.config;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class JacksonConfig {
    @Bean("jackson2ObjectMapperBuilderCustomizer")
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        Jackson2ObjectMapperBuilderCustomizer customizer = jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance)
                        .serializerByType(Long.TYPE, ToStringSerializer.instance)
                        .serializerByType(Boolean.class, NumberBooleanSerializer.instance)
                        .serializerByType(Boolean.TYPE,  NumberBooleanSerializer.instance);

        return customizer;
    }
}

class  NumberBooleanSerializer extends JsonSerializer<Boolean> {
    public static final NumberBooleanSerializer instance = new NumberBooleanSerializer();
    @Override
    @SneakyThrows
    public  void serialize(Boolean b, JsonGenerator jsonGenerator, SerializerProvider serializerProvider){
        jsonGenerator.writeNumber(b ? 1 : 0);
    }
}
