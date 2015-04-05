package ch.bfh.sokoban.data;

/**
 * LevelPack is a simple data structure to hold the levels loaded from json/sql/whatever
 **/
public class LevelPack
{
    public Level[][] levelPack;

    public static class Level
    {
        public int index;
        public String name;
        public int width;
        public int height;
        public String[] data;
    }
}
