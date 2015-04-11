package ch.bfh.sokoban;

import ch.bfh.sokoban.screens.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Global manager for excessively used assets so they are loaded and destroyed once
 * Implemented as a singleton
 */
public class GlobalAssets
{
    private static GlobalAssets ourInstance = new GlobalAssets();

    public static GlobalAssets getInstance() {
        return ourInstance;
    }

    private TextureAtlas atlas;
    private Skin skin;

    private GlobalAssets()
    {
    }

    public void load(String atlasPath, String skinPath)
    {
        atlas  = new TextureAtlas(Gdx.files.internal(Settings.get(atlasPath)));

        skin = new Skin(Gdx.files.internal(Settings.get(skinPath)), atlas);
    }

    /**
     * @return DO NOT DISPOSE THE ATLAS, IT'S DONE @ LAUNCH / TERMINATION
     **/
    public TextureAtlas getTextures()
    {
        return atlas;
    }

    /**
     * @return DO NOT DISPOSE THE TEXTURE, IT'S DONE @ LAUNCH / TERMINATION
     **/
    public TextureAtlas.AtlasRegion getTexture(String textureId)
    {
        return atlas.findRegion(textureId);
    }

    /**
     * @return DO NOT DISPOSE THE SKIN, IT'S DONE @ LAUNCH / TERMINATION
     **/
    public Skin getSkin()
    {
        return skin;
    }

    public void dispose()
    {
        atlas.dispose();
        skin.dispose();
    }
}
