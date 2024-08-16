package Objects;

public abstract class Entity {
    private String key;
    private float x;
    private float y;
    private float speed;
    private boolean isPlayer;
    private boolean isAI;
    private boolean isHostile;
    private boolean isPoints;
    private boolean isActive;
    public Entity(String key, float x, float y, float speed, boolean isPlayer, boolean isAI, boolean isHostile){
        this.key = key;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.isPlayer = isPlayer;
        this.isAI = isAI;
        this.isHostile = isHostile;
    }
    public boolean getIsAI() {
        return isAI;
    }
    public void setIsAI(boolean isAI) {
        this.isAI = isAI;
    }
    public boolean getIsPlayer() {return isPlayer;}
    public void setIsPlayer(boolean isPlayer) {
        this.isPlayer = isPlayer;
    }
    public boolean getIsHostile() {return isHostile;}
    public void setIsHostile(boolean isHostile) {
        this.isHostile = isHostile;
    }
    public boolean getIsPoints() {return isPoints;}
    public void setIsPoints(boolean isPoints) {
        this.isPoints = isPoints;
    }
    public boolean getIsActive() {return isActive;}
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public float getSpeed() {
        return speed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public abstract void draw();
}
