package com.mygdx.game;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Timer;

public class SoundParticle extends GameObject {
    float x, y;
    Vector2 startPos;
    Body body;
    float lifeTime;

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
        body.setUserData(this);
        startPos=new Vector2();
        startPos.x=position.x;
        startPos.y=position.y;
        
    }

    @Override
    public void Draw(SpriteBatch batch) {
        System.out.println("drawing");
    }
    
    public void Draw(ShapeRenderer shapeRenderer) {
    	shapeRenderer.begin(ShapeType.Filled);
    	shapeRenderer.setColor(0f, 1f-lifeTime/5, 0f, 0f);
    	shapeRenderer.circle(body.getPosition().x, body.getPosition().y, 5);
    	shapeRenderer.end();
    }

    public static void emit(Vector2 position, int amount, float speed, World world, List<SoundParticle> particles) {
       for (int i = 0; i < amount; i++) {
            Vector2 velocity = new Vector2(1, 1).scl(speed).rotate(360.0f / amount * i);
            particles.add(new SoundParticle(position, velocity, world));
       }
    }
    
    public float GetDistance() {
    	return startPos.dst(body.getPosition());
    }
    
    public float GetLifeTime() {
    	lifeTime+=Gdx.graphics.getDeltaTime();
    	return lifeTime;
    }

	public void DestroyBody() {
		body.getWorld().destroyBody(body);
		body=null;
	}
}
