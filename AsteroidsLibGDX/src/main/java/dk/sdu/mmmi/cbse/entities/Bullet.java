/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import dk.sdu.mmmi.cbse.main.Game;

/**
 *
 * @author polse123
 */
public class Bullet extends SpaceObject {

    private float maxSpeed;
    private float acceleration;
    private float deceleration;
    private int lifetime;

    public Bullet(SpaceObject so) {

        x = so.x;
        y = so.y;

        maxSpeed = 200;
        acceleration = so.speed + 300;
        deceleration = 0;

        shapex = new float[1];
        shapey = new float[1];

        radians = so.radians;
        rotationSpeed = 3;
        lifetime = 100;

    }

    private void setShape() {
        shapex[0] = x;
        shapey[0] = y;

    }

    public void update(float dt) {

        lifetime--;
        // accelerating
        if (true) {
            dx += MathUtils.cos(radians) * acceleration * dt;
            dy += MathUtils.sin(radians) * acceleration * dt;
        }

        // deceleration
        float vec = (float) Math.sqrt(dx * dx + dy * dy);
        if (vec > 0) {
            dx -= (dx / vec) * deceleration * dt;
            dy -= (dy / vec) * deceleration * dt;
        }
        if (vec > maxSpeed) {
            dx = (dx / vec) * maxSpeed;
            dy = (dy / vec) * maxSpeed;
        }
        this. // set position
                x += dx * dt;
        y += dy * dt;

        // set shape
        setShape();

        // screen wrap
        wrap();

    }

    public void draw(ShapeRenderer sr) {

        sr.setColor(Color.CYAN);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        sr.circle(x, y, 3);
        //sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);

        sr.end();

    }

    public int getLifetime() {
        return lifetime;
    }
}
