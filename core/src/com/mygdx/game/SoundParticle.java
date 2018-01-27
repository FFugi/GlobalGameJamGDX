package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class SoundParticle extends GameObject {
	public float x, y;
	public Vector2 startPos;
	public Body body;
	public boolean isOnDoor;
	public float scaleSize = 1;

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
		isOnDoor=false;
	}

	@Override
	public void Draw(SpriteBatch batch) {
		System.out.println("drawing");
	}

	public void Draw(ShapeRenderer shapeRenderer, float progress, Color color) {
		float radius = 0;
		if (body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0) {
			radius = 3+ scaleSize;
			scaleSize += Gdx.graphics.getDeltaTime() * 2;
		} else {
			radius = 3;
		}
		Gdx.gl.glEnable(GL30.GL_BLEND);
		shapeRenderer.begin(ShapeType.Filled);
		
		if(isOnDoor) {
			shapeRenderer.setColor(1f, 0f, 0f, 1f-progress);
		}
		else {
			shapeRenderer.setColor(color.r,color.g, color.b, 1f-progress);
		}
		shapeRenderer.circle(body.getPosition().x, body.getPosition().y, radius);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);
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
