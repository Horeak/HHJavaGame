package Items.Utils;

import BlockFiles.Util.Block;
import WorldFiles.World;

import java.util.ArrayList;

public class ItemStack {

	private IItem itemStored;

	private int stackSize;
	private int stackDamage;

	private String stackName;
	private String source = "DEFAULT";

	public int slot;

	public ItemStack(ItemStack stack) throws CloneNotSupportedException {
		this(stack.getItem().clone(), stack.getStackSize(), stack.getStackDamage());
	}

	public ItemStack(IItem item){
		this(item, 1, 0);
	}

	public ItemStack(IItem item, int stackSize){
		this(item, stackSize, 0);
	}

	public ItemStack(IItem item, int stackSize, int damage){
		this.itemStored = item;
		this.stackSize = stackSize;
		this.stackDamage = damage;
		this.stackName = item.getItemName();
		this.source = item.getClass().getName();
	}

	public IItem getItem(){
		return itemStored;
	}

	public Block getBlock(){
		return isBlock() ? ((Block)itemStored) : null;
	}


	public boolean isBlock(){
		return itemStored instanceof Block;
	}

	public String getStackID(){
		return (isBlock() ? "block." : "item.") + getItem().getItemName().replace(" ", "_") + "-" + getStackName().replace(" ", "_") + ":" + source;
	}

	public int getStackSize(){
		return stackSize;
	}

	public int getMaxStackSize(){
		return itemStored.getItemMaxStackSize();
	}

	public void setStackSize(int i ){
		stackSize = i;
	}

	public void decreaseStackSize( int i ){
		stackSize -= i;
	}

	public void increaseStackSize( int i ){
		stackSize += i;

		if(stackSize > getMaxStackSize()) stackSize = getMaxStackSize();
	}

	public int getStackDamage(){
		return stackDamage;
	}

	public void setStackDamage(int i){
		stackDamage = i;
	}


	public String getStackName(){
		return stackName;
	}

	public void setStackName(String name){
		stackName = name;
	}

	public ArrayList<String> getTooltips(){
		if(itemStored.getTooltips(this) != null){
			return itemStored.getTooltips(this);
		}else{
			return null;
		}
	}


	public boolean useItem(World world, int x, int y){
		if(getItem().useItem(world, x, y, this)){
			return true;
		}
		return false;
	}


	public boolean equals( ItemStack item ) {
		return equals(item, true, false);
	}

	public boolean equals( ItemStack item, boolean stackDamage, boolean stackSize ) {
		if(item == null) return false;

		boolean t1 = stackDamage ? this.stackDamage == item.stackDamage : true;
		boolean t2 = stackSize ? this.stackSize == item.stackSize : true;

		return getItem().equals(item.getItem()) && t1 && t2;
	}
}
