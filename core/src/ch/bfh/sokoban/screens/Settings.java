package ch.bfh.sokoban.screens;

import ch.bfh.sokoban.Sokoban;
import ch.bfh.sokoban.game.LevelManager;
import ch.bfh.sokoban.security.Pseudo;
import ch.bfh.sokoban.utils.Lan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectMap;

public class Settings extends MyScreenAdapter
{
    Table table;

    Label tileSizeDisplay;

    SelectBox<String> languagesBox;

    @Override
    public void show()
    {
        super.show();
        
        //load background
        bg = new Texture(Settings.get("SettingsScreen"));
        sprite = new Sprite(bg);
    	sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
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
        languagesBox.addListener(e ->
        {
            set("SelectedLanguage", languagesBox.getSelected());
            return true;
        });

        TextButton btnAdd = new TextButton(Lan.g("AddLevelPack"), skin);
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

        TextButton btnReset = new TextButton(Lan.g("ResetLevelPacks"), skin);
        btnReset.pad(20);
        btnReset.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                LevelManager.instance().reset();
            }
        });

        TextButton btnBack = new TextButton(Lan.g("Back"), skin, Settings.get("BackButtonSize"));
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

        CheckBox notShowEula = new CheckBox(Lan.g("ShowEulaOnStartup"), skin);
        notShowEula.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.set("EULA", notShowEula.isChecked() ? "False" : "True");
            }
        });

        table.add(Lan.g("Settings")).colspan(5).center();
        table.row();

        table.add().width(table.getWidth() / 5);
        table.add(Lan.g("TileSize"), "small").left();
        table.add(tileSizeSlider).expandX().expandY().left();
        table.add(tileSizeDisplay).pad(10).left();
        table.add();
        table.row();

        table.add();
        table.add("LevelPacks", "small").left();
        table.add(btnAdd).pad(10).expandX().expandY().left();
        table.add(btnReset).pad(10).expandX().expandY().left();
        table.add();
        table.row();

        table.add();
        table.add(Lan.g("ShowEulaTitle"), "small").left();
        table.add(notShowEula).pad(10).expandX().expandY().left();
        table.add();
        table.add();
        table.row();

        table.add();
        table.add(Lan.g("Language"), "small").left();
        table.add(languagesBox).pad(10).expandX().expandY().left();
        table.add(Lan.g("RestartRequired")).left();
        table.add();
        table.row();

        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.row();

        table.add();
        table.add();
        table.add();
        table.add();
        table.add(btnBack).bottom().right();
        table.row();

        //table.debug();

        table.invalidateHierarchy();

        stage.addActor(table);
    }
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        sprite.draw(batch);
        batch.end();

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
    public static int getInt(String key)
    {
        return Integer.parseInt(get(key));
    }
    public static long getLong(String key)
    {
        return Long.parseLong(get(key));
    }
    public static float getFloat(String key)
    {
        return Float.parseFloat(get(key));
    }
    public static double getDouble(String key)
    {
        return Integer.parseInt(get(key));
    }
    public static boolean has(String key)
    {
        return Manager.getInstance().map.containsKey(key);
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
        private static final String settingsPath = "SokobanSettings";
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
            Settings.set("StartERKSplashScreen", "img/ERKsplash.png");
            Settings.set("StartBFHSplashScreen", "img/BFHsplash.png");
            Settings.set("StartScreen", "img/startbackground.png");
            Settings.set("MenuScreen", "img/MenuScreen.png");
            Settings.set("LevelSelectScreen", "img/LevelSelectScreen.png");
            Settings.set("GameScreen", "img/GameScreen.png");
            Settings.set("LevelEditorScreen", "img/LevelEditorScreen.png");
            Settings.set("SettingsScreen", "img/SettingsScreen.png");
            Settings.set("CompletedSplashScreen", "img/levelCompleted.png");
            Settings.set("LevelDataExternalPath", "SokobanLevelData.jsld");
            Settings.set("CustomLevelDataExternalPath", "SokobanCustomLevelData.jscd");
            Settings.set("BackButtonSize", "small");
            Settings.set("PlayButtonSize", "small");
            Settings.set("SaveButtonSize", "small");
            Settings.set("NextButtonSize", "small");
            Settings.set("TileSize", "50");
            Settings.set("EULA", "0");
            //TODO
            Settings.set("MenuTitle", Sokoban.TITLE + " v"+Sokoban.VERSION);
            Settings.set("LvlSelTitle", Sokoban.TITLE+" - "+"Level Selection");
            // Settings.set("LevelTitle", Sokoban.TITLE+" - "+"value"); 	directly set in Game-Screen
            Settings.set("EditTitle", Sokoban.TITLE+" - "+"Level Editor");
            Settings.set("SettingsTitle", Sokoban.TITLE+" - "+"Settings");

        }
        public static void save()
        {
            Pseudo.storeMap(getInstance().map, Gdx.files.external(settingsPath));
        }
        public static void load()
        {
            if(Gdx.files.external(settingsPath).exists())
                getInstance().map = Pseudo.loadMap(Gdx.files.external(settingsPath));
            else
            {
                create();
            }
        }
    }
}
