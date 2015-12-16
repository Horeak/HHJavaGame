package Items;

import Items.Rendering.ItemRenderer;

public interface Item {
	//TODO Used similar to how ItemStack works!

	String getItemName();

	ItemRenderer getRender();
}
