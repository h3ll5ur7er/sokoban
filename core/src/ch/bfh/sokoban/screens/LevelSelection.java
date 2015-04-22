package ch.bfh.sokoban.screens;

import ch.bfh.sokoban.data.LevelData;
import ch.bfh.sokoban.data.LevelPackData;
import ch.bfh.sokoban.game.Highscore;
import ch.bfh.sokoban.game.LevelManager;
import ch.bfh.sokoban.utils.Lan;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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

    Container<Table> highscoresContainer = new Container<Table>();

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

        TextButton btnPlay = new TextButton(Lan.g("Play"), skin, Settings.get("PlayButtonSize"));
        TextButton btnBack = new TextButton(Lan.g("Back"), skin, Settings.get("BackButtonSize"));

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
        list2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                refreshHighscores();
            }
        });


        refreshLevels();

        btnPlay.addListener(playListener());
        btnPlay.pad(15);

        btnBack.addListener(backListener());
        btnBack.pad(10);

        Table inner = new Table(skin);
        inner.add(btnPlay).row();
        inner.add(btnBack);


        table.add().width(300);
        table.add(Lan.g("SelectLevel")).width(600).colspan(2);
        table.add().width(300).row();

        table.add("Levelpack").width(300);
        table.add("Level").width(300);
        table.add().width(600).colspan(2).row();


        table.add(scroll1).width(300);
        table.add(scroll2).width(300).expandY();
        table.add(inner).width(300);
        //table.add("highscore").width(300).row();
        table.add(highscoresContainer).width(300).row();

        //table.add().width(300);
        //table.add().width(300);
        //table.add(btnPlay).width(300);
        //table.add(btnBack).width(300).bottom().right()
        //        .expandX().expandY();
        table.invalidateHierarchy();
//        table.debug();

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
     * Refreshes the list showing the content of selected levelpack in list2
     **/
    private void refreshHighscores()
    {
        highscoresContainer.setActor(Highscore.getTable(list2.getSelected().id));
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
