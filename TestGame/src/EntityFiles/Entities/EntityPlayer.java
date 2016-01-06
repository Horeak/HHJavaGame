package EntityFiles.Entities;


import EntityFiles.DamageSourceFiles.DamageBase;
import EntityFiles.DamageSourceFiles.DamageSource;
import EntityFiles.Entity;
import Items.IInventory;
import Items.IItem;
import Utils.RenderUtil;
import org.newdawn.slick.Graphics;

import java.awt.geom.Rectangle2D;


public class EntityPlayer extends Entity implements IInventory {

	//TODO Add proper player render

	static org.newdawn.slick.Image playerRight = RenderUtil.getImage("textures", "player");
	/**
	 * 1 = left
	 * 2 = right
	 */
	public int facing = 0;
	public IItem[] inventoryItems = new IItem[ 50 ];
	private int playerHealth = 100, playerMaxHealth = 100;

	public EntityPlayer( float x, float y ) {
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
		if (facing == 1) {
			playerRight.getFlippedCopy(true, false).draw(renderX, renderY - 64, 32, 64);

		} else {
			playerRight.draw(renderX, renderY - 64, 32, 64);
		}

	}

	public Rectangle2D getPlayerBounds() {
		return new Rectangle2D.Double((int) getEntityPostion().x, (int) getEntityPostion().y - 1, 1, 2);
	}

	@Override
	public Rectangle2D getEntityBounds() {
		return getPlayerBounds();
	}

	@Override
	public IItem[] getItems() {
		return inventoryItems;
	}

	@Override
	public IItem getItem( int i ) {
		return i < inventoryItems.length ? inventoryItems[ i ] : null;
	}

	@Override
	public void setItem( int i, IItem item ) {
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


	public void consumeItem( IItem item ) {
		int size = item.getItemStackSize();

		for (int i = 0; i < getInvetorySize(); i++) {
			IItem it = getItem(i);
			if (it != null && item != null) {

				if (it.equals(item) && it.getItemStackSize() >= size) {
					getItem(i).decreaseStackSize(item.getItemStackSize());

					if (getItem(i).getItemStackSize() <= 0)
						setItem(i, null);

					return;

				} else if (it.equals(item) && it.getItemStackSize() < size) {
					size -= it.getItemStackSize();
					setItem(i, null);
				}
			}
		}
	}


	//TODO Make sure fix worked. (Items were added twice when adding an item would make a full stack)
	public boolean addItem( IItem item ) {
		if(item == null)
			return false;

		int stack = item.getItemStackSize();

		boolean checkedCurrent = false;

		start:
		for (int g = 0; g < 2; g++)
			for (int i = 0; i < getInvetorySize(); i++) {
				IItem it = getItem(i);

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

							if (stack <= 0) {
								return true;
							}

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
