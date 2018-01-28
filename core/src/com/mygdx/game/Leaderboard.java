package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

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
       load();
    }

    public void load() {
        records = new ArrayList<>();
        prefs = Gdx.app.getPreferences("leaderboard");
        if (!prefs.getString("leaderboard").isEmpty()) {
            JsonValue data = new JsonReader().parse(prefs.getString("leaderboard"));
            JsonValue.JsonIterator iterator = data.get("records").iterator();
            while (iterator.hasNext()) {
                JsonValue record = iterator.next();
                String name = record.getString("name");
                float time = record.getFloat("time");
                int pulses = record.getInt("pulses");
                records.add(new Record(name, time, pulses));
            }
        }
    }

    public void add(String name, float time, int pulses) {
        records.add(new Record(name, time, pulses));
        save();
    }

    public void save() {
        Json json = new Json();
        json.setElementType(Leaderboard.class, "records", Record.class);
        prefs.putString("leaderboard", json.prettyPrint(this));
        prefs.flush();
    }

    public List<Record> getRecordsBy(Ordering ordering) {
       return records.stream()
               .sorted((s1, s2) -> ordering == Ordering.TIME
                            ? (int)(s1.time - s2.time)
                            : s1.pulses - s2.pulses)
               .collect(Collectors.toList());
    }
}
