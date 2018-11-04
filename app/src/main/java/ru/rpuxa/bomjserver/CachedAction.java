package ru.rpuxa.bomjserver;

import java.io.Serializable;
import java.util.Objects;

@DontEditSerializable
public class CachedAction implements Serializable {
    private static final long serialVersionUID = -8972060173461279384L;

    private final short id;
    private final byte level;
    private final byte menu;
    private final String name;
    private final int cost;
    private final byte currency;
    private final short energy;
    private final short food;
    private final short health;
    private final boolean illegal;

    public CachedAction(short id, byte level, byte menu, String name, int cost, byte currency, short energy, short food, short health, boolean illegal) {
        this.id = id;
        this.level = level;
        this.menu = menu;
        this.name = name;
        this.cost = cost;
        this.currency = currency;
        this.energy = energy;
        this.food = food;
        this.health = health;
        this.illegal = illegal;
    }

    public short getId() {
        return id;
    }

    public byte getLevel() {
        return level;
    }

    public byte getMenu() {
        return menu;
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

    public short getEnergy() {
        return energy;
    }

    public short getFood() {
        return food;
    }

    public short getHealth() {
        return health;
    }

    public boolean isIllegal() {
        return illegal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, menu, name, cost, currency, energy, food, health, illegal);
    }
}
