package Blocks.Util;
/*
* Project: Random Java Creations
* Package: Blocks
* Created: 26.07.2015
*/

import java.awt.*;
import java.util.ArrayList;

public abstract class Block
{
	public int x, y;

	public Block(int x, int y){
		this.x = x;
		this.y = y;
	}

	public Block(){
		this(0,0);
	}

	public abstract String getBlockDisplayName();
	public abstract void renderBlock(Graphics2D g2, int renderX, int renderY);
	public abstract Color getDefaultBlockColor();

	public abstract boolean isBlockSolid();
	public boolean canPassThrough(){return isBlockSolid();}

	public ArrayList<String> blockInfoList = new ArrayList<>();
	public void addInfo(){}

	public Shape blockBounds(){
		return new Rectangle(x, y, 1, 1);
	}

	public float movementFriction(){
		return 1F;
	}
}
