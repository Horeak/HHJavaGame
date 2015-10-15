package Blocks;

import Blocks.Util.Block;
import Main.MainFile;
import Render.EnumRenderMode;
import Utils.ConfigValues;
import Utils.RenderUtil;

import java.awt.*;


public class BlockStone extends Block {

	//TODO Add noise

	public BlockStone(int x, int y){
		super(x,y);
	}
	public BlockStone(){
		super();
	}

	@Override
	public String getBlockDisplayName() {
		return "Stone Block";
	}

	@Override
	public void renderBlock(Graphics2D g2, int renderX, int renderY) {
		Color temp = g2.getColor();

		if (ConfigValues.renderMod == EnumRenderMode.render2_5D && MainFile.currentWorld != null) {
			RenderUtil.renderDefault2_5DBlock(g2, this, renderX, renderY, MainFile.currentWorld.getBlock(x, y - 1) == null, MainFile.currentWorld.getBlock(x + 1, y) == null);
		}

		g2.setColor(getDefaultBlockColor());
		RenderUtil.darkenColorBasedOnTime(g2);

		g2.fill(new Rectangle(renderX, renderY, ConfigValues.size, ConfigValues.size));

		g2.setColor(temp);

	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(151, 152, 151);
	}

	@Override
	public boolean isBlockSolid() {
		return true;
	}
}
