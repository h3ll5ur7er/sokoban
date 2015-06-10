package ch.bfh.sokoban.screens;

import ch.bfh.sokoban.Sokoban;
import ch.bfh.sokoban.data.LevelData;
import ch.bfh.sokoban.game.Level;
import ch.bfh.sokoban.utils.Lan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import static com.badlogic.gdx.Input.Keys.*;
/**
 * Game generates a level from the provided data, presents it to the user and gets user input
 **/
public class Game extends MyScreenAdapter
{
    private Table tableScreen;
    private Table tableMenu;
    private Level level;
    private LevelData leveldata;
    boolean saved = false;
    boolean completed = false;
    int navigationCountdown = 30;

    // Timer
    Label lblScore = null;
    Label lblMoves = null;
    Label lblPushes = null;
    Label lblUndoRedo = null;

    /**
     * Create a new Game
     * @param level level to generate
     **/
    public Game(LevelData level)
    {
        leveldata = level;
    }

    /**
     * Load the level and build up the gui
     **/
    @Override
    public void show()
    {
        super.show();

        //load background
        bg = new Texture(Settings.get("GameScreen"));
        sprite = new Sprite(bg);
    	sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        tableScreen = new Table(skin);
        tableScreen.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        tableMenu = new Table(skin);

        this.level = new Level(leveldata, skin);

        lblScore = new Label("",skin, "small");
        lblScore.setText(""+level.getScore());
        lblMoves = new Label("",skin, "small");
        lblMoves.setText(""+level.getMoveCount());
        lblPushes = new Label("",skin, "small");
        lblPushes.setText(""+level.getPushCount());
        lblUndoRedo = new Label("",skin, "small");
        lblUndoRedo.setText(""+level.getUndoRedoCount());

        TextButton btnBack = new TextButton(Lan.g("Back"), skin, Settings.get("BackButtonSize"));
        btnBack.pad(20);
        btnBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                new LevelSelection().activate();
            }
        });

        TextButton btnSave = new TextButton(Lan.g("Save"), skin, Settings.get("SaveButtonSize"));
        btnSave.pad(22);
        btnSave.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // Save data
                level.save();
                saved = true;
            }
        });

        tableScreen.add(leveldata.name).colspan(2).center().height(70);
        tableScreen.row();

        tableScreen.add(level.getTable()).width(Gdx.graphics.getWidth()-200).expandX().expandY();

        tableMenu.add(new Label(Lan.g("Score")+":", skin)).colspan(2).center();
        tableMenu.row();
        tableMenu.add(lblScore).colspan(2).center();
        tableMenu.row();
        tableMenu.add(new Label(Lan.g("Moves")+":   ", skin, "small"));
        tableMenu.add(new Label(Lan.g("Pushes") + ":", skin, "small"));
        tableMenu.row();
        tableMenu.add(lblMoves).center();
        tableMenu.add(lblPushes).center();
        tableMenu.row();
        tableMenu.add();
        tableMenu.row();
        tableMenu.row();
        tableMenu.add();
        tableMenu.row();
        tableMenu.add(new Label(Lan.g("UndoRedo")+":", skin, "small")).colspan(2).center();
        tableMenu.row();
        tableMenu.add(lblUndoRedo).colspan(2).center();
        tableMenu.row();
        tableMenu.add().height(10);
        tableMenu.row();
        tableMenu.add(btnSave).colspan(2).center().width(150);
        tableMenu.row();
        tableMenu.add();
        tableMenu.row();
        tableMenu.add(btnBack).colspan(2).center().width(150);
        tableMenu.invalidateHierarchy();

        tableScreen.add(tableMenu).width(200);
        tableScreen.row();
        tableScreen.invalidateHierarchy();
        
        stage.addActor(level);
        stage.addActor(tableScreen);

        Gdx.graphics.setTitle(Sokoban.TITLE+" - "+leveldata.name);
    }

    /**
     * Handle user input and draw the scene
     * @param delta deltaTime
     */
    @Override
    public void render(float delta)
    {
        getUserInput();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        sprite.draw(batch);
        batch.end();

        lblScore.setText(""+level.getScore());
        lblMoves.setText(""+level.getMoveCount());
        lblPushes.setText(""+level.getPushCount());
        lblUndoRedo.setText(""+level.getUndoRedoCount());
        stage.act(delta);
        stage.draw();


    }

    /**
     * Handle user input
     */
    private void getUserInput()
    {
        if(level.isCompleted())
        {
            if (navigationCountdown>0)
                navigationCountdown--;
            else
                level.terminate(true);
        }
        else
        {
            if(level.isWalking()){}
            else if(pressed(W, UP)){ level.up(); }
            else if(pressed(A, LEFT)){ level.left(); }
            else if(pressed(S, DOWN)){ level.down(); }
            else if(pressed(D, RIGHT)){ level.right(); }
            else if(pressed(BACKSPACE)){ level.undo(); }
            else if(pressed(ESCAPE)){ level.reset(); }
        }
    }

    /**
     * Helper that connects the key.pressed value with an OR
     * @param keys keys
     * @return any of them pressed
     */
    private boolean pressed(int...keys)
    {
        boolean pressed = false;
        for(int key : keys)
        {
            pressed |= Gdx.input.isKeyJustPressed(key);
        }
        return pressed;
    }
}
