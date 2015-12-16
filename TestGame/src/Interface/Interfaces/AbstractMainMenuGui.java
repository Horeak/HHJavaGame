package Interface.Interfaces;

import Blocks.BlockDirt;
import Blocks.BlockGrass;
import Blocks.BlockStone;
import Blocks.Util.Block;
import Interface.Gui;
import Main.MainFile;
import Render.Renders.BlockRendering;
import Utils.ConfigValues;
import WorldFiles.EnumWorldSize;
import WorldFiles.EnumWorldTime;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;


public class AbstractMainMenuGui extends Gui {

	public static int renderStart = 325;
	public static int renderWidth = 190;
	public static World world;

	public AbstractMainMenuGui() {
		initWorld();
	}

	public static void initWorld() {
		if (world == null) {
			world = new World("BackgroundWorld", EnumWorldSize.SMALL);

			for (int x = 0; x < ConfigValues.renderXSize; x++) {
				world.setBlock(new BlockGrass(x, (ConfigValues.renderYSize - 6)), x, (ConfigValues.renderYSize - 6));
			}
			for (int y = (ConfigValues.renderYSize - 5); y < ConfigValues.renderYSize; y++) {
				for (int x = 0; x < ConfigValues.renderXSize; x++) {
					world.setBlock(new BlockDirt(x, y), x, y);
				}
			}
			for (int x = 0; x < ConfigValues.renderXSize; x++) {
				if (MainFile.random.nextInt(3) == 1)
					world.setBlock(new BlockStone(x, (ConfigValues.renderYSize - 2)), x, (ConfigValues.renderYSize - 2));
			}
			for (int x = 0; x < ConfigValues.renderXSize; x++) {
				world.setBlock(new BlockStone(x, (ConfigValues.renderYSize - 1)), x, ConfigValues.renderYSize - 1);
			}

		}
	}

	@Override
	public void render( Graphics g2 ) {
		initWorld();

		Color c = EnumWorldTime.DAY.SkyColor;

		Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));
		GradientFill fill = new GradientFill(rectangle.getX(), rectangle.getY(), c, (rectangle.getX() + rectangle.getWidth()), (rectangle.getY() + rectangle.getHeight()), c.brighter());

		g2.fill(rectangle, fill);

		if (world != null && world.Blocks != null) {
			for (Block[] bb : world.Blocks) {
			for (Block b : bb) {
				if (b != null && b.getRender() != null) {
					b.getRender().renderItem(g2, (BlockRendering.START_X_POS) + (b.x * ConfigValues.size), (BlockRendering.START_Y_POS) + (b.y * ConfigValues.size), ConfigValues.renderMod, b);
				}
			}
		}
		}

	}

	@Override
	public boolean canRender() {
		return true;
	}

}
