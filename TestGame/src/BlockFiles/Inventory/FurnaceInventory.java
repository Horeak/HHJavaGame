package BlockFiles.Inventory;

import BlockFiles.Util.IFuel;
import Crafting.CraftingRegister;
import Items.Utils.IInventory;
import Items.Utils.ItemStack;
import Utils.LoggerUtil;

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

	@Override
	public boolean validItemForSlot( ItemStack stack, int slot ) {
		return slot == 0 && stack != null && CraftingRegister.getFurnaceRecipeFromInput(stack) != null || slot == 1 && stack.getItem() instanceof IFuel || slot == 2 && false;
	}

	@Override
	public boolean canConsumeFromSlot( int slot ) {
		return slot < 2;
	}
	public boolean canOutputfromSlot(int slot){return slot == 2;}

	public boolean canSmelt(){
		return fuel > 0 && canSmeltF();
	}

	public boolean canSmeltF(){
		boolean recipes = CraftingRegister.getFurnaceRecipeFromInput(getItem(0)) != null;

		if(!recipes) return false;
		boolean emptyOrEqualOutput = getItem(2) == null || (getItem(2).getStackSize() + CraftingRegister.getFurnaceRecipeFromInput(getItem(0)).output.getStackSize()) <= getItem(2).getMaxStackSize() && getItem(2).getItem().equals(CraftingRegister.getFurnaceRecipeFromInput(getItem(0)).output.getItem());

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
				consumeFromSlot(null, 1);
			}
		}


		if(!canSmelt() && smeltTime != CraftingRegister.getFurnaceSmeltTimeFromInput(getItem(0))){
			smeltTime = 0;
			return;
		}

		if(smeltTime >= CraftingRegister.getFurnaceSmeltTimeFromInput(getItem(0))){
			ItemStack ot = CraftingRegister.getFurnaceOutputFromInput(getItem(0));

			try {

				addItemToSlot(new ItemStack(ot), 2);

			} catch (Exception e) {
				LoggerUtil.exception(e);
			}
			consumeFromSlot(CraftingRegister.getFurnaceRecipeFromInput(getItem(0)).input, 0);

			smeltTime = 0;
			return;
		}

		smeltTime += 1;
		fuel -= 1;
	}
}
