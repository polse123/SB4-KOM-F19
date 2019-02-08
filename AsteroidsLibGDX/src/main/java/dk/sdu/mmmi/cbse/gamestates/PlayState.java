package dk.sdu.mmmi.cbse.gamestates;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.sdu.mmmi.cbse.entities.EnemySpaceship;
import dk.sdu.mmmi.cbse.entities.Player;
import dk.sdu.mmmi.cbse.managers.GameKeys;
import dk.sdu.mmmi.cbse.managers.GameStateManager;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

public class PlayState extends GameState {

    private ShapeRenderer sr;

    private Player player;
    private EnemySpaceship enemy;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

        sr = new ShapeRenderer();

        player = new Player();
        enemy = new EnemySpaceship();
    }

    public void update(float dt) {

        handleInput();

        player.update(dt);
        enemy.update(dt);

    }

    public void draw() {
        player.draw(sr);
        enemy.draw(sr);
    }

    public void handleInput() {
        player.setLeft(GameKeys.isDown(GameKeys.LEFT));
        player.setRight(GameKeys.isDown(GameKeys.RIGHT));
        player.setUp(GameKeys.isDown(GameKeys.UP));
        player.setShoot(GameKeys.isPressed(GameKeys.SPACE));
        enemy.setEscape(GameKeys.isPressed(GameKeys.ESCAPE));

    }

    

    public void dispose() {
    }

}
