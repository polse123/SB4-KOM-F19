package dk.sdu.mmmi.cbse.player;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.LEFT;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.RIGHT;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.SPACE;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.UP;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.data.entityparts.LifePart;
import dk.sdu.mmmi.cbse.common.data.entityparts.MovingPart;
import dk.sdu.mmmi.cbse.common.data.entityparts.PositionPart;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
    , 
    @ServiceProvider(service = IGamePluginService.class)}
)
public class PlayerControlSystem implements IEntityProcessingService, IGamePluginService {
    private float CD;
    private boolean canShoot = true;

    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world

        Entity player = createPlayerShip(gameData);
        world.addEntity(player);
    }

    @Override
    public void process(GameData gameData, World world) {

        for (Entity player : world.getEntities(Player.class)) {

            PositionPart positionPart = player.getPart(PositionPart.class);
            MovingPart movingPart = player.getPart(MovingPart.class);

            movingPart.setLeft(gameData.getKeys().isDown(LEFT));
            movingPart.setRight(gameData.getKeys().isDown(RIGHT));
            movingPart.setUp(gameData.getKeys().isDown(UP));

            //Shoot
            weaponCD(gameData);
            if (gameData.getKeys().isDown(SPACE) && canShoot) {
                BulletSPI bulletService = Lookup.getDefault().lookup(BulletSPI.class);
                if (bulletService != null) {
                    Entity bullet = bulletService.createBullet(player, gameData);
                    world.addEntity(bullet);
                    canShoot = false;
                    CD = 0.3f;
                }
            }

//            if (player.isHit()) {
//                player.setLife(player.getLife() - 1);
//                player.setIsHit(false);
//            }
            
            movingPart.process(gameData, player);
            positionPart.process(gameData, player);

            updateShape(player);
        }
    }

    private void weaponCD(GameData gameData) {
        if (CD > 0) {
            CD -= gameData.getDelta();
        } else {
            canShoot = true;
        }
    }

    private void updateShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        PositionPart playerPos = entity.getPart(PositionPart.class);
        float x = playerPos.getX();
        float y = playerPos.getY();
        float radians = playerPos.getRadians();

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

    private Entity createPlayerShip(GameData gameData) {

        float deacceleration = 10;
        float acceleration = 200;
        float maxSpeed = 300;
        float rotationSpeed = 5;
        float x = gameData.getDisplayWidth() / 2;
        float y = gameData.getDisplayHeight() / 2;
        float radians = 3.1415f / 2;
        float radius = 4;
        
        Entity playerShip = new Player();
        playerShip.add(new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed));
        playerShip.add(new PositionPart(x, y, radians));
        playerShip.add(new LifePart(1));
        playerShip.setRadius(radius);
        return playerShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity entity : world.getEntities(Player.class)) {
            world.removeEntity(entity);
        }
    }

}
