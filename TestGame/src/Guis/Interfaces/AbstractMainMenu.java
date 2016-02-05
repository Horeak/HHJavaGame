package Guis.Interfaces;

import Blocks.BlockDirt;
import Blocks.BlockGrass;
import Blocks.BlockRender.DefaultBlockRendering;
import Blocks.BlockStone;
import Blocks.Util.Block;
import Interface.UIMenu;
import Items.Utils.ItemStack;
import Main.MainFile;
import Render.Renders.BackgroundRender;
import Render.Renders.BlockRendering;
import Utils.ConfigValues;
import WorldFiles.EnumWorldSize;
import WorldFiles.EnumWorldTime;
import WorldFiles.World;
import WorldGeneration.TreeGeneration;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class AbstractMainMenu extends UIMenu {

	public static int renderStart = 290;
	public static int renderWidth = 190;
	public static World world;
	BackgroundRender render = new BackgroundRender();

	public AbstractMainMenu() {
		initWorld();
	}

	public static void initWorld() {
		if (world == null) {
			world = new World("BackgroundWorld", EnumWorldSize.SMALL);
			world.setTimeOfDay(EnumWorldTime.DAY);
			world.WorldTime /= 2;

			for (int x = 0; x < world.worldSize.xSize; x++) {
				world.setBlock(new BlockGrass(), x, (ConfigValues.renderYSize - 6));
			}
			for (int y = (ConfigValues.renderYSize - 5); y < ConfigValues.renderYSize; y++) {
				for (int x = 0; x < world.worldSize.xSize; x++) {
					world.setBlock(new BlockDirt(), x, y);
				}
			}
			for (int x = 0; x < world.worldSize.xSize; x++) {
				for(int y = (ConfigValues.renderYSize - 3); y < (ConfigValues.renderYSize); y++) {
					if (MainFile.random.nextInt(3) == 1) {
						world.setBlock(new BlockStone(), x, y);
					}else{
						world.setBlock(new BlockDirt(), x, y);
					}
				}
			}

			for(int y = (ConfigValues.renderYSize); y < world.worldSize.ySize; y++) {
				for (int x = 0; x < world.worldSize.xSize; x++) {
					world.setBlock(new BlockStone(), x, y);
				}
			}

			for(int x = 0; x < world.worldSize.xSize; x+= 5){
				if(MainFile.random.nextInt(5) == 0)
				new TreeGeneration().generate(world, x, ConfigValues.renderYSize - 6);
			}
		}
		world.updateLightForBlocks(false);
	}

	@Override
	public void render( Graphics g2 ) {
		initWorld();

		render.render(g2, world);

		if (world != null && world.Blocks != null) {

			HashMap<Point, Block> bbb = new HashMap<>();

			for(int x = 0; x < (world.worldSize.xSize); x++){
				for(int y = 0; y < world.worldSize.ySize; y++){
					Block b = world.getBlock(x, y);

					if(x > ((MainFile.game.gameContainer.getScreenWidth() / ConfigValues.size) + 1) || y > ((MainFile.game.gameContainer.getScreenHeight() / ConfigValues.size) + 1)) continue;

					if (b != null && b.getRender() != null) {
						if (b.isBlockSolid()) {
							((DefaultBlockRendering)b.getRender()).renderBlock(g2, (BlockRendering.START_X_POS) + ((x-1) * ConfigValues.size), (BlockRendering.START_Y_POS) + (y * ConfigValues.size), ConfigValues.renderMod, new ItemStack(b), world, x, y);
						} else {
							bbb.put(new Point(x, y), b);
						}
					}

			}
		}

			for(Map.Entry<Point, Block> ent : bbb.entrySet()){
				Block b = ent.getValue();

				if (b != null && b.getRender() != null) {
					((DefaultBlockRendering)b.getRender()).renderBlock(g2, (BlockRendering.START_X_POS) + ((ent.getKey().x - 1) * ConfigValues.size), (BlockRendering.START_Y_POS) + (ent.getKey().y * ConfigValues.size), ConfigValues.renderMod, new ItemStack(b), world, ent.getKey().x, ent.getKey().y);
				}
			}



		}

		g2.setColor(org.newdawn.slick.Color.black);
		g2.drawLine(renderStart, BlockRendering.START_Y_POS, renderStart, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size));
		g2.drawLine(renderStart + renderWidth, BlockRendering.START_Y_POS, renderStart + renderWidth, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size));

		g2.setColor(new Color(152, 152, 152, 96));
		g2.fill(MainFile.blockRenderBounds);

		g2.setColor(new Color(95, 95, 95, 112));
		g2.fill(new Rectangle(renderStart, BlockRendering.START_Y_POS, renderWidth, (ConfigValues.renderYSize * ConfigValues.size)));

	}


	@Override
	public boolean canRender() {
		return true;
	}

}