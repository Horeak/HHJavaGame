package Render.Renders;

import Blocks.BlockRender.EnumBlockSide;
import Blocks.Util.Block;
import Blocks.Util.ILightSource;
import EntityFiles.Entities.EntityPlayer;
import Main.MainFile;
import Render.AbstractWindowRender;
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
			blockDiscoved = new boolean[MainFile.getServer().getWorld().worldSize.xSize][MainFile.getServer().getWorld().worldSize.ySize];
		}

		int distance = 8;
		for(int x = -distance; x < distance;  x++) {
			for (int y = -distance; y < distance; y++) {
				int xPos = ((int)MainFile.getClient().getPlayer().getEntityPostion().x + x);
				int yPos = ((int)MainFile.getClient().getPlayer().getEntityPostion().y + y);

				if(xPos < MainFile.getServer().getWorld().worldSize.xSize && xPos >= 0 && yPos < MainFile.getServer().getWorld().worldSize.ySize && yPos >= 0){
					if(MainFile.getClient().getPlayer().getEntityPostion().distance(xPos, yPos) < distance) {
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

		g2.setColor(MainFile.getServer().getWorld().worldTimeOfDay.SkyColor);
		g2.fill(rectangle);

		g2.setColor(Color.yellow);
		g2.draw(rectangle);

		rectangle = new Rectangle(StartX, StartY, Width - 1, Height - 1);
		g2.setClip(rectangle);

		int size = 8;

		Width /= size;
		Height /= size;

		int xx = (int)MainFile.getClient().getPlayer().getEntityPostion().x - (Width / 2);
		int yy = (int)MainFile.getClient().getPlayer().getEntityPostion().y - (Height / 2);

		for(int x = -(Width); x < (Width);  x++){
			for(int y = -(Height); y < (Height); y++){
				int xPos = (xx + x);
				int yPos = (yy + y);

				Block block = MainFile.getServer().getWorld().getBlock(xPos, yPos);
				float xStart = StartX + ((x) * size), yStart = StartY + ((y) * size);

				if(block != null && blockDiscoved[xPos][yPos]){
					block.getBlockTextureFromSide(EnumBlockSide.FRONT, MainFile.getServer().getWorld(), xPos, yPos).draw(xStart, yStart, size, size);

					float t = (float)block.getLightValue(MainFile.getServer().getWorld(), xPos, yPos) / (float) ILightSource.MAX_LIGHT_STRENGTH;

					Color temp = block.getLightUnit().getLightColor();
					Color c = new Color(0, 0, 0, 1F - t);
					g2.setColor(c);

					g2.fill(new Rectangle(xStart, yStart, size, size));

					if(temp != ILightSource.DEFAULT_LIGHT_COLOR){
						g2.setColor(new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), t));
						g2.fill(new Rectangle(xStart, yStart, size, size));
					}

				}else{
					boolean t = xPos < MainFile.getServer().getWorld().worldSize.xSize && xPos >= 0 && yPos < MainFile.getServer().getWorld().worldSize.ySize && yPos >= 0;
					if(t && !blockDiscoved[xPos][yPos] || !t){
						g2.setColor(Color.darkGray);
						g2.fill(new Rectangle(xStart, yStart, size, size));
					}
				}
			}
		}

		EntityPlayer.playerTexutre.getFlippedCopy(MainFile.getClient().getPlayer().facing == 1, false).draw(StartX + ((float)get() / 2F) - 4F, StartY + ((float)get() / 2F) - 12F, 8, 16);
	}

	@Override
	public boolean canRender() {
		return MainFile.getServer().getWorld() != null && ConfigValues.RENDER_MINIMAP;
	}

	@Override
	public boolean canRenderWithWindow() {
		return false;
	}

	public int get(){
		return ConfigValues.MinimapSize == "Large" ? 256 + 8 : ConfigValues.MinimapSize == "Normal" ? 128 + 8: 64 + 8;
	}

	public static void reset(){
		blockDiscoved = new boolean[MainFile.getServer().getWorld().worldSize.xSize][MainFile.getServer().getWorld().worldSize.ySize];
	}

	public static boolean discoved(int x, int y){
		boolean t = x < MainFile.getServer().getWorld().worldSize.xSize && x >= 0 && y < MainFile.getServer().getWorld().worldSize.ySize && y >= 0;

		return t && blockDiscoved[x][y];
	}
}
