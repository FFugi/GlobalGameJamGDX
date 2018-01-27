package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends GameObject {

	private Body body;
	private Sprite sprite;
	private BodyDef bodyDef;
	private float speed;

	public Player(Sprite sprite,World world) {
		speed = 100;
		this.sprite=sprite; 
		 
		bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(100, 100);
		bodyDef.fixedRotation=true;
		bodyDef.linearDamping=0;
		
		
		CircleShape  shape = new CircleShape ();
		shape.setRadius(sprite.getHeight()/2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.friction=0;
		fixtureDef.filter.categoryBits = 4;
		
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
		body.setLinearVelocity(speed*horizontalInput , speed*verticalInput );
	}
	
	public Vector2 getPosition() {
		return new Vector2(body.getPosition());
	}
}
