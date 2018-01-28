package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class MyGdxGame extends Game {

	PlayScreen playScreen;
	MenuScreen menuScreen;
	VictoryScreen victoryScreen;	
	
	Leaderboard leaderboard;
	
	@Override
	public void create() {
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
