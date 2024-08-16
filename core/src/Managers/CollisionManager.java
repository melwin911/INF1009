package Managers;

import Objects.Entity;
import Objects.EntityCircle;
import Objects.EntityTexture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Objects;
    public class CollisionManager {
        private final EntityManager entityManager;
        private final SimulationLifecycleManager simulationLifecycleManager;
        public final MusicManager musicManager;

        public CollisionManager(EntityManager entityManager, SimulationLifecycleManager simulationLifecycleManager, MusicManager musicManager) {
            this.entityManager = entityManager;
            this.simulationLifecycleManager = simulationLifecycleManager;
            this.musicManager = musicManager;
        }
        public void handleCollision(EntityManager entityManager) {
            Entity playerEntity = entityManager.getPlayerEntity();
            ArrayList<Entity> aiEntities = EntityManager.getAllAiEntities();

            Entity aiEntityCollided = playerCollidedWithEntity(playerEntity, aiEntities);
            if (aiEntityCollided != null) {
                if (aiEntityCollided.getIsHostile()) {
                    // mortal collision, end game
                    simulationLifecycleManager.setIsEndGame(true);
                } else {
                    aiCollectibleBehavior(aiEntityCollided, playerEntity);
                }
            }
        }

        private void aiCollectibleBehavior(Entity aiCollectible, Entity playerEntity)
        {
            if(aiCollectible.getKey().equals("Powerup"))
            {
                playerEntity.setSpeed(playerEntity.getSpeed() + 200);
                aiCollectible.setX(3000);
                aiCollectible.setY(MathUtils.random(20, Gdx.graphics.getHeight()));
                musicManager.playSoundEffect("Buff");
            } else if (aiCollectible.getKey().equals("Debuff")) {
                playerEntity.setSpeed(playerEntity.getSpeed() - 200);
                aiCollectible.setX(3000);
                aiCollectible.setY(MathUtils.random(20, Gdx.graphics.getHeight()));
                musicManager.playSoundEffect("Debuff");
            } else if (aiCollectible.getKey().equals("Points")) {
                simulationLifecycleManager.setScore(simulationLifecycleManager.getScore() + 1);
                aiCollectible.setX(3000);
                aiCollectible.setY(MathUtils.random(20, Gdx.graphics.getHeight()));
                musicManager.playSoundEffect("Points");
            }
            else
            {
                // collected letters
                simulationLifecycleManager.setScore(simulationLifecycleManager.getScore() + 2);
                simulationLifecycleManager.appendLettersCollected(aiCollectible.getKey());
                musicManager.playSoundEffect("Points");
                aiCollectible.setX(3000);
                aiCollectible.setY(MathUtils.random(20, Gdx.graphics.getHeight()));
            }

            System.out.println("Player Speed: " + playerEntity.getSpeed());
        }

        private Entity playerCollidedWithEntity(Entity playerEntity, ArrayList<Entity> entitiesToCheck) {
            String playerType = entityManager.getEntityType(playerEntity);
            for (Entity entityToCheck : entitiesToCheck) {
                String entityToCheckType = entityManager.getEntityType(entityToCheck);
                if (playerType.equals("texture") && entityToCheckType.equals("texture"))
                {
                    EntityTexture playerTexture = (EntityTexture) playerEntity;
                    EntityTexture EntityCheckTexture = (EntityTexture) entityToCheck;
                    // Texture (Player) & Texture collision
                    if (checkCollision(playerTexture, EntityCheckTexture))
                        return entityToCheck;
                } else if (playerType.equals("texture") && entityToCheckType.equals("circle")) {
                    EntityTexture playerTexture = (EntityTexture) playerEntity;
                    EntityCircle EntityCheckCircle = (EntityCircle) entityToCheck;
                    // Texture (Player) & Circle collision
                    if (checkCollision(playerTexture, EntityCheckCircle))
                        return entityToCheck;
                } else if (playerType.equals("circle") && entityToCheckType.equals("texture")) {
                    EntityCircle playerCircle = (EntityCircle) playerEntity;
                    EntityTexture EntityCheckTexture = (EntityTexture) entityToCheck;
                    // Circle (Player) & Texture collision
                    if (checkCollision(EntityCheckTexture, playerCircle)) // Note the parameters are swapped
                        return entityToCheck;
                } else if (Objects.equals(playerType, "circle") && Objects.equals(entityToCheckType, "circle")) {
                    EntityCircle playerCircle = (EntityCircle) playerEntity;
                    EntityCircle EntityCheckCircle = (EntityCircle) entityToCheck;
                    // Circle (Player) & Circle collision
                    if (checkCollision(playerCircle, EntityCheckCircle))
                        return entityToCheck;
                }
            }
            return null;
        }
        private boolean checkCollision(EntityTexture firstTextureEntity, EntityTexture secondTextureEntity) {
            return firstTextureEntity.getX() < secondTextureEntity.getX() + secondTextureEntity.getWidth() &&
                    firstTextureEntity.getX() + firstTextureEntity.getWidth() > secondTextureEntity.getX() &&
                    firstTextureEntity.getY() < secondTextureEntity.getY() + secondTextureEntity.getHeight() &&
                    firstTextureEntity.getY() + firstTextureEntity.getHeight() > secondTextureEntity.getY();
        }
        private boolean checkCollision(EntityTexture textureEntity, EntityCircle circleEntity) {
            float circleDiameter = circleEntity.getRadius() * 2;
            return textureEntity.getX() < circleEntity.getX() + circleDiameter &&
                    textureEntity.getX() + textureEntity.getWidth() > circleEntity.getX() &&
                    textureEntity.getY() < circleEntity.getY() + circleDiameter &&
                    textureEntity.getY() + textureEntity.getHeight() > circleEntity.getY();
        }
        private boolean checkCollision(EntityCircle firstCircleEntity, EntityCircle secondCircleEntity) {
            double distance = Math.sqrt(Math.pow(firstCircleEntity.getX() - secondCircleEntity.getX(), 2) + Math.pow(firstCircleEntity.getY() - secondCircleEntity.getY(), 2));
            float minDistance = firstCircleEntity.getRadius() + secondCircleEntity.getRadius();
            return distance < minDistance;
        }
    }

