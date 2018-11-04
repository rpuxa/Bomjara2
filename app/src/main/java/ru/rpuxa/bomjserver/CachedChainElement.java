package ru.rpuxa.bomjserver;

import java.io.Serializable;
import java.util.Objects;

@DontEditSerializable
public class CachedChainElement implements Serializable {
    private static final long serialVersionUID = 8912671345127635497L;

    private final String name;
    private final byte location;
    private final byte friend;
    private final byte transport;
    private final byte home;
    private final byte course;
    private final int cost;
    private final byte currency;

    public CachedChainElement(String name, byte location, byte friend, byte transport, byte home, byte course, int cost, byte currency) {
        this.name = name;
        this.location = location;
        this.friend = friend;
        this.transport = transport;
        this.home = home;
        this.course = course;
        this.cost = cost;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public byte getLocation() {
        return location;
    }

    public byte getFriend() {
        return friend;
    }

    public byte getTransport() {
        return transport;
    }

    public byte getHome() {
        return home;
    }

    public byte getCourse() {
        return course;
    }

    public int getCost() {
        return cost;
    }

    public byte getCurrency() {
        return currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, friend, transport, home, course, cost, currency);
    }
}
