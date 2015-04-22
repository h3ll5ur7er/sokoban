package ch.bfh.sokoban.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomLevelDataCollection
{
    public List<LevelData> levels;

    public CustomLevelDataCollection()
    {
        levels = new ArrayList<LevelData>();
    }
    public CustomLevelDataCollection(LevelData...lvls)
    {
        this();
        Collections.addAll(levels, lvls);
    }
    public void add(LevelData lvl)
    {
        levels.add(lvl);
    }
    public LevelPackData toLevelPack()
    {
        Object[] lvls = levels.toArray();
        return new LevelPackData("Custom", "These are the levels generated in the editor", "", "","", Arrays.copyOf(lvls, lvls.length, LevelData[].class));
    }
}
