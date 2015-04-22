package ch.bfh.sokoban.screens;

import ch.bfh.sokoban.Sokoban;
import ch.bfh.sokoban.utils.Lan;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
    private static boolean eulaShown = false;

    @Override
    public void show()
    {
        super.show();

        table = new Table(skin);
        table.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        TextButton btnPlay = new TextButton(Lan.g("Play"), skin);
        btnPlay.pad(20);
        btnPlay.setSize(300, 100);
        btnPlay.addListener(playListener());

        TextButton btnEdit = new TextButton(Lan.g("Editor"), skin);
        btnEdit.pad(22);
        btnEdit.setSize(300, 100);
        btnEdit.addListener(editorListener());

        TextButton btnSettings = new TextButton(Lan.g("Settings"), skin);
        btnSettings.pad(22);
        btnSettings.setSize(300, 100);
        btnSettings.addListener(settingsListener());

        TextButton btnExit = new TextButton(Lan.g("Exit"), skin);
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

        showEulaIfNeeded();
    }

    private void showEulaIfNeeded()
    {
        if(Settings.get("EULA").equals("True")) return;

        if(eulaShown) return;
        eulaShown = true;

        Dialog eulaDialog = new Dialog(Lan.g("EulaTitle"), skin);

        Label eulaText = new Label(Lan.g("EULA"), skin);
        eulaText.setWrap(true);

        CheckBox notShowEula = new CheckBox(Lan.g("NotShowAgain"), skin);
        notShowEula.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.set("EULA", notShowEula.isChecked() ? "True" : "False");
            }
        });

        TextButton btnClose = new TextButton(Lan.g("Exit"), skin);
        btnClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                eulaDialog.remove();
            }
        });

        eulaDialog.getContentTable().add(new ScrollPane(eulaText)).width(700).height(500).expandX().expandY();
        eulaDialog.getButtonTable().add(btnClose);
        eulaDialog.getButtonTable().add(notShowEula);
        eulaDialog.show(stage);
    }

    private ClickListener playListener()
    {
        return new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                new LevelSelection().activate();
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
