package Managers;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
public class IOManager extends InputAdapter {
    private final PlayerControlManager playerControlManager;
    public IOManager(PlayerControlManager playerControlManager) {
        this.playerControlManager = playerControlManager;
        Gdx.input.setInputProcessor(this);
    }
    @Override
    public boolean keyDown(int keycode) {
        playerControlManager.handleKeyPress(keycode);
        return true;
    }
    @Override
    public boolean keyUp(int keycode) {
        playerControlManager.handleKeyRelease(keycode);
        return true;
    }
}
