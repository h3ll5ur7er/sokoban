//*********************************************************//
// Author: Käser Robin, Knecht Emanuel                     //
// Berner Fachhochschule                                   //
//*********************************************************//

package ch.bfh.sokoban.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Parses .slc files into LevelPack data
 */
public class SlcParser
{
    String path;

    /**
     * Create a new instance for every file you want to parse.
     * @param absolutePath path to target file
     */
    public SlcParser(String absolutePath)
    {
        path = absolutePath;
    }

    /**
     * @return the parsed LevelPack data or null if parsing failed
     */
    public LevelPackData parse()
    {
        try
        {
            //Workaround:
            //Replace all '<L>%%%</L>' tags with '<L>-%%%-</L>' so spaces at the beginning/ending are not trimmed
            String data = Gdx.files.absolute(path).readString();
            data = data.replace("<L>","<L>-").replace("</L>", "-</L>");
            XmlReader.Element root = new XmlReader().parse(data);

            Object[] objects = Arrays.stream(root.getChildrenByNameRecursively("Level").toArray())
                    .map
                            (
                                    e ->
                                    {
                                        Object[] o = Arrays.stream(e.getChildrenByName("L").toArray())
                                                .map(x->x.getText().substring(1, x.getText().length()-1))
                                                .map(x ->
                                                {
                                                    while (x.length() < e.getInt("Width"))
                                                        x += " ";
                                                    return x;
                                                })
                                                .toArray();

                                        String[] s = Arrays.copyOf(o, o.length, String[].class);
                                        return new LevelData(
                                                e.get("Id"),
                                                e.getInt("Width"),
                                                e.getInt("Height"),
                                                s);
                                    }
                            )
                    .collect(Collectors.toList()).toArray();

            return new LevelPackData(
                    root.get("Title",""),
                    root.get("Description", ""),
                    root.get("Email",""),
                    root.get("Url",""),
                    root.getChildByName("LevelCollection").get("Copyright",""),
                    Arrays.copyOf(objects, objects.length, LevelData[].class));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private static <T> T foo(T bar)
    {
        System.out.println(bar);
        return bar;
    }
}
