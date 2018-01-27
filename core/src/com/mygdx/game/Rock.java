package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Rock {
    private Vector2[] vertices;
    private Body body;
	
	public Body getBody() {
		return body;
	}

	Rock(World world, Vector2[] vertices, Vector2 position){
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(position.x, position.y);

		this.vertices = vertices.clone();
        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.filter.categoryBits = 1;
		
		this.body = world.createBody(bodyDef);
		 
		Fixture fixture = this.body.createFixture(fixtureDef);

		shape.dispose();
	}
	
	public void SetPosition(int x, int y) {
		body.setTransform(x, y, body.getAngle());
	}

	public List<Vector2> getVertices() {
	    return Arrays.stream(vertices)
                .map(v -> v.add(body.getPosition()))
                .collect(Collectors.toList());
    }
}
