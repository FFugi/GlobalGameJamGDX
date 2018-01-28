package com.mygdx.game;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class PlayScreen implements Screen {

	private SpriteBatch batch;
	private Texture img;
	private Sprite playerSprite;
	private OrthographicCamera camera;

	private ParticleManager particleManager;

	// Box2d World
	private World world;

	private Player player;

	private GameMap map;

	private Box2DDebugRenderer debugRenderer;
	private Matrix4 debugMatrix;

	private float deltaTime;
	private ShapeRenderer shapeRenderer;

	public PlayScreen(String mapPath) {

		batch = new SpriteBatch();
		img = new Texture("graphics/light.png");
		playerSprite = new Sprite(img);

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

		// Box2d experiments
		world = new World(new Vector2(0, 0), true);
		world.setContinuousPhysics(true);

		player = new Player(playerSprite, world);

		debugMatrix = batch.getProjectionMatrix();
		debugMatrix.translate(-Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2, 0);

		debugRenderer = new Box2DDebugRenderer();

		map = GameMap.LoadFromFile(world, "filename");

		shapeRenderer = new ShapeRenderer();

		SetCollisionListener();

		particleManager = new ParticleManager(world);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		deltaTime = Gdx.graphics.getDeltaTime();
		HandleInput();

		world.step(1 / 30f, 6, 2);

		SetCamera();

		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.setProjectionMatrix(camera.combined);

		map.collectibles.forEach(c -> c.Draw(shapeRenderer));
		map.gates.forEach(c -> c.Draw(shapeRenderer));
		map.updateGates();

		// debugRenderer.render(world, debugMatrix);

		shapeRenderer.setProjectionMatrix(camera.combined);

		particleManager.DisplayParticles(shapeRenderer);
		player.Draw(batch);
		// For debug purpose
		batch.begin();

		batch.end();


		if(map.captureCollectibles(player, particleManager)) {
			SoundManager.GetInstance().playCollectSound();
		}

		if(map.goal.update(player, particleManager)) {
			System.out.println("Congrats");
		}

	}

	private void drawRockDots() {
		List<Rock> objects = map.getObjects();
		for (Rock object : objects) {
			for (Vector2 pos : object.getVertices()) {
				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(1, 0, 0, 1);
				shapeRenderer.circle(pos.x, pos.y, 3);
				shapeRenderer.end();
			}

			for (Vector2 center : object.getCenters()) {
				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(0, 0, 1, 1);
				shapeRenderer.circle(center.x, center.y, 3);
				shapeRenderer.end();
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}

	public void HandleInput() {
		float verticalInput = 0;
		float horizontalInput = 0;
		if (Gdx.input.isKeyPressed(Keys.W)) {
			verticalInput = 1;
		} else if (Gdx.input.isKeyPressed(Keys.S)) {
			verticalInput = -1;
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			horizontalInput = -1;
		} else if (Gdx.input.isKeyPressed(Keys.D)) {
			horizontalInput = 1;
		}
		player.setVelocity(horizontalInput, verticalInput);

		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
		/*	int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			Vector3 v3 = camera.unproject(new Vector3(x, y, 0));
			Vector2 position = new Vector2();
			position.x = v3.x;
			position.y = v3.y;*/
			Vector2 position = player.getPosition();
			particleManager.RequestBurst(position, new Color(0,1,0,0));
			SoundManager.GetInstance().playEmitSound();
		}
	}

	public void SetCollisionListener() {
		world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();
				if (fixtureB.getBody().getUserData() instanceof SoundParticle
						&& fixtureA.getBody().getUserData() instanceof Rock) {
					if (fixtureA.getBody().getUserData() instanceof Gate) {
						SoundParticle particle=(SoundParticle) fixtureB.getBody().getUserData();
						particle.isOnDoor=true;
					}
					fixtureB.getBody().setLinearVelocity(0, 0);

				}
				if (fixtureA.getBody().getUserData() instanceof SoundParticle
						&& (fixtureB.getBody().getUserData() instanceof Rock)) {
					if (fixtureB.getBody().getUserData() instanceof Gate) {
						SoundParticle particle=(SoundParticle) fixtureA.getBody().getUserData();
						particle.isOnDoor=true;
					}
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

	public void SetCamera() {

		camera.position.x = player.getPosition().x;
		camera.position.y = player.getPosition().y;
		camera.update();

	}
}
