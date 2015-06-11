//*********************************************************//
// Author: Käser Robin, Knecht Emanuel                     //
// Berner Fachhochschule                                   //
//*********************************************************//

package ch.bfh.sokoban.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ch.bfh.sokoban.Sokoban;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = Sokoban.TITLE + " v"+Sokoban.VERSION;
        config.vSyncEnabled = true;
        config.width = 1200;
        config.height = 720;
        config.resizable = false;
		new LwjglApplication(new Sokoban(), config);
	}
}