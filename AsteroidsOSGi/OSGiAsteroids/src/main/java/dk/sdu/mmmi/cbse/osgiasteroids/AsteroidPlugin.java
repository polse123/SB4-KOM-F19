package dk.sdu.mmmi.cbse.osgiasteroids;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

/**
 *
 * @author corfixen
 */
public class AsteroidPlugin implements IGamePluginService {

    public static String ID;

    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        Entity asteroid = createAsteroid(gameData);
        ID = asteroid.getID();
        world.addEntity(asteroid);
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        world.removeEntity(world.getEntity(ID));

    }

    private Entity createAsteroid(GameData gameData) {
        Entity asteroid = new Asteroid();

        asteroid.setPosition(30, 30);

        float radians = (float) Math.random() * 2 * 3.1415f;
        asteroid.setRadians(radians);

        asteroid.setLife(3);

        return asteroid;
    }
}
