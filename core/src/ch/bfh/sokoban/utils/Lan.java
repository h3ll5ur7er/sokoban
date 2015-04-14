package ch.bfh.sokoban.utils;

import ch.bfh.sokoban.screens.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.PropertiesUtils;

import java.io.IOException;

public class Lan
{
    public static final ObjectMap<String, String> d = new ObjectMap<String, String>();

    public static void init()
    {
        try
        {
            PropertiesUtils.load(d, Gdx.files.internal("lang/"+ Settings.get("SelectedLanguage")+".sdic").reader());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String g(String tag)
    {
        if(!d.containsKey(tag))
        {
            System.out.println(tag + " not found in the current dictionary");
            return "";
        }
        return d.get(tag);
    }
}
