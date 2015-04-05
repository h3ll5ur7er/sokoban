package ch.bfh.sokoban.data;

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
