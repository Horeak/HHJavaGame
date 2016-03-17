package BlockFiles;


import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import BlockFiles.Util.ITickBlock;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.DataHandler;
import WorldFiles.World;
import javafx.scene.shape.DrawMode;
import org.json.simple.JSONObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;


public class BlockGrass extends Block {
	public static Image topTexture =  null;
	public static Image sideTexture =  null;

	public static HashMap<String, Image> sideImages = new HashMap<>();
	public static HashMap<String, Image> topImages = new HashMap<>();

	//TODO Will be used to for adding different grass types like for eksample snow
	public static String[] textureNames = new String[]{"Normal"};

	public static String getBlockType(World world, int x, int y){
		//TODO
		return textureNames[0];
	}

	public static boolean canGrassGrow( World world, int x, int y ) {
		Block block = world.getBlock(x, y);
		Block temp = world.getBlock(x, y - 1);

		if(block == null){
			try {
				throw new Exception("Attampted to check null block for canGrassGrow!");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		boolean light = block.getLightValue(world, x, y) >= (3 * world.worldTimeOfDay.lightMultiplier);
		boolean above = temp == null || temp != null && !temp.isBlockSolid();

		return light && above;
	}

	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return side == EnumBlockSide.SIDE || side == EnumBlockSide.FRONT ? sideImages.get(getBlockType(world, x, y)) : topImages.get(getBlockType(world, x, y));
	}

	@Override
	public void loadTextures() {
		for(String t : textureNames){
			topImages.put(t, MainFile.game.imageLoader.getImage("blocks","grassTop_" + t));
			sideImages.put(t, MainFile.game.imageLoader.getImage("blocks","grassSide_" + t));
		}
	}

	@Override
	public String getBlockDisplayName() {
		return "Grass";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.green.darker().darker();
	}

	public ItemStack getItemDropped(World world, int x, int y) {
		return new ItemStack(Blocks.blockDirt);
	}

	public int getMaxBlockDamage() {
		return 5;
	}

	public ITickBlock getTickBlock(){
		return new grassTickBlock();
	}


	class grassTickBlock implements ITickBlock{
		@Override
		public boolean shouldUpdate( World world, int x, int y) {
			return true;
		}


		public int time = 0;
		@Override
		public int getTimeSinceUpdate(World world, int x, int y) {
			return time;
		}

		@Override
		public void setTimeSinceUpdate(World world, int x, int y, int i ) {
			time = i;
		}

		@Override
		public void tickBlock( World world, int xx, int yy) {
			if(world.getBlock(xx, yy) == null) return;

			if (!canGrassGrow(world, xx, yy)) {
				world.setBlock(Blocks.blockDirt, xx, yy);
			}

			if (canGrassGrow(world, xx, yy)) {
				if (MainFile.random.nextInt(32) == 1) {
					int tempX = 0, tempY = 0;
					for (int x = xx - 1; x < xx + 2; x++) {
						for (int y = yy - 1; y < yy + 2; y++) {
							Block block = world.getBlock(x, y);

							if (tempX == 1 && tempY == 0 || tempX == 1 && tempY == 2) continue;


							if (block != null) {
								if (block instanceof BlockDirt) {
									if (canGrassGrow(world, x, y)) {
										if (MainFile.random.nextInt(10) == 3) {
											world.setBlock(Blocks.blockGrass, x, y);
										}
									}
								}
							}

							tempY += 1;
						}
						tempY = 0;
						tempX += 1;
					}
				}
			}
		}

		public int blockUpdateDelay() {
			return 5;
		}
	}
}
