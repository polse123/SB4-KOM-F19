package dk.sdu.mmmi.cbse.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import dk.sdu.mmmi.cbse.main.Game;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class EnemySpaceship extends SpaceObject {

    private float maxSpeed;
    private float acceleration;
    private float deceleration;

    private boolean escape;

    public void setEscape(boolean b) {
        escape = b;
    }

    private ArrayList<Bullet> bullets = new ArrayList();

    Random rand = new Random();

    public EnemySpaceship() {

        x = Game.WIDTH * (float) rand.nextDouble();
        y = Game.HEIGHT * (float) rand.nextDouble();

        maxSpeed = 50;
        acceleration = 200;
        deceleration = 10;

        shapex = new float[4];
        shapey = new float[4];

        radians = 3.1415f / (float) rand.nextDouble();
        rotationSpeed = 3;

    }

    private void setShape() {
        shapex[0] = x + MathUtils.cos(radians) * 20;
        shapey[0] = y + MathUtils.sin(radians) * 20;

        shapex[1] = x + MathUtils.cos(radians - 4 * 3.1415f / 5) * 15;
        shapey[1] = y + MathUtils.sin(radians - 4 * 3.1145f / 5) * 15;

        shapex[2] = x + MathUtils.cos(radians + 3.1415f) * 5;
        shapey[2] = y + MathUtils.sin(radians + 3.1415f) * 5;

        shapex[3] = x + MathUtils.cos(radians + 4 * 3.1415f / 5) * 15;
        shapey[3] = y + MathUtils.sin(radians + 4 * 3.1415f / 5) * 15;
    }
    
    public void SoundClipTest() {

        try {
            // Open an audio input stream.
            URL url = this.getClass().getClassLoader().getResource("laser.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            
            
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void update(float dt) {

        int value = rand.nextInt(5);

        Iterator<Bullet> iter = bullets.iterator();

        while (iter.hasNext()) {
            Bullet b = iter.next();

            if (b.getLifetime() <= 0) {
                iter.remove();
            } else {
                b.update(dt);
            }
        }

        if (escape) {
            bullets.clear();
        }

        // turning
        {
            if (value == 1) {
                radians += rotationSpeed * dt;
                //SoundClipTest();
                bullets.add(new Bullet(this));

            } else if (value == 0) {
                radians -= rotationSpeed * dt;
                bullets.add(new Bullet(this));
                //SoundClipTest();
            }
        }

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

        // set position
        x += dx * dt;
        y += dy * dt;

        // set shape
        setShape();

        // screen wrap
        wrap();

    }

    public void draw(ShapeRenderer sr) {
        for (Bullet b : bullets) {
            b.draw(sr);
        }
        sr.setColor(Color.RED);

        sr.begin(ShapeType.Line);

        for (int i = 0, j = shapex.length - 1;
                i < shapex.length;
                j = i++) {

            sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
        }
        sr.end();

    }
}
