package ch.bfh.sokoban.security;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.PropertiesUtils;

import java.io.IOException;
import java.util.Base64;

public class Pseudo
{
    public static String crypt(String clear)
    {
        return Base64.getEncoder().encodeToString(clear.getBytes());
    }
    public static String decrypt(String cipher)
    {
        return new String(Base64.getDecoder().decode(cipher));
    }

    public static void storeCrypt(String clear, FileHandle target)
    {
        target.writeString(crypt(clear), false);
    }
    public static String loadCrypt(FileHandle target)
    {
        return decrypt(target.readString());
    }


    public static void storeMap(ObjectMap<String, String> map, FileHandle target)
    {
        ObjectMap<String, String> outMap = new ObjectMap<String, String>();
        for(ObjectMap.Entry<String, String> entry : map)
        {
            outMap.put(entry.key, crypt(entry.value));
        }
        try
        {
            PropertiesUtils.store(outMap, target.writer(false, "UTF-8"), "");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static ObjectMap<String, String> loadMap(FileHandle target)
    {
        ObjectMap<String, String> map = new ObjectMap<String, String>();
        ObjectMap<String, String> outMap = new ObjectMap<String, String>();
        try
        {
            if(!target.exists())
            {
                return new ObjectMap<String, String>();
            }
            PropertiesUtils.load(map, target.reader("UTF-8"));

            for(ObjectMap.Entry<String, String> entry : map)
            {
                outMap.put(entry.key, decrypt(entry.value));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return outMap;
    }


}
