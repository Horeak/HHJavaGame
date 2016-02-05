package Render.Renders;

import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import Blocks.Util.ILightSource;
import EntityFiles.Entities.EntityPlayer;
import Main.MainFile;
import Rendering.AbstractWindowRender;
import Utils.BlockUtils;
import Utils.ConfigValues;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class MinimapRender extends AbstractWindowRender {

	public static boolean[][] blockDiscoved;

	public int Width;
	public int Height;

	public float StartX;
	public float StartY;

	@Override
	public void render( Graphics g2 ) {
		if(blockDiscoved == null){
			blockDiscoved = new boolean[MainFile.game.getServer().getWorld().worldSize.xSize][MainFile.game.getServer().getWorld().worldSize.ySize];
		}

		int distance = 8;
		for(int x = -distance; x < distance;  x++) {
			for (int y = -distance; y < distance; y++) {
				int xPos = ((int)MainFile.game.getClient().getPlayer().getEntityPostion().x + x);
				int yPos = ((int)MainFile.game.getClient().getPlayer().getEntityPostion().y + y);

				if(xPos < MainFile.game.getServer().getWorld().worldSize.xSize && xPos >= 0 && yPos < MainFile.game.getServer().getWorld().worldSize.ySize && yPos >= 0){
					if(MainFile.game.getClient().getPlayer().getEntityPostion().distance(xPos, yPos) < distance) {
						blockDiscoved[ xPos ][ yPos ] = true;
					}
				}
			}
		}


		Width = get();
		Height = get();
		StartX = MainFile.xWindowSize - Width - 25;
		StartY = 25;

		Rectangle rectangle = new Rectangle(StartX, StartY, Width, Height);

		g2.setColor(MainFile.game.getServer().getWorld().worldTimeOfDay.SkyColor);
		g2.fill(rectangle);

		g2.setColor(Color.yellow);
		g2.draw(rectangle);

		//This rectangle need StarY + 1 on windows but not on mac? wierd...
		rectangle = new Rectangle(StartX, StartY + 1, Width - 1, Height - 1);
		g2.setClip(rectangle);

		int size = 8;

		Width /= size;
		Height /= size;

		int xx = Math.round(MainFile.game.getClient().getPlayer().getEntityPostion().x) - (Width / 2);
		int yy = Math.round(MainFile.game.getClient().getPlayer().getEntityPostion().y) - (Height / 2);

		for(int x = -(Width); x < (Width);  x++){
			for(int y = -(Height); y < (Height); y++){
				int xPos = (xx + x);
				int yPos = (yy + y);

				Block block = MainFile.game.getServer().getWorld().getBlock(xPos, yPos);
				float xStart = StartX + ((x) * size), yStart = StartY + ((y) * size);

				if(block != null && blockDiscoved[xPos][yPos]){
					if(ConfigValues.simpleBlockRender){
						BlockUtils.renderDefaultBlockDebug(g2, block, (int)xStart, (int)yStart, size, size);
					}else{
						block.getBlockTextureFromSide(EnumBlockSide.FRONT, MainFile.game.getServer().getWorld(), xPos, yPos).draw(xStart, yStart, size, size);
					}

					float t = (float)block.getLightValue(MainFile.game.getServer().getWorld(), xPos, yPos) / (float) ILightSource.MAX_LIGHT_STRENGTH;

					Color temp = block.getLightUnit().getLightColor();
					Color c = new Color(0, 0, 0, 1F - t);
					g2.setColor(c);

					g2.fill(new Rectangle(xStart, yStart, size, size));

					if(temp != ILightSource.DEFAULT_LIGHT_COLOR){
						g2.setColor(new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), t));
						g2.fill(new Rectangle(xStart, yStart, size, size));
					}

				}else{
					boolean t = xPos < MainFile.game.getServer().getWorld().worldSize.xSize && xPos >= 0 && yPos < MainFile.game.getServer().getWorld().worldSize.ySize && yPos >= 0;
					if(t && !blockDiscoved[xPos][yPos] || !t){
						g2.setColor(Color.darkGray);
						g2.fill(new Rectangle(xStart, yStart, size, size));
					}
				}
			}
		}

		EntityPlayer.playerTexutre.getFlippedCopy(MainFile.game.getClient().getPlayer().facing == 1, false).draw(StartX + ((float)get() / 2F) - 4F, StartY + ((float)get() / 2F) - 12F, 8, 16);
	}

	@Override
	public boolean canRender() {
		return MainFile.game.getServer().getWorld() != null && ConfigValues.RENDER_MINIMAP && !MainFile.game.getServer().getWorld().generating;
	}

	@Override
	public boolean canRenderWithWindow() {
		return false;
	}

	public int get(){
		return ConfigValues.MinimapSize == "Large" ? 256 + 8 : ConfigValues.MinimapSize == "Normal" ? 128 + 8: 64 + 8;
	}

	public static void reset(){
		if(MainFile.game.getServer().getWorld() != null)
		blockDiscoved = new boolean[MainFile.game.getServer().getWorld().worldSize.xSize][MainFile.game.getServer().getWorld().worldSize.ySize];
	}

	public static boolean discoved(int x, int y){
		boolean t = x < MainFile.game.getServer().getWorld().worldSize.xSize && x >= 0 && y < MainFile.game.getServer().getWorld().worldSize.ySize && y >= 0;

		return t && blockDiscoved[x][y];
	}
}
