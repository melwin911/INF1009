package Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;

public class MusicManager {
    private Music backgroundMusic;
    private Music inGameMusic;
    private Music endGameMusic;
    private Music gameComplete;
    private Music MissileSoundEffect;
    private Music PointsSoundEffect;
    private Music BuffSoundEffect;
    private Music DebuffSoundEffect;
    private Music WarningSoundEffect;
    private Music ExplosionSoundEffect;
    private float bgMusicVolume;
    private boolean bgMusicMuted;
    private float inGameMusicVolume;
    private boolean inGameMusicMuted;
    private float endGameMusicVolume;
    private boolean endGameMusicMuted;
    private float soundEffectVolume;
    private boolean soundEffectMuted;
    private static final String PREFS_NAME = "MusicSettings";
    private static final String BG_MUSIC_VOLUME_KEY = "bgMusicVolume";
    private static final String BG_MUSIC_MUTED_KEY = "bgMusicMuted";
    private static final String IN_GAME_MUSIC_VOLUME_KEY = "inGameMusicVolume";
    private static final String IN_GAME_MUSIC_MUTED_KEY = "inGameMusicMuted";
    private static final String END_GAME_MUSIC_VOLUME_KEY = "endGameMusicVolume";
    private static final String END_GAME_MUSIC_MUTED_KEY = "endGameMusicMuted";
    private static final String SOUND_EFFECT_VOLUME_KEY = "soundEffectVolume";
    private static final String SOUND_EFFECT_MUTED_KEY = "soundEffectMuted";
    private Preferences preferences;

    public void initialize(String bgMusicPath, String inGameMusicPath, String endGameMusicPath, String gameCompleteMusicPath,
                           String warningSoundPath, String explosionSoundPath, String missileSoundPath,
                           String pointsSoundPath, String buffSoundPath, String debuffSoundPath) {
        // Load music files with specified file paths
        backgroundMusic = loadMusic(bgMusicPath);
        inGameMusic = loadMusic(inGameMusicPath);
        endGameMusic = loadMusic(endGameMusicPath);
        gameComplete = loadMusic(gameCompleteMusicPath);
        WarningSoundEffect = loadMusic(warningSoundPath);
        ExplosionSoundEffect = loadMusic(explosionSoundPath);
        MissileSoundEffect = loadMusic(missileSoundPath);
        PointsSoundEffect = loadMusic(pointsSoundPath);
        BuffSoundEffect = loadMusic(buffSoundPath);
        DebuffSoundEffect = loadMusic(debuffSoundPath);

        preferences = Gdx.app.getPreferences(PREFS_NAME);
        loadDefaultSettings();

        // Set looping for in-game music
        backgroundMusic.setLooping(true);
        inGameMusic.setLooping(true);

        applyVolumeAndMuteSettings();
    }

    public void playBackgroundMusic() {
        stopAllMusic();
        backgroundMusic.play();
    }

    public void playInGameMusic() {
        stopAllMusic();
        inGameMusic.play();
    }

    public void playEndGameMusic(boolean isGameCompleted) {
        stopAllMusic();
        if(!isGameCompleted)
        {
            endGameMusic.play();
        }
        else
        {
            gameComplete.play();
        }
    }
    public void applyVolumeAndMuteSettings() {
        getSettings();
        backgroundMusic.setVolume(bgMusicVolume * (bgMusicMuted ? 0 : 1));
        inGameMusic.setVolume(inGameMusicVolume * (inGameMusicMuted ? 0 : 1));
        endGameMusic.setVolume(endGameMusicVolume * (endGameMusicMuted ? 0 : 1));
        gameComplete.setVolume(endGameMusicVolume * (endGameMusicMuted ? 0: 1));

        //Sound Effects
        MissileSoundEffect.setVolume(soundEffectVolume * (soundEffectMuted ? 0 : 1));
        PointsSoundEffect.setVolume(soundEffectVolume * (soundEffectMuted ? 0 : 1));
        BuffSoundEffect.setVolume(soundEffectVolume * (soundEffectMuted ? 0 : 1));
        DebuffSoundEffect.setVolume(soundEffectVolume * (soundEffectMuted ? 0 : 1));
        WarningSoundEffect.setVolume(soundEffectVolume * (soundEffectMuted ? 0 : 1));
        ExplosionSoundEffect.setVolume(soundEffectVolume * (soundEffectMuted ? 0 : 1));
        saveSettings();
    }

    public void playSoundEffect(String SoundEffectName)
    {
        switch (SoundEffectName) {
            case ("Warning"):
                WarningSoundEffect.play();
                break;
            case ("Points"):
                PointsSoundEffect.play();
                break;
            case ("Explosion"):
                ExplosionSoundEffect.play();
                break;
            case ("Missile"):
                MissileSoundEffect.play();
                break;
            case ("Buff"):
                BuffSoundEffect.play();
                break;
            case ("Debuff"):
                DebuffSoundEffect.play();
                break;
        }
    }

    public void stopSoundEffect(String SoundEffectName) {
        switch (SoundEffectName) {
            case ("Warning"):
                WarningSoundEffect.stop();
                break;
            case ("Points"):
                PointsSoundEffect.stop();
                break;
            case ("Explosion"):
                ExplosionSoundEffect.stop();
                break;
            case ("Missile"):
                MissileSoundEffect.stop();
                break;
            case ("Buff"):
                BuffSoundEffect.stop();
                break;
            case ("Debuff"):
                DebuffSoundEffect.stop();
                break;
        }
    }

    public void stopAllSoundEffect() {
        WarningSoundEffect.stop();
        PointsSoundEffect.stop();
        ExplosionSoundEffect.stop();
        MissileSoundEffect.stop();
        BuffSoundEffect.stop();
        DebuffSoundEffect.stop();
    }
    private Music loadMusic(String filePath) {
        return Gdx.audio.newMusic(Gdx.files.internal(filePath));
    }

    public void stopAllMusic() {
        backgroundMusic.stop();
        inGameMusic.stop();
        endGameMusic.stop();
        gameComplete.stop();
    }
    private void saveSettings() {
        preferences.putFloat(BG_MUSIC_VOLUME_KEY, bgMusicVolume);
        preferences.putBoolean(BG_MUSIC_MUTED_KEY, bgMusicMuted);
        preferences.putFloat(IN_GAME_MUSIC_VOLUME_KEY, inGameMusicVolume);
        preferences.putBoolean(IN_GAME_MUSIC_MUTED_KEY, inGameMusicMuted);
        preferences.putFloat(END_GAME_MUSIC_VOLUME_KEY, endGameMusicVolume);
        preferences.putBoolean(END_GAME_MUSIC_MUTED_KEY, endGameMusicMuted);
        preferences.putFloat(SOUND_EFFECT_VOLUME_KEY, soundEffectVolume);
        preferences.putBoolean(SOUND_EFFECT_MUTED_KEY, soundEffectMuted);
        preferences.flush();
    }

    private void getSettings()
    {
        bgMusicVolume = preferences.getFloat(BG_MUSIC_VOLUME_KEY);
        bgMusicMuted = preferences.getBoolean(BG_MUSIC_MUTED_KEY);
        inGameMusicVolume = preferences.getFloat(IN_GAME_MUSIC_VOLUME_KEY);
        inGameMusicMuted = preferences.getBoolean(IN_GAME_MUSIC_MUTED_KEY);
        endGameMusicVolume = preferences.getFloat(END_GAME_MUSIC_VOLUME_KEY);
        endGameMusicMuted = preferences.getBoolean(END_GAME_MUSIC_MUTED_KEY);
        soundEffectVolume = preferences.getFloat(SOUND_EFFECT_VOLUME_KEY);
        soundEffectMuted = preferences.getBoolean(SOUND_EFFECT_MUTED_KEY);
    }

    private void loadDefaultSettings() {
        bgMusicVolume = preferences.getFloat(BG_MUSIC_VOLUME_KEY, 0.5f);
        bgMusicMuted = preferences.getBoolean(BG_MUSIC_MUTED_KEY, false);
        inGameMusicVolume = preferences.getFloat(IN_GAME_MUSIC_VOLUME_KEY, 0.5f);
        inGameMusicMuted = preferences.getBoolean(IN_GAME_MUSIC_MUTED_KEY, false);
        endGameMusicVolume = preferences.getFloat(END_GAME_MUSIC_VOLUME_KEY, 0.5f);
        endGameMusicMuted = preferences.getBoolean(END_GAME_MUSIC_MUTED_KEY, false);
        soundEffectVolume = preferences.getFloat(SOUND_EFFECT_VOLUME_KEY, 1.0f);
        soundEffectMuted = preferences.getBoolean(SOUND_EFFECT_MUTED_KEY, false);
    }
}
