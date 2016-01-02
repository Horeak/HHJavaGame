package Crafting;

import Items.IItem;

import java.util.Arrays;

public class CraftingRecipe {
	public IItem output;
	public IItem[] input;

	public CraftingRecipe( IItem[] input, IItem output ) {
		this.input = input;
		this.output = output;
	}

	@Override
	public boolean equals( Object o ) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CraftingRecipe)) {
			return false;
		}

		CraftingRecipe that = (CraftingRecipe) o;

		if (output != null ? !output.equals(that.output) : that.output != null) {
			return false;
		}
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		return Arrays.equals(input, that.input);

	}

	@Override
	public int hashCode() {
		int result = output != null ? output.hashCode() : 0;
		result = 31 * result + Arrays.hashCode(input);
		return result;
	}
}
