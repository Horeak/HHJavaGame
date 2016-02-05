package Utils;

import Blocks.Util.Block;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import Main.MainFile;
import Render.Renders.HotbarRender;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class BlockAction {
	private static int waitTimeForAction = 20;
	private static int waitActionTime = waitTimeForAction;

	public static void mouseClick( int button ) {
		try {
			int selected = (HotbarRender.slotSelected - 1);

			if (selected < MainFile.game.getClient().getPlayer().getInvetorySize()) {
				ItemStack item = MainFile.game.getClient().getPlayer().getItem(selected);

				if (button == Input.MOUSE_LEFT_BUTTON) {
					if (MainFile.game.getServer().getWorld().getBlock(BlockSelection.selectedX, BlockSelection.selectedY) != null) {

						Block b = MainFile.game.getServer().getWorld().getBlock(BlockSelection.selectedX, BlockSelection.selectedY);

						if (b.getBlockDamage() >= b.getMaxBlockDamage()) {
							MainFile.game.getServer().getWorld().breakBlock(BlockSelection.selectedX, BlockSelection.selectedY);

							if(item != null)
							if(item.getItem() instanceof Item){
								((Item)item.getItem()).damageItem(item);
							}

						} else {
							b.setBlockDamage(b.getBlockDamage() + (item != null ? item.getItem().getBlockDamageValue(MainFile.game.getServer().getWorld(), BlockSelection.selectedX, BlockSelection.selectedY, item) : 1));

							if(b.getBlockDamage() >= b.getMaxBlockDamage()){
								mouseClick(button);
							}
						}
					}

				} else if (button == Input.MOUSE_RIGHT_BUTTON) {
					if (item != null) {
						item.useItem(MainFile.game.getServer().getWorld(), BlockSelection.selectedX, BlockSelection.selectedY);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void update( GameContainer container ) {
		if (MainFile.game.getServer().getWorld() != null && waitActionTime >= waitTimeForAction) {
			BlockSelection.update(container);

			if (container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				mouseClick(Input.MOUSE_LEFT_BUTTON);
				waitActionTime = 0;

			} else if (container.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
				mouseClick(Input.MOUSE_RIGHT_BUTTON);
				waitActionTime = 0;
			}
		} else if (waitActionTime < waitTimeForAction) {
			waitActionTime += 1;
		}
	}
}