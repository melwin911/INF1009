package Scenes;

import Managers.SimulationLifecycleManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static Managers.SimulationLifecycleManager.*;

public class GameScene implements Screen {
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private float backgroundOffsetX = 0;
    private Stage stage;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private final SimulationLifecycleManager simulationLifecycleManager;
    private Label PlayerScoreLabel;
    private Label CurrentLevelLabel;
    private Label CurrentWordLabel;
    private Array<Label> letterLabels = new Array<>();
    private Label TimerLabel;
    private Texture background;
    public GameScene(SimulationLifecycleManager simulationLifecycleManager)
    {
        this.simulationLifecycleManager = simulationLifecycleManager;
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();
    }
    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        PlayerScoreLabel = new Label("Score: " + simulationLifecycleManager.getScore(), skin);
        PlayerScoreLabel.setPosition(20, Gdx.graphics.getHeight() - PlayerScoreLabel.getHeight() - 10);
        PlayerScoreLabel.setFontScale(1.7f);
        stage.addActor(PlayerScoreLabel);

        CurrentLevelLabel = new Label("Level: " + simulationLifecycleManager.getLevel(), skin);
        CurrentLevelLabel.setPosition(260, Gdx.graphics.getHeight() - CurrentLevelLabel.getHeight() - 10);
        CurrentLevelLabel.setFontScale(1.7f);
        stage.addActor(CurrentLevelLabel);

        CurrentWordLabel = new Label("Letters to collect: ", skin);
        CurrentWordLabel.setPosition(480, Gdx.graphics.getHeight() - CurrentWordLabel.getHeight() - 10);
        CurrentWordLabel.setFontScale(1.7f);
        stage.addActor(CurrentWordLabel);

        TimerLabel = new Label("Time Left: " + "60", skin);
        TimerLabel.setPosition(1150, Gdx.graphics.getHeight() - TimerLabel.getHeight() - 10);
        TimerLabel.setFontScale(1.7f);
        stage.addActor(TimerLabel);

        updateLetterLabels();
    }

    private void updateLetterLabels() {
        // Clear previous letter labels
        for (Label label : letterLabels) {
            label.remove();
        }

        letterLabels.clear();
        simulationLifecycleManager.updateLevelName();
        // Get the new word for the current level
        String word = simulationLifecycleManager.getLevelName();

        // Add labels for each letter in the new word
        for (int i = 0; i < word.length(); i++) {
            Label letterLabel = new Label(word.substring(i, i + 1), skin);
            letterLabel.setPosition(880 + i * 45, Gdx.graphics.getHeight() - CurrentWordLabel.getHeight() - 10);
            letterLabel.setFontScale(1.7f);
            letterLabel.setColor(Color.RED);
            stage.addActor(letterLabel);
            letterLabels.add(letterLabel);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        float backgroundScrollSpeed = 30f;

        backgroundOffsetX = (backgroundOffsetX + backgroundScrollSpeed * delta) % Gdx.graphics.getWidth();

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        if (simulationLifecycleManager.getLevel() == 3){
            background = SATURN_BACKGROUND;
        } else if (simulationLifecycleManager.getLevel() == 2) {
            background = MOON_BACKGROUND;
        } else if (simulationLifecycleManager.getLevel() == 1){
            background = MARS_BACKGROUND;
        }


        batch.begin();
            batch.draw(background, -backgroundOffsetX, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.draw(background, Gdx.graphics.getWidth() - backgroundOffsetX, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        PlayerScoreLabel.setText("Score: " + simulationLifecycleManager.getScore());
        CurrentLevelLabel.setText("Level: " + simulationLifecycleManager.getLevel());
        TimerLabel.setText("Time Left: " + (int) simulationLifecycleManager.getTimeRemaining() + "s");

        for (int i = 0; i < simulationLifecycleManager.getLettersCollected().length(); i++) {
            Label letterLabel = letterLabels.get(i);
            letterLabel.setColor(Color.GREEN);
        }

        simulationLifecycleManager.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}