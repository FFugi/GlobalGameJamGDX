package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class MenuScreen implements Screen {

	BitmapFont optionsFont;
	BitmapFont logoFont;
	SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;

	private static final int optionsFontSize = 40;
	private static final int logoFontSize = 70;
	

	private int menuPosition;

	private int SCREENWIDTH;
	private int SCREENHEIGHT;

	private MyGdxGame game;

	private float logoTimer;
	private final static float logoAnimationTime = 5;

	public MenuScreen(MyGdxGame game) {

		this.game = game;
		shapeRenderer = new ShapeRenderer();

		batch = new SpriteBatch();

		menuPosition = 1;

		ConfigureFont();

		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

		SCREENWIDTH = Gdx.graphics.getWidth();
		SCREENHEIGHT = Gdx.graphics.getHeight();

	}

	private void ConfigureFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Supply-Bold.otf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = optionsFontSize;
		parameter.borderWidth = 4f;
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

	}

	@Override
	public void render(float delta) {
		HandleInput();

		DrawFrame();

		DrawLogo();

		DrawOptions();
	}

	private void DrawOptions() {
		String[] menuOptions;
		menuOptions = new String[] { new String("Exit"), new String("Start Game") };
		int i = 0;
		batch.begin();
		for (String option : menuOptions) {
			optionsFont.setColor(0f, 1f, 0.5f, 1f);
			if (menuPosition == i) {
				optionsFont.setColor(Color.GOLD);
			}
			optionsFont.draw(batch, option, SCREENWIDTH / 2 - optionsFontSize * option.length() / 3,
					SCREENHEIGHT / 2.5f+ optionsFontSize * i++ * 3);
		}
		batch.end();
	}

	private void DrawLogo() {
		float green = 0;
		if (logoTimer < logoAnimationTime / 2) {
			green = logoTimer / (logoAnimationTime / 2);
		} else {
			green = 1  - (logoTimer - (logoAnimationTime / 2)) / (logoAnimationTime / 2);
		}
		logoFont.setColor(0.1f, green, 0.3f, 1f);
		logoTimer += Gdx.graphics.getDeltaTime();
		if (logoTimer > logoAnimationTime) {
			logoTimer = 0;
		}
		batch.begin();
		logoFont.draw(batch, "ECHO", SCREENWIDTH / 2 - logoFontSize * "ECHO".length() / 3, SCREENHEIGHT / 1.1f);
		batch.end();
	}

	private void DrawFrame() {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(0, 0.5f, 0.5f, 1);
		//shapeRenderer.rect(SCREENWIDTH / 4, SCREENHEIGHT / 4, SCREENWIDTH / 2, SCREENHEIGHT / 2);
		shapeRenderer.end();
	}

	private void HandleInput() {
		if (Gdx.input.isKeyJustPressed(Keys.UP)) {
			menuPosition--;
		} else if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
			menuPosition++;
		} else if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			if (menuPosition == 1) {
				game.setScreen(game.playScreen);
			} else if (menuPosition == 0) {
				game.dispose();
			}
		}
		if (menuPosition < 0) {
			menuPosition = 1;
		} else if (menuPosition > 1) {
			menuPosition = 0;
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
		shapeRenderer.dispose();
	}

}
