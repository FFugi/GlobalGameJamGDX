package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.stream.Collectors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class GameMap implements Json.Serializable{

	private World world;
	private List<Rock> objects;

	GameMap(World world) {
		this.world = world;
		this.objects = new ArrayList<Rock>();
	}

	static GameMap LoadFromFile(World world, String filename) {
		GameMap map = new GameMap(world);

		JsonValue jsonData = new JsonReader().parse(new FileHandle("maps/mapobjects.json"));
		JsonValue jsonObjects = jsonData.get("objects");
		JsonValue instances = jsonData.get("instances");
		JsonValue.JsonIterator instanceIterator = instances.iterator();
		while (instanceIterator.hasNext()) {
			JsonValue instance = instanceIterator.next();
			String name = instance.getString("name");
			JsonValue object = jsonObjects.get(name);
			JsonValue.JsonIterator objectIterator = object.iterator();
			List<Vector2> vertices = new ArrayList<Vector2>();
			while (objectIterator.hasNext()) {
				JsonValue current = objectIterator.next();
				Vector2 v = new Vector2(current.getFloat(0), current.getFloat(1));
				vertices.add(v);
			}
			Vector2 position = new Vector2();
			position.x = instance.get("position").getInt(0);
			position.y = instance.get("position").getInt(1);
			float scale = instance.getFloat("scale");
			float angle = instance.getFloat("angle");
			vertices = vertices.stream()
					.map(v -> v.scl(scale))
					.map(v -> v.rotate(angle))
					.collect(Collectors.toList());
			Rock rock = new Rock(world, vertices.toArray(new Vector2[vertices.size()]), position);
			map.objects.add(rock);

		}
		/*map.objects.add(new Rock( world,
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
		*/
		return map;
	}


	@java.lang.Override
	public void write(Json json) {

	}

	@java.lang.Override
	public void read(Json json, JsonValue jsonData) {

	}
}
