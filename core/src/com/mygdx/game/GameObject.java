package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public abstract class GameObject {
	
	private int x;
	private int y;
	
	public abstract void Draw(SpriteBatch batch);
	

}
