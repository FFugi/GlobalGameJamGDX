package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Sprite playerSprite;
	OrthographicCamera camera;

	// Box2d World
	World world;

	Body testBody;
	
	Player player;

	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;

	float deltaTime;
	ShapeRenderer shapeRenderer;

	@Override
	public void create() {
		shapeRenderer=new ShapeRenderer();
		batch = new SpriteBatch();
		img = new Texture("graphics/light.png");
		playerSprite = new Sprite(img);

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

		// Box2d experiments
		world = new World(new Vector2(0, 0), true);

		player = new Player(playerSprite, world);

		debugMatrix = new Matrix4(camera.combined);
		debugMatrix.translate(-Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2, 0);

		debugRenderer = new Box2DDebugRenderer();
		
		
		
		// TEST
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(100,200);
		
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(100, 100);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		
		testBody = world.createBody(bodyDef);
		 
		Fixture fixture = testBody.createFixture(fixtureDef);
		
		shape.dispose();
		
		
		
	}

	@Override
	public void render() {
		deltaTime = Gdx.graphics.getDeltaTime();
		HandleInput();

		world.step(deltaTime, 6, 2);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		player.Draw(batch);

		batch.begin();
		// BoxObjectManager.GetWorld() gets the reference to Box2d World object
		debugRenderer.render(world, debugMatrix);
		batch.end();
			
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}

	public void HandleInput() {
		float verticalInput = 0;
		float horizontalInput = 0;
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			verticalInput = 1;
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			verticalInput = -1;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			horizontalInput = -1;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			horizontalInput = 1;
		}
		player.setVelocity(horizontalInput, verticalInput);
	}
}
