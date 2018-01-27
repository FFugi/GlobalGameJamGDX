package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends GameObject {

	Body body;
	Sprite sprite;
	BodyDef bodyDef;
	float speed;
	float verticalVelocity;

	float horizontalVelocity;


	public Player(Sprite sprite,World world) {
		speed = 100;
		this.sprite=sprite; 
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(this.sprite.getX(), this.sprite.getY());
		bodyDef.fixedRotation=true;
		
		CircleShape  shape = new CircleShape ();
		shape.setRadius(sprite.getHeight()/2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		
		body = world.createBody(bodyDef);
		 
		Fixture fixture = body.createFixture(fixtureDef);
		
		sprite.setCenter(sprite.getWidth()/2, sprite.getHeight()/2);
		shape.dispose();
	}

	@Override
	public void Draw(SpriteBatch batch) {
		sprite.setPosition(body.getPosition().x-sprite.getWidth()/2, body.getPosition().y-sprite.getHeight()/2);
		
		batch.begin();
		batch.draw(sprite, sprite.getX(), sprite.getY());
		batch.end();
	}

	public void setVelocity(float horizontalInput, float verticalInput) {
		// TODO Auto-generated method stub
		body.setLinearVelocity(speed * horizontalInput, speed * verticalInput);
	}
	
}
