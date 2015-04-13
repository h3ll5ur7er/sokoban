package ch.bfh.sokoban.game;

import ch.bfh.sokoban.GlobalAssets;
import ch.bfh.sokoban.data.LevelData;
import ch.bfh.sokoban.data.LevelPackData;
import ch.bfh.sokoban.data.LevelPackDataCollection;
import ch.bfh.sokoban.data.SlcParser;
import ch.bfh.sokoban.screens.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;

import java.util.List;
import java.util.UUID;

/**
 * The LevelManager handles the levelPacks
 * it is used to load, import and save levels
 */
public class LevelManager
{
    private LevelPackDataCollection data;

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
        if(Gdx.files.external(Settings.get("LevelDataExternalPath")).exists())
            data = new Json().fromJson(LevelPackDataCollection.class, Gdx.files.external(Settings.get("LevelDataExternalPath")));
        else
            reset();
    }
    /**
     * saves the current levelData to the default location
     **/
    public void save()
    {
        new Json().toJson(data, Gdx.files.external(Settings.get("LevelDataExternalPath")));
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
        LevelPackData lpd = new SlcParser(absolutePath).parse();
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

    /**
     * @return get all LevelPacks
     */
    public List<LevelPackData> getLevelPacks()
    {
        return data.levelPacks;
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
        Dialog d = new Dialog(title, GlobalAssets.getInstance().getSkin());
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
