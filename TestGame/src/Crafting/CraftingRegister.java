package Crafting;

import Blocks.BlockTorch;
import Blocks.BlockWood;
import Items.IItem;
import Main.MainFile;
import Utils.ItemUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class CraftingRegister {

	public static ArrayList<CraftingRecipe> recipes = new ArrayList<>();

	public static CraftingRecipe getRecipeFromInput( IItem[] input ) {
		for (CraftingRecipe res : recipes) {
			if (Arrays.equals(res.input, input)) {
				return res;
			}
		}


		return null;
	}

	public static void addRecipe( IItem[] input, IItem output ) {
		try {
			if (input.length > 4) {
				throw new Exception("Recipes cant be longer then 4 input items!");
			}

			recipes.add(new CraftingRecipe(input, output));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean hasMaterialFor( CraftingRecipe recipe ) {
		for (IItem item : recipe.input) {
			if (!hasMaterial(item)) {
				return false;
			}
		}


		return true;
	}

	public static boolean hasMaterial( IItem item ) {
		boolean hasItem = false;
		int hasSize = 0;

		for (IItem tem : MainFile.currentWorld.player.inventoryItems) {
			if (!hasItem && hasSize >= item.getItemStackSize()) {
				hasItem = true;
				break;
			}

			if (item.equals(tem) && tem.getItemStackSize() >= item.getItemStackSize()) {
				hasItem = true;
				hasSize = item.getItemStackSize();
			} else if (item.equals(tem) && tem.getItemStackSize() < item.getItemStackSize()) {
				hasSize += tem.getItemStackSize();
				continue;
			}
		}

		return hasItem;
	}

	public static void registerRecipes() {
		addRecipe(new IItem[]{ ItemUtil.getItem(new BlockWood(), 2) }, ItemUtil.getItem(new BlockTorch(), 5));
	}

}
