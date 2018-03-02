package dk.sdu.mmmi.cbse.osgienemy;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import org.openide.util.Lookup;

public class EnemyProcessor implements IEntityProcessingService {

    private final BulletSPI bulletService = Lookup.getDefault().lookup(BulletSPI.class);

    @Override
    public void process(GameData gameData, World world) {

        for (Entity entity : world.getEntities(Enemy.class)) {

            float x = entity.getX();
            float y = entity.getY();
            float dt = gameData.getDelta();
            float dx = entity.getDx();
            float dy = entity.getDy();
            float acceleration = entity.getAcceleration();
            float deceleration = entity.getDeacceleration();
            float maxSpeed = entity.getMaxSpeed();
            float radians = entity.getRadians();
            float rotationSpeed = entity.getRotationSpeed();
            double random = Math.random();

            // turning
            if (random < 0.2) {
                radians += rotationSpeed * dt;
            }
            if (random > 0.3 && random < 0.5) {
                radians -= rotationSpeed * dt;
            }

            //Shoot
            if (random > 0.98 && bulletService != null) {
                Entity bullet = bulletService.createBullet(entity, gameData);
                world.addEntity(bullet);
            }

            // accelerating
            if (random > 0.7 && random < 0.9) {
                dx += cos(radians) * acceleration * dt;
                dy += sin(radians) * acceleration * dt;
            }

            // deceleration
            float vec = (float) sqrt(dx * dx + dy * dy);
            if (vec > 0) {
                dx -= (dx / vec) * deceleration * dt;
                dy -= (dy / vec) * deceleration * dt;
            }
            if (vec > maxSpeed) {
                dx = (dx / vec) * maxSpeed;
                dy = (dy / vec) * maxSpeed;
            }

            // set position
            x += dx * dt;
            if (x > gameData.getDisplayWidth()) {
                x = 0;
            } else if (x < 0) {
                x = gameData.getDisplayWidth();
            }

            y += dy * dt;
            if (y > gameData.getDisplayHeight()) {
                y = 0;
            } else if (y < 0) {
                y = gameData.getDisplayHeight();
            }

            if (entity.isHit()) {
                entity.setLife(entity.getLife() - 1);
                entity.setIsHit(false);
            }

            // Update entity
            entity.setPosition(x, y);
            entity.setDx(dx);
            entity.setDy(dy);
            entity.setRadians(radians);

            updateShape(entity);
        }
    }

    private void updateShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        float x = entity.getX();
        float y = entity.getY();
        float radians = entity.getRadians();

        shapex[0] = (float) (x + Math.cos(radians) * 8);
        shapey[0] = (float) (y + Math.sin(radians) * 8);

        shapex[1] = (float) (x + Math.cos(radians - 4 * 3.1415f / 5) * 8);
        shapey[1] = (float) (y + Math.sin(radians - 4 * 3.1145f / 5) * 8);

        shapex[2] = (float) (x + Math.cos(radians + 3.1415f) * 5);
        shapey[2] = (float) (y + Math.sin(radians + 3.1415f) * 5);

        shapex[3] = (float) (x + Math.cos(radians + 4 * 3.1415f / 5) * 8);
        shapey[3] = (float) (y + Math.sin(radians + 4 * 3.1415f / 5) * 8);

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
    }

}
