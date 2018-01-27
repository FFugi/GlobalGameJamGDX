package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class GameMap {

	private World world;
	private List<Rock> objects;

	GameMap(World world) {
		this.world = world;
		this.objects = new ArrayList<Rock>();
	}

	static GameMap LoadFromFile(World world, String filename) {
		GameMap map = new GameMap(world);

		JsonValue jsonData = new JsonReader().parse(Gdx.files.internal("maps/maze01.json"));
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
			Vector2 scale;
			JsonValue jsonScale = instance.get("scale");
			if (jsonScale == null) {
				scale = new Vector2(1, 1);
			} else {
				scale = new Vector2(jsonScale.getFloat(0), jsonScale.getFloat(1));
			}
			float angle = instance.getFloat("angle", 0);
			instance.get("scale");
			vertices = vertices.stream()
					.map(v -> new Vector2(scale.x*v.x, scale.y*v.y))
					.map(v -> v.rotate(angle))
					.collect(Collectors.toList());
			Rock rock = new Rock(world, vertices.toArray(new Vector2[vertices.size()]), position);
			map.objects.add(rock);

		}
		return map;
	}

	
	public List<Rock> getObjects() {
		return objects;
	}

}
