package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class GameMap {

	public List<Gate> gates;
	private int collected;
	public List<Collectible> collectibles;
	private World world;
	private List<Rock> objects;

	GameMap(World world) {
		this.world = world;
		this.objects = new ArrayList<Rock>();
		this.collectibles = new ArrayList<Collectible>();
		this.gates = new ArrayList<Gate>();
		this.collected = 0;
	}

	static GameMap LoadFromFile(World world, String filename) {
		GameMap map = new GameMap(world);

		JsonValue jsonData = new JsonReader().parse(Gdx.files.internal("maps/level01.json"));
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

		JsonValue gates = jsonData.get("gates");
		JsonValue.JsonIterator gatesIterator = gates.iterator();
		while (gatesIterator.hasNext()) {
			JsonValue gate = gatesIterator.next();
			String name = gate.getString("name");
			JsonValue object = jsonObjects.get(name);
			JsonValue.JsonIterator objectIterator = object.iterator();
			List<Vector2> vertices = new ArrayList<Vector2>();
			while (objectIterator.hasNext()) {
				JsonValue current = objectIterator.next();
				Vector2 v = new Vector2(current.getFloat(0), current.getFloat(1));
				vertices.add(v);
			}
			Vector2 position = new Vector2();
			position.x = gate.get("position").getInt(0);
			position.y = gate.get("position").getInt(1);
			Vector2 scale;
			JsonValue jsonScale = gate.get("scale");
			if (jsonScale == null) {
				scale = new Vector2(1, 1);
			} else {
				scale = new Vector2(jsonScale.getFloat(0), jsonScale.getFloat(1));
			}
			float angle = gate.getFloat("angle", 0);
			gate.get("scale");
			vertices = vertices.stream()
					.map(v -> new Vector2(scale.x*v.x, scale.y*v.y))
					.map(v -> v.rotate(angle))
					.collect(Collectors.toList());

			int count = gate.getInt("count");
			Gate newgate = new Gate(world, vertices.toArray(new Vector2[vertices.size()]), position, count);
			map.gates.add(newgate);
		}

		JsonValue collectibles = jsonData.get("collectibles");
		if (collectibles != null) {
			JsonValue.JsonIterator collectibleIterator = collectibles.iterator();
			while (collectibleIterator.hasNext()) {
				JsonValue collectible = collectibleIterator.next();
				Vector2 position = new Vector2(collectible.getFloat(0), collectible.getFloat(1));
				map.collectibles.add(new Collectible(position));
			}
		}
		return map;
	}

	
	public List<Rock> getObjects() {
		return objects;
	}

	public boolean captureCollectibles(Player player) {
		int size = collectibles.size();
		collectibles = collectibles.stream()
				.filter(c -> c.position.dst(player.getPosition()) > c.radius + player.getRadius())
				.collect(Collectors.toList());

		collected += size - collectibles.size();
		return size != collectibles.size();
	}

	public boolean updateGates() {
		gates.stream().filter(g -> g.count <= collected).forEach(g -> g.disable());
		int size = gates.size();
		gates = gates.stream()
				.filter(g -> g.enabled)
				.collect(Collectors.toList());
		return size != gates.size();
	}

}
