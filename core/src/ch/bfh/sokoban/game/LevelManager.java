package ch.bfh.sokoban.game;

import ch.bfh.sokoban.GlobalAssets;
import ch.bfh.sokoban.data.*;
import ch.bfh.sokoban.screens.Settings;
import ch.bfh.sokoban.security.Pseudo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The LevelManager handles the levelPacks
 * it is used to load, import and save levels
 */
public class LevelManager
{
    private LevelPackDataCollection data;
    private CustomLevelDataCollection custom;

    //SINGLETON
    private static final Object padlock = new Object();
    private static LevelManager inst;
    private LevelManager(){}
    public static LevelManager instance()
    {
        if(inst == null)
        {
            synchronized (padlock)
            {
                if(inst == null)
                {
                    inst = new LevelManager();
                }
            }
        }
        return inst;
    }
    ///SINGLETON

    /**
     * loads the levelData form the default location
     **/
    public void load()
    {
        if(Gdx.files.external(Settings.get("CustomLevelDataExternalPath")).exists())
        {
            custom = new Json()
                    .fromJson(
                            CustomLevelDataCollection.class,
                            Pseudo.decrypt(
                                    Gdx.files.external(Settings.get("CustomLevelDataExternalPath"))
                                            .readString()));
        }
        else
        {
            custom = new CustomLevelDataCollection();
        }
        if(Gdx.files.external(Settings.get("LevelDataExternalPath")).exists())
        {
            data = new Json()
                    .fromJson(
                            LevelPackDataCollection.class,
                            Pseudo.decrypt(
                                    Gdx.files.external(Settings.get("LevelDataExternalPath"))
                                            .readString()));
        }
        else
        {
            reset();
        }
    }
    /**
     * saves the current levelData to the default location
     **/
    public void save()
    {
         Gdx.files.external(Settings.get("LevelDataExternalPath")).writeString(Pseudo.crypt(new Json().toJson(data)), false);
         Gdx.files.external(Settings.get("CustomLevelDataExternalPath")).writeString(Pseudo.crypt(new Json().toJson(custom)), false);
    }

    /**
     * resets the levelPacks to default
     **/
    public void reset()
    {
        data = new Json().fromJson(LevelPackDataCollection.class, Gdx.files.internal(Settings.get("DefaultLevelCollectionPath")));
        save();
    }

    /**
     * imports a slc file intot the current collection
     * @param absolutePath path to the .slc file
     **/
    public void importSlc(String absolutePath)
    {
        Instant startTime = java.time.Instant.now();
        LevelPackData lpd = new SlcParser(absolutePath).parse();
        System.out.println("Imported " + absolutePath + " in "+java.time.Duration.between(startTime, Instant.now()).toMillis()+" ms");
        if(lpd == null)
        {
            notifyUser("Error", "Failed to load "+ absolutePath);
        }
        else
        {
            data.levelPacks.add(lpd);

            save();

            notifyUser("Success", "Successfully added");
        }
    }

    public void addCustomLevel(int width, int height, String[] data)
    {
        Gdx.input.getTextInput(new Input.TextInputListener()
        {
            @Override
            public void input(String text)
            {
                custom.levels.add(new LevelData(text, width, height, data));
                save();
            }

            @Override
            public void canceled() {
                addCustomLevel(width, height, data);
            }
        }, "Enter a name for the level", "", "Name");
    }

    /**
     * @return get all LevelPacks
     */
    public List<LevelPackData> getLevelPacks()
    {
        List<LevelPackData> packs = new ArrayList<>();
        if(custom != null && custom.levels.size()>0)
            packs.add(custom.toLevelPack());

        packs.addAll(data.levelPacks);

        return packs;
    }

    /**
     * @return get all LevelPacks
     */
    public LevelData[] getLevelPacks(int packIndex)
    {
        return data.levelPacks.get(packIndex).levels;
    }

    /**
     * Shows a dialog to the user that closes as soon as
     * @param title dialog title
     * @param content dialog message
     */
    private void notifyUser(String title, String content)
    {
        Dialog d = new Dialog(title, GlobalAssets.getInstance().getSkin(), "default_black");
        d.add(content);
        d.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                d.remove();
            }
        });
        d.show((Stage) Gdx.input.getInputProcessor());
        System.out.println(title + " : " + content);
    }

    public void conv()
    {
        for(LevelPackData lp : data.levelPacks)
        {
            if(lp.identifier.equals(""))
                lp.identifier = UUID.randomUUID().toString();
            for (LevelData l : lp.levels)
            {
                if(l.id.equals(""))
                {
                    l.id = UUID.randomUUID().toString();
                }
            }
        }
    }
}
