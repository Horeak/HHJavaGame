package EntityFiles.Entities;


import EntityFiles.DamageSourceFiles.DamageBase;
import EntityFiles.DamageSourceFiles.DamageSource;
import EntityFiles.Entity;
import Items.Utils.Armor.ArmorType;
import Items.Utils.Armor.IArmor;
import Items.Utils.IInventory;
import Items.Utils.ItemStack;
import Main.MainFile;
import Render.Renders.HotbarRender;
import Utils.TexutrePackFiles.TextureLoader;
import WorldFiles.GameMode;
import org.newdawn.slick.Graphics;

import java.awt.geom.Rectangle2D;


public class EntityPlayer extends Entity implements IInventory {

	//TODO Add proper player render
	public static org.newdawn.slick.Image playerTexutre =  null;

	//TODO Render equiped armor and maybe even the currently held item?
	/**
	 * 1 = left
	 * 2 = right
	 */
	public int facing = 0;
	public static int INV_SIZE = 50;

	public ItemStack[] items = new ItemStack[getInventorySize()];
	public ArmorInventory armorInventory = new ArmorInventory();

	private int playerHealth = 0, playerMaxHealth = 20;
	public String name;

	public int deaths = 0;

	//TODO Allow this being changed per player
	public GameMode playerGameMode = GameMode.SURVIVAL;

	//TODO Add veriables for speed, defence, jump height, damage and similar and make sure all has a static start value. This will make it possible to change these values with for example armor (RPG like)

	public EntityPlayer( float x, float y, String name ) {
		super(x, y);
		this.name = name;

		playerHealth = playerMaxHealth;
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
	public int getEntityMaxHealth() {
		return playerMaxHealth;
	}

	//TODO Decreasing health?
	@Override
	public void healEntity( int heal ) {
		playerHealth += heal;

		if(playerHealth > playerMaxHealth){
			playerHealth = playerMaxHealth;
		}
	}

	@Override
	public boolean shouldDamage( DamageSource source ) {
		return playerGameMode.canPlayerReceiveDamage;
	}

	@Override
	public void damageEntity( DamageSource source, DamageBase damage ) {
		//TODO Take armor/defence into consideration when it is added.

		if(source.shouldDamage(this)) {
			source.doDamageEffects(this, MainFile.game.getServer().getWorld());
			playerHealth -= damage.getDamageAmount();
		}
	}

	@Override
	public void renderEntity( Graphics g2, int renderX, int renderY ) {
		if (facing == 1) {
			playerTexutre.getFlippedCopy(true, false).draw(renderX, renderY - 64, 32, 64);

		} else {
			playerTexutre.draw(renderX, renderY - 64, 32, 64);
		}

	}

	public boolean shouldLoadChunk(){return true;}


	@Override
	public void loadTextures(TextureLoader imageLoader) {
		playerTexutre = imageLoader.getImage("textures", "player");
	}

	@Override
	public void onDeath() {
		MainFile.game.getServer().getWorld().loadChunk(0, 0);
		int xx = 0, yy = (MainFile.game.getServer().getWorld().getHeight(0) - 2);
		setEntityPosition(xx, yy);

		deaths += 1;
		blocksFallen = 0;
		playerHealth = (int)(playerMaxHealth * 0.25F);
	}

	public Rectangle2D getPlayerBounds() {
		return new Rectangle2D.Double((int) getEntityPostion().x, (int) getEntityPostion().y - 1, 1, 2);
	}

	@Override
	public Rectangle2D getEntityBounds() {
		return getPlayerBounds();
	}



	//TODO Improve dropItem()
	public void dropItem(){
		float distance = 1.5F;

		//TODO Add dropped by to delay pickup time when item is dropped
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
		return items;
	}

	@Override
	public ItemStack getItem( int i ) {
		return i < INV_SIZE ? items[i] : null;
	}

	@Override
	public void setItem( int i, ItemStack item ) {
		if (i < INV_SIZE) {
			items[i] = item;
		}
	}

	@Override
	public int getInventorySize() {
		return INV_SIZE;
	}

	@Override
	public String getInventoryName() {
		return "Player Inventory";
	}

	@Override
	public boolean validItemForSlot( ItemStack stack, int slot ) {
		return true;
	}

	@Override
	public boolean canConsumeFromSlot( int slot ) {
		return true;
	}


	public boolean flying = false;

	public void updateEntity() {
		super.updateEntity();

		shouldFall = !flying;

		for(int i = 0; i < getInventorySize(); i++){
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

	public static class ArmorInventory implements IInventory{

		public ItemStack[] armorItems = new ItemStack[ getInventorySize()];

		@Override
		public ItemStack[] getItems() {
			return armorItems;
		}

		@Override
		public ItemStack getItem( int i ) {
			return armorItems[i];
		}

		@Override
		public void setItem( int i, ItemStack item ) {
			armorItems[i] = item;
		}

		@Override
		public int getInventorySize() {
			return 4;
		}

		@Override
		public String getInventoryName() {
			return "Armor";
		}

		@Override
		public boolean validItemForSlot( ItemStack stack, int slot ) {
			if(stack.getItem() instanceof IArmor){
				IArmor am = (IArmor) stack.getItem();

				if(am.getArmorType() == ArmorType.Helmet){
					return slot == 0;

				}else if(am.getArmorType() == ArmorType.Chestplate){
					return slot == 1;

				}else if(am.getArmorType() == ArmorType.Leggings){
					return slot == 2;

				}else if(am.getArmorType() == ArmorType.Boots){
					return slot == 3;

				}
			}
			return false;
		}

		@Override
		public boolean canConsumeFromSlot( int slot ) {
			return true;
		}
	}
}
