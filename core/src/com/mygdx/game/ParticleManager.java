package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class ParticleManager {

	private List<ParticlePool> particlepools;
	
	private World world;

	public ParticleManager(World world) {
		this.world = world;
		particlepools = new ArrayList<ParticlePool>();
		for (int i = 0; i < 4; i++) {
			particlepools.add(new ParticlePool(world));
		}
	}

	public void RequestBurst(Vector2 position, Color color) {
		for (ParticlePool particlePool : particlepools) {
			if (particlePool.isAvailable()) {
				particlePool.Burst(position, color);
				break;
			}
		}
		particlepools.sort((p1, p2) -> (int)(p1.timeWhenActivated - p2.timeWhenActivated));
	}

	public void DisplayParticles(ShapeRenderer shaperenderer) {
		for (ParticlePool particlePool : particlepools) {
			if (!particlePool.isAvailable()) {
				particlePool.Display(shaperenderer);
			}
		}
	}
}
