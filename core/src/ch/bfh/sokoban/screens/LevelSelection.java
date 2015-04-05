package ch.bfh.sokoban.screens;

import ch.bfh.sokoban.GlobalAssets;
import ch.bfh.sokoban.data.LevelPack;
import ch.bfh.sokoban.screens.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;

/**
 * Screen to select a level to play
 */
public class LevelSelection extends MyScreenAdapter
{
    Table table;

    List<Difficulty> list1;
    List<String> list2;

    LevelPack lvls;

    /**
     * Load levelPack from json and build up the gui
     */
    @Override
    public void show()
    {
        super.show();

        lvls = new Json().fromJson(LevelPack.class, Gdx.files.internal("lvl/levels.json"));
        table = new Table(skin);
        list1 = new List<Difficulty>(skin);
        list2 = new List<String>(skin);

        ScrollPane scroll1 = new ScrollPane(list1, skin);
        ScrollPane scroll2 = new ScrollPane(list2, skin);

        TextButton btnPlay = new TextButton("PLAY", skin);
        TextButton btnBack = new TextButton("BACK", skin, "small");

        Gdx.input.setInputProcessor(stage);

        table.setBounds(0,0, 1200, 720);

        list1.setItems(Difficulty.values());

        list2.setItems("1", "2", "3", "4", "5", "6", "7", "8", "9");

        btnPlay.addListener(playListener());
        btnPlay.pad(15);

        btnBack.addListener(backListener());
        btnBack.pad(10);

        table.add().width(300);
        table.add("SELECT LEVEL").width(600).colspan(2);
        table.add().width(300).row();

        table.add(scroll1).width(300);
        table.add(scroll2).width(300);
        table.add(btnPlay).width(300);
        table.add(btnBack).width(300).bottom().right()
            .expandX().expandY();
        table.invalidateHierarchy();

        stage.addActor(table);
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
                new Game(lvls.levelPack[list1.getSelected().numeric][list2.getSelectedIndex()]).activate();
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

    /**
     * The difficulties available in the current levelPack (To be done dynamic as soon as the levelpack is in a db and not in a file)
     */
    public static enum Difficulty
    {
        Hard  (0, "Too Hard",        "hard"),
        Nuts  (1, "Completely Nuts", "nuts"),
        Sick  (2, "Totally Sick",    "sick"),
        Insane(3, "Horribly Insane", "insane");

        private final int numeric;
        private final String display, asset;

        Difficulty(int numeric, String display, String asset)
        {
            this.numeric = numeric;
            this.display = display;
            this.asset = asset;
        }

        public String getAssetName()
        {
            return asset;
        }

        @Override
        public String toString() {
            return display;
        }
    }
}
