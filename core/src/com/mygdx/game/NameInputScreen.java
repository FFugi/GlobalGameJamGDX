package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class NameInputScreen implements Screen {

	private TextField textField;
	private MyGdxGame game;
	private Stage stage;
	private Skin skin;

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
	}

	public void HandeInput() {

		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			this.game.leaderboard.add(textField.getText(), (float) (TimeUtils.millis() - game.timeWhenStarted) / 1000,
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
