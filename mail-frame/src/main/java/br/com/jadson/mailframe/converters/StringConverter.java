package br.com.jadson.mailframe.converters;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Component
@Named("StringConverter")
public class StringConverter {

    final String DELIMITER = ";";

    @Named("stringToList")
    public List<String> stringToList(String to) {
        if (to != null && to.length() > 0) {
            return Arrays.asList(to.split(DELIMITER));
        }
        return Collections.emptyList();
    }

    @Named("listToString")
    public String listToString(List<String> to) {
        if(to != null && to.size() > 0) {
            StringBuilder buffer = new StringBuilder();
            for (String t : to) {
                buffer.append(t+DELIMITER);
            }
            return buffer.toString();
        }
        return null;
    }

}
