package Scenes;

import Managers.MusicManager;
import Managers.SimulationLifecycleManager;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.InputAdapter;

import static Managers.SimulationLifecycleManager.skin;

public class SettingsScene implements Screen {
    private Stage stage;
    private Slider bgMusicVolumeSlider;
    private CheckBox bgMusicMuteCheckBox;
    private Slider inGameMusicVolumeSlider;
    private CheckBox inGameMusicMuteCheckBox;
    private Slider endGameMusicVolumeSlider;
    private CheckBox endGameMusicMuteCheckBox;
    private Slider soundEffectVolumeSlider;
    private CheckBox soundEffectMuteCheckBox;
    private Label currentKeyBindLabel;
    private final SimulationLifecycleManager simulationLifecycleManager;
    private float bgMusicVolume;
    private boolean bgMusicMuted;
    private float inGameMusicVolume;
    private boolean inGameMusicMuted;
    private float endGameMusicVolume;
    private boolean endGameMusicMuted;
    private float soundEffectVolume;
    private boolean soundEffectMuted;
    private Preferences preferences;

    public SettingsScene(SimulationLifecycleManager simulationLifecycleManager) {
        this.simulationLifecycleManager = simulationLifecycleManager;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        preferences = Gdx.app.getPreferences("MusicSettings");

        //Title
        Label titleLabel = new Label("Settings", skin);
        titleLabel.setFontScale(3); // Increase font size
        table.add(titleLabel).colspan(4).padBottom(100).row();

        // Background Music Settings
        table.add(new Label("Background Music: ", skin)).colspan(2).padBottom(20).padRight(15);

        bgMusicVolumeSlider = new Slider(0, 1, 0.1f, false, skin);
        bgMusicVolumeSlider.setValue(preferences.getFloat("bgMusicVolume"));
        bgMusicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                bgMusicVolume = bgMusicVolumeSlider.getValue();
                preferences.putFloat("bgMusicVolume", bgMusicVolume);
                preferences.flush();
                simulationLifecycleManager.applyMusicSettings();
            }
        });

        bgMusicMuteCheckBox = new CheckBox("Mute", skin);
        bgMusicMuteCheckBox.setChecked(preferences.getBoolean("bgMusicMuted"));
        bgMusicMuteCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                bgMusicMuted = bgMusicMuteCheckBox.isChecked();
                preferences.putBoolean("bgMusicMuted", bgMusicMuted);
                preferences.flush();
                simulationLifecycleManager.applyMusicSettings();
            }
        });

        //table.add(new Label("Volume:", skin)).padRight(10);
        table.add(bgMusicVolumeSlider).width(300).padBottom(20).padRight(20);
        table.add(bgMusicMuteCheckBox).colspan(2).padBottom(20).row();

        // In-Game Music Settings
        table.add(new Label("In-Game Music: ", skin)).colspan(2).padBottom(20).padRight(15);

        inGameMusicVolumeSlider = new Slider(0, 1, 0.1f, false, skin);
        inGameMusicVolumeSlider.setValue(preferences.getFloat("inGameMusicVolume"));
        inGameMusicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                inGameMusicVolume = inGameMusicVolumeSlider.getValue();
                preferences.putFloat("inGameMusicVolume", inGameMusicVolume);
                preferences.flush();
                simulationLifecycleManager.applyMusicSettings();
            }
        });

        inGameMusicMuteCheckBox = new CheckBox("Mute", skin);
        inGameMusicMuteCheckBox.setChecked(preferences.getBoolean("inGameMusicMuted"));
        inGameMusicMuteCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                inGameMusicMuted = inGameMusicMuteCheckBox.isChecked();
                preferences.putBoolean("inGameMusicMuted", inGameMusicMuted);
                preferences.flush();
                simulationLifecycleManager.applyMusicSettings();
            }
        });

        table.add(inGameMusicVolumeSlider).width(300).padBottom(20).padRight(20);
        table.add(inGameMusicMuteCheckBox).colspan(2).padBottom(20).row();

        // End-Game Music Settings
        table.add(new Label("End-Game Music: ", skin)).colspan(2).padBottom(20).padRight(15);

        endGameMusicVolumeSlider = new Slider(0, 1, 0.1f, false, skin);
        endGameMusicVolumeSlider.setValue(preferences.getFloat("endGameMusicVolume"));
        endGameMusicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                endGameMusicVolume = endGameMusicVolumeSlider.getValue();
                preferences.putFloat("endGameMusicVolume", endGameMusicVolume);
                preferences.flush();
                simulationLifecycleManager.applyMusicSettings();
            }
        });

        endGameMusicMuteCheckBox = new CheckBox("Mute", skin);
        endGameMusicMuteCheckBox.setChecked(preferences.getBoolean("endGameMusicMuted"));
        endGameMusicMuteCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                endGameMusicMuted = endGameMusicMuteCheckBox.isChecked();
                preferences.putBoolean("endGameMusicMuted", endGameMusicMuted);
                preferences.flush();
                simulationLifecycleManager.applyMusicSettings();
            }
        });

        table.add(endGameMusicVolumeSlider).width(300).padBottom(20).padRight(20);
        table.add(endGameMusicMuteCheckBox).colspan(2).padBottom(20).row();

        // Sound Effect Settings
        table.add(new Label("Sound Effect: ", skin)).colspan(2).padBottom(20).padRight(15);

        soundEffectVolumeSlider = new Slider(0, 1, 0.1f, false, skin);
        soundEffectVolumeSlider.setValue(preferences.getFloat("soundEffectVolume"));
        soundEffectVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundEffectVolume = soundEffectVolumeSlider.getValue();
                preferences.putFloat("soundEffectVolume", soundEffectVolume);
                preferences.flush();
                simulationLifecycleManager.applyMusicSettings();
            }
        });

        soundEffectMuteCheckBox = new CheckBox("Mute", skin);
        soundEffectMuteCheckBox.setChecked(preferences.getBoolean("soundEffectMuted"));
        soundEffectMuteCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundEffectMuted = soundEffectMuteCheckBox.isChecked();
                preferences.putBoolean("soundEffectMuted", soundEffectMuted);
                preferences.flush();
                simulationLifecycleManager.applyMusicSettings();
            }
        });

        table.add(soundEffectVolumeSlider).width(300).padBottom(20).padRight(20);
        table.add(soundEffectMuteCheckBox).colspan(2).padBottom(20).row();

        // Label to show current key bind
        currentKeyBindLabel = new Label("Current Up Key: " + Input.Keys.toString(simulationLifecycleManager.getUpKeyBinding()), skin);
        table.add(currentKeyBindLabel).colspan(2).padBottom(20);
        currentKeyBindLabel.setFontScale(1.5f);


        // Start of the new code block for "Change Up Key" button
        TextButton changeUpKeyButton = new TextButton("Change Up Key", skin);
        changeUpKeyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Show the first dialog and keep a reference to it
                Dialog firstDialog = simulationLifecycleManager.getDialogService().showNoDialog(stage, "Press any key to change 'Up'");

                Gdx.input.setInputProcessor(new InputAdapter() {
                    @Override
                    public boolean keyDown(int keycode) {
                        // Close the first dialog before showing the next
                        firstDialog.hide();

                        // Update the key binding for "Up" key
                        simulationLifecycleManager.updateUpKeyBinding(keycode);

                        // Show the second dialog
                        simulationLifecycleManager.getDialogService().showDialog(stage, "The 'Up' key has been changed to " + Input.Keys.toString(keycode) + ".\n\nPlease remember to save the key binds.");

                        // Reset the input processor to the stage
                        Gdx.input.setInputProcessor(stage);
                        return true;
                    }
                });
            }
        });
        table.add(changeUpKeyButton).colspan(4).padBottom(20).row();

        // Back Button
        TextButton backButton = new TextButton("Save", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveSettings();
                simulationLifecycleManager.applyMusicSettings();
                simulationLifecycleManager.setTitleScreen(true);
            }
        });
        table.add(backButton).colspan(4);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        currentKeyBindLabel.setText("Current Up Key: " + Input.Keys.toString(simulationLifecycleManager.getUpKeyBinding()));
        simulationLifecycleManager.update(delta);
    }
    private void saveSettings() {
        preferences.putFloat("bgMusicVolume", bgMusicVolume);
        preferences.putBoolean("bgMusicMuted", bgMusicMuted);
        preferences.putFloat("inGameMusicVolume", inGameMusicVolume);
        preferences.putBoolean("inGameMusicMuted", inGameMusicMuted);
        preferences.putFloat("endGameMusicVolume", endGameMusicVolume);
        preferences.putBoolean("endGameMusicMuted", endGameMusicMuted);
        preferences.putFloat("soundEffectVolume", soundEffectVolume);
        preferences.putBoolean("soundEffectMuted", soundEffectMuted);
        preferences.flush();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}
