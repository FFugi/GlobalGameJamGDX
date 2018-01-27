package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Collectible {
	public Vector2 position;
	public float radius;
	public float blinkTimer;
	public final static float lightenTime = 2;
	public final static float disappearTime = 10;

	public Collectible(Vector2 position) {
		this.position = position;
		blinkTimer = MathUtils.random(0, 12);
		radius = 5;
	}

	public void Draw(ShapeRenderer renderer) {
		blinkTimer += Gdx.graphics.getDeltaTime();
		if (blinkTimer > lightenTime + disappearTime)
			blinkTimer = 0;
		Gdx.gl.glEnable(GL30.GL_BLEND);
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		if (blinkTimer <= lightenTime) {
			float progress = blinkTimer / lightenTime;
			renderer.setColor(0.7f, 0.1f, 0f, progress);
		} else {
			float progress = (blinkTimer - lightenTime) / disappearTime;
			renderer.setColor(0.7f, 0.1f, 0f, 1-progress);
		}
		renderer.circle(position.x, position.y, radius);
		renderer.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);
	}
}
