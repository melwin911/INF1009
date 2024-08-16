package Scenes;

import Managers.SimulationLifecycleManager;
import Services.DatabaseService;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static Managers.SimulationLifecycleManager.skin;

public class LeaderboardScene implements Screen {
    private Stage stage;
    private final SimulationLifecycleManager simulationLifecycleManager;

    public LeaderboardScene(SimulationLifecycleManager simulationLifecycleManager) {
        this.simulationLifecycleManager = simulationLifecycleManager;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(1920, 1080));
        Gdx.input.setInputProcessor(stage);

        // Inside the show() method
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();
        stage.addActor(mainTable);

        Label leaderboardTitle = new Label("Leaderboard", skin);
        leaderboardTitle.setFontScale(3.0f);
        mainTable.add(leaderboardTitle).pad(20).row();

// Create a new table for the scrollable content
        Table contentTable = new Table();
        contentTable.defaults().pad(10).space(50); // Adjust the spacing

        Array<DatabaseService.LeaderboardEntry> leaderboardData = DatabaseService.loadLeaderboardData();

        for (DatabaseService.LeaderboardEntry entry : leaderboardData) {
            Label usernameLabel = new Label(entry.getUsername(), skin);
            Label pointsLabel = new Label(String.valueOf(entry.getPoints()), skin);
            usernameLabel.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1.5f)));
            pointsLabel.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1.5f)));

            contentTable.add(usernameLabel).expandX().align(Align.center);
            contentTable.add(pointsLabel).expandX().align(Align.center);
            contentTable.row();
        }

        ScrollPane scrollPane = new ScrollPane(contentTable, skin);

// Create a new table for the button
        Table buttonTable = new Table();
        TextButton backButton = new TextButton("Back to Title", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                simulationLifecycleManager.setTitleScreen(true);
            }
        });
        buttonTable.add(backButton).padTop(10).align(Align.center);

// Add the scrollable content and the button table to the main table
        mainTable.add(scrollPane).expand().fill().top().padBottom(buttonTable.getHeight());
        mainTable.row();
        mainTable.add(buttonTable).expandX().bottom().padBottom(20);

        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        simulationLifecycleManager.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Implement if necessary
    }

    @Override
    public void resume() {
        // Implement if necessary
    }

    @Override
    public void hide() {
        // Implement if necessary
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
