package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class ParticlePool {

	private int size = 200;
	private List<SoundParticle> particles;
	private boolean isAvailable;
	private World world;
	private final static float animationTime=4;
	float lifeTime;

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
				particle.Draw(shapeRenderer ,lifeTime/animationTime);
			}
			if (lifeTime > animationTime) {
				setAvailable(true);
				lifeTime = 0;
				for (SoundParticle particle : particles) {
					particle.scaleSize = 0;
				}
			}
		}
	}

	public void Burst(Vector2 position) {
		isAvailable = false;
		int i = 0;
		for (SoundParticle particle : particles) {
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
				particle.getBody().setTransform(new Vector2(-1000, -1000), 0);

			}
		}
		this.isAvailable = isAvailable;
	}
}
