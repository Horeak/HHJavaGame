package EntityFiles.Entities;


import Blocks.TorchBlock;
import EntityFiles.DamageSourceFiles.DamageBase;
import EntityFiles.DamageSourceFiles.DamageSource;
import EntityFiles.Entity;
import Items.Inventory;
import Items.Item;
import Utils.RenderUtil;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;
import java.awt.geom.Rectangle2D;


public class EntityPlayer extends Entity implements Inventory {

	//TODO Add inventory
	//TODO Make hotbar connected to player/inventory
	//TODO Add proper player render
	//TODO Add entity physics to make player fall and not able to fly.
	//TODO Make player spawn above ground instead of at a fixed position under ground

	/**
	 * 1 = left
	 * 2 = right
	 */
	public int facing = 0;
	private int playerHealth = 100, playerMaxHealth = 100;
	//TODO Change items size when adding proper inventory
	private Item[] inventoryItems = new Item[ 10 ];

	public EntityPlayer( float x, float y ) {
		super(x, y);

		for (int i = 0; i < 10; i++)
			addItem(new TorchBlock());
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
	public boolean shouldDamage( DamageSource source ) {
		return true;
	}

	@Override
	public void damageEntity( DamageSource source, DamageBase damage ) {
		//TODO Take armor/defence into consideration when it is added.
		playerHealth -= damage.getDamageAmount();

	}

	@Override
	public void renderEntity( Graphics g2, int renderX, int renderY ) {
		g2.setColor(RenderUtil.getColorToSlick(new Color(141, 141, 141)));

		g2.draw(new Rectangle(renderX - 4, renderY - 58, 21, 21));
		g2.draw(new Rectangle(renderX + 2, renderY - 37, 10, 26));
		g2.draw(new Rectangle(renderX - 6, renderY - 11, 25, 11));

		if (facing == 1) {
			g2.draw(new Rectangle(renderX - 10, renderY - 50, 6, 6));

		} else if (facing == 2) {
			g2.draw(new Rectangle(renderX + 17, renderY - 50, 6, 6));
		}
	}

	public Rectangle2D getPlayerBounds() {
		return new Rectangle2D.Double(getEntityPostion().x, getEntityPostion().y - 1, 1, 2);
	}

	@Override
	public Rectangle2D getEntityBounds() {
		return getPlayerBounds();
	}

	@Override
	public Item[] getItems() {
		return inventoryItems;
	}

	@Override
	public Item getItem( int i ) {
		return i < inventoryItems.length ? inventoryItems[ i ] : null;
	}

	@Override
	public void setItem( int i, Item item ) {
		if (i < inventoryItems.length) {
			inventoryItems[ i ] = item;
		}
	}

	@Override
	public int getInvetorySize() {
		return inventoryItems.length;
	}

	@Override
	public String getInventoryName() {
		return "Player Inventory";
	}


	//TODO Improve both the consumeItem and the addItem as these are only rough temp versions
	public void consumeItem( Item item ) {
		for (int i = 0; i < getInvetorySize(); i++) {
			Item it = getItem(i);

			if (it != null) {
				if (it.getItemID().equals(item.getItemID())) {
					if (it.getItemStackSize() <= 1) {
						setItem(i, null);
						return;
					} else {
						it.decreaseStackSize(1);
						return;
					}
				}
			}
		}
	}

	//TODO Fix bug when adding a item that fills up a stack it also creates a new one before getting the next item
	public boolean addItem( Item item ) {
		int stack = item.getItemStackSize();

		boolean checkedCurrent = false;

		start:
		for (int g = 0; g < 2; g++)
			for (int i = 0; i < getInvetorySize(); i++) {
				Item it = getItem(i);

				if (checkedCurrent) {
					if (it == null && item != null) {
						setItem(i, item);
						return true;
					}
				} else {
					if (it != null && it.getItemID().equals(item.getItemID()) && it.getItemStackSize() < it.getItemMaxStackSize()) {
						int t = it.getItemMaxStackSize() - it.getItemStackSize();
						int tt = t - stack;

						if (tt > 0) {
							it.setStackSize(it.getItemMaxStackSize() - tt);
							return true;
						} else {
							it.setStackSize(it.getItemMaxStackSize() - tt);
							stack = -(it.getItemMaxStackSize() - tt);

							continue;
						}

					}
				}

				if (i == (getInvetorySize() - 1)) {
					checkedCurrent = true;
					continue start;
				}
			}

		return false;
	}
}
