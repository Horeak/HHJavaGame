package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import BlockFiles.Util.ITickBlock;
import Items.Utils.ItemStack;
import Main.MainFile;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import java.awt.*;
import java.util.HashMap;

public class BlockFurnace extends Block implements ITickBlock {

	//TODO Must find a way to be able to store data in blocks
	public static Image furnaceFrontOff, furnaceFrontOn, furnaceSide, furnaceTop;
	public static HashMap<World, HashMap<Point, ItemStack[]>> furnaceData = new HashMap<>();

	public void setInputStack(World world, int x, int y, ItemStack stack){
		if(!furnaceData.containsKey(world)) furnaceData.put(world, new HashMap<Point, ItemStack[]>());
		if(!furnaceData.get(world).containsKey(new Point(x, y))) furnaceData.get(world).put(new Point(x, y), new ItemStack[2]);

		furnaceData.get(world).get(new Point(x, y))[0] = stack;
	}

	public void setOutputStack(World world, int x, int y, ItemStack stack){
		if(!furnaceData.containsKey(world)) furnaceData.put(world, new HashMap<Point, ItemStack[]>());
		if(!furnaceData.get(world).containsKey(new Point(x, y))) furnaceData.get(world).put(new Point(x, y), new ItemStack[2]);

		furnaceData.get(world).get(new Point(x, y))[1] = stack;
	}


	public ItemStack getInputStack(World world, int x, int y){
		if(!furnaceData.containsKey(world)) furnaceData.put(world, new HashMap<Point, ItemStack[]>());
		if(!furnaceData.get(world).containsKey(new Point(x, y))) furnaceData.get(world).put(new Point(x, y), new ItemStack[2]);

		return furnaceData.get(world).get(new Point(x, y))[0];
	}

	public ItemStack getOutputStack(World world, int x, int y){
		if(!furnaceData.containsKey(world)) furnaceData.put(world, new HashMap<Point, ItemStack[]>());
		if(!furnaceData.get(world).containsKey(new Point(x, y))) furnaceData.get(world).put(new Point(x, y), new ItemStack[2]);

		return furnaceData.get(world).get(new Point(x, y))[1];
	}

	@Override
	public String getBlockDisplayName() {
		return "Furnace";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.darkGray;
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return side == EnumBlockSide.FRONT ? (getInputStack(world, x, y) != null) ? furnaceFrontOn : furnaceFrontOff : side == EnumBlockSide.SIDE ? furnaceSide : furnaceTop;
	}

	@Override
	public void loadTextures() {
		furnaceFrontOff = MainFile.game.imageLoader.getImage("blocks", "furnaceFrontOff");
		furnaceFrontOn = MainFile.game.imageLoader.getImage("blocks", "furnaceFrontOn");

		furnaceSide = MainFile.game.imageLoader.getImage("blocks", "furnaceSide");
		furnaceTop = MainFile.game.imageLoader.getImage("blocks", "furnaceTop");
	}

	public void updateBlock( World world, int fromX, int fromY, int curX, int curY ) {
	}

	public boolean blockClicked(World world, int x, int y, ItemStack stack){
		if(stack != null && getInputStack(world, x, y) == null){
			setInputStack(world, x, y, stack);
			return true;
		}else if(stack == null && getInputStack(world, x, y) != null){
			setInputStack(world, x, y, null);
			return true;
		}

		return false;
	}


	@Override
	public boolean shouldupdate( World world, int x, int y ) {
		return false;
	}

	@Override
	public int getTimeSinceUpdate() {
		return 0;
	}

	@Override
	public void setTimeSinceUpdate( int i ) {

	}

	@Override
	public void updateBlock( World world, int x, int y ) {

	}
}
