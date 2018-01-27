package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

class SoundEmitter {

    private float maxAge;
    private float age;
    private World world;
    private Vector2 position;

    public SoundEmitter(Vector2 position, World world) {
       this.position = position;
       this.world = world;
       age = 0f;
       maxAge = 10f;
    }

    public void Draw(ShapeRenderer renderer) {
       age += Gdx.graphics.getDeltaTime();
       renderer.begin(ShapeRenderer.ShapeType.Filled);
       float green = (maxAge - age) / maxAge * 1.0f;
       renderer.setColor(0, green, 0, 1);
       renderer.circle(position.x, position.y, 10 * age);
       renderer.end();
    }

    public boolean isDead() {
        return age > maxAge;
    }
}
