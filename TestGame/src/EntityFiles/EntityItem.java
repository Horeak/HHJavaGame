package EntityFiles;

import Blocks.Util.Block;
import EntityFiles.DamageSourceFiles.DamageBase;
import EntityFiles.DamageSourceFiles.DamageSource;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.RenderUtil;
import org.newdawn.slick.Graphics;

import java.awt.geom.Rectangle2D;

public class EntityItem extends Entity {

	public ItemStack stack;
	public int delay = 0, delayTo = 5;

	public static int DESPAWN_TIME = 3000;

	public EntityItem( float x, float y, ItemStack stack ) {
		super(x, y);

		this.stack = stack;
		delay = delayTo;
	}

	@Override
	public String getEntityDisplayName() {
		return stack.getStackName();
	}

	@Override
	public Rectangle2D getEntityBounds() {
		return new Rectangle2D.Double(pos.x, pos.y, 0, 0);
	}

	@Override
	public int getEntityHealth() {
		return -1;
	}

	@Override
	public boolean shouldDamage( DamageSource source ) {
		return false;
	}

	@Override
	public void damageEntity( DamageSource source, DamageBase damage ) {

	}

	private int renderOff = 0, renderTop = 10;
	private boolean top = false;

	@Override
	public void renderEntity( Graphics g2, int renderX, int renderY ) {
		if(stack == null) return;

		g2.pushTransform();

		g2.scale(0.5F, 0.5F);
		g2.translate(renderX + 16, renderY - (48 + renderOff) + 8);

		RenderUtil.renderItem(g2, stack, renderX, renderY, stack.getItem().getRenderMode());

		g2.scale(2, 2);
		g2.popTransform();
	}

	public void updateEntity() {
		super.updateEntity();

		if(timeAlive > DESPAWN_TIME){
			MainFile.game.getServer().getWorld().RemoveEntities.add(this);
		}

		if(!top && renderOff < renderTop){
			renderOff += MainFile.random.nextInt(2);
		}else if(renderOff >= renderTop && !top){
			top = true;
		}else if(top && renderOff > 0){
			renderOff -= MainFile.random.nextInt(2);
		}else if(top && renderOff <= 0){
			top = false;
		}

		if(renderOff > renderTop) renderOff = renderTop;
		if(renderOff < 0) renderOff = 0;

		if(delay >= delayTo) {
			if (MainFile.game.getClient().getPlayer().getEntityPostion().distance(pos) < 1.5F) {
				MainFile.game.getClient().getPlayer().addItem(stack);
				stack = null;

				MainFile.game.getServer().getWorld().RemoveEntities.add(this);
			}
		}else if(delay < delayTo){
			delay += 1;
		}
	}

	public Block getBlockBelow() {
		return MainFile.game.getServer().getWorld().getBlock((int) getEntityPostion().x, (int) getEntityPostion().y + 1);
	}

}
