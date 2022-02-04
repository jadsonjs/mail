package br.com.jadson.mailframe.converters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class StringConverterTest {

    @Test
    void stringToArray() {
        List<String> list = new StringConverter().stringToList("jadson@jadson;test@test;");

        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals("jadson@jadson", list.get(0));
        Assertions.assertEquals("test@test", list.get(1));
    }

    @Test
    void arrayToString() {

        String text = new StringConverter().listToString(List.of("jadson@jadson", "test@test"));

        Assertions.assertEquals("jadson@jadson;test@test;", text);
    }
}