package io.github.moyugroup.web.config.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.github.moyugroup.web.util.DateFormatUtil;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * LocalDateTime 反序列化
 * <p>
 * Created by fanfan on 2024/01/17.
 */
public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    public LocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
        String date = jsonparser.getText();
        return DateFormatUtil.parseLocalDateTime(date);
    }
}
