package Managers;

import Objects.Entity;
import Objects.EntityCircle;
import Objects.EntityTexture;

import java.util.*;

public class EntityManager {
    private static Map<String, Entity> entityMap = null;
    public EntityManager(){
        entityMap = new HashMap<>();
    }
    public void addEntity(Entity entity){
        entityMap.put(entity.getKey(), entity);
    }
    public void addEntities(Entity[] entities) {
        for(Entity entity : entities) {
            entityMap.put(entity.getKey(), entity);
        }
    }
    public String getEntityType(Entity entity){
        if (entity instanceof EntityTexture){
            return "texture";
        } else if (entity instanceof EntityCircle){
            return "circle";
        }
        return null;
    }
    public Entity getPlayerEntity(){
        for (Entity entity : entityMap.values()) {
            if (entity.getIsPlayer()){
                return entity;
            }
        }
        return null;
    }

    public static ArrayList<Entity> getAllAiEntities(){
        ArrayList<Entity> aiEntities = new ArrayList<>();
        for (Entity entity : entityMap.values()) {
            if (entity.getIsAI()){
                aiEntities.add(entity);
            }
        }
        return aiEntities;
    }

    public Entity getEntityByKey(String key){
        for (Entity entity : entityMap.values()) {
            if (entity.getKey().equals(key)){
                return entity;
            }
        }
        return null;
    }

    public static ArrayList<Entity> getAllSortedHostileEntities(String partialTrackerKey, String partialProjectileKey){
        ArrayList<Entity> unsortedHostileEntities = new ArrayList<>();
        for (Entity entity : entityMap.values()) {
            if (entity.getIsAI() && entity.getIsHostile()){
                unsortedHostileEntities.add(entity);
            }
        }

        ArrayList<Entity> sortedTrackers = new ArrayList<>();
        ArrayList<Entity> sortedProjectiles = new ArrayList<>();

        for (Entity entity : unsortedHostileEntities) {
            if (entity.getKey().startsWith(partialTrackerKey)) {
                sortedTrackers.add(entity);
            } else if (entity.getKey().startsWith(partialProjectileKey)) {
                sortedProjectiles.add(entity);
            }
        }

        sortedTrackers.sort((entity1, entity2) -> {
            int num1 = entity1.getKey().charAt(entity1.getKey().length() - 1);
            int num2 = entity2.getKey().charAt(entity2.getKey().length() - 1);
            return Integer.compare(num1, num2);
        });

        sortedProjectiles.sort((entity1, entity2) -> {
            int num1 = entity1.getKey().charAt(entity1.getKey().length() - 1);
            int num2 = entity2.getKey().charAt(entity2.getKey().length() - 1);
            return Integer.compare(num1, num2);
        });

        // interleave both arrays
        ArrayList<Entity> interleavedEntities = new ArrayList<>();
        int maxLength = Math.max(sortedTrackers.size(), sortedProjectiles.size());
        for (int i = 0; i < maxLength; i++) {
                interleavedEntities.add(sortedTrackers.get(i));
                interleavedEntities.add(sortedProjectiles.get(i));
        }
        return interleavedEntities;
    }

    public void drawEntities(){
        for (Entity entity : entityMap.values()) {
            entity.draw();
        }
    }
}
