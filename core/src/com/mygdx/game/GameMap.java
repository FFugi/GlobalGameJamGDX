package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameMap {

	private World world;
	private List<Rock> objects;

	GameMap(World world) {
		this.world = world;
		this.objects = new ArrayList<Rock>();
	}

	static GameMap LoadFromFile(World world, String filename) {
		GameMap map = new GameMap(world);
		

		map.objects.add(new Rock( world,
				new Vector2[] {
				new Vector2(8f  , 0f),
				new Vector2(13f , 40f  ),
				new Vector2(200f , 140f  ),
		}));
		
		
		return map;
	}

	
}
