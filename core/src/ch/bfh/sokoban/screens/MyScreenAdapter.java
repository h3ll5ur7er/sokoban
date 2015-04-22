package ch.bfh.sokoban.screens;

import ch.bfh.sokoban.GlobalAssets;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Instead of loading and unloading the same stuff in every screen,
 * it's just handled in a superclass
 */
public abstract class MyScreenAdapter extends ScreenAdapter
{
    protected TextureAtlas atlas;
    protected Skin skin;
    protected Stage stage;
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
        stage = new Stage();

        Gdx.input.setInputProcessor(stage);
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
        stage.dispose();
    }
}