package ch.bfh.sokoban;

import ch.bfh.sokoban.screens.Splash;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Sokoban extends Game
{
    public static final String TITLE = "Sokoban";
    public static final String VERSION = "0.1 PRE_ALPHA";

	Splash splash;
	@Override
	public void create ()
    {
        GlobalAssets.getInstance().load("lvl/atlas.pack", "lvl/game_style.json");

        setScreen(splash = new Splash());
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
        splash.dispose();
        super.dispose();
    }
}
