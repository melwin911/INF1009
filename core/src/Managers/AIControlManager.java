package Managers;

import Objects.Entity;
import Objects.EntityCircle;
import Objects.EntityTexture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeUseImpl;
import jdk.internal.vm.annotation.ForceInline;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static Managers.SimulationLifecycleManager.warningPixmap;

public class AIControlManager {
    private boolean isChasing = true;
    private float warningTimer = 5.0f;
    private float spawnCooldown = 1.0f; // Cooldown timer for spawning a new point entity
    private float delayTimer = 5f;
    private ShapeRenderer shapeRenderer;
    private final EntityManager entityManager;
    private final SimulationLifecycleManager simulationLifecycleManager;
    private final SpriteBatch batch = new SpriteBatch();
    private MusicManager musicManager;

    public AIControlManager(EntityManager entityManager, SimulationLifecycleManager simulationLifecycleManager, MusicManager musicManager){
        this.entityManager = entityManager;
        this.simulationLifecycleManager = simulationLifecycleManager;
        this.musicManager = musicManager;
    }
    public void attackPlayer(String partialTrackerKey, String partialProjectileKey){
        ArrayList<Entity> hostileEntities = EntityManager.getAllSortedHostileEntities(partialTrackerKey, partialProjectileKey);
        int numHostilePairs = hostileEntities.size() / 2; // 1 pair == 1 warningTriangle + 1 projectile

        // limit logic to 3 levels as too many projectiles will destroy game balance
        if (numHostilePairs != simulationLifecycleManager.getLevel() && simulationLifecycleManager.getLevel() != 3){
            entityManager.addEntity(new EntityTexture(partialTrackerKey + simulationLifecycleManager.getLevel(), "assets/Pictures/RedTriangle.png", 40, 30, Gdx.graphics.getWidth() - 40, Gdx.graphics.getHeight() / 2f - 300, 200, false, true, true));
            entityManager.addEntity(new EntityTexture(partialProjectileKey + simulationLifecycleManager.getLevel(), "assets/Pictures/meteor.png", 120, 100, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2f - 300, 500, false, true, true));
        }
        hostileEntities = EntityManager.getAllSortedHostileEntities(partialTrackerKey, partialProjectileKey);

        EntityTexture player = (EntityTexture) entityManager.getPlayerEntity();

        float delta = Gdx.graphics.getDeltaTime();

        if (isChasing) {
            trackTarget(hostileEntities, player);
        } else {
            handleWarningPhase(hostileEntities, delta);
            if (warningTimer <= 0) {
                launchProjectile(hostileEntities);
            }
        }
    }

    private void trackTarget(ArrayList<Entity> hostileEntities, EntityTexture entityToTrack) {
        float delta = Gdx.graphics.getDeltaTime();
        if (simulationLifecycleManager.getLevel() == 1){
            EntityTexture tracker = (EntityTexture) hostileEntities.get(0);
            EntityTexture projectile = (EntityTexture) hostileEntities.get(1);

            float entityToTrackMidY = entityToTrack.getY() + entityToTrack.getHeight()/2.0f;
            float aiTrackerMidY = tracker.getY() + tracker.getHeight()/2.0f;
            float distanceTargetY = entityToTrackMidY - aiTrackerMidY;

            tracker.setY(tracker.getY() + Math.signum(distanceTargetY) * tracker.getSpeed() * delta);
            projectile.setY(aiTrackerMidY - projectile.getHeight()/2.0f);

            // stop chasing when target is caught
            if (Math.abs(aiTrackerMidY - entityToTrackMidY) < 3) {
                warningTimer = 3.0f;
                musicManager.playSoundEffect("Warning");
                isChasing = false;
            }
        } else if (simulationLifecycleManager.getLevel() == 2 || simulationLifecycleManager.getLevel() == 3){

            EntityTexture tracker1 = (EntityTexture) hostileEntities.get(0);
            EntityTexture tracker2 = (EntityTexture) hostileEntities.get(2);
            EntityTexture projectile1 = (EntityTexture) hostileEntities.get(1);
            EntityTexture projectile2 = (EntityTexture) hostileEntities.get(3);

            float entityToTrackMidY = entityToTrack.getY() + entityToTrack.getHeight()/2.0f;

            float aiTrackerMidY1 = tracker1.getY() + tracker1.getHeight()/2.0f;
            float aiTrackerMidY2 = tracker2.getY() + tracker2.getHeight()/2.0f;
            float distanceTargetY1 = entityToTrackMidY - aiTrackerMidY1;
            float distanceTargetY2 = entityToTrackMidY - aiTrackerMidY2;

            tracker1.setY(tracker1.getY() + Math.signum(distanceTargetY1) * tracker1.getSpeed() * delta);
            projectile1.setY(aiTrackerMidY1 - projectile1.getHeight()/2.0f);

            tracker2.setY(tracker2.getY() + Math.signum(distanceTargetY2) * tracker2.getSpeed() * delta);
            projectile2.setY(aiTrackerMidY2 - projectile2.getHeight()/2.0f);

            // stop chasing when target is caught
            if (Math.abs(aiTrackerMidY1 - entityToTrackMidY) < 10 || Math.abs(aiTrackerMidY2 - entityToTrackMidY) < 10) {
                warningTimer = 3.0f;
                musicManager.playSoundEffect("Warning");
                isChasing = false;
            }
        }

    }

    private void handleWarningPhase(ArrayList<Entity> hostileEntities, float delta) {
        shapeRenderer = new ShapeRenderer();

        if (simulationLifecycleManager.getLevel() == 1){
            EntityTexture tracker1 = (EntityTexture) hostileEntities.get(0);
            if (warningTimer > 0) {
                showWarning();
                drawProjectileTrajectory(tracker1);
                warningTimer -= delta;
            } else {
                musicManager.stopSoundEffect("Warning");
            }
        } else if (simulationLifecycleManager.getLevel() == 2 || simulationLifecycleManager.getLevel() == 3){
            EntityTexture tracker1 = (EntityTexture) hostileEntities.get(0);
            EntityTexture tracker2 = (EntityTexture) hostileEntities.get(2);
            if (warningTimer > 0) {
                showWarning();
                drawProjectileTrajectory(tracker1);
                drawProjectileTrajectory(tracker2);
                warningTimer -= delta;
            } else {
                musicManager.stopSoundEffect("Warning");
            }
        }
    }
    private void showWarning() {
        batch.begin();
        batch.draw(warningPixmap, 600, 400);
        batch.end();
    }

    private void drawProjectileTrajectory(EntityTexture tracker) {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.line(Gdx.graphics.getWidth() - tracker.getWidth(), tracker.getY() + tracker.getHeight() / 2.0f, 0, tracker.getY() + tracker.getHeight() / 2.0f);
        shapeRenderer.end();
    }

    private void launchProjectile(ArrayList<Entity> hostileEntities) {
        float delta = Gdx.graphics.getDeltaTime();

        if (simulationLifecycleManager.getLevel() == 1){
            EntityTexture projectile = (EntityTexture)hostileEntities.get(1);
            projectile.setSpeed(500);
            projectile.setX(projectile.getX() - projectile.getSpeed() * delta);
            musicManager.playSoundEffect("Missile");
            if (projectile.getX() + projectile.getWidth() < 0) {
                projectile.setX(1600);
                isChasing = true;
            }
        } else if (simulationLifecycleManager.getLevel() == 2 || simulationLifecycleManager.getLevel() == 3){
            EntityTexture projectile1 = (EntityTexture)hostileEntities.get(1);
            EntityTexture projectile2 = (EntityTexture)hostileEntities.get(3);

            if (simulationLifecycleManager.getLevel() == 2){
                // level 2
                projectile1.setSpeed(800);
                projectile2.setSpeed(800);
            } else {
                // level 3
                projectile1.setSpeed(1000);
                projectile2.setSpeed(1000);
            };

            if (projectile1.getX() != projectile2.getX()){
                //reset x positions
                projectile1.setX(1600);
                projectile2.setX(1600);
            } else {
                projectile1.setX(projectile1.getX() - projectile1.getSpeed() * delta);
                projectile2.setX(projectile2.getX() - projectile2.getSpeed() * delta);
            }

            musicManager.playSoundEffect("Missile");
            if (projectile1.getX() + projectile1.getWidth() < 0 && projectile2.getX() + projectile2.getWidth() < 0) {
                projectile1.setX(1600);
                projectile2.setX(1600);
                isChasing = true;
            }
        }
    }

    private Character findFirstDifference(String collected, String levelName) {
        if (collected.length() != levelName.length()) {
            return levelName.charAt(collected.length());
        } else {
            return null;
        }
    }

    private void spawnLetters(float delta){
        Character nextKey = findFirstDifference(simulationLifecycleManager.getLettersCollected(), simulationLifecycleManager.getLevelName());
        if (nextKey != null){
            EntityTexture nextLetterEntity = (EntityTexture) entityManager.getEntityByKey(nextKey.toString());
            nextLetterEntity.setX(nextLetterEntity.getX() - nextLetterEntity.getSpeed() * delta);
            if (nextLetterEntity.getX() + nextLetterEntity.getWidth() < 0) {
                float newY = MathUtils.random(20, (int) (Gdx.graphics.getHeight() - nextLetterEntity.getHeight()));
                nextLetterEntity.setX(Gdx.graphics.getWidth() + 50);
                nextLetterEntity.setY(newY);
            }
        } else {
            // all letters collected sequentially, proceed to next level
            simulationLifecycleManager.transition();
        }
    }

    private void spawnBuff(float delta){
        EntityTexture buffEntity = (EntityTexture) entityManager.getEntityByKey("Powerup");

        buffEntity.setX(buffEntity.getX() - buffEntity.getSpeed() * delta);

        if (buffEntity.getX() + buffEntity.getWidth() < 0) {
            float newY = MathUtils.random(20, (int) (Gdx.graphics.getHeight() - buffEntity.getHeight()));
            buffEntity.setX(Gdx.graphics.getWidth() + 50);
            buffEntity.setY(newY);
        }


        if (simulationLifecycleManager.getLevel() == 1){
            buffEntity.setSpeed(600);
        } else if (simulationLifecycleManager.getLevel() == 2){
            buffEntity.setSpeed(700);
        } else if (simulationLifecycleManager.getLevel() == 3){
            buffEntity.setSpeed(800);
        }
    }


    private void spawnDebuff(float delta){
        EntityTexture debuffEntity = (EntityTexture) entityManager.getEntityByKey("Debuff");
        debuffEntity.setX(debuffEntity.getX() - debuffEntity.getSpeed() * delta);

        if (debuffEntity.getX() + debuffEntity.getWidth() < 0) {
            float newY = MathUtils.random(20, (int) (Gdx.graphics.getHeight() - debuffEntity.getHeight()));
            debuffEntity.setX(Gdx.graphics.getWidth() + 50);
            debuffEntity.setY(newY);
        }

        if (simulationLifecycleManager.getLevel() == 1){
            debuffEntity.setSpeed(600);
        } else if (simulationLifecycleManager.getLevel() == 2){
            debuffEntity.setSpeed(700);
        } else if (simulationLifecycleManager.getLevel() == 3){
            debuffEntity.setSpeed(800);
        }
    }
    private void spawnPoints(float delta){
        EntityTexture pointEntity = (EntityTexture) entityManager.getEntityByKey("Points");
        pointEntity.setX(pointEntity.getX() - pointEntity.getSpeed() * delta);

        if (pointEntity.getX() + pointEntity.getWidth() < 0) {
            float newY = MathUtils.random(20, (int) (Gdx.graphics.getHeight() - pointEntity.getHeight()));
            pointEntity.setX(Gdx.graphics.getWidth() + 50);
            pointEntity.setY(newY);
        }

        if (simulationLifecycleManager.getLevel() == 1){
            pointEntity.setSpeed(600);
        } else if (simulationLifecycleManager.getLevel() == 2){
            pointEntity.setSpeed(700);
        } else if (simulationLifecycleManager.getLevel() == 3){
            pointEntity.setSpeed(800);
        }
    }

    public void spawnCollectibles(){
        float delta = Gdx.graphics.getDeltaTime();

        //spawn letters
        spawnLetters(delta);

        //buffs
        spawnBuff(delta);

        //debuffs
        spawnDebuff(delta);

        //points
        spawnPoints(delta);
    }

    public float getDelayTimer()
    {
        return delayTimer;
    }
    public void setDelayTimer(float delayTimer)
    {
        this.delayTimer = delayTimer;
    }

    public void dispose(){
        shapeRenderer.dispose();
    }


}

