package Crafting;

import BlockFiles.*;
import Items.*;
import Items.Utils.IItemRegistry;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.ConfigValues;
import Utils.LoggerUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

public class CraftingRegister {

	public static ArrayList<CraftingRecipe> craftingRecipes = new ArrayList<>();
	public static ArrayList<FuranceRecipe> furanceRecipes = new ArrayList<>();

	public static final int DEFAUILT_SMELT_TIME = 5; //5 sec

	public static CraftingRecipe getCraftingRecipeFromInput( ItemStack[] input ) {
		for (CraftingRecipe res : craftingRecipes) {
			if (Arrays.equals(res.input, input)) {
				return res;
			}
		}

		return null;
	}

	public static void addCraftingRecipe( ItemStack[] input, ItemStack output ) {
		try {
			if (input.length > 4) {
				throw new Exception("Recipes cant be longer then 4 input items!");
			}

			craftingRecipes.add(new CraftingRecipe(input, output));
		} catch (Exception e) {
		LoggerUtil.exception(e);
		}
	}

	public static void addFurnaceRecipe( ItemStack input, ItemStack output ) {
		addFurnaceRecipe(input, output, DEFAUILT_SMELT_TIME);
	}

	public static void addFurnaceRecipe( ItemStack input, ItemStack output, int smeltTime ) {
		try {
			furanceRecipes.add(new FuranceRecipe(input, output, smeltTime));
		} catch (Exception e) {
			LoggerUtil.exception(e);
		}
	}

	public static FuranceRecipe getFurnaceRecipeFromInput(ItemStack input){
		if(input == null) return null;

		for(FuranceRecipe r : furanceRecipes){
			//TODO Check item not stacksize
			if(IItemRegistry.getID(r.input.getItem()) == IItemRegistry.getID(input.getItem()) && input.getStackSize() >= r.input.getStackSize()){
				return r;
			}
		}

		return null;
	}

	public static ItemStack getFurnaceOutputFromInput(ItemStack input){
		return getFurnaceRecipeFromInput(input) != null ? getFurnaceRecipeFromInput(input).output : null;
	}

	public static int getFurnaceSmeltTimeFromInput(ItemStack input){
		return getFurnaceRecipeFromInput(input) != null ? getFurnaceRecipeFromInput(input).smeltTime : DEFAUILT_SMELT_TIME;
	}


	public static int getAmount(ItemStack item){
		boolean hasItem = false;
		int hasSize = 0;

		for (ItemStack tem : MainFile.game.getClient().getPlayer().inventoryItems.values()) {
			if(tem != null && item != null) {
				if (tem.equals(item)) {
					hasSize += tem.getStackSize();
				}
			}
		}

		return hasSize;
	}

	public static boolean hasMaterialFor( CraftingRecipe recipe ) {
		if(ConfigValues.debug) return true;

		if(recipe != null)
		for (ItemStack item : recipe.input) {
			if(item != null)
			if (!hasMaterial(item)) {
				return false;
			}
		}


		return true;
	}

	public static boolean hasMaterial( ItemStack item ) {
		boolean hasItem = false;
		int hasSize = 0;

		for (ItemStack tem : MainFile.game.getClient().getPlayer().inventoryItems.values()) {
			if (!hasItem && hasSize >= item.getStackSize()) {
				hasItem = true;
				break;
			}

			if (item.equals(tem) && tem.getStackSize() >= item.getStackSize()) {
				hasItem = true;
				hasSize = item.getStackSize();
			} else if (item.equals(tem) && tem.getStackSize() < item.getStackSize()) {
				hasSize += tem.getStackSize();
				continue;
			}
		}

		return hasItem;
	}

	//TODO Add furance craftingRecipes
	public static void registerRecipes() {
		addCraftingRecipe(new ItemStack[]{ new ItemStack(Blocks.blockWood)}, new ItemStack(Blocks.blockPlanks, 3));
		addCraftingRecipe(new ItemStack[]{ new ItemStack(Blocks.blockPlanks, 2)}, new ItemStack(Items.itemStick, 4));

		addCraftingRecipe(new ItemStack[]{ new ItemStack(Items.itemStick, 1), new ItemStack(Blocks.blockPlanks)}, new ItemStack(Blocks.blockTorch, 5));

		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Blocks.blockStone, 1)}, new ItemStack(Items.itemShovel));
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Blocks.blockStone, 2)}, new ItemStack(Items.itemAxe));
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Blocks.blockStone, 3)}, new ItemStack(Items.itemPickaxe));

		addCraftingRecipe(new ItemStack[]{new ItemStack(Blocks.blockWood), new ItemStack(Blocks.blockStone, 6)}, new ItemStack(Blocks.blockFurnace));

		//TODO Add ingots for ores and add smelting recipes
		addFurnaceRecipe(new ItemStack(Blocks.blockIronOre), new ItemStack(Items.itemIronIngot));


		if(ConfigValues.debug){
			addCraftingRecipe(new ItemStack[]{new ItemStack(Blocks.blockDirt)}, new ItemStack(Items.debugChunkDestoryer));
			addCraftingRecipe(new ItemStack[]{new ItemStack(Blocks.blockDirt)}, new ItemStack(Items.debugChunkReloader));
			addCraftingRecipe(new ItemStack[]{new ItemStack(Blocks.blockDirt)}, new ItemStack(Items.debugChunkRegenerator));
		}

		LoggerUtil.out.log(Level.INFO, "Crafting recipes registered.");
	}

}
