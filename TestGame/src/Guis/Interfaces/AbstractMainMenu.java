package Guis.Interfaces;

import BlockFiles.BlockRender.DefaultBlockRendering;
import BlockFiles.Blocks;
import BlockFiles.Util.Block;
import Interface.UIMenu;
import Items.Utils.ItemStack;
import Main.MainFile;
import Render.Renders.BackgroundRender;
import Utils.ConfigValues;
import WorldFiles.Chunk;
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

			world.shouldSave = false;
			world.shouldLoad = false;

			world.ingnoreLightingHeight = true;

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
		world.updateLightForBlocks();
	}

	@Override
	public void render( Graphics g2 ) {
		initWorld();

		render.render(g2, world);

		if (world != null && world.worldChunks != null) {
			for(int i = 0; i < 3; i++) {
				HashMap<Point, Block> bbb = new HashMap<>();

				for (int x = 0; x < (length); x++) {
					for (int y = 0; y < 25; y++) {
						Block b = world.getBlock(x, y);

						if (x > ((MainFile.game.gameContainer.getScreenWidth() / ConfigValues.size) + 1) || y > ((MainFile.game.gameContainer.getScreenHeight() / ConfigValues.size) + 1))
							continue;

						if (b != null && b.getRender() != null) {
							if (b.isBlockSolid()) {
								((DefaultBlockRendering) b.getRender()).renderBlock(g2, ((x - 1) * ConfigValues.size),(y * ConfigValues.size), new ItemStack(b), world, x, y, i);
							} else {
								bbb.put(new Point(x, y), b);
							}
						}

					}
				}

				for (Map.Entry<Point, Block> ent : bbb.entrySet()) {
					Block b = ent.getValue();

					if (b != null && b.getRender() != null) {
						((DefaultBlockRendering) b.getRender()).renderBlock(g2, ((ent.getKey().x - 1) * ConfigValues.size), (ent.getKey().y * ConfigValues.size), new ItemStack(b), world, ent.getKey().x, ent.getKey().y, i);
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
			g2.drawLine(renderStart, 0, renderStart, (ConfigValues.renderYSize * ConfigValues.size));
			g2.drawLine(renderStart + renderWidth,0, renderStart + renderWidth, (ConfigValues.renderYSize * ConfigValues.size));

			g2.setColor(new Color(95, 95, 95, 112));
			g2.fill(new Rectangle(renderStart, 0, renderWidth, (ConfigValues.renderYSize * ConfigValues.size)));
		}
	}


	@Override
	public boolean canRender() {
		return true;
	}

}
