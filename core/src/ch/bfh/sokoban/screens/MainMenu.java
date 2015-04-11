package ch.bfh.sokoban.screens;

import ch.bfh.sokoban.Sokoban;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Main menu of the game
 * - Play
 *   Takes the user to the levelSelection screen
 * - Editor
 *   Takes the user to the editor where he can create his own levels
 * - Exit
 *   Closes the application
 */
public class MainMenu extends MyScreenAdapter
{
    Table table;

    @Override
    public void show()
    {
        super.show();
        Gdx.input.setInputProcessor(stage);

        table = new Table(skin);
        table.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        TextButton btnPlay = new TextButton("PLAY", skin);
        btnPlay.pad(20);
        btnPlay.setSize(300, 100);
        btnPlay.addListener(playListener());

        TextButton btnEdit = new TextButton("EDIT", skin);
        btnEdit.pad(22);
        btnEdit.setSize(300, 100);
        btnEdit.addListener(editorListener());

        TextButton btnSettings = new TextButton("SETTINGS", skin);
        btnSettings.pad(22);
        btnSettings.setSize(300, 100);
        btnSettings.addListener(settingsListener());

        TextButton btnExit = new TextButton("EXIT", skin);
        btnExit.pad(24);
        btnExit.setSize(300, 100);
        btnExit.addListener(exitListener());



        table.add(Sokoban.TITLE);           //Sokoban
        table.row();                        //-------
        table.add(btnPlay);                 //|PLAY|
        table.row();                        //-------
        table.add(btnEdit);                 //|EDIT|
        table.row();                        //-------
        table.add(btnSettings);             //|SETTINGS|
        table.row();                        //-------
        table.add(btnExit);                 //|EXIT|
        table.invalidateHierarchy();

        table.layout();

        stage.addActor(table);
    }

    private ClickListener playListener()
    {
        return new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                ((Game)Gdx.app.getApplicationListener()).setScreen(new LevelSelection());
            }
        };
    }

    private ClickListener editorListener()
    {
        return new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {

            }
        };
    }

    private ClickListener settingsListener()
    {
        return new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                new Settings().activate();
            }
        };
    }

    private ClickListener exitListener()
    {
        return new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                Gdx.app.exit();
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
