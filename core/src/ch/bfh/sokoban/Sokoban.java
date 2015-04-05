package ch.bfh.sokoban;

import ch.bfh.sokoban.screens.MainMenu;
import ch.bfh.sokoban.screens.Splash;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Main class of the game core
 * @see com.badlogic.gdx.Game
 **/
public class Sokoban extends Game
{
    public static final String TITLE = "Sokoban";
    public static final String VERSION = "0.2 ALPHA";

	@Override
	public void create ()
    {
        GlobalAssets.getInstance().load("lvl/atlas.pack", "lvl/game_style.json");

        new Splash<MainMenu>("img/splash.png", 1, 1, MainMenu.class).activate();
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
    }
}
