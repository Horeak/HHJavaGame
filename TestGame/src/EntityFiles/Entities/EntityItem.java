package EntityFiles.Entities;

import BlockFiles.Util.Block;
import EntityFiles.DamageSourceFiles.DamageBase;
import EntityFiles.DamageSourceFiles.DamageSource;
import EntityFiles.Entity;
import Items.Utils.ItemStack;
import Main.MainFile;
import Utils.RenderUtil;
import Utils.TexutrePackFiles.TextureLoader;
import org.newdawn.slick.Graphics;

import java.awt.geom.Rectangle2D;

public class EntityItem extends Entity {
	//TODO Add droppedBy to make it delay pickup if dropped by same person
	public ItemStack stack;
	public int delay = 0, delayTo = 5;

	public static int DESPAWN_TIME = 3000;
	public static float PICKUP_RANGE = 1.5F;

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
		return new Rectangle2D.Double(getEntityPostion().x, getEntityPostion().y, 0, 0);
	}

	@Override
	public int getEntityHealth() {
		return -1;
	}

	@Override
	public int getEntityMaxHealth() {
		return 0;
	}

	@Override
	public void healEntity( int heal ) {

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

		if(stack != null && stack.getItem() != null)
		RenderUtil.renderItem(g2, stack, renderX, renderY);

		g2.scale(2, 2);
		g2.popTransform();
	}

	@Override
	public void loadTextures(TextureLoader imageLoader) {

	}

	@Override
	public void onDeath() {

	}

	public void updateEntity() {
		super.updateEntity();

		//Despawns the item after the DESPAWN_TIME has been reached
		if(timeAlive > DESPAWN_TIME){
			MainFile.game.getServer().getWorld().despawnEntity(this);
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


		//Gives the player the item if withing the range of the item ad despawns the entity
		if(delay >= delayTo) {
			if (MainFile.game.getClient().getPlayer().getEntityPostion().distance(getEntityPostion()) < PICKUP_RANGE) {
				MainFile.game.getClient().getPlayer().addItem(stack);
				stack = null;

				MainFile.game.getServer().getWorld().despawnEntity(this);
			}
		}else if(delay < delayTo){
			delay += 1;
		}
	}

	public Block getBlockBelow() {
		return MainFile.game.getServer().getWorld().getBlock((int) getEntityPostion().x, (int) getEntityPostion().y + 1);
	}

}
