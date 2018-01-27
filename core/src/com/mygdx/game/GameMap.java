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
		
		map.objects.get(0).SetPosition(20, 100);
		
		map.objects.add(new Rock( world,
				new Vector2[] {
				new Vector2(30f  , 90f),
				new Vector2(43f , 45f  ),
				new Vector2(200f , 10f  ),
		}));
		
		map.objects.get(1).SetPosition(400, 160);
		
		map.objects.add(new Rock( world,
				new Vector2[] {
				new Vector2(43f  , 70f),
				new Vector2(10 , 80f  ),
				new Vector2(25f , 30f  ),
		}));
		
		map.objects.get(2).SetPosition(200, 50);
		
		map.objects.add(new Rock( world,
				new Vector2[] {
				new Vector2(0  , 100),
				new Vector2(100 , 100  ),
				new Vector2(100 , 0  ),
		}));
		
		map.objects.get(2).SetPosition(500, 350);
		
		return map;
	}

	
}
