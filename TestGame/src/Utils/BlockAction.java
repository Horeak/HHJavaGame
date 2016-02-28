package Utils;

import BlockFiles.Util.Block;
import Items.Utils.Item;
import Items.Utils.ItemStack;
import Main.MainFile;
import Render.Renders.HotbarRender;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class BlockAction {
	private static int waitTimeForAction = 20;
	private static int waitActionTime = waitTimeForAction;

	public static int prevX = -1, prevY = -1;
	public static int blockBreakDelay = 0, blockBreakReach = 200, timeSince;
	public static int blockDamage;

	public static void mouseClick( int button, int delta ) {
		float speed = 1F / ((float)delta / 25);


		try {
			int selected = (HotbarRender.slotSelected - 1);

			if (selected < MainFile.game.getClient().getPlayer().getInvetorySize()) {
				ItemStack item = MainFile.game.getClient().getPlayer().getItem(selected);

				if (button == Input.MOUSE_LEFT_BUTTON) {
					if (MainFile.game.getServer().getWorld().getBlock(BlockSelection.selectedX, BlockSelection.selectedY) != null) {
						Block b = MainFile.game.getServer().getWorld().getBlock(BlockSelection.selectedX, BlockSelection.selectedY);

						if(prevX == -1 && prevY == -1 || prevX == BlockSelection.selectedX && prevY == BlockSelection.selectedY) {
							if (blockDamage >= b.getMaxBlockDamage()) {
								MainFile.game.getServer().getWorld().breakBlock(BlockSelection.selectedX, BlockSelection.selectedY);

								if (item != null)
									if (item.getItem() instanceof Item) {
										((Item) item.getItem()).damageItem(item);
									}

								prevX = -1;
								prevY = -1;
								blockDamage = 0;
								blockBreakDelay = 0;

							} else {
								prevX = BlockSelection.selectedX;
								prevY = BlockSelection.selectedY;
								timeSince = 0;

								if(blockBreakDelay >= blockBreakDelay){
									blockDamage += item != null && item.getItem() != null ? item.getItem().getBlockDamageValue( MainFile.game.getServer().getWorld(), BlockSelection.selectedX, BlockSelection.selectedY, item) : 1;
									blockBreakDelay = 0;
								}else{
									blockBreakDelay += speed;
								}

								if (blockDamage >= b.getMaxBlockDamage()) {
									mouseClick(button, delta);
								}
							}
						}else{
							prevX = -1;
							prevY = -1;
							blockDamage = 0;
							blockBreakDelay = 0;
						}
					}

				} else if (button == Input.MOUSE_RIGHT_BUTTON) {
					Block b = MainFile.game.getServer().getWorld().getBlock(BlockSelection.selectedX, BlockSelection.selectedY);

					if(b == null || b != null && !b.blockClicked(MainFile.game.getServer().getWorld(), BlockSelection.selectedX, BlockSelection.selectedY, item)) {
						if (item != null) {
							item.useItem(MainFile.game.getServer().getWorld(), BlockSelection.selectedX, BlockSelection.selectedY);
						}
					}
				}
			}

		} catch (Exception e) {
			LoggerUtil.exception(e);
		}
	}

	public static void update( GameContainer container, int delta ) {
		if (MainFile.game.getServer().getWorld() != null && waitActionTime >= waitTimeForAction) {
			BlockSelection.update(container);

			if (container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
				mouseClick(Input.MOUSE_LEFT_BUTTON, delta);
				waitActionTime = 0;

			} else if (container.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
				mouseClick(Input.MOUSE_RIGHT_BUTTON, delta);
				waitActionTime = 0;
			}
		} else if (waitActionTime < waitTimeForAction) {
			waitActionTime += 1;
		}
	}
}
