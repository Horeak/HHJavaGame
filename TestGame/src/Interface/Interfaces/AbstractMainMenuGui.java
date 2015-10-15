package Interface.Interfaces;

import Blocks.BlockDirt;
import Blocks.BlockGrass;
import Blocks.BlockStone;
import Blocks.Util.Block;
import Interface.Gui;
import Main.MainFile;
import Render.EnumRenderMode;
import Render.Renders.BlockRendering;
import Utils.ConfigValues;
import Utils.RenderUtil;
import WorldFiles.EnumWorldTime;

import javax.swing.*;
import java.awt.*;

public class AbstractMainMenuGui extends Gui {

	Block[][] backgroundRenderBlocks = new Block[ConfigValues.renderXSize][ConfigValues.renderYSize];

	public static int renderStart = 325;
	public static int renderWidth = 190;

	public AbstractMainMenuGui(){
		for(int x = 0; x < ConfigValues.renderXSize; x++){
			backgroundRenderBlocks[x][(ConfigValues.renderYSize - 6)] = new BlockGrass(x, (ConfigValues.renderYSize - 6));
		}
		for(int y = (ConfigValues.renderYSize -5); y < ConfigValues.renderYSize; y++) {
			for(int x = 0; x < ConfigValues.renderXSize; x++){
				backgroundRenderBlocks[x][y] = new BlockDirt(x, y);
			}
		}
		for(int x = 0; x < ConfigValues.renderXSize; x++){
			if(MainFile.random.nextInt(3) == 1)
				backgroundRenderBlocks[x][(ConfigValues.renderYSize - 2)] = new BlockStone(x, (ConfigValues.renderYSize - 2));
		}
		for(int x = 0; x < ConfigValues.renderXSize; x++){
			backgroundRenderBlocks[x][(ConfigValues.renderYSize - 1)] = new BlockStone(x, (ConfigValues.renderYSize - 1));
		}

	}

	@Override
	public void render(JFrame frame, Graphics2D g2) {
		Color c = EnumWorldTime.DAY.SkyColor;

		Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));

		g2.setPaint(new GradientPaint(rectangle.x, rectangle.y, c, (rectangle.x + rectangle.width), (rectangle.y + rectangle.height), c.brighter()));
		g2.fill(rectangle);

		for(Block[] bb : backgroundRenderBlocks){
			for(Block b : bb) {
				if (b != null) {

					if(ConfigValues.renderMod == EnumRenderMode.render2_5D && !ConfigValues.simpleBlockRender){
						RenderUtil.renderDefault2_5DBlock(g2, b, (BlockRendering.START_X_POS) + (b.x * ConfigValues.size), (BlockRendering.START_Y_POS) + (b.y * ConfigValues.size), b instanceof BlockGrass, false);
					}

					RenderUtil.renderBlock(g2, b, (BlockRendering.START_X_POS) + (b.x * ConfigValues.size), (BlockRendering.START_Y_POS) + (b.y * ConfigValues.size));

				}
			}
		}

	}

	@Override
	public boolean canRender(JFrame frame) {
		return true;
	}

}
