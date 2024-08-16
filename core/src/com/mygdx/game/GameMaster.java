package com.mygdx.game;

import Managers.SimulationLifecycleManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GameMaster extends Game {
	@Override
	public void create() {
		SimulationLifecycleManager simulationLifecycleManager = new SimulationLifecycleManager(this);
		simulationLifecycleManager.initialize();

	}

	@Override
	public void render () {
		// Clear the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

	@Override
	public void dispose () {
		super.dispose();
	}
}

