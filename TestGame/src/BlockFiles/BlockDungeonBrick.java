package BlockFiles;

import BlockFiles.BlockRender.EnumBlockSide;
import BlockFiles.Util.Block;
import BlockFiles.Util.Material;
import Items.Utils.ItemStack;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.World;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class BlockDungeonBrick extends Block {

	public int id;

    public static String[] brickNames = new String[]{"Blue Dungeon Bricks", "Green Dungeon Bricks", "Red Dungeon Bricks", "Yellow Dungeon Bricks"};
	public static Image[] images = new Image[brickNames.length];

	public int getMaxBlockDamage() {
		return 10 * ((id + 2));
	}

	public BlockDungeonBrick(int id){
		this.id = id;
	}

	@Override
	public String getBlockDisplayName() {
		return brickNames[id];
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.black;
	}

	@Override
	public Image getBlockTextureFromSide( EnumBlockSide side, World world, int x, int y ) {
		return images[id];
	}

	@Override
	public void loadTextures(TextureLoader imageLoader) {
		for(int i = 0; i < images.length; i++){
			images[i] = imageLoader.getImage("blocks", "dungeonBrick" + i);
		}
	}

	public ConcurrentHashMap<Integer, ArrayList<String>> toolTips = new ConcurrentHashMap<>();
	public ArrayList<String> getTooltips( ItemStack stack)
	{
		if(!toolTips.containsKey(id)){
			ArrayList<String> t = new ArrayList<>();
			t.add("This seems to originate from somewhere...");

			toolTips.put(id, t);

			return toolTips.get(id);
		}


		return toolTips.get(id);
	}

	@Override
	public Material getBlockMaterial() {
		return Material.ROCK;
	}
}
