package Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class EntityCircle extends Entity{
    private Color color;
    private float radius;
    private final ShapeRenderer shapeRenderer;
    public EntityCircle(String key, Color color, float radius, float x, float y, float speed, boolean isPlayer, boolean isAI, boolean isHostile) {
        super(key, x, y, speed, isPlayer, isAI, isHostile);
        this.color = color;
        this.radius = radius;
        this.shapeRenderer = new ShapeRenderer();
    }
    public float getRadius() {
        return radius;
    }
    public void setRadius(float radius) {
        this.radius = radius;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    @Override
    public void draw() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(color);
            shapeRenderer.circle(getX(), getY(), radius);
        shapeRenderer.end();
    }
}
