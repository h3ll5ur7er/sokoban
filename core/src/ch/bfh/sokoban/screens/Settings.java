package ch.bfh.sokoban.screens;

import ch.bfh.sokoban.Sokoban;
import ch.bfh.sokoban.game.LevelManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.PropertiesUtils;

import java.io.IOException;

public class Settings extends MyScreenAdapter
{
    Table table;

    Label tileSizeDisplay;

    SelectBox<String> languagesBox;

    Label selectedLanguageDisplay;


    @Override
    public void show()
    {
        super.show();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        table = new Table(skin);
        table.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        int tileSize;
        Slider tileSizeSlider = new Slider(20, 80, 1, false, skin);
        tileSizeSlider.setValue(tileSize = Integer.parseInt(get("TileSize")));
        tileSizeSlider.addListener(
            (e)->
            {
                Integer ts = ((int) tileSizeSlider.getValue());
                tileSizeDisplay.setText(ts.toString());
                set("TileSize", ts.toString());
                return true;
            }
        );

        tileSizeDisplay = new Label(Integer.toString(tileSize), skin);


        languagesBox = new SelectBox<String>(skin);
        languagesBox.setItems("Deutsch", "Français", "English");
        languagesBox.setSelected(get("SelectedLanguage"));
        selectedLanguageDisplay = new Label(languagesBox.getSelected(), skin);
        languagesBox.addListener(e->
        {
            selectedLanguageDisplay.setText(languagesBox.getSelected());
            set("SelectedLanguage", languagesBox.getSelected());
            return true;
        });

        TextButton btnAdd = new TextButton(get("AddPackButtonText"), skin);
        btnAdd.pad(20);
        btnAdd.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);
                Gdx.input.getTextInput(new Input.TextInputListener()
                {
                    @Override
                    public void input(String text)
                    {
                        LevelManager.instance().importSlc(text);
                    }

                    @Override
                    public void canceled()
                    {

                    }
                }, "Enter path", "", "path");
            }
        });

        TextButton btnReset = new TextButton(get("ResetPackButtonText"), skin);
        btnReset.pad(20);
        btnReset.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                LevelManager.instance().reset();
            }
        });

        TextButton btnBack = new TextButton(get("BackButtonText"), skin);
        btnBack.pad(20);
        btnBack.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                super.clicked(event, x, y);
                new MainMenu().activate();
            }
        });


        table.add().width(table.getWidth()/5);
        table.add().width(table.getWidth() / 5);
        table.add(get("SettingsTitle")).width(table.getWidth() / 5);
        table.add().width(table.getWidth() / 5);
        table.add().width(table.getWidth()/5);
        table.row();

        table.add();
        table.add(get("TileSizeLabelText"), "small");
        table.add(tileSizeSlider).expandX().expandY();
        table.add(tileSizeDisplay).pad(10);
        table.add();
        table.row();

        table.add();
        table.add(get("LanguageLabelText"), "small");
        table.add(languagesBox);
        table.add(selectedLanguageDisplay);
        table.add();
        table.row();

        table.add();
        table.add();
        table.add(btnAdd);
        table.add(btnReset);
        table.add();
        table.row();

        table.add();
        table.add();
        table.add();
        table.add();
        table.add(btnBack);
        table.row();


        table.invalidateHierarchy();

        stage.addActor(table);
    }
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act(delta);
        stage.draw();
    }
    @Override
    public void dispose() {
        Manager.save();
        super.dispose();
    }

    public static String get(String key)
    {
        if(Manager.getInstance().map.containsKey(key))
            return Manager.getInstance().map.get(key);
        return null;
    }
    public static void set(String key, String value)
    {
        Manager.getInstance().map.put(key, value);
    }

    public static void load()
    {
        Manager.load();
    }
    public static void save()
    {
        Manager.save();
    }

    private static class Manager
    {
        private ObjectMap<String, String> map = new ObjectMap<String, String>();

        //SINGLETON
        private static Manager inst;
        private static final Object padlock = new Object();
        private Manager(){}
        public static Manager getInstance()
        {
            if(inst ==null)
            {
                synchronized (padlock)
                {
                    if(inst == null)
                    {
                        inst = new Manager();
                    }
                }
            }
            return inst;
        }
        ///SINGLETON

        public static void create()
        {
            getInstance().map = new ObjectMap<>();

            Settings.set("SelectedLanguage", "English");
            Settings.set("DefaultTextureAtlas", "lvl/atlas.pack");
            Settings.set("DefaultSkin", "lvl/game_style.json");
            Settings.set("DefaultLevelCollectionPath","lvl/defaultLevels.json");
            Settings.set("StartSplashScreen", "img/splash.png");
            Settings.set("CompletedSplashScreen", "img/levelCompleted.png");
            Settings.set("LevelDataExternalPath", "SokobanLevelData.jsld");
            Settings.set("SettingsExternalPath", "SokobanSettings.config");
            Settings.set("PlayButtonText", "PLAY");
            Settings.set("EditorButtonText", "EDITOR");
            Settings.set("SettingsButtonText", "SETTINGS");
            Settings.set("SettingsTitle", "Settings");
            Settings.set("ExitButtonText", "EXIT");
            Settings.set("BackButtonText", "BACK");
            Settings.set("BackButtonSize", "small");
            Settings.set("AddPackButtonText", "Add LevelPack");
            Settings.set("ResetPackButtonText", "Reset LevelPacks");
            Settings.set("TileSizeLabelText", "Tile size");
            Settings.set("LanguageLabelText", "Language");
            Settings.set("TileSize", "50");
        }
        public static void save()
        {
            try
            {
                PropertiesUtils.store(getInstance().map, Gdx.files.external("SokobanSettings.config").writer(false), Sokoban.TITLE);
            }
            catch (IOException e)
            {
                System.err.println("Error writing properties");
            }
        }
        public static void load()
        {
            try
            {
                if(Gdx.files.external(Sokoban.TITLE).exists())
                    PropertiesUtils.load(getInstance().map, Gdx.files.external("SokobanSettings.config").reader());
                else
                {
                    create();
                }
            }
            catch (IOException e)
            {
                System.err.println("Error reading properties");
            }
        }
    }
}
