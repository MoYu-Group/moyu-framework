package io.github.moyugroup.web.config.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.github.moyugroup.web.util.DateFormatUtil;

import java.io.IOException;
import java.util.Date;

/**
 * Date 反序列化
 * <p>
 * Created by fanfan on 2024/01/17.
 */
public class DateDeserializer extends StdDeserializer<Date> {

    public DateDeserializer() {
        super(Date.class);
    }

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
        String date = jsonparser.getText();
        return DateFormatUtil.parseDate(date);
    }
}
