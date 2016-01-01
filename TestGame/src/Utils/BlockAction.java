package Utils;

import Blocks.Util.Block;
import Items.IItem;
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

			if (selected < MainFile.currentWorld.player.getInvetorySize()) {
				IItem item = MainFile.currentWorld.player.getItem(selected);

				if (button == Input.MOUSE_LEFT_BUTTON) {
					if (MainFile.currentWorld.getBlock(BlockSelection.selectedX, BlockSelection.selectedY) != null) {

						Block b = MainFile.currentWorld.getBlock(BlockSelection.selectedX, BlockSelection.selectedY);

						if (b.getBlockDamage() >= b.getMaxBlockDamage()) {
							if (MainFile.currentWorld.getBlock(BlockSelection.selectedX, BlockSelection.selectedY).getItemDropped() != null) {
								MainFile.currentWorld.player.addItem(MainFile.currentWorld.getBlock(BlockSelection.selectedX, BlockSelection.selectedY).getItemDropped());
							}

							MainFile.currentWorld.setBlock(null, BlockSelection.selectedX, BlockSelection.selectedY);
						} else {
							b.setBlockDamage(b.getBlockDamage() + (item != null ? item.getBlockDamageValue(MainFile.currentWorld, BlockSelection.selectedX, BlockSelection.selectedY) : 1));
						}
					}

				} else if (button == Input.MOUSE_RIGHT_BUTTON) {
					if (item != null) {
						if (item.useItem(MainFile.currentWorld, BlockSelection.selectedX, BlockSelection.selectedY)) {
							item.onItemUsed(selected);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void update( GameContainer container ) {
		if (MainFile.currentWorld != null && waitActionTime >= waitTimeForAction) {
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
