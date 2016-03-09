package Guis.Interfaces;

import BlockFiles.BlockDirt;
import BlockFiles.BlockGrass;
import BlockFiles.BlockRender.DefaultBlockRendering;
import BlockFiles.BlockStone;
import BlockFiles.Blocks;
import BlockFiles.Util.Block;
import Interface.UIMenu;
import Items.Utils.ItemStack;
import Main.MainFile;
import Render.EnumRenderMode;
import Render.Renders.BackgroundRender;
import Render.Renders.BlockRendering;
import Utils.ConfigValues;
import Utils.FileUtil;
import WorldFiles.Chunk;
import WorldFiles.EnumWorldTime;
import WorldFiles.World;
import WorldGeneration.TreeGeneration;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class AbstractMainMenu extends UIMenu {

	public static int renderStart = 290;
	public static int renderWidth = 190;
	public static World world;
	BackgroundRender render = new BackgroundRender();

	public boolean renderBar = true;
	public boolean renderOverlayBlur = true;
	
	public static int length = 64;

	public AbstractMainMenu() {
		initWorld();
	}

	public static void initWorld() {
		if (world == null) {
			world = new World("BackgroundWorld");
			world.setTimeOfDay(EnumWorldTime.DAY);
			world.WorldTime /= 2;
			world.loading = true;
			world.generateChunks = false;

			for(int x = 0; x < length; x++){
				for(int y = 0; y < ConfigValues.renderYSize - 25; y++){
					world.setBlock(Blocks.blockAir, x, y);
				}
			}

			for (int x = 0; x < length; x++) {
				world.setBlock(Blocks.blockGrass, x, (ConfigValues.renderYSize - 4));
			}
			for (int y = (ConfigValues.renderYSize - 3); y < ConfigValues.renderYSize; y++) {
				for (int x = 0; x < length; x++) {
					world.setBlock(Blocks.blockDirt, x, y);
				}
			}
			for (int x = 0; x < length; x++) {
				for(int y = (ConfigValues.renderYSize - 1); y < (ConfigValues.renderYSize); y++) {
					if (MainFile.random.nextInt(3) == 1) {
						world.setBlock(Blocks.blockStone, x, y);
					}else{
						world.setBlock(Blocks.blockDirt, x, y);
					}
				}
			}

			for(int y = (ConfigValues.renderYSize); y < 5; y++) {
				for (int x = 0; x < length; x++) {
					world.setBlock(Blocks.blockStone, x, y);
				}
			}

			for(int x = 0; x < length; x+= 5){
				if(MainFile.random.nextInt(5) == 0) {
					if(world.getChunk(x, ConfigValues.renderYSize - 5) != null) {
						Chunk ch = world.getChunk(x, ConfigValues.renderYSize - 5);
						ch.world = world;

						new TreeGeneration().generate(ch, x - (ch.chunkX * Chunk.chunkSize), (ConfigValues.renderYSize - 4) - (ch.chunkY * Chunk.chunkSize));
					}
				}
			}
		}
		world.updateLightForBlocks(false);
	}

	@Override
	public void render( Graphics g2 ) {
		initWorld();

		render.render(g2, world);

		if (world != null && world.worldChunks != null) {
			for(int i = (ConfigValues.renderMod == EnumRenderMode.render2D || ConfigValues.simpleBlockRender ? 2 : 0); i < 3; i++) {
				HashMap<Point, Block> bbb = new HashMap<>();

				for (int x = 0; x < (length); x++) {
					for (int y = 0; y < 25; y++) {
						Block b = world.getBlock(x, y);

						if (x > ((MainFile.game.gameContainer.getScreenWidth() / ConfigValues.size) + 1) || y > ((MainFile.game.gameContainer.getScreenHeight() / ConfigValues.size) + 1))
							continue;

						if (b != null && b.getRender() != null) {
							if (b.isBlockSolid()) {
								((DefaultBlockRendering) b.getRender()).renderBlock(g2, (BlockRendering.START_X_POS) + ((x - 1) * ConfigValues.size), (BlockRendering.START_Y_POS) + (y * ConfigValues.size), ConfigValues.renderMod, new ItemStack(b), world, x, y, i);
							} else {
								bbb.put(new Point(x, y), b);
							}
						}

					}
				}

				for (Map.Entry<Point, Block> ent : bbb.entrySet()) {
					Block b = ent.getValue();

					if (b != null && b.getRender() != null) {
						((DefaultBlockRendering) b.getRender()).renderBlock(g2, (BlockRendering.START_X_POS) + ((ent.getKey().x - 1) * ConfigValues.size), (BlockRendering.START_Y_POS) + (ent.getKey().y * ConfigValues.size), ConfigValues.renderMod, new ItemStack(b), world, ent.getKey().x, ent.getKey().y, i);
					}
				}


			}

		}

		if(renderOverlayBlur){
			g2.setColor(new Color(152, 152, 152, 96));
			g2.fill(MainFile.blockRenderBounds);
		}

		if(renderBar) {
			g2.setColor(org.newdawn.slick.Color.black);
			g2.drawLine(renderStart, BlockRendering.START_Y_POS, renderStart, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size));
			g2.drawLine(renderStart + renderWidth, BlockRendering.START_Y_POS, renderStart + renderWidth, (BlockRendering.START_Y_POS) + (ConfigValues.renderYSize * ConfigValues.size));

			g2.setColor(new Color(95, 95, 95, 112));
			g2.fill(new Rectangle(renderStart, BlockRendering.START_Y_POS, renderWidth, (ConfigValues.renderYSize * ConfigValues.size)));
		}
	}


	@Override
	public boolean canRender() {
		return true;
	}

}
