package Managers;

import Objects.Entity;
import Objects.EntityCircle;
import Objects.EntityTexture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class PlayerControlManager {

    private SimulationLifecycleManager simulationLifecycleManager;
    public PlayerControlManager(SimulationLifecycleManager simulationLifecycleManager){
        this.simulationLifecycleManager = simulationLifecycleManager;
    }
    private boolean isUpPressed;
    private int upKeyCode = Input.Keys.UP;

    public void handleKeyPress(int keycode) {
        if (keycode == upKeyCode) {
            isUpPressed = true;
        }
    }

    public void handleKeyRelease(int keycode) {
        if (keycode == upKeyCode) {
            isUpPressed = false;
        }
    }

    public void updatePlayerPosition(EntityManager entityManager) {
        float delta = Gdx.graphics.getDeltaTime();
        Entity player = entityManager.getPlayerEntity();
        String entityType = entityManager.getEntityType(player);
        float newY;
        float gravity = 250;

        if(entityType.equals("texture")){
            // dealing with texture
            EntityTexture playerTexture = (EntityTexture) player;
            newY = playerTexture.getY() - gravity * delta;
            // Calculate the maximum and minimum allowed Y positions
            float maxAllowedY = Gdx.graphics.getHeight() - playerTexture.getHeight();
            float minAllowedY = 0;

            // Ensure the player stays within the screen boundaries
            newY = Math.min(newY, maxAllowedY);
            newY = Math.max(newY, minAllowedY);

            if (newY > 0) {
                player.setY(newY);
            }
        } else {
            // dealing with circle
            EntityCircle playerCircle = (EntityCircle) player;
            newY = playerCircle.getY() - gravity * delta;

            if (isUpPressed) {
                newY = player.getY() + player.getSpeed() * delta;
            }
            // Calculate the maximum and minimum allowed Y positions
            float maxAllowedY = Gdx.graphics.getHeight() - playerCircle.getRadius();
            float minAllowedY = 0;

            // Ensure the player stays within the screen boundaries
            newY = Math.min(newY, maxAllowedY);
            newY = Math.max(newY, minAllowedY);

            if (newY > 0) {
                player.setY(newY);
            }
        }
    }
    public void updatePlayerPosition(EntityManager entityManager, Texture upTexture, Texture downTexture) {
        float delta = Gdx.graphics.getDeltaTime();
        Entity player = entityManager.getPlayerEntity();
        String entityType = entityManager.getEntityType(player);
        float newY;
        float gravity = 250;

        if(entityType.equals("texture")){
            // dealing with texture
            EntityTexture playerTexture = (EntityTexture) player;
            newY = playerTexture.getY() - gravity * delta;

            if (isUpPressed) {
                newY = player.getY() + player.getSpeed() * delta;
                simulationLifecycleManager.showPlayerFlying();
            } else {
                simulationLifecycleManager.showPlayerFalling();
            }
            // Calculate the maximum and minimum allowed Y positions
            float maxAllowedY = Gdx.graphics.getHeight() - playerTexture.getHeight();
            float minAllowedY = 0;

            // Ensure the player stays within the screen boundaries
            newY = Math.min(newY, maxAllowedY);
            newY = Math.max(newY, minAllowedY);

            if (newY > 0) {
                player.setY(newY);
            }
        } else {
            // dealing with circle
            EntityCircle playerCircle = (EntityCircle) player;
            newY = playerCircle.getY() - gravity * delta;

            if (isUpPressed) {
                newY = player.getY() + player.getSpeed() * delta;
            }
            // Calculate the maximum and minimum allowed Y positions
            float maxAllowedY = Gdx.graphics.getHeight() - playerCircle.getRadius();
            float minAllowedY = 0;

            // Ensure the player stays within the screen boundaries
            newY = Math.min(newY, maxAllowedY);
            newY = Math.max(newY, minAllowedY);

            if (newY > 0) {
                player.setY(newY);
            }
        }
    }

    public int getUpKeyCode() {
        return upKeyCode;
    }

    public void setUpKeyCode(int upKeyCode) {
        this.upKeyCode = upKeyCode;
    }
}

