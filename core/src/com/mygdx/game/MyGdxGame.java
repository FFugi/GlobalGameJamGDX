package com.mygdx.game;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {

	PlayScreen playScreen;
	
	@Override
	public void create() {
		playScreen=new PlayScreen("path");
		setScreen(playScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {

	}

}
