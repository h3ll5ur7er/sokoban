package ch.bfh.sokoban.screens;

import ch.bfh.sokoban.data.LevelData;
import ch.bfh.sokoban.data.LevelPackData;
import ch.bfh.sokoban.game.LevelManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Arrays;

/**
 * Screen to select a level to play
 */
public class LevelSelection extends MyScreenAdapter
{
    private static int selectedPack, selectedLevel;

    Table table;

    List<LevelPackData> list1;
    List<LevelData> list2;

    /**
     * Load levelPack from json and build up the gui
     */
    @Override
    public void show()
    {
        super.show();

        table = new Table(skin);
        list1 = new List<LevelPackData>(skin);
        list2 = new List<LevelData>(skin);

        ScrollPane scroll1 = new ScrollPane(list1, skin);
        ScrollPane scroll2 = new ScrollPane(list2, skin);

        TextButton btnPlay = new TextButton(Settings.get("PlayButtonText"), skin, Settings.get("PlayButtonSize"));
        TextButton btnBack = new TextButton(Settings.get("BackButtonText"), skin, Settings.get("BackButtonSize"));

        Gdx.input.setInputProcessor(stage);

        table.setBounds(0,0, 1200, 720);

        Object[] packObjects = LevelManager.instance().getLevelPacks().toArray();
        LevelPackData[] packs = Arrays.copyOf(packObjects, packObjects.length, LevelPackData[].class);

        list1.setItems(packs);
        list1.setSelectedIndex(selectedPack >0? selectedPack :0);
        list1.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                selectedLevel = 0;
                refreshLevels();
            }
        });


        refreshLevels();

        btnPlay.addListener(playListener());
        btnPlay.pad(15);

        btnBack.addListener(backListener());
        btnBack.pad(10);

        table.add().width(300);
        table.add("SELECT LEVEL").width(600).colspan(2);
        table.add().width(300).row();

        table.add("Levelpack").width(300);
        table.add("Level").width(300);
        table.add().width(600).colspan(2).row();

        table.add(scroll1).width(300);
        table.add(scroll2).width(300);
        table.add(btnPlay).width(300);
        table.add(btnBack).width(300).bottom().right()
            .expandX().expandY();
        table.invalidateHierarchy();
        // table.debug();

        stage.addActor(table);
    }

    /**
     * Refreshes the list showing the content of selected levelpack in list2
     **/
    private void refreshLevels()
    {
        list2.setItems(list1.getSelected().levels);
        list2.setSelectedIndex(selectedLevel>0||selectedLevel<list1.getSelected().levels.length?selectedLevel:0);
    }

    /**
     * @return the action performed by the play button
     */
    private ClickListener playListener()
    {
        return new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                //read leveldata from db
                selectedPack = list1.getSelectedIndex();
                selectedLevel = list2.getSelectedIndex();

                new Game(list2.getSelected()).activate();
            }
        };
    }

    /**
     * @return the action performed by the back button
     */
    private ClickListener backListener()
    {
        return new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                selectedPack = list1.getSelectedIndex();
                selectedLevel = list2.getSelectedIndex();

                new MainMenu().activate();
            }
        };
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act(delta);
        stage.draw();
    }
}
