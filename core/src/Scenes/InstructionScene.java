package Scenes;

import Managers.SimulationLifecycleManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static Managers.SimulationLifecycleManager.*;

public class InstructionScene implements Screen {
    private final SimulationLifecycleManager simulationLifecycleManager;
    private Stage stage;
    private Label titleLabel;

    public InstructionScene(SimulationLifecycleManager simulationLifecycleManager) {
        this.simulationLifecycleManager = simulationLifecycleManager;
    }

    @Override
    public void show() {
        setupStage();
        setupUI();
    }

    private void setupStage() {
        stage = new Stage(new FitViewport(1920, 1080));
        Gdx.input.setInputProcessor(stage);
    }

    private void setupUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        addTitle(mainTable);
        addInstructions(mainTable);
        addSpecialImages();
        addBackButton(mainTable);

        mainTable.top();
    }

    private void addTitle(Table table) {
        titleLabel = new Label("Instructions", skin);
        titleLabel.setFontScale(4.0f);
        table.add(titleLabel).padTop(20).center().row();
    }

    private void addInstructions(Table table) {
        addInstructionWithImage(table, INSTRUCTION_1_IMG_PATH, "Use up arrow key to control astronaut", 2.0f);
        addInstructionWithImage(table, INSTRUCTION_2_IMG_PATH, "Collect letters to spell out different planets while avoiding missiles", 2.0f);
        addInstructionWithImage(table, INSTRUCTION_3_IMG_PATH, "Collect stars to gain points", 2.0f);
    }

    private void addInstructionWithImage(Table table, String imageName, String instructionText, float fontScale) {
        Table rowTable = new Table();
        Image image = new Image(new Texture(Gdx.files.internal(imageName)));
        Label instructionLabel = new Label(instructionText, skin);
        instructionLabel.setFontScale(fontScale);

        rowTable.add(image).size(150, 150).padRight(30);
        rowTable.add(instructionLabel);
        table.add(rowTable).padTop(40).left().row();
    }

    private void addSpecialImages() {
        placeSpecialImage(AESTHETIC_METEOR_IMG_PATH, 1500, 700);
        placeSpecialImage(AESTHETIC_EARTH_IMG_PATH, 1500, 40);
        placeSpecialImage(AESTHETIC_ASTRONAUT_IMG_PATH, 20, 20);
    }

    private void placeSpecialImage(String fileName, float x, float y) {
        Image image = new Image(new Texture(Gdx.files.internal(fileName)));
        image.setSize(400, 400);
        image.setPosition(x, y);
        stage.addActor(image);
    }

    private void addBackButton(Table table) {
        TextButton backButton = new TextButton("Back to Main Menu", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                simulationLifecycleManager.setTitleScreen(true);
            }
        });
        table.add(backButton).padTop(50).center();
        backButton.setWidth(300);
        backButton.setHeight(150);
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
