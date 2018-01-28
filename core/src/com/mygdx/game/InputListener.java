package com.mygdx.game;

import com.badlogic.gdx.Input.TextInputListener;

public class InputListener implements TextInputListener {
	String text;

	@Override
	public void input(String text) {
		this.text = text;
	}

	@Override
	public void canceled() {
	}

	public String getText() {
		return text;
	}
}