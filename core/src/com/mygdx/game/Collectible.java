package com.mygdx.game;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Collectible {
    public Vector2 position;
    public float radius;

    public Collectible(Vector2 position) {
        this.position = position;
        radius = 5;
    }

    public void Draw(ShapeRenderer renderer) {

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(1, 0, 0, 1);
        renderer.circle(position.x, position.y, radius);
        renderer.end();
    }
}
