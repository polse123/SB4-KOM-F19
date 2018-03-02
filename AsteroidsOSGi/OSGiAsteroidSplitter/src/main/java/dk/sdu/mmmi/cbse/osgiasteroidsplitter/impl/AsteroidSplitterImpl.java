package dk.sdu.mmmi.cbse.osgiasteroidsplitter.impl;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author corfixen
 */
public class AsteroidSplitterImpl implements IAsteroidSplitter {

    @Override
    public Entity createSplitAsteroid(Entity e) {

        Entity asteroid = new Asteroid();
        asteroid.setRadians(e.getRadians() - 1);
        asteroid.setRadius(e.getRadius());
        asteroid.setLife(e.getLife());

        float by = (float) sin(e.getRadians() - 1) * e.getRadius() * asteroid.getRadius();
        float bx = (float) cos(e.getRadians() - 1) * e.getRadius() * asteroid.getRadius();
        asteroid.setPosition(e.getX() + bx, e.getY() + by);

        return asteroid;
    }

}
