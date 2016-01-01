package Blocks;

import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import Items.IItem;
import Utils.RenderUtil;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

public class BlockLeaves extends Block {
	public static Image texture;

	@Override
	public String getBlockDisplayName() {
		return "Leaves";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.green;
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side ) {
		if (texture == null) {
			texture = RenderUtil.getBlockImage("leaves");
		}

		return texture;
	}

	//TODO Add saplings
	@Override
	public IItem getItemDropped() {
		return null;
	}

	public boolean isBlockSolid() {
		return false;
	}

	public boolean canPassThrough() {
		return false;
	}

	public boolean canStay() {
		for (int x = -3; x <= 3; x++) {
			for (int y = -3; y <= 3; y++) {
				int xPos = x + this.x;
				int yPos = y + this.y;

				if (world.getBlock(xPos, yPos) instanceof BlockWood) {
					return true;
				}
			}
		}

		return false;
	}

	public int getMaxBlockDamage() {
		return 2;
	}

	@Override
	public void updateBlock( World world, int fromX, int fromY ) {
		super.updateBlock(world, fromX, fromY);

		if (!canStay()) {
			world.setBlock(null, x, y);
		}
	}
}
