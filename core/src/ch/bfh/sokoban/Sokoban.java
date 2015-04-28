package ch.bfh.sokoban;

import ch.bfh.sokoban.game.Highscore;
import ch.bfh.sokoban.game.LevelManager;
import ch.bfh.sokoban.screens.Settings;
import ch.bfh.sokoban.screens.Splash;
import ch.bfh.sokoban.screens.Title;
import ch.bfh.sokoban.utils.Lan;
import com.badlogic.gdx.Game;

/**
 * Main class of the game core
 * @see com.badlogic.gdx.Game
 **/
public class Sokoban extends Game
{
    public static final String TITLE = "Sokoban";
    public static final String VERSION = "0.5 BETA";

	@Override
	public void create ()
    {
        Settings.load();
        GlobalAssets.getInstance().load("DefaultTextureAtlas", "DefaultSkin");
        LevelManager.instance().load();
        Lan.init();

        //new Splash<MainMenu>("StartSplashScreen", 1, 1, MainMenu.class).activate();
        new Splash("StartSplashScreen", 1.0f, 1.0f, Splash.class, "StartSplashScreen", 1.0f, 1.0f, Title.class).activate();
	}

    @Override
    public void resize(int width, int height)
    {
        super.resize(width, height);
    }

	@Override
	public void render ()
    {
		super.render();
	}

    @Override
    public void pause()
    {
        super.pause();
    }

    @Override
    public void resume()
    {
        super.resume();
    }

    @Override
    public void dispose()
    {
        super.dispose();
        GlobalAssets.getInstance().dispose();
        Highscore.save();
        Settings.save();
        LevelManager.instance().save();
    }
}
