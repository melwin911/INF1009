package Scenes;

import Managers.SimulationLifecycleManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static Managers.SimulationLifecycleManager.*;
import static Managers.SimulationLifecycleManager.AESTHETIC_ASTRONAUT_IMG_PATH;

public class TitleScene implements Screen {
    private Stage stage;
    private TextField nameTextField;
    private final SimulationLifecycleManager simulationLifecycleManager;

    public TitleScene(SimulationLifecycleManager simulationLifecycleManager) {
        this.simulationLifecycleManager = simulationLifecycleManager;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(1920, 1080));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);
        addSpecialImages();

        Label titleLabel = new Label("Rocket Robert", skin);
        titleLabel.setFontScale(2.0f);
        titleLabel.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1.5f)));
        table.add(titleLabel).center().padBottom(15).row();

        nameTextField = new TextField("", skin);
        nameTextField.setMessageText("Enter Your Name"); // Set placeholder text
        table.add(nameTextField).width(700).height(150).padBottom(15).center().row();

        TextButton startButton = new TextButton("Start Game", skin);
        startButton.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(0.5f), Actions.fadeIn(1.5f)));
        table.add(startButton).width(700).height(150).padBottom(15).center().row();

        TextButton leaderboardButton = new TextButton("Leaderboard", skin);
        leaderboardButton.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(1f), Actions.fadeIn(1.5f)));
        table.add(leaderboardButton).width(700).height(150).padBottom(15).center().row();

        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(1.5f), Actions.fadeIn(1.5f)));
        table.add(settingsButton).width(700).height(150).padBottom(15).center().row();

        TextButton instructionsButton = new TextButton("Instructions", skin);
        instructionsButton.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(2f), Actions.fadeIn(1.5f)));
        table.add(instructionsButton).width(700).height(150).padBottom(15).center().row();

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addAction(Actions.sequence(Actions.alpha(0), Actions.delay(2.5f), Actions.fadeIn(1.5f)));
        table.add(exitButton).width(700).height(150).padBottom(15).center().row();

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String playerName = nameTextField.getText();
                if (isValidPlayerName(playerName)) {
                    System.out.println("Starting game with player name: " + playerName);
                    simulationLifecycleManager.getPlayerData().setName(playerName);
                    simulationLifecycleManager.setShowGameScreen(true);
                } else {
                    simulationLifecycleManager.getDialogService().showDialog(stage, "Error:\n\nInvalid player name!\nPlease enter a valid name.");
                    stage.setKeyboardFocus(nameTextField);

                }
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                simulationLifecycleManager.setShowSettingsScreen(true);
            }
        });
        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                simulationLifecycleManager.setLeaderboardScreen(true);
            }
        });
        instructionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                simulationLifecycleManager.setShowInstructionsScreen(true);
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                simulationLifecycleManager.setIsExitGame(true);
            }
        });

        stage.addActor(table);
    }

    private void addSpecialImages() {
        placeSpecialImage(AESTHETIC_METEOR_IMG_PATH, 1500, 600);
        placeSpecialImage(AESTHETIC_EARTH_IMG_PATH, 1500, 40);
        placeSpecialImage(AESTHETIC_ASTRONAUT_IMG_PATH, 20, 20);
        placeSpecialImage(AESTHETIC_SPACE_IMG_PATH, 20, 600);
    }

    private void placeSpecialImage(String fileName, float x, float y) {
        Image image = new Image(new Texture(Gdx.files.internal(fileName)));
        image.setSize(400, 400);
        image.setPosition(x, y);
        image.getColor().a = 0;
        image.addAction(Actions.fadeIn(3.0f));
        stage.addActor(image);
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
        // Method body can be left empty if not used
    }

    @Override
    public void pause() {
        // Method body can be left empty if not used
    }

    @Override
    public void resume() {
        // Method body can be left empty if not used
    }

    @Override
    public void hide() {
        // Method body can be left empty if not used
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private boolean isValidPlayerName(String playerName) {
        return !playerName.trim().isEmpty();
    }
}
