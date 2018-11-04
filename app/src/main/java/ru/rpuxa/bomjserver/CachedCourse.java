package ru.rpuxa.bomjserver;

import java.io.Serializable;
import java.util.Objects;

@DontEditSerializable
public class CachedCourse implements Serializable {
    private static final long serialVersionUID = -7514723895204965125L;

    private final byte id;
    private final String name;
    private final int cost;
    private final byte currency;
    private final short length;

    public CachedCourse(byte id, String name, int cost, byte currency, short length) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.currency = currency;
        this.length = length;
    }

    public byte getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public byte getCurrency() {
        return currency;
    }

    public short getLength() {
        return length;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cost, currency, length);
    }
}
