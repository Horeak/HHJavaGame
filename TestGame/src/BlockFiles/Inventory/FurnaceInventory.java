package BlockFiles.Inventory;

import BlockFiles.Util.IFuel;
import Crafting.CraftingRegister;
import Items.Utils.IInventory;
import Items.Utils.ItemStack;

public class FurnaceInventory implements IInventory {

	//0=input
	//1=fuel
	//2=output
	public ItemStack[] stacks = new ItemStack[3];

	public int smeltTime = 0;
	public int timeSinceUpdate = 0;

	public int fuel = 0;

	@Override
	public ItemStack[] getItems() {
		return stacks;
	}

	@Override
	public ItemStack getItem( int i ) {
		return stacks[i];
	}

	@Override
	public void setItem( int i, ItemStack item ) {
		stacks[i] = item;
	}

	@Override
	public int getInventorySize() {
		return 3;
	}

	@Override
	public String getInventoryName() {
		return "Furnace";
	}

	public boolean canSmelt(){
		return fuel > 0 && canSmeltF();
	}

	public boolean canSmeltF(){
		boolean recipes = CraftingRegister.getFurnaceRecipeFromInput(getItem(0)) != null;

		if(!recipes) return false;
		boolean emptyOrEqualOutput = getItem(2) == null || (getItem(2).getStackSize() + CraftingRegister.getFurnaceRecipeFromInput(getItem(0)).output.getStackSize()) < getItem(2).getMaxStackSize() && getItem(2).getItem().equals(CraftingRegister.getFurnaceRecipeFromInput(getItem(0)).output.getItem());

		return recipes && emptyOrEqualOutput;
	}

	public void update(){
		if(!canSmelt() && fuel > 0){
			fuel -= 1;
			return;
		}

		if(canSmeltF() && fuel <= 0){
			if(getItem(1) != null && getItem(1).getItem() instanceof IFuel){
				fuel += ((IFuel)getItem(1).getItem()).getFuelValue();

				getItem(1).decreaseStackSize(1);

				if(getItem(1).getStackSize() <= 0){
					setItem(1, null);
				}
			}
		}


		if(!canSmelt() && smeltTime != CraftingRegister.getFurnaceSmeltTimeFromInput(getItem(0))){
			smeltTime = 0;
			return;
		}

		if(smeltTime >= CraftingRegister.getFurnaceSmeltTimeFromInput(getItem(0))){
			ItemStack ot = CraftingRegister.getFurnaceOutputFromInput(getItem(0));

			if(getItem(2) != null && getItem(2).getStackSize() > 0){
				getItem(2).increaseStackSize(ot.getStackSize());
			}else{
				setItem(2, new ItemStack(ot.getItem(), ot.getStackSize(), ot.getStackDamage()));
			}

			getItem(0).decreaseStackSize(CraftingRegister.getFurnaceRecipeFromInput(getItem(0)).input.getStackSize());

			if(getItem(0).getStackSize() <= 0){
				setItem(0, null);
			}

			smeltTime = 0;
			return;
		}

		smeltTime += 1;
		fuel -= 1;
	}
}
