package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class NameInputScreen implements Screen {

	BitmapFont logoFont;
	private TextField textField;
	private MyGdxGame game;
	private Stage stage;
	private Skin skin;
	SpriteBatch batch;
	
	
	private int SCREENWIDTH;
	private int SCREENHEIGHT;
	
	private float logoTimer;
	private final static float logoAnimationTime = 5;

	private static final int logoFontSize = 70;

	public NameInputScreen(MyGdxGame game) {
		this.game = game;
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		textField = new TextField("Name", skin);
		textField.setX(Gdx.graphics.getWidth() / 2 - 100);
		textField.setY(Gdx.graphics.getHeight() / 2);
		textField.setWidth(200);
		textField.setHeight(30);
		stage.addActor(textField);
		ConfigureFont();
		
		batch = new SpriteBatch();
		
		SCREENWIDTH = Gdx.graphics.getWidth();
		SCREENHEIGHT = Gdx.graphics.getHeight();
	}

	private void ConfigureFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Supply-Bold.otf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = logoFontSize;
		parameter.borderWidth = 1.0f;
		parameter.borderColor = Color.BLACK;
		logoFont = generator.generateFont(parameter);
		generator.dispose();
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
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		HandeInput();
		DrawLogo();
	}

	public void HandeInput() {

		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			this.game.leaderboard.add(textField.getText(), game.finaltime,
					game.emits);
			this.game.leaderboard.save();
			this.game.setScreen(game.victoryScreen);
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		stage.getViewport().update(width, height, true);

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
		stage.dispose();

	}

}
