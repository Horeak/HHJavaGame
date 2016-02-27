package EntityFiles.Entities;


import EntityFiles.DamageSourceFiles.DamageBase;
import EntityFiles.DamageSourceFiles.DamageSource;
import EntityFiles.Entity;
import EntityFiles.EntityItem;
import Items.Utils.IArmor;
import Items.Utils.IInventory;
import Items.Utils.ItemStack;
import Main.MainFile;
import Render.Renders.HotbarRender;
import org.newdawn.slick.Graphics;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;


public class EntityPlayer extends Entity implements IInventory {

	//TODO Add proper player render
	public static org.newdawn.slick.Image playerTexutre =  null;
	/**
	 * 1 = left
	 * 2 = right
	 */
	public int facing = 0;
	public static int INV_SIZE = 50;

	public HashMap<Integer, ItemStack> inventoryItems = new HashMap<>();
	public IArmor[] armorInventory = new IArmor[4];

	private int playerHealth = 100, playerMaxHealth = 100;
	public String name;

	public EntityPlayer( float x, float y, String name ) {
		super(x, y);
		this.name = name;
	}

	@Override
	public String getEntityDisplayName() {
		return name;
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
			playerTexutre.getFlippedCopy(true, false).draw(renderX, renderY - 64, 32, 64);

		} else {
			playerTexutre.draw(renderX, renderY - 64, 32, 64);
		}

	}

	@Override
	public void loadTextures() {
		playerTexutre =  MainFile.game.imageLoader.getImage("textures", "player");
	}

	public Rectangle2D getPlayerBounds() {
		return new Rectangle2D.Double((int) getEntityPostion().x, (int) getEntityPostion().y - 1, 1, 2);
	}

	@Override
	public Rectangle2D getEntityBounds() {
		return getPlayerBounds();
	}


	public void dropItem(){
		float distance = 1.5F;

		if(getItem(HotbarRender.slotSelected - 1) != null){
			ItemStack stack = getItem(HotbarRender.slotSelected - 1);
			EntityItem entityItem = new EntityItem(getEntityPostion().x + (facing == 1 ? -distance : distance), getEntityPostion().y - (distance / 8), stack);

			if(entityItem.canMoveTo(entityItem.getEntityPostion().x, entityItem.getEntityPostion().y)) {
				entityItem.delay = 0;
				if (entityItem != null) {
					setItem(HotbarRender.slotSelected - 1, null);
					MainFile.game.getServer().getWorld().Entities.add(entityItem);
				}
			}
		}
	}

	@Override
	public ItemStack[] getItems() {
		return inventoryItems.values().toArray(new ItemStack[inventoryItems.values().size()]);
	}

	@Override
	public ItemStack getItem( int i ) {
		return i < INV_SIZE ? inventoryItems.get(i) : null;
	}

	@Override
	public void setItem( int i, ItemStack item ) {
		if (i < INV_SIZE) {
			inventoryItems.put(i, item);
		}
	}

	@Override
	public int getInvetorySize() {
		return INV_SIZE;
	}

	@Override
	public String getInventoryName() {
		return "Player Inventory";
	}


	public void consumeItem( ItemStack item ) {
		int size = item.getStackSize();

		for (int i = 0; i < getInvetorySize(); i++) {
			ItemStack it = getItem(i);
			if (it != null && item != null) {

				if (it.equals(item) && it.getStackSize() >= size) {
					getItem(i).decreaseStackSize(item.getStackSize());

					if (getItem(i).getStackSize() <= 0)
						setItem(i, null);

					return;

				} else if (it.equals(item) && it.getStackSize() < size) {
					size -= it.getStackSize();
					setItem(i, null);
				}
			}
		}
	}


	public boolean addItem( ItemStack item ) {
		if(item == null)
			return false;

		int stack = item.getStackSize();

		boolean checkedCurrent = false;

		start:
		for (int g = 0; g < 2; g++)
			for (int i = 0; i < getInvetorySize(); i++) {
				ItemStack it = getItem(i);

				if (checkedCurrent) {
					if (it == null && item != null) {
						setItem(i, item);
						return true;
					}
				} else {
					if (it != null && it.equals(item) && it.getStackSize() < it.getMaxStackSize()) {
						int t = it.getMaxStackSize() - it.getStackSize();
						int tt = t - stack;

						if (tt > 0) {
							it.setStackSize(it.getMaxStackSize() - tt);
							return true;
						} else {
							it.setStackSize(it.getMaxStackSize() - tt);
							stack = -(it.getMaxStackSize() - tt);

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


	public void updateEntity() {
		super.updateEntity();

		for(int i = 0; i < getInvetorySize(); i++){
			if(getItem(i) != null){
				getItem(i).slot = i;
			}
		}
	}

	@Override
	public String toString() {
		return "EntityPlayer{" +
				"name='" + name + '\'' +
				", playerHealth=" + playerHealth +
				", facing=" + facing +
				'}';
	}
}
