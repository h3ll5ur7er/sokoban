package ch.bfh.sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.Arrays;

public class GlobalAssets
{
    private static GlobalAssets ourInstance = new GlobalAssets();

    public static GlobalAssets getInstance() {
        return ourInstance;
    }

    private TextureAtlas atlas1;
    private TextureAtlas atlas2;
    private TextureAtlas atlas;
    private Skin skin;

    private GlobalAssets()
    {
    }

    public void load(String atlasPath, String skinPath)
    {
        //atlas1 = new TextureAtlas(Gdx.files.internal(atlas1Path));
        //atlas2 = new TextureAtlas(Gdx.files.internal(atlas2Path));
        atlas  = new TextureAtlas(Gdx.files.internal(atlasPath));

        skin = new Skin(Gdx.files.internal(skinPath), atlas);
    }

    public TextureAtlas getTextures()
    {
        return atlas;
    }

    public TextureAtlas.AtlasRegion getTextures(String textureId)
    {
        return atlas.findRegion(textureId);
    }

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
