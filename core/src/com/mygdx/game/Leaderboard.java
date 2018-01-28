package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Leaderboard {
    public enum Ordering {
        TIME,
        PULSES
    }
    private List<Record> records;
    private Preferences prefs;

    public Leaderboard() {
       this.records = new ArrayList<Record>();
       load();
    }

    public void load() {
        this.prefs = Gdx.app.getPreferences("leaderboard");
        Json json = new Json();
        if (!prefs.getString("records").isEmpty()) {
            records = json.fromJson(List.class, prefs.getString("records"));
        }
    }

    public void add(String name, float time, int pulses) {
        records.add(new Record(name, time, pulses));
    }

    public void save() {
        Json json = new Json();
        json.setElementType(List.class, "records", Record.class);
        prefs.putString("records", json.prettyPrint(records));
    }

    public List<Record> getRecordsBy(Ordering ordering) {
       return records.stream()
               .sorted((s1, s2) -> ordering == Ordering.TIME
                            ? (int)(s1.time - s2.time)
                            : s1.pulses - s2.pulses)
               .collect(Collectors.toList());
    }
}
