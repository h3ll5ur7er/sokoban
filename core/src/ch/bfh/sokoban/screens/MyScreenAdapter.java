package ch.bfh.sokoban.screens;

import ch.bfh.sokoban.GlobalAssets;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MyScreenAdapter extends ScreenAdapter
{
    protected TextureAtlas atlas;
    protected Skin skin;
    public void activate()
    {
        ((Game) Gdx.app.getApplicationListener()).setScreen(this);
    }

    @Override
    public void show()
    {
        super.show();

        atlas = GlobalAssets.getInstance().getTextures();
        skin = GlobalAssets.getInstance().getSkin();
    }

    @Override
    public void hide()
    {
        super.hide();
        dispose();
    }

    @Override
    public void dispose()
    {
        super.dispose();
    }
}