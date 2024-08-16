package Scenes;

import Managers.AIControlManager;
import Managers.MusicManager;
import Managers.SimulationLifecycleManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static Managers.SimulationLifecycleManager.skin;

public class EndGameScene implements Screen {
    private Stage stage;
    private Label resultLabel;
    private final SimulationLifecycleManager simulationLifecycleManager;

    public EndGameScene(SimulationLifecycleManager simulationLifecycleManager) {
        this.simulationLifecycleManager = simulationLifecycleManager;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(1920, 1080));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        resultLabel = new Label("Congrats, " + simulationLifecycleManager.getPlayerData().getName() + ", you scored: " + simulationLifecycleManager.getScore(), skin);
        resultLabel.setAlignment(Align.center);
        resultLabel.setFontScale(2);

        TextButton playAgainButton = new TextButton("Play Again", skin);
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                simulationLifecycleManager.setPlayAgainStatus(true);
            }
        });

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        resultLabel.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(2)));
        playAgainButton.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(1), Actions.fadeIn(2)));
        exitButton.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(2), Actions.fadeIn(2)));

        table.add(resultLabel).colspan(2).padBottom(20).row();
        table.add(playAgainButton).size(500, 100).padTop(20).colspan(2).row();
        table.add(exitButton).size(500, 100).padTop(20).colspan(2).row();
        table.align(Align.center);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        simulationLifecycleManager.update(delta);

        if(simulationLifecycleManager.getIsGameComplete())
        {
            resultLabel.setText("Congrats, " + simulationLifecycleManager.getPlayerData().getName() + " you have completed the game, your total score is: " + simulationLifecycleManager.getScore());
        }
        else
        {
            resultLabel.setText(simulationLifecycleManager.getPlayerData().getName() + ", your score is: " + simulationLifecycleManager.getScore());
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Ensure the stage resizes properly
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
