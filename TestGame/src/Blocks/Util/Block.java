package Blocks.Util;
/*
* Project: Random Java Creations
* Package: Blocks
* Created: 26.07.2015
*/

import Blocks.BlockRender.DefaultBlockRendering;
import Blocks.BlockRender.EnumBlockSide;
import Items.Item;
import Items.Rendering.ItemRenderer;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import java.util.ArrayList;

public abstract class Block implements Item {
	public int x, y;
	public World world;

	public ArrayList<String> blockInfoList = new ArrayList<>();

	public Block( int x, int y ) {
		this.x = x;
		this.y = y;
		this.world = MainFile.currentWorld;
	}
	public Block() {
		this(0, 0);
	}

	public String getItemName() {
		return getBlockDisplayName();
	}

	public ItemRenderer getRender() {
		return DefaultBlockRendering.staticReference;
	}

	//Register image when block is initiated
	public Image getBlockTextureFromSide( EnumBlockSide side ) {
		return null;
	}

	public boolean useBlockTexture() {
		return true;
	}

	public abstract String getBlockDisplayName();
	public abstract Color getDefaultBlockColor();

	public float getMovementFriction() {
		return 1F;
	}

	public abstract boolean isBlockSolid();
	public boolean canPassThrough() {
		return isBlockSolid();
	}

	public void addInfo() {
	}

	public java.awt.Shape blockBounds() {
		return new java.awt.Rectangle(x, y, 1, 1);
	}
}
