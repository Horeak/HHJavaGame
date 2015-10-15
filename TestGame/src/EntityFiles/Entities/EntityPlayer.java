package EntityFiles.Entities;


import Blocks.Util.Block;
import EntityFiles.DamageSourceFiles.DamageBase;
import EntityFiles.DamageSourceFiles.DamageSource;
import EntityFiles.Entity;
import Main.MainFile;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;


public class EntityPlayer extends Entity {

	//TODO Add inventory
	//TODO Make hotbar connected to player/inventory
	//TODO Add proper player render
	//TODO Add entity physics to make player fall and not able to fly.
	//TODO Make player spawn above ground instead of at a fixed position under ground

	/**
	 *  1 = left
	 *  2 = right
	 */
	public int facing = 0;
	private int playerHealth = 100, playerMaxHealth = 100;

	public float motionY;
	public static float  motionIncreaseY = 0.8F;
	public static float motionMaxY = 6F;

	public boolean jump = true;

	public EntityPlayer(float x, float y) {
		super(x, y);
	}

	@Override
	public String getEntityDisplayName() {
		return "<ENTITYPLAYER>:INSERT_PLAYER_NAME_HERE";
	}

	@Override
	public int getEntityHealth() {
		return playerHealth;
	}

	@Override
	public boolean shouldDamage(DamageSource source) {
		return true;
	}

	@Override
	public void damageEntity(DamageSource source, DamageBase damage) {
		//TODO Take armor/defence into consideration when it is added.
		playerHealth -= damage.getDamageAmount();

	}

	@Override
	public void renderEntity(JFrame frame, Graphics2D g2, int renderX, int renderY) {
		g2.draw(new Rectangle(renderX - 4,renderY - 36, 21, 21));
		g2.draw(new Rectangle(renderX + 2, renderY - 15, 10, 26));
		g2.draw(new Rectangle(renderX - 6, renderY + 11, 25, 11));

		if(facing == 1){
			g2.draw(new Rectangle(renderX - 10, renderY - 28, 6, 6));

		}else if(facing == 2){
			g2.draw(new Rectangle(renderX + 17, renderY - 30, 6, 6));
		}
	}


	public Rectangle2D getPlayerBounds(){
		return new Rectangle2D.Double(Math.round(getEntityPostion().x), Math.round(getEntityPostion().y - 1), 1, 2);
	}

	public Block getBlockBelow(){
		return MainFile.currentWorld.getBlock(Math.round(getEntityPostion().x), Math.round(getEntityPostion().y) + 1);
	}

	public boolean canMoveTo(float x, float y){
		if (x >= 0 && x < MainFile.currentWorld.worldSize.xSize && y >= 0 && y < MainFile.currentWorld.worldSize.ySize) {

			//TODO Fix render issue when going x12 or less
			if(x >= 12 || getEntityPostion().x < 12)
				return MainFile.currentWorld.getBlock(Math.round(x), Math.round(y)) == null && MainFile.currentWorld.getBlock(Math.round(x), Math.round(y) - 1) == null;
		}
		return false;
	}

	public boolean moveTo(float x, float y){
		Block targetBlock = MainFile.currentWorld.getBlock(Math.round(x), Math.round(y));

		if(targetBlock != null && !targetBlock.blockBounds().contains(x, y) && targetBlock != null && !targetBlock.blockBounds().intersects(getPlayerBounds()) || targetBlock != null && targetBlock.canPassThrough() || targetBlock == null) {
			if (canMoveTo(x, y)) {
				setEntityPosition(x, y);
				return true;
			}
		}

		return false;
	}

	//TODO Improve movement. (Round up numbers for checking if the player can walk there?)
	public void updateEntity(){
		if(motionY > motionMaxY)
			motionY = motionMaxY;

		Block b = getBlockBelow();

		if(b == null){
				moveTo(getEntityPostion().x, getEntityPostion().y + motionY);

				if (motionY < motionMaxY) {
					motionY += motionIncreaseY;
				}

		}else if(b != null){
			motionY = 0;
			jump = true;

			moveTo(getEntityPostion().x, Math.round(getEntityPostion().y));
		}


	}
}
