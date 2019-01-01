package ru.rpuxa.bomjara.cache;

import java.io.Serializable;
import java.util.Map;

@Deprecated
public class ToSerialize implements Serializable {
    private static long serialVersionUID = -8980506436121272101L;
    public String name;
    public Map<String, Object> fields;

    public ToSerialize(String name, Map<String, Object> fields) {
        this.name = name;
        this.fields = fields;
    }
}
