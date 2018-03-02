package dk.sdu.mmmi.cbse.osgiasteroids;

import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

public class AsteroidProcessor implements IEntityProcessingService {

    private IAsteroidSplitter asteroidSplitter;

    @Override
    public void process(GameData gameData, World world) {

        Entity asteroid = world.getEntity(AsteroidPlugin.ID);

        float x = asteroid.getX();
        float y = asteroid.getY();
        float dx = asteroid.getDx();
        float dy = asteroid.getDy();
        float dt = gameData.getDelta();
        float radians = asteroid.getRadians();

        float speed = (float) Math.random() * 10f + 20f;
        int numPoints = 12;
        int width = 40;

        // Set size based on life
        int life = asteroid.getLife();
        if (life == 1) {
            numPoints = 8;
            width = 12;
            speed = (float) Math.random() * 30f + 70f;
        } else if (life == 2) {
            numPoints = 10;
            width = 20;
            speed = (float) Math.random() * 10f + 50f;
        }

        dx = (float) Math.cos(radians) * speed;
        dy = (float) Math.sin(radians) * speed;

        x = wrapScreenX(x, dx, dt, gameData);
        y = wrapScreenY(y, dy, dt, gameData);

        asteroid.setRadius(width / 2);
        asteroid.setPosition(x, y);
        asteroid.setDx(dx);
        asteroid.setDy(dy);
        asteroid.setRadians(radians);

        setShape(asteroid, numPoints);

        if (asteroid.isHit()) {
            asteroid.setIsHit(false);
            asteroid.setLife(asteroid.getLife() - 1);

            Entity newAsteroid = asteroidSplitter.createSplitAsteroid(asteroid);
            world.addEntity(newAsteroid);
        }
    }

    /**
     * Dependency Injection using OSGi Declarative Services
     */
    public void setAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = asteroidSplitter;
    }

    public void removeAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = null;
    }

    private void setShape(Entity entity, int numPoints) {
        float[] shapex = new float[numPoints];
        float[] shapey = new float[numPoints];
        float radians = entity.getRadians();
        float x = entity.getX();
        float y = entity.getY();
        float radius = entity.getRadius();

        float angle = 0;

        for (int i = 0; i < numPoints; i++) {
            shapex[i] = x + (float) Math.cos(angle + radians) * radius;
            shapey[i] = y + (float) Math.sin(angle + radians) * radius;
            angle += 2 * 3.1415f / numPoints;
        }

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
    }

    private float wrapScreenY(float y, float dy, float dt, GameData gameData) {
        y += dy * dt;
        if (y > gameData.getDisplayHeight()) {
            y = 0;
        } else if (y < 0) {
            y = gameData.getDisplayHeight();
        }
        return y;
    }

    private float wrapScreenX(float x, float dx, float dt, GameData gameData) {
        x += dx * dt;
        if (x > gameData.getDisplayWidth()) {
            x = 0;
        } else if (x < 0) {
            x = gameData.getDisplayWidth();
        }
        return x;
    }

}
