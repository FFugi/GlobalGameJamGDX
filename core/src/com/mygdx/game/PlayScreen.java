package com.mygdx.game;

import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
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
import com.badlogic.gdx.utils.TimeUtils;

public class PlayScreen implements Screen {

	private SpriteBatch batch;
	private Texture img;
	private Texture background;
	private Sprite playerSprite;
	private Sprite backGroundSprite;
	private OrthographicCamera camera;

	private ParticleManager particleManager;

	// Box2d World
	private World world;

	private Player player;

	private GameMap map;

	private MyGdxGame game;

	private Box2DDebugRenderer debugRenderer;
	private Matrix4 debugMatrix;

	private float deltaTime;
	private ShapeRenderer shapeRenderer;

	private int emits;
	private long timeWhenStarted;

	public PlayScreen(String mapPath, MyGdxGame game) {
		this.game = game;
		batch = new SpriteBatch();
		img = new Texture("graphics/light.png");
		background = new Texture("graphics/background.png");
		playerSprite = new Sprite(img);
		backGroundSprite = new Sprite(background);
		backGroundSprite.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);

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
		timeWhenStarted = TimeUtils.millis();
	}

	@Override
	public void render(float delta) {
		deltaTime = Gdx.graphics.getDeltaTime();
		HandleInput();

		world.step(1 / 30f, 6, 2);

		SetCamera();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		backGroundSprite.draw(batch);
		batch.end();
		

        Gdx.gl.glEnable(GL30.GL_BLEND);

		shapeRenderer.setProjectionMatrix(camera.combined);

		map.collectibles.forEach(c -> c.Draw(shapeRenderer));
		map.gates.forEach(c -> c.Draw(shapeRenderer));
		map.updateGates();

		// debugRenderer.render(world, debugMatrix);

		shapeRenderer.setProjectionMatrix(camera.combined);

		particleManager.DisplayParticles(shapeRenderer);

		
		player.Draw(shapeRenderer);

		map.goal.draw(shapeRenderer);
        Gdx.gl.glDisable(GL30.GL_BLEND);





		

		if (map.captureCollectibles(player, particleManager)) {
			SoundManager.GetInstance().playCollectSound();
		}

		if (map.goal.update(player, particleManager)) {
			System.out.println("Congrats");
			this.game.leaderboard.add("Player", (float) (TimeUtils.millis() - timeWhenStarted) / 1000, emits);
			this.game.leaderboard.save();
			this.game.setScreen(game.victoryScreen);

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
			/*
			 * int x = Gdx.input.getX(); int y = Gdx.input.getY(); Vector3 v3 =
			 * camera.unproject(new Vector3(x, y, 0)); Vector2 position = new Vector2();
			 * position.x = v3.x; position.y = v3.y;
			 */
			Vector2 position = player.getPosition();
			if (particleManager.RequestBurst(position, new Color(0, 1, 0, 0))) {
				emits++;
			}
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
						SoundParticle particle = (SoundParticle) fixtureB.getBody().getUserData();
						particle.isOnDoor = true;
					}
					fixtureB.getBody().setLinearVelocity(0, 0);

				}
				if (fixtureA.getBody().getUserData() instanceof SoundParticle
						&& (fixtureB.getBody().getUserData() instanceof Rock)) {
					if (fixtureB.getBody().getUserData() instanceof Gate) {
						SoundParticle particle = (SoundParticle) fixtureA.getBody().getUserData();
						particle.isOnDoor = true;
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
