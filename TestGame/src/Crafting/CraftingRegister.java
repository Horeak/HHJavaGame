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

	public static void registerRecipes() {
		addCraftingRecipe(new ItemStack[]{ new ItemStack(Blocks.blockWood)}, new ItemStack(Blocks.blockPlanks, 3));
		addCraftingRecipe(new ItemStack[]{ new ItemStack(Blocks.blockPlanks, 2)}, new ItemStack(Items.itemStick, 4));

		addCraftingRecipe(new ItemStack[]{ new ItemStack(Items.itemStick, 1), new ItemStack(Blocks.blockPlanks)}, new ItemStack(Blocks.blockTorch, 5));

		addCraftingRecipe(new ItemStack[]{new ItemStack(Blocks.blockWood), new ItemStack(Blocks.blockStone, 6)}, new ItemStack(Blocks.blockFurnace));
		addCraftingRecipe(new ItemStack[]{new ItemStack(Blocks.blockPlanks, 8)}, new ItemStack(Blocks.blockChest));


		//Wood
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Blocks.blockPlanks, 1)}, new ItemStack(Items.itemWoodShovel));
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Blocks.blockPlanks, 2)}, new ItemStack(Items.itemWoodAxe));
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Blocks.blockPlanks, 3)}, new ItemStack(Items.itemWoodPickaxe));

		//Stone
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Blocks.blockStone, 1)}, new ItemStack(Items.itemStoneShovel));
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Blocks.blockStone, 2)}, new ItemStack(Items.itemStoneAxe));
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Blocks.blockStone, 3)}, new ItemStack(Items.itemStonePickaxe));

		//Iron
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Items.itemIronIngot, 1)}, new ItemStack(Items.itemIronShovel));
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Items.itemIronIngot, 2)}, new ItemStack(Items.itemIronAxe));
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Items.itemIronIngot, 3)}, new ItemStack(Items.itemIronPickaxe));

		//Gold
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Items.itemGoldIngot, 1)}, new ItemStack(Items.itemGoldShovel));
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Items.itemGoldIngot, 2)}, new ItemStack(Items.itemGoldAxe));
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Items.itemGoldIngot, 3)}, new ItemStack(Items.itemGoldPickaxe));

		//Silver
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Items.itemSilverIngot, 1)}, new ItemStack(Items.itemSilverShovel));
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Items.itemSilverIngot, 2)}, new ItemStack(Items.itemSilverAxe));
		addCraftingRecipe(new ItemStack[]{new ItemStack(Items.itemStick, 2), new ItemStack(Items.itemSilverIngot, 3)}, new ItemStack(Items.itemSilverPickaxe));


		addFurnaceRecipe(new ItemStack(Blocks.blockIronOre), new ItemStack(Items.itemIronIngot));
		addFurnaceRecipe(new ItemStack(Blocks.blockSilverOre), new ItemStack(Items.itemSilverIngot));
		addFurnaceRecipe(new ItemStack(Blocks.blockGoldOre), new ItemStack(Items.itemGoldIngot));


		if(ConfigValues.debug){
			addCraftingRecipe(new ItemStack[]{new ItemStack(Blocks.blockDirt)}, new ItemStack(Items.debugChunkDestoryer));
			addCraftingRecipe(new ItemStack[]{new ItemStack(Blocks.blockDirt)}, new ItemStack(Items.debugChunkReloader));
			addCraftingRecipe(new ItemStack[]{new ItemStack(Blocks.blockDirt)}, new ItemStack(Items.debugChunkRegenerator));
		}

		LoggerUtil.out.log(Level.INFO, "Crafting recipes registered.");
	}

}
