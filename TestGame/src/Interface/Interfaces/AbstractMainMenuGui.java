package Interface.Interfaces;

import Blocks.BlockDirt;
import Blocks.BlockGrass;
import Blocks.BlockStone;
import Blocks.Util.Block;
import Interface.Menu;
import Main.MainFile;
import Render.Renders.BackgroundRender;
import Render.Renders.BlockRendering;
import Utils.ConfigValues;
import WorldFiles.EnumWorldSize;
import WorldFiles.EnumWorldTime;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;


public class AbstractMainMenuGui extends Menu {

	public static int renderStart = 290;
	public static int renderWidth = 190;
	public static World world;
	BackgroundRender render = new BackgroundRender();

	public AbstractMainMenuGui() {
		initWorld();
	}

	public static void initWorld() {
		if (world == null) {
			world = new World("BackgroundWorld", EnumWorldSize.SMALL);
			world.setTimeOfDay(EnumWorldTime.DAY);
			world.WorldTime /= 2;

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

		world.updateLightForBlocks();
	}

	@Override
	public void render( Graphics g2 ) {
		initWorld();

		render.render(g2, world);
		Rectangle rectangle = new Rectangle(BlockRendering.START_X_POS, BlockRendering.START_Y_POS, (ConfigValues.renderXSize * ConfigValues.size), (ConfigValues.renderYSize * ConfigValues.size));

		if (world != null && world.Blocks != null) {
			for (Block[] bb : world.Blocks) {
			for (Block b : bb) {
				if (b != null && b.getRender() != null) {
					b.getRender().renderItem(g2, (BlockRendering.START_X_POS) + (b.x * ConfigValues.size), (BlockRendering.START_Y_POS) + (b.y * ConfigValues.size), ConfigValues.renderMod, b);
				}
			}
		}
		}

		g2.setColor(org.newdawn.slick.Color.black);
		g2.drawLine(renderStart, BlockRendering.START_Y_POS, renderStart, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size));
		g2.drawLine(renderStart + renderWidth, BlockRendering.START_Y_POS, renderStart + renderWidth, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size));

		g2.setColor(new Color(152, 152, 152, 96));
		g2.fill(rectangle);

		g2.setColor(new Color(95, 95, 95, 112));
		g2.fill(new Rectangle(renderStart, BlockRendering.START_Y_POS, renderWidth, (ConfigValues.renderYSize * ConfigValues.size)));

	}

	@Override
	public boolean canRender() {
		return true;
	}

}
