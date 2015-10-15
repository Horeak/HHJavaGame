package Blocks;

import Blocks.Util.Block;
import Main.MainFile;
import Render.EnumRenderMode;
import Utils.ConfigValues;
import Utils.RenderUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BlockDirt extends Block {

	BufferedImage dirtImage;

	public BlockDirt(int x, int y){
		super(x,y);
	}
	public BlockDirt(){
		super();
	}

	@Override
	public String getBlockDisplayName() {
		return "Dirt Block";
	}

	@Override
	public void renderBlock(Graphics2D g2, int renderX, int renderY) {
//		if(dirtImage == null){
//			dirtImage = getDirtImage();
//		}
//
//		g2.drawImage(dirtImage, renderX, renderY, null);

		Color temp = g2.getColor();

		if(ConfigValues.renderMod == EnumRenderMode.render2_5D && MainFile.currentWorld != null) {
			RenderUtil.renderDefault2_5DBlock(g2, this, renderX, renderY, MainFile.currentWorld.getBlock(x, y - 1) == null, MainFile.currentWorld.getBlock(x + 1, y) == null);
		}

		g2.setColor(getDefaultBlockColor());
		RenderUtil.darkenColorBasedOnTime(g2);

		g2.fill(new Rectangle(renderX, renderY, ConfigValues.size, ConfigValues.size));

		g2.setColor(temp);
	}

	@Override
	public Color getDefaultBlockColor() {
		return new Color(107, 62, 33);
	}

	@Override
	public boolean isBlockSolid() {
		return true;
	}

//	public static BufferedImage getDirtImage(){
//			BufferedImage off_Image = new BufferedImage(100, 50,BufferedImage.TYPE_INT_ARGB);
//			Graphics2D temp_g2 = off_Image.createGraphics();
//
//			temp_g2.setColor(new Color(107, 62, 33));
//			temp_g2.fill(new Rectangle(0, 0, ConfigValues.size, ConfigValues.size));
//
//			for(int x = 0; x < ConfigValues.size; x += 2) {
//				for (int y = 0; y < ConfigValues.size; y += 2) {
//					if (MainFile.random.nextInt(3) == 1) {
//
//						temp_g2.setColor(new Color(102, 56, 34));
//						int size = MainFile.random.nextInt(3) + 1;
//
//						if (size + x > ConfigValues.size) {
//							size = (ConfigValues.size) - (size + x);
//						} else if (size + y > ConfigValues.size) {
//							size = (ConfigValues.size) - (size + y);
//						}
//
//						Rectangle tangle = new Rectangle(x, y, size, size);
//						temp_g2.fill(tangle);
//
//					}
//
//				}
//			}
//
//			return off_Image;
//	}
}
