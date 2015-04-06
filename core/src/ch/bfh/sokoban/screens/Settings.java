package ch.bfh.sokoban.screens;

import ch.bfh.sokoban.GlobalAssets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * TODO
 */
public class Settings extends MyScreenAdapter
{
    Table table;
    @Override
    public void show()
    {
        super.show();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        table = new Table(skin);
        table.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //TODO:

        table.add();
        table.add("Settings");
        table.add();

        //TODO:

        table.invalidateHierarchy();

        stage.addActor(table);
    }

    @Override
    public void render(float delta)
    {

    }

}
