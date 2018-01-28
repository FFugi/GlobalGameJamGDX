package com.mygdx.game;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.Leaderboard.Ordering;

public class VictoryScreen implements Screen {

	BitmapFont optionsFont;
	BitmapFont logoFont;
	SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;

	private static final int optionsFontSize = 40;
	private static final int logoFontSize = 70;

	private int SCREENWIDTH;
	private int SCREENHEIGHT;

	private MyGdxGame game;

	private List<Record> records;
	
	private float logoTimer;
	private final static float logoAnimationTime = 5;
	
	private int startY;
	
	private Leaderboard.Ordering ordering;

	String name;

	public VictoryScreen(MyGdxGame game) {
		ordering=ordering.TIME;
		this.game = game;
		shapeRenderer = new ShapeRenderer();

		batch = new SpriteBatch();

		ConfigureFont();

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

		SCREENWIDTH = Gdx.graphics.getWidth();
		SCREENHEIGHT = Gdx.graphics.getHeight();
		
		startY=0;
	}

	private void ConfigureFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Supply-Bold.otf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = optionsFontSize;
		parameter.borderWidth = 1.0f;
		parameter.borderColor = Color.BLACK;
		optionsFont = generator.generateFont(parameter);
		optionsFont.setColor(Color.GREEN);
		parameter.size = logoFontSize;
		logoFont = generator.generateFont(parameter);
		parameter.size = optionsFontSize;
		generator.dispose(); // don't forget to dispose to avoid memory leaks!

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		game.leaderboard.load();
	}

	@Override
	public void render(float delta) {
		
		HandleInput();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		DrawFrame();
		DrawLogo();
		DrawOptions();
	}

	private void DrawOptions() {
		List<Record> records=game.leaderboard.getRecordsBy(ordering);
		
		int i = 0;
		batch.begin();
		for (Record record : records) {
			optionsFont.setColor(0f, 1f, 0.5f, 1f);
			float yValue = SCREENHEIGHT / 1.35f - optionsFontSize  *((i+startY)*  1.5f);
			if(i+startY>=0) {
			optionsFont.draw(batch,String.valueOf(i)+". "+ record.name, SCREENWIDTH / 2 - SCREENWIDTH / 3,
					yValue);
			optionsFont.draw(batch, String.valueOf(record.pulses), SCREENWIDTH / 2 + SCREENWIDTH / 8,
					yValue);
			optionsFont.draw(batch, String.valueOf(record.time), SCREENWIDTH / 2 + SCREENWIDTH / 4,
					yValue);
			}
			i++;
		}
		batch.end();
	}

	private void DrawLogo() {
		float green = 0;
		if (logoTimer < logoAnimationTime / 2) { 
			green = logoTimer / (logoAnimationTime / 2);
		} else {
			green = 1 - (logoTimer - (logoAnimationTime / 2)) / (logoAnimationTime / 2);
		}
		logoFont.setColor(0.1f, green, 0.3f, 1f);
		logoTimer += Gdx.graphics.getDeltaTime();
		if (logoTimer > logoAnimationTime) {
			logoTimer = 0;
		}
		batch.begin();
		logoFont.draw(batch, "Victory!", SCREENWIDTH / 2 - logoFontSize * "Victory!".length() / 3, SCREENHEIGHT / 1.1f);
		batch.end();
	}

	private void DrawFrame() {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(0, 0.5f, 0.5f, 1);
		// shapeRenderer.rect(SCREENWIDTH / 4, SCREENHEIGHT / 4, SCREENWIDTH / 2,
		// SCREENHEIGHT / 2);
		shapeRenderer.end();
	}

	private void HandleInput() {
		if (Gdx.input.isKeyJustPressed(Keys.W)) {
			startY++;
		}
		if (Gdx.input.isKeyJustPressed(Keys.S)) {
			startY--;
		}
		if (Gdx.input.isKeyJustPressed(Keys.A)) {
			toggleOrdering();
		}
		if (Gdx.input.isKeyJustPressed(Keys.D)) {
			toggleOrdering();
		}
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.dispose();
		}
	}

	private void toggleOrdering() {
		if(ordering==ordering.PULSES) {
			ordering=ordering.TIME;
		}
		else {
			ordering=ordering.PULSES;
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
		// TODO Auto-generated method stub
		batch.dispose();
		optionsFont.dispose();
		logoFont.dispose();
		shapeRenderer.dispose();
	}

}
