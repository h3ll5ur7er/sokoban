package ch.bfh.sokoban.screens;

import ch.bfh.sokoban.data.LevelPack;
import ch.bfh.sokoban.game.Level;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import static com.badlogic.gdx.Input.Keys.*;
/**
 * Game generates a level from the provided data, presents it to the user and gets user input
 **/
public class Game extends MyScreenAdapter
{
    private Table table;
    private Level level;
    private LevelPack.Level leveldata;


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

        table = new Table(skin);
        table.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.level = new Level(leveldata, skin);

        TextButton btnBack = new TextButton("BACK", skin, "small");
        btnBack.pad(20);
        btnBack.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                new LevelSelection().activate();
            }
        });

        table.add();
        table.add(leveldata.name).colspan(2);
        table.add();
        table.row();

        table.add().colspan(4);
        table.row();

        table.add();
        table.add(level.getTable()).colspan(2).expandX().expandY();
        table.add();
        table.row();

        table.add().colspan(3);
        table.add(btnBack);

        table.invalidateHierarchy();

        stage.addActor(level);
        stage.addActor(table);

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
        if(level.isWalking()) return;
        if(pressed(W, UP)){ level.up(); }
        if(pressed(A, LEFT)){ level.left(); }
        if(pressed(S, DOWN)){ level.down(); }
        if(pressed(D, RIGHT)){ level.right(); }
        if(pressed(BACKSPACE)){ level.undo(); }
        if(pressed(ESCAPE)){ while(level.undo()){} }
    }

    /**
     * Helper that connects the key.pressed value with a OR
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
