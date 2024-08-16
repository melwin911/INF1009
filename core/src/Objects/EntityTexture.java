package Objects;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EntityTexture extends Entity{
    private Texture texture;
    private final SpriteBatch batch;
    private float width;
    private float height;
    public EntityTexture(String key, String imagePath, float width, float height, float x, float y, float speed, boolean isPlayer, boolean isAI, boolean isHostile) {
        super(key, x, y, speed, isPlayer, isAI, isHostile);
        this.texture = new Texture(imagePath);
        this.batch = new SpriteBatch();
        this.width = width;
        this.height = height;
    }
    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public void draw() {
        batch.begin();
            batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        batch.end();
    }
}
