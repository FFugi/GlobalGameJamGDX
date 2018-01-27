package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

class SoundEmitter {

    private float maxAge;
    private float age;
    private float radius;
    private World world;
    private Vector2 position;

    public SoundEmitter(Vector2 position, World world, float maxAge, float radius) {
       this.position = position;
       this.world = world;
       this.maxAge = maxAge;
       this.radius = radius;
       age = 0f;
    }

    public void Draw(ShapeRenderer renderer) {
       age += Gdx.graphics.getDeltaTime();
       Gdx.gl.glEnable(GL30.GL_BLEND);
       renderer.begin(ShapeRenderer.ShapeType.Filled);
       float green = (maxAge - age) / maxAge * 1.0f;
       renderer.setColor(0, green, 0, 1);
       renderer.circle(position.x, position.y, radius * age / maxAge);
        renderer.setColor(0, 0, 0, 0.25f);
        renderer.circle(position.x, position.y, 0.95f*(radius * age / maxAge));
       renderer.end();
       Gdx.gl.glDisable(GL30.GL_BLEND);

    }

    public boolean isDead() {
        return age > maxAge;
    }
}
