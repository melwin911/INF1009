package Managers;

import Objects.Entity;
import Objects.EntityTexture;
import Objects.Player;
import Scenes.*;
import Services.DatabaseService;
import Services.DialogService;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import java.util.ArrayList;
import java.util.List;

public class SimulationLifecycleManager {
    private final Game game;
    private static final int SCREEN_WIDTH = 1500;
    private static final int SCREEN_HEIGHT = 780;
    public static final Skin skin = new Skin(Gdx.files.internal("assets/Skin/pixthulhu-ui.json"));
    public static final Texture warningPixmap = new Texture("assets/Pictures/warning.png");
    public static final Texture SATURN_BACKGROUND = new Texture("assets/Pictures/saturn.jpg");
    public static final Texture MOON_BACKGROUND = new Texture("assets/Pictures/moon.jpg");
    public static final Texture MARS_BACKGROUND = new Texture("assets/Pictures/mars.jpg");
    public static final String LEADERBOARD_FILE = "assets/Database/Leaderboard.txt";
    private static final String BG_MUSIC_PATH = "Music/BackgroundMusic.mp3";
    private static final String IN_GAME_MUSIC_PATH = "Music/InGameMusic.mp3";
    private static final String END_GAME_MUSIC_PATH = "Music/GameOverMusic.mp3";
    private static final String GAME_COMPLETE_MUSIC_PATH = "Music/GameCompleteBG.mp3";
    private static final String WARNING_SOUND_EFFECT_PATH = "Music/Warning_SoundEffect.mp3";
    private static final String POINTS_SOUND_EFFECT_PATH = "Music/Points_SoundEffect.mp3";
    private static final String EXPLOSION_SOUND_EFFECT_PATH = "Music/Explosion_SoundEffect.mp3";
    private static final String MISSILE_SOUND_EFFECT_PATH = "Music/Missile_SoundEffect.mp3";
    private static final String BUFF_SOUND_EFFECT_PATH = "Music/Buff_SoundEffect.mp3";
    private static final String DEBUFF_SOUND_EFFECT_PATH = "Music/Debuff_SoundEffect.mp3";
    public static final String INSTRUCTION_1_IMG_PATH = "assets/Pictures/instruction1.png";
    public static final String INSTRUCTION_2_IMG_PATH = "assets/Pictures/instruction2.png";
    public static final String INSTRUCTION_3_IMG_PATH = "assets/Pictures/instruction3.png";
    public static final String AESTHETIC_METEOR_IMG_PATH = "assets/Pictures/meteor.png";
    public static final String AESTHETIC_EARTH_IMG_PATH = "assets/Pictures/earth.png";
    public static final String AESTHETIC_ASTRONAUT_IMG_PATH = "assets/Pictures/astronaut.png";
    public static final String AESTHETIC_SPACE_IMG_PATH = "assets/Pictures/space.png";
    private static final int NUM_ALPHABETS = 26;
    private float LEVEL_UP_INTERVAL = 80f; // Interval for level up in seconds
    private Timer levelTimer;
    private float startTime; // Variable to store the start time of the timer
    private float totalDuration; // Variable to store the total duration of the timer
    private boolean showGameScreen;
    private boolean isEndGame;
    private boolean showSettingsScreen;
    private boolean leaderboardScreen;
    private boolean titleScreen;
    private boolean isPlaying;
    private boolean isExitGame;
    private boolean showInstructionsScreen;
    private boolean inputProcessingInitialized;
    private SceneManager sceneManager;
    private EntityManager entityManager;
    private PlayerControlManager playerControlManager;
    private AIControlManager aiControlManager;
    private CollisionManager collisionManager;
    private MusicManager musicManager;
    private EntityTexture player;
    private EntityTexture Points;
    private EntityTexture PowerUp;
    private EntityTexture Debuff;
    private Texture[] playerTexture;
    private int score;
    private int level;
    private boolean LevelChanged;
    private List<Entity> pointEntities;
    private boolean isLetterCollected;
    private boolean IsAllNotActive = false;
    private Player playerData;
    private DialogService dialogService;
    private DatabaseService databaseService;
    private int playerLvl1Score;
    private  int playerLvl2Score;
    private boolean goNextLevel;
    private boolean isplayAgain;
    private boolean isGameComplete;
    private boolean restartCurrentLevel;
    private boolean isNextLevel;
    private Preferences preferences;

    private String levelName;
    private String lettersCollected;

    public SimulationLifecycleManager(Game game) {
        this.game = game;
        this.showGameScreen = false;
        this.showSettingsScreen = false;
        this.showInstructionsScreen = false;


        this.isPlaying = false;
        this.isEndGame = false;
        this.isExitGame = false;

        this.inputProcessingInitialized = false;

        this.playerData = new Player();
        this.dialogService = new DialogService();
        this.databaseService = new DatabaseService();

        this.level = 1;

        this.goNextLevel = false;
        this.restartCurrentLevel = false;
        this.isplayAgain = false;
        this.isGameComplete = false;

        this.levelName = "";
        this.lettersCollected = "";

        this.pointEntities = new ArrayList<>();
        musicManager = new MusicManager();
        preferences = Gdx.app.getPreferences("KeyBindings");
    }

    public void initialize() {
        Gdx.graphics.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);

        //Music
        musicManager.initialize(BG_MUSIC_PATH, IN_GAME_MUSIC_PATH, END_GAME_MUSIC_PATH, GAME_COMPLETE_MUSIC_PATH,
                                WARNING_SOUND_EFFECT_PATH, EXPLOSION_SOUND_EFFECT_PATH, MISSILE_SOUND_EFFECT_PATH,
                                POINTS_SOUND_EFFECT_PATH, BUFF_SOUND_EFFECT_PATH, DEBUFF_SOUND_EFFECT_PATH);

        player = new EntityTexture("player","assets/Pictures/player_falling.png", 100, 100, 0, Gdx.graphics.getHeight() - 150, 500, true, false, false);
        playerTexture = new Texture[2];
        playerTexture[0] = new Texture("assets/Pictures/player_falling.png");
        playerTexture[1] = new Texture("assets/Pictures/player_flying.png");

        //Collectibles
        Points = new EntityTexture("Points", "assets/Pictures/Star.png", 50, 60, Gdx.graphics.getWidth() + 50, MathUtils.random(30, (Gdx.graphics.getHeight() - 80)), 600, false, true, false);
        PowerUp = new EntityTexture("Powerup", "assets/Pictures/powerup.png", 60, 70, Gdx.graphics.getWidth() + 50, MathUtils.random(20, (Gdx.graphics.getHeight() - 70)), 400, false, true, false);
        Debuff = new EntityTexture("Debuff", "assets/Pictures/Debuff.png", 70, 80, Gdx.graphics.getWidth() + 50, MathUtils.random(10, (Gdx.graphics.getHeight() - 80)), 500, false, true, false);

        entityManager = new EntityManager();
        entityManager.addEntity(player);
        entityManager.addEntity(Points);
        entityManager.addEntity(PowerUp);
        entityManager.addEntity(Debuff);

        // add alphabets to entityManager
        for (int i = 0; i < NUM_ALPHABETS - 1; i ++){
            int asciiValue = 65 + i; //get ASCII values for A-Z. starts from 65 (i.e. "A")
            String key = Character.toString ((char) asciiValue);
            entityManager.addEntity(new EntityTexture(key, "assets/Pictures/Characters/" + key + ".png", 50, 60, Gdx.graphics.getWidth() + 50, MathUtils.random(20, Gdx.graphics.getHeight() - 60), 500, false, true, false));
        }

        playerControlManager = new PlayerControlManager(this);

        aiControlManager = new AIControlManager(entityManager, this, musicManager);

        //Scene
        sceneManager = new SceneManager(game);
        sceneManager.addScene("Title", new TitleScene(this));
        sceneManager.addScene("Game", new GameScene(this));
        sceneManager.addScene("Setting", new SettingsScene(this));
        sceneManager.addScene("Leaderboard", new LeaderboardScene(this));
        sceneManager.addScene("Instructions", new InstructionScene(this));
        sceneManager.addScene("EndGame", new EndGameScene(this));
        sceneManager.addScene("TransitionScene", new TransitionScene(this));

        collisionManager = new CollisionManager(entityManager, this, musicManager);

        musicManager.playBackgroundMusic();
        sceneManager.showScene("Title");

        int upKeyCode = preferences.getInteger("upKeyBinding", Input.Keys.UP); // Load the custom "Up" key code
        playerControlManager.setUpKeyCode(upKeyCode);
    }

    public void update(float delta) {
        // If the game screen should be shown
        if (getShowGameScreen()) {
            // If restarting the current level
            if (getRestartCurrentLevel()) {
                // Adjust score and level
                if(getLevel() == 2)
                {
                    setScore(0);
                } else if (getLevel() ==3) {
                    setScore(playerLvl1Score);
                }
                setLevel(getLevel() - 1);
                aiControlManager.setDelayTimer(5f);
                setRestartCurrentLevel(false); // Reset restart flag


                resetLettersCollected();
            }
            if(getIsNextLevel())
            {
                aiControlManager.setDelayTimer(5f);
                resetLettersCollected();

            }
            musicManager.playInGameMusic(); // Play in-game music
            sceneManager.showScene("Game"); // Show game scene
            setIsPlaying(true); // Set playing flag to true
            level = getLevel(); // Update current level
            startLevelTimer(); // Start level timer
            setShowGameScreen(false); // Reset show game screen flag
        }
        if(isplayAgain)
        {
            initialize();
            setLevel(1);
            setScore(0);
            setShowGameScreen(true);
            setIsGameComplete(false);
            setPlayAgainStatus(false);

            resetLettersCollected();
        }

        // If the settings screen should be shown
        if (getShowSettingsScreen()) {
            musicManager.playBackgroundMusic(); // Play background music
            sceneManager.showScene("Setting"); // Show settings scene
            setShowSettingsScreen(false); // Reset show settings screen flag
        }

        // If the leaderboard screen should be shown
        if (getLeaderboardScreen()) {
            musicManager.playBackgroundMusic(); // Play background music
            sceneManager.showScene("Leaderboard"); // Show leaderboard scene
            setLeaderboardScreen(false); // Reset leaderboard screen flag
        }

        // If the instructions screen should be shown
        if (getShowInstructionsScreen()) {
            musicManager.playBackgroundMusic(); // Play background music
            sceneManager.showScene("Instructions"); // Show instructions scene
            setShowInstructionsScreen(false); // Reset show instructions screen flag
        }

        // If the title screen should be shown
        if (getTitleScreen()) {
            musicManager.playBackgroundMusic(); // Play background music
            sceneManager.showScene("Title"); // Show title scene
            setTitleScreen(false); // Reset title screen flag
        }
        if (getIsPLaying()) {
            // If input processing is not initialized, initialize it
            if (!inputProcessingInitialized) {
                new IOManager(playerControlManager);
                inputProcessingInitialized = true;
            }

            entityManager.drawEntities();
            playerControlManager.updatePlayerPosition(entityManager, playerTexture[1], playerTexture[0]);
            collisionManager.handleCollision(entityManager);

            // If player entity is null, game ends
            if (entityManager.getPlayerEntity() == null) {
                setIsEndGame(true);
            }

            aiControlManager.attackPlayer("warningTriangle", "meteor");
            aiControlManager.spawnCollectibles();
        }

        if (getIsEndGame()) {
            setIsPlaying(false);
            musicManager.stopAllSoundEffect();

            //Save Player Name and Score into the Database
            Array<DatabaseService.LeaderboardEntry> leaderboardData = DatabaseService.loadLeaderboardData();
            // Flag to indicate if the player name exists in the leaderboard
            boolean playerExists = false;

            // Iterate over the leaderboard data
            for (DatabaseService.LeaderboardEntry entry : leaderboardData) {
                if (entry.getUsername().equals(playerData.getName())) {
                    // Player name exists, update the score
                    if (getScore() > Integer.parseInt(entry.getPoints())) {
                        entry.setPoints(Integer.toString(getScore()));
                    }
                    playerExists = true;
                    break;
                }
            }
            // If player name doesn't exist, add a new entry
            if (!playerExists) {
                DatabaseService.LeaderboardEntry newEntry = new DatabaseService.LeaderboardEntry(playerData.getName(), Integer.toString(getScore()));
                leaderboardData.add(newEntry);
            }

            DatabaseService.saveLeaderboardData(leaderboardData); // Save leaderboard data

            if(getIsGameComplete())
            {
                musicManager.playEndGameMusic(true); // Play end game music
            }
            else
            {
                musicManager.playSoundEffect("Explosion"); // Play sound effect
                musicManager.playEndGameMusic(false); // Play end game music
            }
            inputProcessingInitialized = false; // Reset input processing flag
            sceneManager.showScene("EndGame"); // Show end game scene
            stopLevelTimer();
            setIsEndGame(false); // Reset end game flag
        }

        if (getIsExitGame()){
            setIsExitGame(false);
            Gdx.app.exit();
        }
    }

    public void transition(){
        if(getLevel() == 1)
        {
            playerLvl1Score = getScore();
        } else if (getLevel() == 2) {
            playerLvl2Score = getScore();
        }

        setLevel(getLevel() + 1); // Increment level
        stopLevelTimer(); // Reset level timer

        setIsPlaying(false); // Set playing flag to false to stop playing
        setInputProcessingInitialized(false); // Set input processing flag to false
        musicManager.stopAllSoundEffect(); // Stop all sound effects
        playerControlManager.handleKeyRelease(playerControlManager.getUpKeyCode());
        sceneManager.showScene("TransitionScene"); // Show transition scene
    }

    private void resetLettersCollected(){
        setLettersCollected("");
    }

    public void updateLevelName(){
        if (getLevel() == 1){
            setLevelName("MARS");
        } else if(getLevel() == 2){
            setLevelName("MOON");
        } else if(getLevel() == 3){
            setLevelName("SATURN");
        }
    }

    public void appendLettersCollected(String letterKey){
        String appendedLetters = getLettersCollected() + letterKey;
        setLettersCollected(appendedLetters);
    }

    public void showPlayerFlying(){
        if (playerTexture[1] != null){
            player.setTexture(playerTexture[1]);
        }
    }
    public void showPlayerFalling(){
        player.setTexture(playerTexture[0]);
    }
    public boolean getIsGameComplete()
    {
        return isGameComplete;
    }
    public void setIsGameComplete(boolean isGameComplete)
    {
        this.isGameComplete = isGameComplete;
    }
    public boolean getIsNextLevel()
    {
        return isNextLevel;
    }
    public void setIsNextLevel(boolean isNextLevel)
    {
        this.isNextLevel = isNextLevel;
    }
    public boolean getRestartCurrentLevel() {
        return restartCurrentLevel;
    }
    public void setRestartCurrentLevel(boolean restartCurrentLevel) {
        this.restartCurrentLevel = restartCurrentLevel;
    }
    public boolean getPlayAgainStatus() {
        return isplayAgain;
    }
    public void setPlayAgainStatus(boolean isplayAgain) {
        this.isplayAgain = isplayAgain;
    }
    public boolean getInputProcessInitialized() {
        return inputProcessingInitialized;
    }
    public void setInputProcessingInitialized(boolean inputProcessingInitialized) {
        this.inputProcessingInitialized = inputProcessingInitialized;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;

    }
    public DatabaseService getDatabaseService() {
        return databaseService;
    }
    public void setDatabaseService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }
    public DialogService getDialogService() {
        return dialogService;
    }
    public void setDialogService(DialogService dialogService) {
        this.dialogService = dialogService;
    }
    public Player getPlayerData() {
        return playerData;
    }
    public void setPlayerData(Player playerData) {
        this.playerData = playerData;
    }
    public boolean getShowGameScreen() {
        return showGameScreen;
    }
    public void setShowGameScreen(boolean showGameScreenBool) {
        this.showGameScreen = showGameScreenBool;
    }
    public boolean getIsEndGame() {
        return isEndGame;
    }
    public void setIsEndGame(boolean endGameBool) {
        this.isEndGame = endGameBool;
    }
    public boolean getShowSettingsScreen() {
        return showSettingsScreen;
    }
    public void setShowSettingsScreen(boolean showSettingsScreenBool) {
        this.showSettingsScreen = showSettingsScreenBool;
    }
    public boolean getTitleScreen() {
        return titleScreen;
    }
    public void setTitleScreen(boolean titleScreen) {
        this.titleScreen = titleScreen;
    }
    public boolean getLeaderboardScreen() {
        return leaderboardScreen;
    }
    public void setLeaderboardScreen(boolean leaderboardScreen) {
        this.leaderboardScreen = leaderboardScreen;
    }
    public boolean getIsPLaying() {
        return isPlaying;
    }
    public void setIsPlaying(boolean isPlayingBool) {
        this.isPlaying = isPlayingBool;
    }
    public boolean getShowInstructionsScreen() {
        return showInstructionsScreen;
    }
    public void setShowInstructionsScreen(boolean showInstructionsScreen) {
        this.showInstructionsScreen = showInstructionsScreen;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        if (level > 3)
        {
            System.out.println("Game Ended");
            setIsGameComplete(true);
            setIsEndGame(true);
        }
        else
        {
            this.level = level;
        }
    }
    public boolean getIsAllNotActive() {return IsAllNotActive;}
    public void setIsAllNotActive(boolean IsAllNotActive) {
        this.IsAllNotActive = IsAllNotActive;
    }
    private void startLevelTimer() {
        levelTimer = new Timer();
        float interval = 1.0f; // Print countdown every second
        totalDuration = LEVEL_UP_INTERVAL;
        startTime = TimeUtils.nanoTime(); // Record the start time
        levelTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                float elapsedTime = (TimeUtils.nanoTime() - startTime) / 1000000000.0f; // Calculate elapsed time in seconds
                float timeRemaining = totalDuration - elapsedTime;
                if (timeRemaining <= 0 && !getIsAllNotActive()) {
                    setIsEndGame(true);
                    levelTimer.stop();
                } else {
                    //System.out.println("Time remaining: " + timeRemaining + " seconds");
                }
            }
        }, 0, interval);
    }

    public void stopLevelTimer() {
        if (levelTimer != null) {
            levelTimer.clear(); // Clear the timer tasks
        }
    }

    public float getTimeRemaining() {
        if (levelTimer != null) {
            float elapsedTime = (TimeUtils.nanoTime() - startTime) / 1000000000.0f; // Calculate elapsed time in seconds
            return Math.max(totalDuration - elapsedTime, 0); // Return remaining time, ensuring it's non-negative
        }
        return 0; // Return 0 if the timer is not running
    }
    public String getLettersCollected() {
        return lettersCollected;
    }
    public void setLettersCollected(String lettersCollected) {
            this.lettersCollected= lettersCollected;
    }

    public String getLevelName() {
        return levelName;
    }
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public boolean getIsExitGame() {
        return isExitGame;
    }
    public void setIsExitGame(boolean exitGameBool) {
        this.isExitGame = exitGameBool;
    }
    public int getUpKeyBinding() {
        return preferences.getInteger("upKeyBinding");
    }
    public void updateUpKeyBinding(int newKeyCode) {
        playerControlManager.setUpKeyCode(newKeyCode); // Update the key code in PlayerControlManager
        preferences.putInteger("upKeyBinding", newKeyCode);
        preferences.flush(); // Save changes
    }

    public void applyMusicSettings()
    {
        musicManager.applyVolumeAndMuteSettings();
    }
}
