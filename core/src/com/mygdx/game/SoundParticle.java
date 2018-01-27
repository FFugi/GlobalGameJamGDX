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

	float scaleSize = 1;

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
		startPos = new Vector2();
		startPos.x = position.x;
		startPos.y = position.y;
	}

	@Override
	public void Draw(SpriteBatch batch) {
		System.out.println("drawing");
	}

	public void Draw(ShapeRenderer shapeRenderer, float progress) {
		float radius = 0;
		if (body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0) {
			radius = 3+ scaleSize;
			scaleSize += Gdx.graphics.getDeltaTime() * 2;
		} else {
			radius = 3;
		}
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0f, 1f-progress, 0f, 0f);
		shapeRenderer.circle(body.getPosition().x, body.getPosition().y, radius);
		shapeRenderer.end();
	}


	public float GetDistance() {
		return startPos.dst(body.getPosition());
	}


	public void DestroyBody() {
		body.getWorld().destroyBody(body);
		body = null;
	}

	public Body getBody() {
		return body;
	}
}
