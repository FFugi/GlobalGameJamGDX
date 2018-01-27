package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class SoundParticle extends GameObject {
    float x, y;
    Body body;

    public SoundParticle(Vector2 position, Vector2 velocity, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.linearVelocity.set(velocity);
        FixtureDef fixture = new FixtureDef();
        fixture.shape = new CircleShape();
        fixture.shape.setRadius(1);
        fixture.filter.categoryBits = 2;
        fixture.filter.maskBits = 1;
        body = world.createBody(bodyDef);
        body.createFixture(fixture);
    }

    @Override
    public void Draw(SpriteBatch batch) {
        System.out.println("drawing");
    }

    public static void emit(Vector2 position, int amount, float speed, World world) {
       for (int i = 0; i < amount; i++) {
            Vector2 velocity = new Vector2(1, 1).scl(speed).rotate(360.0f / amount * i);
            new SoundParticle(position, velocity, world);
       }
    }
}
