package Managers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private final Map<String, Screen> scenes;
    private final Game game;
    public SceneManager(Game game) {
        this.scenes = new HashMap<>();
        this.game = game;
    }
    public void addScene(String sceneKey, Screen scene) {
        scenes.put(sceneKey, scene);
    }
    public void showScene(String sceneKey) {
        Screen sceneToShow = scenes.get(sceneKey);
        if (sceneToShow != null) {
            game.setScreen(sceneToShow);
        } else {
            System.out.println("Scene with ID '" + sceneKey + "' not found.");
        }
    }
}
