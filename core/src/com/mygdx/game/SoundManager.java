package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SoundManager {

    private World world;
    private List<SoundEmitter> emitters;

    SoundManager(World world) {
       this.world = world;
       emitters = new ArrayList<>();
    }

    public void addEmitter(SoundEmitter emitter) {
       emitters.add(emitter);
    }

    public void draw(ShapeRenderer renderer) {
       emitters = emitters.stream().filter(e -> !e.isDead()).collect(Collectors.toList());
       emitters.forEach(e -> e.Draw(renderer));
    }
}
