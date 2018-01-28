package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

public class MyGdxGame extends Game {

	PlayScreen playScreen;
	MenuScreen menuScreen;
	VictoryScreen victoryScreen;	
	
	Leaderboard leaderboard;
	Controller gamepad;
	
	@Override
	public void create() {
		gamepad = Controllers.getControllers().size > 0
				? Controllers.getControllers().get(0)
				: null;

		leaderboard = new Leaderboard();
		playScreen = new PlayScreen("path", this);
		menuScreen = new MenuScreen(this);
		victoryScreen = new VictoryScreen(this);
		SoundManager.GetInstance().playBgMusic();
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
