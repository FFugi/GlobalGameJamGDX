package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

public class ParticlePool {

	private int size = 300;
	private List<SoundParticle> particles;
	private boolean isAvailable;
	private World world;
	private final static float animationTime=4;
	float lifeTime;
	public long timeWhenActivated;
	public Color color;
	
	public ParticlePool(World world) {
		this.world = world;
		particles = new ArrayList<SoundParticle>();
		for (int i = 0; i < size; i++) {
			particles.add(new SoundParticle(new Vector2(-1000, -1000), new Vector2(0, 0), world));
		}
		isAvailable = true;
	}

	public void Display(ShapeRenderer shapeRenderer) {
		if (!isAvailable) {
			lifeTime += Gdx.graphics.getDeltaTime();
			// System.out.println(String.valueOf(lifeTime ));
			for (SoundParticle particle : particles) {
				particle.Draw(shapeRenderer ,lifeTime/animationTime, color);
			}
			if (lifeTime > animationTime) {
				setAvailable(true);
				lifeTime = 0;
				for (SoundParticle particle : particles) {
					particle.scaleSize = 0;
					particle.isOnDoor=false;
					particle.getBody().setActive(false);
				}
			}
		}
	}

	public void Burst(Vector2 position, Color color) {
		this.color=color;
		setAvailable(false);
		int i = 0;
		for (SoundParticle particle : particles) {
			particle.getBody().setActive(true);
			particle.getBody().setTransform(position, 0);

			Vector2 velocity = new Vector2(1, 1).scl(1000).rotate(360.0f / size * i);
			particle.getBody().setLinearVelocity(velocity);
			i++;
		}
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		if (isAvailable) {
			for (SoundParticle particle : particles) {
				particle.getBody().setLinearVelocity(0, 0);
			//	particle.getBody().setTransform(new Vector2(-1000, -1000), 0);
				
			}
		}
		else {
			timeWhenActivated=TimeUtils.millis();
		}
		this.isAvailable = isAvailable;
	}
}
