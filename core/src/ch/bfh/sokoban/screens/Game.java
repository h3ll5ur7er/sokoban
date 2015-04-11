package ch.bfh.sokoban.screens;

import ch.bfh.sokoban.data.LevelPack;
import ch.bfh.sokoban.game.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
    private LevelPack.Level leveldata;
    boolean completed = false;
    int navigationCountdown = 30;
    
    // Timer


    /**
     * Create a new Game
     * @param level level to generate
     **/
    public Game(LevelPack.Level level)
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

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        tableScreen = new Table(skin);
        tableScreen.setBounds(0,0, Gdx.graphics.getWidth()-200, Gdx.graphics.getHeight());
        
        tableMenu = new Table(skin);
        tableMenu.setBounds(Gdx.graphics.getWidth()-200,0, 200, Gdx.graphics.getHeight());
        

        this.level = new Level(leveldata, skin);

        TextButton btnBack = new TextButton("BACK", skin);
        btnBack.pad(20);
        btnBack.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                new LevelSelection().activate();
            }
        });
        
        TextButton btnSave = new TextButton("SAVE", skin);
        btnSave.pad(20);
        btnSave.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                // Save data
            }
        });
        
        tableScreen.add();
        tableScreen.add(leveldata.name).colspan(2);
        tableScreen.add();
        tableScreen.row();

        tableScreen.add().colspan(4);
        tableScreen.row();

        tableScreen.add();
        tableScreen.add(level.getTable()).colspan(2).expandX().expandY();
        tableScreen.add();
        tableScreen.row();

        /*tableScreen.add().colspan(3);
        tableScreen.add(btnBack);*/

        tableScreen.invalidateHierarchy();
        
        tableMenu.add(new Label("Score:", skin)).colspan(2).center();
        tableMenu.row();
        tableMenu.add(new Label(""+level.getScore(), skin)).colspan(2).center();
        tableMenu.row();
        tableMenu.add(new Label("Moves:   ", skin, "small"));
        tableMenu.add(new Label("Pushes:", skin, "small"));
        tableMenu.row();
        tableMenu.add(new Label(level.getMoveCount()+"",skin, "small")).center();
        tableMenu.add(new Label(level.getPushCount()+"",skin, "small")).center();
        tableMenu.row();
        tableMenu.add();
        tableMenu.row();
        tableMenu.row();
        tableMenu.add();
        tableMenu.row();
        tableMenu.add(new Label("Undo/Redo's:", skin, "small")).colspan(2).center();
        tableMenu.row();
        tableMenu.add(new Label("" + level.getUndoRedoCount(), skin, "small")).colspan(2).center();
        tableMenu.row();
        tableMenu.add();
        tableMenu.row();
        tableMenu.add(btnSave).colspan(2).center();
        tableMenu.row();
        tableMenu.add();
        tableMenu.row();
        tableMenu.add(btnBack).colspan(2).center();
        tableMenu.invalidateHierarchy();

        stage.addActor(level);
        stage.addActor(tableScreen);
        stage.addActor(tableMenu);

        Gdx.graphics.setTitle(leveldata.name);
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
            if(level.isWalking()) return;
            if(pressed(W, UP)){ level.up(); }
            if(pressed(A, LEFT)){ level.left(); }
            if(pressed(S, DOWN)){ level.down(); }
            if(pressed(D, RIGHT)){ level.right(); }
            if(pressed(BACKSPACE)){ level.undo(); }
            if(pressed(ESCAPE)){ while(level.undo()){} }
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
