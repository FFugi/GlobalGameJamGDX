package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class Goal {
    private Color color;
    private ParticleManager particleManager;
    private Vector2 position;
    private boolean captured;
    private float burstTimer;

    public Goal(Vector2 position) {
        this.position = position;
        captured = false;
        this.color = new Color(1, 1, 1, 1);
    }

    public boolean update(Player player, ParticleManager particleManager) {
        if (captured) return true;

        if (burstTimer <= 0) {
            particleManager.RequestBurst(position, color);
            burstTimer = 5;
        } else {
            burstTimer -= Gdx.graphics.getDeltaTime();
        }

        captured = position.dst(player.getPosition()) <= player.getRadius() + 10;
        return captured;
    }
}
