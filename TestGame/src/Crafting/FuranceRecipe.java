package Crafting;

import Items.Utils.ItemStack;

import java.util.Arrays;

public class FuranceRecipe {

	public ItemStack output;
	public ItemStack input;
	public int smeltTime;

	public FuranceRecipe( ItemStack input, ItemStack output, int smeltTime ) {
		this.input = input;
		this.output = output;
		this.smeltTime = smeltTime;
	}


	@Override
	public boolean equals( Object o ) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof FuranceRecipe)) {
			return false;
		}

		FuranceRecipe that = (FuranceRecipe) o;

		if (smeltTime != that.smeltTime) {
			return false;
		}
		if (output != null ? !output.equals(that.output) : that.output != null) {
			return false;
		}
		if (input != null ? !input.equals(that.input) : that.input != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = output != null ? output.hashCode() : 0;
		result = 31 * result + (input != null ? input.hashCode() : 0);
		result = 31 * result + smeltTime;
		return result;
	}
}
