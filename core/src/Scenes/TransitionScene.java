package Scenes;

import Managers.SimulationLifecycleManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Random;

import static Managers.SimulationLifecycleManager.skin;

public class TransitionScene implements Screen {
    private Stage stage;
    private final SimulationLifecycleManager simulationLifecycleManager;
    private ArrayList<String> funFactsList;
    private Random random;
    public TransitionScene(SimulationLifecycleManager simulationLifecycleManager) {
        this.simulationLifecycleManager = simulationLifecycleManager;
        funFactsList = new ArrayList<>();
        funFactsList.add("The gravity on the Moon is just one-sixth of the Earth’s, so you would weigh less on the Moon. You could jump higher and carry much heavier things too!");
        funFactsList.add("Solar eclipses occur when the Moon gets between the Earth and the Sun, meaning we can’t see the Sun anymore.");
        funFactsList.add("There are eight planets in the solar system. The four inner planets are Mercury, Venus, Earth and Mars, while the four outer planets are Jupiter, Saturn, Uranus and Neptune.");
        funFactsList.add("Mars is about half the size of Earth. If Earth were the size of a nickel, Mars would be about as big as a raspberry.");
        funFactsList.add("Gravity on Mars is about 3/8th of Earth's gravity. If you weigh 80kg on Earth, you would weigh around 30 kg on Mars.");
        funFactsList.add("Mars is the fourth planet from the Sun in our solar system.");
        funFactsList.add("Saturn is famous for its beautiful rings made of ice, dust, and rocks. Imagine Saturn as a giant planet wearing a sparkling hula hoop!");
        funFactsList.add("The Sun is a star, and it's the biggest and brightest star in our solar system. It gives us light and heat!");
        funFactsList.add("Astronauts eat special food in space, like freeze-dried fruits and vegetables and yummy space ice cream!");
        funFactsList.add("Astronauts eat special food in space, like freeze-dried fruits and vegetables and yummy space ice cream!");
        random = new Random();
    }
    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Label titleLabel = new Label("Level Completed!", skin);
        titleLabel.setFontScale(2);
        table.add(titleLabel).colspan(2).padBottom(20).row();

        TextButton continueButton = new TextButton("Continue", skin);
        TextButton restartButton = new TextButton("Restart", skin);

        float buttonWidth = 500f;

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                simulationLifecycleManager.setShowGameScreen(true);
                simulationLifecycleManager.setIsNextLevel(true);
            }
        });
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                simulationLifecycleManager.setShowGameScreen(true);
                simulationLifecycleManager.setRestartCurrentLevel(true);
            }
        });

        table.add(continueButton).width(buttonWidth).height(100).padBottom(10).colspan(2).row();
        table.add(restartButton).width(buttonWidth).height(100).padBottom(10).colspan(2).row();
        stage.addActor(table);

        String randomFunFact = funFactsList.get(random.nextInt(funFactsList.size()));
        Label funFact = new Label(randomFunFact, skin);
        funFact.setWrap(true);
        funFact.setWidth(700f);
        funFact.setFontScale(1);
        table.add(funFact).width(700f).padBottom(10).colspan(2).padTop(20).row();
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