package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class MyGdxGame extends Game {

	PlayScreen playScreen;
	MenuScreen menuScreen;

	@Override
	public void create() {
		playScreen = new PlayScreen("path");
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		
		Gdx.app.exit();
	}

}
