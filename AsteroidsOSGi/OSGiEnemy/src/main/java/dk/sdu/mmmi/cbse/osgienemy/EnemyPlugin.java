package dk.sdu.mmmi.cbse.osgienemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.enemy.Enemy;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

public class EnemyPlugin implements IGamePluginService {
    private String enemyID;

    public EnemyPlugin() {
    }

    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        Entity enemy = createEnemyShip(gameData);
        enemyID = world.addEntity(enemy);
        
    }

    private Entity createEnemyShip(GameData gameData) {
        Entity enemyShip = new Enemy();

        enemyShip.setPosition(gameData.getDisplayWidth() / 3, gameData.getDisplayHeight() / 3);

        enemyShip.setMaxSpeed(300);
        enemyShip.setAcceleration(200);
        enemyShip.setDeacceleration(10);

        enemyShip.setShapeX(new float[4]);
        enemyShip.setShapeY(new float[4]);

        enemyShip.setRadians(3.1415f / 2);
        enemyShip.setRotationSpeed(5);
        
        enemyShip.setLife(1);
        
        enemyShip.setRadius(4);
        
        return enemyShip;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        world.removeEntity(enemyID);
    }

}
