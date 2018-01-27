package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Rock {
    protected Vector2[] vertices;
    protected Body body;
	
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
		body.setUserData(this);
		 
		Fixture fixture = this.body.createFixture(fixtureDef);

		shape.dispose();
	}
	
	public void SetPosition(int x, int y) {
		body.setTransform(x, y, body.getAngle());
	}

	public List<Vector2> getVertices() {
	    return Arrays.stream(vertices)
                .map(v -> new Vector2(v).add(body.getPosition()))
                .collect(Collectors.toList());
    }

    public List<Vector2> getCenters() {
	    List<Vector2> vertices = getVertices();
	    List<Vector2> centers = new ArrayList<>();
	    for (int i = 0; i < vertices.size() - 1; i++) {
	        Vector2 a = new Vector2(vertices.get(i));
            Vector2 b = new Vector2(vertices.get(i + 1));
	        centers.add(new Vector2(b).sub(a).scl(0.5f).add(a));
        }
        Vector2 a = new Vector2(vertices.get(vertices.size() - 1));
        Vector2 b = new Vector2(vertices.get(0));
        centers.add(new Vector2(b).sub(a).scl(0.5f).add(a));

        return centers;
    }

}
