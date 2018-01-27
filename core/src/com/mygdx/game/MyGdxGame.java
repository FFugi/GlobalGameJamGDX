package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture img;
	private Sprite playerSprite;
	private OrthographicCamera camera;

	// Box2d World
	private World world;

	private Player player;
	private List<SoundParticle> particles;

	private GameMap map;

	private Box2DDebugRenderer debugRenderer;
	private Matrix4 debugMatrix;

	private float deltaTime;
	// ShapeRenderer shapeRenderer;
	private ShapeRenderer shapeRenderer;

	@Override
	public void create() {
		// shapeRenderer=new ShapeRenderer();
		batch = new SpriteBatch();
		img = new Texture("graphics/light.png");
		playerSprite = new Sprite(img);

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

		// Box2d experiments
		world = new World(new Vector2(0, 0), true);

		player = new Player(playerSprite, world);

		debugMatrix = new Matrix4(camera.combined);
		debugMatrix.translate(-Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2, 0);

		debugRenderer = new Box2DDebugRenderer();
		map = new GameMap(world);
		map.LoadFromFile(world, "filename");

		particles = new ArrayList<SoundParticle>();
		shapeRenderer = new ShapeRenderer();

		SetCollisionListener();
	}

	@Override
	public void render() {
		deltaTime = Gdx.graphics.getDeltaTime();
		HandleInput();

		world.step(1f / 60f, 6, 2);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		player.Draw(batch);

		batch.begin();
		debugRenderer.render(world, debugMatrix);
		batch.end();

		for (SoundParticle particle : particles) {
			particle.Draw(shapeRenderer);
			if (particle.GetLifeTime() > 5) {
				particle.DestroyBody();
				System.out.println("destroyed");
			}
		}
		for (int i = 0; i < particles.size(); i++) {
			if (particles.get(i).body == null) {
				particles.remove(i);
				i--;
			}
		}

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

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			Vector3 v3 = camera.unproject(new Vector3(x, y, 0));
			Vector2 position = new Vector2();
			position.x = v3.x;
			position.y = v3.y;
			SoundParticle.emit(position, 500, 1000, world, particles);
		}
	}

	public void SetCollisionListener() {
		world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				if (fixtureB.getBody().getUserData() instanceof SoundParticle) {
					fixtureB.getBody().setLinearVelocity(0, 0);
				}
				if (fixtureA.getBody().getUserData() instanceof SoundParticle) {
					fixtureA.getBody().setLinearVelocity(0, 0);
				}
			}

			@Override
			public void endContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}

		});
	}
}
