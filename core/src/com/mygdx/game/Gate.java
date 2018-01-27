package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.List;

public class Gate extends Rock {

    private float[] vcs;
    public int count;
    public boolean enabled;

    Gate(World world, Vector2[] vertices, Vector2 position, int count) {
        super(world, vertices, position);

        this.count = count;

        List<Vector2> verts = getVertices();
        vcs = new float[verts.size() * 2];
        for (int i = 0; i < verts.size(); i++) {
            vcs[i * 2] = verts.get(i).x;
            vcs[i * 2 + 1] = verts.get(i).y;
        }

        enabled = true;
    }

    public void Draw(ShapeRenderer renderer) {
        if (enabled) {
            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setColor(1, 0, 0, 1);
            renderer.polygon(vcs);
            renderer.end();
        }
    }

    public void disable() {
        enabled = false;
        body.setActive(false);
    }
}
