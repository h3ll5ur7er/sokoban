package ch.bfh.sokoban.screens;

import static ch.bfh.sokoban.game.Constants.BOX_FLOOR;
import static ch.bfh.sokoban.game.Constants.BOX_FLOOR_D;
import static ch.bfh.sokoban.game.Constants.BOX_GOAL;
import static ch.bfh.sokoban.game.Constants.BOX_GOAL_D;
import static ch.bfh.sokoban.game.Constants.FLOOR;
import static ch.bfh.sokoban.game.Constants.FLOOR_D;
import static ch.bfh.sokoban.game.Constants.GOAL;
import static ch.bfh.sokoban.game.Constants.GOAL_D;
import static ch.bfh.sokoban.game.Constants.PLAYER_FLOOR;
import static ch.bfh.sokoban.game.Constants.PLAYER_FLOOR_D;
import static ch.bfh.sokoban.game.Constants.PLAYER_GOAL;
import static ch.bfh.sokoban.game.Constants.PLAYER_GOAL_D;
import static ch.bfh.sokoban.game.Constants.WALL;
import static ch.bfh.sokoban.game.Constants.WALL_D;

import java.util.LinkedList;

import ch.bfh.sokoban.game.Constants;
import ch.bfh.sokoban.utils.Lan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 *	The Editor to create own levels
 */
public class LevelEditor extends MyScreenAdapter {
	
	LinkedList<LinkedList<EditorTile>> levelCreation = new LinkedList<>();
	private int editStep = 1;
	private int goalCount = 0;
	private int boxCount = 0;
	private int boxOnGoalCount = 0;
	private boolean playerAdded = false;
	private int[]playerCoords = new int[2];
	
	public LevelEditor()
	{
		playerCoords[0] = -1;
		
		for(int i = 0; i<5;i++)
		{
			levelCreation.add(new LinkedList<EditorTile>());
		}
		
		
		for(int i = 0; i<5;i++)
		{
			levelCreation.get(i).add(new EditorTile(Constants.WALL));
			levelCreation.get(i).add(new EditorTile(Constants.WALL));
			levelCreation.get(i).add(new EditorTile(Constants.WALL));
		}

		levelCreation.get(1).get(1).textureId = Constants.FLOOR;
		levelCreation.get(2).get(1).textureId = Constants.FLOOR;
		levelCreation.get(3).get(1).textureId = Constants.FLOOR;

	}
	
	Table mainTable;
	Table editTable;
	Table navigationTable;
	
    /**
     * Starts a basic Level grid ready to be edited to make a complete level
     */
    @Override
    public void show()
    {
        super.show();
        Gdx.input.setInputProcessor(stage);
        Label title = new Label(Lan.g("LevelEditor"), skin, "big");
        Label desc = new Label(Lan.g("DescStep1"), skin, "small");
        desc.setWrap(true);
        
        
        Dialog errorDialog1 = new Dialog(Lan.g("WentWrong"), skin);
        Label errorText1 = new Label(Lan.g("CantGoNextStep1"), skin);
        errorText1.setWrap(true);
        errorDialog1.getContentTable().add(errorText1).width(400).height(500).expandX().expandY();
        
        TextButton btnCloseErrorWindow1 = new TextButton(Lan.g("Exit"), skin);
        btnCloseErrorWindow1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                errorDialog1.remove();
            }
        });
        errorDialog1.getButtonTable().add(btnCloseErrorWindow1);
        

        Dialog errorDialog2 = new Dialog(Lan.g("WentWrong"), skin);
        Label errorText2 = new Label(Lan.g("CantGoNextStep2"), skin);
        errorText2.setWrap(true);
        errorDialog2.getContentTable().add(errorText2).width(400).height(150).expandX().expandY();
        
        TextButton btnCloseErrorWindow2 = new TextButton(Lan.g("Exit"), skin);
        btnCloseErrorWindow2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                errorDialog2.remove();
            }
        });
        errorDialog2.getButtonTable().add(btnCloseErrorWindow2);
        
        
        Dialog errorDialog3 = new Dialog(Lan.g("WentWrong"), skin);
        Label errorText3 = new Label(Lan.g("CantGoNextStep3"), skin);
        errorText3.setWrap(true);
        errorDialog3.getContentTable().add(errorText3).width(400).height(150).expandX().expandY();
        
        TextButton btnCloseErrorWindow3 = new TextButton(Lan.g("Exit"), skin);
        btnCloseErrorWindow3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                errorDialog3.remove();
            }
        });
        errorDialog3.getButtonTable().add(btnCloseErrorWindow3);
        
        
        Dialog errorDialog4 = new Dialog(Lan.g("WentWrong"), skin);
        Label errorText4 = new Label(Lan.g("CantGoNextStep4"), skin);
        errorText4.setWrap(true);        
        errorDialog4.getContentTable().add(errorText4).width(400).height(150).expandX().expandY();
        
        TextButton btnCloseErrorWindow4 = new TextButton(Lan.g("Exit"), skin);
        btnCloseErrorWindow4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                errorDialog4.remove();
            }
        });
        errorDialog4.getButtonTable().add(btnCloseErrorWindow4);
        
        
        mainTable = new Table(skin);
        mainTable.setBounds(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        editTable = new Table(skin);
        editTable.setBounds(0,  0, Gdx.graphics.getWidth()-300, Gdx.graphics.getHeight()-title.getHeight());
        
        navigationTable = new Table(skin);
        navigationTable.setBounds(Gdx.graphics.getWidth()-300,  0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()-title.getHeight());
        
        ScrollPane scroll = new ScrollPane(editTable, skin);
        
        TextButton btnBack = new TextButton(Lan.g("Back"), skin, Settings.get("BackButtonSize"));
        btnBack.pad(20);
        TextButton btnNext = new TextButton(Lan.g("Next"), skin, Settings.get("NextButtonSize"));
        btnNext.pad(20);
        
        btnBack.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                switch(editStep)
                {
            		case 1:
            			new MainMenu().activate();
            			break;
            		case 2:
            			desc.setText(Lan.g("DescStep1"));
            			editStep--;
            			break;
            		case 3:
            			desc.setText(Lan.g("DescStep2"));
            			editStep--;
            			break;
            		case 4:
            			desc.setText(Lan.g("DescStep3"));
            			btnNext.setText(Lan.g("Next"));
            			editStep--;
            			break;
                }
            }
        });
        
        btnNext.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                switch(editStep)
                {
            		case 1:
            			if(isSolvable())
            			{
            				editStep++;
            				desc.setText(Lan.g("DescStep2"));
            			} else {
            				errorDialog1.show(stage);    
            			}
            			break;
            		case 2:
            			if(goalCount >= 1)
            			{
            				editStep++;
            				desc.setText(Lan.g("DescStep3"));
            			} else {
            				errorDialog2.show(stage);   
            			}
            			break;
            		case 3:
            			if(goalCount == boxCount && goalCount > boxOnGoalCount)
            			{
            				editStep++;
            				btnNext.setText(Lan.g("Create"));
            				desc.setText(Lan.g("DescStep4"));
            			} else {
            				errorDialog3.show(stage);  
            			}
            			break;
            		case 4:
            			if(playerAdded)
            			{
            				saveLevel();
            			} else {
            				errorDialog4.show(stage);  
            			}
                }
            }
        });
        
        for(int y = 0; y<levelCreation.get(0).size();y++)
        {
        	for(int x = 0; x<levelCreation.size();x++)
        	{
        		editTable.add(levelCreation.get(x).get(y));
        	}
        	editTable.row();
        }
        editTable.invalidateHierarchy();
        
        navigationTable.add(desc).width(300).height(navigationTable.getHeight()-112).expandX().expandY();
        navigationTable.row();
        navigationTable.add(btnNext).width(300).expandX().expandY();
        navigationTable.row();
        navigationTable.add(btnBack).width(300).expandX().expandY();
        navigationTable.invalidateHierarchy();
        
        mainTable.add(title).colspan(2).center();
        mainTable.row();
        mainTable.add(scroll).size(Gdx.graphics.getWidth()-300, Gdx.graphics.getHeight()-title.getHeight());
        mainTable.add(navigationTable).bottom();
        mainTable.invalidateHierarchy();
        mainTable.debug();

        stage.addActor(mainTable);
    }
    
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }
    
    /**
     * Expands the Game grid
     * @param tile	The Tile that got clicked to expand
     */
    public void expandGameGrid(EditorTile tile)
    {
    	// If the tile x == list lenght, add a column at the right
    	// If the tile y == list height, add a row at the bottom
    	// If the tile x == 0, add a column at the right and move the whole content 1 column to the right
    	// If the tile y == 0, add a row at the bottom and move the whole content 1 row down
    	int[]coordinates = calculateCoords(tile);
    	levelCreation.get(coordinates[0]).get(coordinates[1]).textureId = Constants.FLOOR;
    	
    	// first row from the left, no corner
    	if(coordinates[0] == 0
    			&& coordinates[1] != 0 && coordinates[1] != levelCreation.get(0).size()-1)
    	{
    		levelCreation.addFirst(new LinkedList<EditorTile>());
    		for(int i = 0; i<levelCreation.get(1).size();i++)
    		{
    			levelCreation.get(0).add(new EditorTile(Constants.WALL));
    		}
    	// first row from top, no corner
    	} else if(coordinates[1] == 0
    			&& coordinates[0] != 0 && coordinates[0] != levelCreation.size()-1)
    	{
    		for(int i = 0; i<levelCreation.size();i++)
    		{
    			levelCreation.get(i).addFirst(new EditorTile(Constants.WALL));
    		}
    	// first row from the right, no corner
    	} else if(coordinates[0] == levelCreation.size()-1
    			&& coordinates[1] != 0 && coordinates[1] != levelCreation.get(0).size()-1)
    	{
        	levelCreation.add(new LinkedList<EditorTile>());
    		for(int i = 0; i<levelCreation.get(1).size();i++)
    		{
    			levelCreation.get(levelCreation.size()-1).add(new EditorTile(Constants.WALL));
    		}
    	// first row from bottom, no corner	
    	} else if(coordinates[1] == levelCreation.get(coordinates[0]).size()-1
    			&& coordinates[0] != 0 && coordinates[0] != levelCreation.size()-1)
    	{
    		for(int i = 0; i<levelCreation.size();i++)
    		{
    			levelCreation.get(i).add(new EditorTile(Constants.WALL));
    		}
    	// corner left-top
    	} else if(coordinates[0] == 0 && coordinates[1] == 0)
    	{
    		levelCreation.addFirst(new LinkedList<EditorTile>());
    		for(int i = 0; i<levelCreation.get(1).size();i++)
    		{
    			levelCreation.get(0).add(new EditorTile(Constants.WALL));
    		}
    		
    		for(int i = 0; i<levelCreation.size();i++)
    		{
    			levelCreation.get(i).addFirst(new EditorTile(Constants.WALL));
    		}
    	// corner left-bottom	
    	} else if(coordinates[0] == 0 && coordinates[1] == levelCreation.get(0).size()-1)
    	{
    		levelCreation.addFirst(new LinkedList<EditorTile>());
    		for(int i = 0; i<levelCreation.get(1).size();i++)
    		{
    			levelCreation.get(0).add(new EditorTile(Constants.WALL));
    		}

    		for(int i = 0; i<levelCreation.size();i++)
    		{
    			levelCreation.get(i).addLast(new EditorTile(Constants.WALL));
    		}
    	// corner right-top
    	} else if(coordinates[0] == levelCreation.size()-1 && coordinates[1] == 0)
    	{
    		levelCreation.add(new LinkedList<EditorTile>());
    		for(int i = 0; i<levelCreation.get(1).size();i++)
    		{
    			levelCreation.get(levelCreation.size()-1).add(new EditorTile(Constants.WALL));
    		}

    		for(int i = 0; i<levelCreation.size();i++)
    		{
    			levelCreation.get(i).addFirst(new EditorTile(Constants.WALL));
    		}
    	// corner right-down	
    	} else if(coordinates[0] == levelCreation.size()-1 && coordinates[1] == levelCreation.get(0).size()-1)
    	{
        	levelCreation.add(new LinkedList<EditorTile>());
    		for(int i = 0; i<levelCreation.get(1).size();i++)
    		{
    			levelCreation.get(levelCreation.size()-1).add(new EditorTile(Constants.WALL));
    		}
    		
    		for(int i = 0; i<levelCreation.size();i++)
    		{
    			levelCreation.get(i).add(new EditorTile(Constants.WALL));
    		}
    	} else {
    		return;
    	}
    	
		editTable.clear();
		updateLevel();
    }
    
    /**
     * Changes the Tile to a wall, if its at a Level Edge it changes the outer tile to a transparent one
     * @param tile The tile that should be removed
     */
    public void shrinkGameGrid(EditorTile tile)
    {
    	int[]coordinates = calculateCoords(tile);    	
   		levelCreation.get(coordinates[0]).get(coordinates[1]).textureId = Constants.WALL;
   		
    	// second row from the left, no corner
    	if(coordinates[0] == 1
    			&& coordinates[1] != 1 && coordinates[1] != levelCreation.get(0).size()-2)
    	{
    		outerloop:
	    		while(true)
	    		{
		    		for(int i = 0; i<levelCreation.get(1).size();i++)
		    		{
		    			if(!(levelCreation.get(1).get(i).textureId == Constants.WALL))
		    			{
		    				break outerloop;
		    			}
		    			if(i == levelCreation.get(1).size()-1) levelCreation.removeFirst();
		    		}
	    		}
    	// second row from top, no corner	
    	} else if(coordinates[1] == 1
    			&& coordinates[0] != 1 && coordinates[0] != levelCreation.size()-2)
    	{
    		outerloop:
	    		while(true)
	    		{
		    		for(int i = 0; i<levelCreation.size();i++)
		    		{
		    			if(!(levelCreation.get(i).get(1).textureId == Constants.WALL))
		    			{
		    				break outerloop;
		    			}
		    			if(i == levelCreation.size()-1)
		    			{
		    	    		for(int j = 0; j<levelCreation.size();j++)
		    	    		{
		    	    			levelCreation.get(j).removeFirst();
		    	    		}
		    			}
		    		}
	    		}
    	// second row from the right, no corner
    	} else if(coordinates[0] == levelCreation.size()-2
    			&& coordinates[1] != 1 && coordinates[1] != levelCreation.get(0).size()-2)
    	{
    		outerloop:
	    		while(true)
	    		{
		    		for(int i = 0; i<levelCreation.get(0).size();i++)
		    		{
		    			if(!(levelCreation.get(levelCreation.size()-2).get(i).textureId == Constants.WALL))
		    			{
		    				break outerloop;
		    			}
		    			if(i == levelCreation.get(0).size()-1) levelCreation.removeLast();
		    		}
	    		}
    	// second row from bottom, no corner
    	} else if(coordinates[1] == levelCreation.get(0).size()-2
    			&& coordinates[0] != 1 && coordinates[0] != levelCreation.size()-2)
    	{
    		outerloop:
    			while(true){
		    		for(int i = 0; i<levelCreation.size();i++)
		    		{
		    			if(!(levelCreation.get(i).get(levelCreation.get(0).size()-2).textureId == Constants.WALL))
		    			{
		    				break outerloop;
		    			}
		    			if(i == levelCreation.size()-1)
		    			{
		    	    		for(int j = 0; j<levelCreation.size();j++)
		    	    		{
		    	    			levelCreation.get(j).removeLast();
		    	    		}
		    			}
		    		}
    			}
    	// 3x3 Matrix - A Floor surrounded by Walls
    	}else if(coordinates[0] == 1 && coordinates[1] == 1 &&
    			coordinates[0] == levelCreation.size()-2 && coordinates[1] == levelCreation.get(0).size()-2)
    	{
    		// Do nothing
    	// 1-1 corner left-top	
    	} else if(coordinates[0] == 1 && coordinates[1] == 1)
    	{
    		leftloop:
    			while(true)
    			{
    				for(int i = 0; i<levelCreation.get(1).size();i++)
    				{
    					if(!(levelCreation.get(1).get(i).textureId == Constants.WALL))
    					{
    						break leftloop;
    					}
    					if(i == levelCreation.get(1).size()-1) levelCreation.removeFirst();
    				}
    			}
    	
	    	toploop:
	    		while(true)
	    		{
	    			for(int i = 0; i<levelCreation.size();i++)
	    			{
	    				if(!(levelCreation.get(i).get(1).textureId == Constants.WALL))
	    				{
	    					break toploop;
	    				}
	    				if(i == levelCreation.size()-1)
	    				{
	    					for(int j = 0; j<levelCreation.size();j++)
	    					{
	    						levelCreation.get(j).removeFirst();
	    					}
	    				}
	    			}
	    		}
    	// 1-1 corner left-bottom	
    	} else if(coordinates[0] == 1 && coordinates[1] == levelCreation.get(0).size()-2)
    	{
    		leftloop:
	    		while(true)
	    		{
	    			for(int i = 0; i<levelCreation.get(1).size();i++)
	    			{
	    				if(!(levelCreation.get(1).get(i).textureId == Constants.WALL))
	    				{
	    					break leftloop;
	    				}
	    				if(i == levelCreation.get(1).size()-1) levelCreation.removeFirst();
	    			}
	    		}

    		bottomloop:
	    		while(true)
	    		{
	    			for(int i = 0; i<levelCreation.size();i++)
	    			{
	    				if(!(levelCreation.get(i).get(levelCreation.get(0).size()-2).textureId == Constants.WALL))
	    				{
	    					break bottomloop;
	    				}
	    				if(i == levelCreation.size()-1)
	    				{
	    					for(int j = 0; j<levelCreation.size();j++)
	    					{
	    						levelCreation.get(j).removeLast();
	    					}
	    				}
	    			}
	    		}
    	// 1-1 corner right-top	
    	} else if(coordinates[0] == levelCreation.size()-2 && coordinates[1] == 1)
    	{
    		rightloop:
	    		while(true)
	    		{
	    			for(int i = 0; i<levelCreation.get(0).size();i++)
	    			{
	    				if(!(levelCreation.get(levelCreation.size()-2).get(i).textureId == Constants.WALL))
	    				{
	    					break rightloop;
	    				}
	    				if(i == levelCreation.get(0).size()-1) levelCreation.removeLast();
	    			}
	    		}

    		toploop:
	    		while(true)
	    		{
	    			for(int i = 0; i<levelCreation.size();i++)
	    			{
	    				if(!(levelCreation.get(i).get(1).textureId == Constants.WALL))
	    				{
	    					break toploop;
	    				}
	    				if(i == levelCreation.size()-1)
	    				{
	    					for(int j = 0; j<levelCreation.size();j++)
	    					{
	    						levelCreation.get(j).removeFirst();
	    					}
	    				}
	    			}
	    		}
    	// 1-1 corner right-bottom	
    	} else if(coordinates[0] == levelCreation.size()-2 && coordinates[1] == levelCreation.get(0).size()-2)
    	{
    		rightloop:
	    		while(true)
	    		{
		    		for(int i = 0; i<levelCreation.get(0).size();i++)
		    		{
		    			if(!(levelCreation.get(levelCreation.size()-2).get(i).textureId == Constants.WALL))
		    			{
		    				break rightloop;
		    			}
		    			if(i == levelCreation.get(0).size()-1) levelCreation.removeLast();
		    		}
	    		}
    		
    		bottomloop:
    			while(true)
    			{
		    		for(int i = 0; i<levelCreation.size();i++)
		    		{
		    			if(!(levelCreation.get(i).get(levelCreation.get(0).size()-2).textureId == Constants.WALL))
		    			{
		    				break bottomloop;
		    			}
		    			if(i == levelCreation.size()-1)
		    			{
		    	    		for(int j = 0; j<levelCreation.size();j++)
		    	    		{
		    	    			levelCreation.get(j).removeLast();
		    	    		}
		    			}
		    		}
	    		}
    	} else
    	{
    		return;
    	}
    	
		editTable.clear();
		updateLevel();
    }
    
    /**
     * Recreates the Table that stores the Level that is being created.
     * Afterwards it clears the stage and re-adds the mainTable
     */
    public void updateLevel()
    {
        for(int y = 0; y<levelCreation.get(0).size();y++)
        {
        	for(int x = 0; x<levelCreation.size();x++)
        	{
        		editTable.add(levelCreation.get(x).get(y));
        	}
        	editTable.row();
        }
        editTable.invalidateHierarchy();
        stage.clear();
        stage.addActor(mainTable);
    }
    
    /**
     * Checks if the created Level is solvable
     * @return true, if it is solvable
     */
    public boolean isSolvable()
    {
    	//TODO solver, is every floor reachable?
    	return true;
    }
    
    /**
     * Saves the created Level into a Json file
     */
    public void saveLevel()
    {
    	char[][] myLevel = new char[levelCreation.size()][levelCreation.get(0).size()];
        for(int y = 0; y<levelCreation.get(0).size();y++)
        {
        	for(int x = 0; x<levelCreation.size();x++)
        	{
        		myLevel[x][y] = levelCreation.get(x).get(y).textureId;
        	}
        }
    	// TODO save array into json file
    }
    
    /**
     * Calculates the Coordinates in the LinkedList Matrix of the given Tile
     * @param tile the tile whose coordinates should be calculated
     * @return int-Array, [0] = x; [1] = y; Returns -1,-1 if not found
     */
    public int[] calculateCoords(EditorTile tile)
    {
    	int[] coords = new int[2];

    	coords[1] = -1;
    	
    	outerloop:
	    	for(int x = 0; x <levelCreation.size(); x++)
	    	{
	    		for(int y = 0; y <levelCreation.get(x).size(); y++)
	    		{
	    			if(levelCreation.get(x).get(y).equals(tile))
	    			{
	    				coords[0] = x;
	    				coords[1] = y;
	    				break outerloop;
	    			}
	    		}
	    	}
    	return coords;
    }
    
    /**
     * The tiles that are used in the Level Editor(They have less functionalities than the ones used in the Levels).
     */
    public class EditorTile extends Actor
    {
        private char textureId;

        public EditorTile(char textureId)
        {
        	this.textureId = textureId;
        	setSize(Settings.getInt("TileSize"), Settings.getInt("TileSize"));

        	addListener(new ClickListener()
        	{
        		@Override
        		public void clicked(InputEvent event, float x, float y)
        		{
        			super.clicked(event, x, y);
        			switch(editStep){
        			case 1: // expand/shrink game area
        				if(EditorTile.this.textureId == Constants.WALL) expandGameGrid(EditorTile.this);
        				else if(EditorTile.this.textureId == Constants.FLOOR) shrinkGameGrid(EditorTile.this);
        				break;
        			case 2: // add goals
        				switch(EditorTile.this.textureId)
        				{
        				case Constants.FLOOR:
        					EditorTile.this.textureId = Constants.GOAL; goalCount++;
        					break;
        				case Constants.GOAL:
        					EditorTile.this.textureId = Constants.FLOOR; goalCount--;
        					break;
        				case Constants.BOX_FLOOR:
        					EditorTile.this.textureId = Constants.BOX_GOAL; goalCount++;
        					break;
        				case Constants.BOX_GOAL:
        					EditorTile.this.textureId = Constants.BOX_FLOOR; goalCount--;
        					break;
        				case Constants.PLAYER_FLOOR:
        					EditorTile.this.textureId = Constants.PLAYER_GOAL; goalCount++;
        					break;
        				case Constants.PLAYER_GOAL:
        					EditorTile.this.textureId = Constants.PLAYER_FLOOR; goalCount--;
        					break;
        				}
        				break;
        			case 3: // add boxes
        				switch(EditorTile.this.textureId)
        				{
        				case Constants.FLOOR:
        					EditorTile.this.textureId = Constants.BOX_FLOOR; boxCount++;
        					break;
        				case Constants.GOAL:
        					EditorTile.this.textureId = Constants.BOX_GOAL; boxCount++; boxOnGoalCount++;
        					break;
        				case Constants.BOX_FLOOR:
        					EditorTile.this.textureId = Constants.FLOOR; boxCount--;
        					break;
        				case Constants.BOX_GOAL:
        					EditorTile.this.textureId = Constants.GOAL; boxCount--; boxOnGoalCount--;
        					break;
        				}
        				break;
        			case 4: // add player
        				if(EditorTile.this.textureId == Constants.FLOOR || EditorTile.this.textureId == Constants.GOAL)
        				{
        					if(LevelEditor.this.playerAdded)
        					{
        						outerloop:
	        						for(int i = 0; i <levelCreation.size(); i++)
	        						{
	        							for(int j = 0; j <levelCreation.get(i).size(); j++)
	        							{
	        								if(levelCreation.get(i).get(j).textureId == Constants.PLAYER_FLOOR)
	        								{
	        									levelCreation.get(i).get(j).textureId = Constants.FLOOR;
	        									playerAdded = false;
	        									break outerloop;
	        								}
	        								else if(levelCreation.get(i).get(j).textureId == Constants.PLAYER_GOAL)
	        								{
	        									levelCreation.get(i).get(j).textureId = Constants.GOAL;
	        									playerAdded = false;
	        									break outerloop;
	        								}
	        							}
	        						}
        					}
        					// set the new Player Location
        					if(EditorTile.this.textureId == Constants.FLOOR) {EditorTile.this.textureId = Constants.PLAYER_FLOOR; playerAdded = true;}
        					else if(EditorTile.this.textureId == Constants.GOAL) {EditorTile.this.textureId = Constants.PLAYER_GOAL; playerAdded = true;}                 			
        				}
        				else if(EditorTile.this.textureId == Constants.PLAYER_FLOOR){EditorTile.this.textureId = Constants.FLOOR; playerAdded = false;}
        				else if(EditorTile.this.textureId == Constants.PLAYER_GOAL){EditorTile.this.textureId = Constants.GOAL; playerAdded = false;}
        			}

        		}

        	});
        }

        EditorTile getTile(){
        	return this;
        }

        public boolean isGoal()
        {
        	return textureId == GOAL || textureId == PLAYER_GOAL || textureId == BOX_GOAL;
        }
        public boolean isPlayer()
        {
        	return textureId == PLAYER_FLOOR || textureId == PLAYER_GOAL;
        }
        public boolean isBox()
        {
        	return textureId == BOX_FLOOR || textureId == BOX_GOAL;
        }
        public boolean isWall()
        {
        	return textureId == WALL;
        }

        public String texture()
        {
        	switch(textureId)
        	{
        	case FLOOR:
        		return FLOOR_D;
        	case GOAL:
        		return GOAL_D;
        	case PLAYER_FLOOR:
        		return PLAYER_FLOOR_D;
        	case PLAYER_GOAL:
        		return PLAYER_GOAL_D;
        	case BOX_FLOOR:
        		return BOX_FLOOR_D;
        	case BOX_GOAL:
        		return BOX_GOAL_D;
        	case WALL:
        		return WALL_D;
        	}
        	return "";
        }

        @Override
        public void draw(Batch batch, float parentAlpha)
        {
        	super.draw(batch, parentAlpha);
        	batch.draw(LevelEditor.this.skin.getRegion(texture()), getX(), getY(), getWidth(), getHeight());
        }
    }
}