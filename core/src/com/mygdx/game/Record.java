package com.mygdx.game;

public class Record {
    public String name;
    public float time;
    public int pulses;

    public Record() {}

    public Record(String name, float time, int pulses) {
        this.name = name;
        this.time = time;
        this.pulses = pulses;
    }
}
