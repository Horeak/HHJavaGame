package EntityFiles;

import Blocks.Util.Block;
import EntityFiles.DamageSourceFiles.DamageBase;
import EntityFiles.DamageSourceFiles.DamageSource;
import Main.MainFile;
import com.sun.javafx.geom.Point2D;

import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.HashMap;

public abstract class Entity {

	public boolean isOnGround = true;
	Point2D pos;
	public int blocksFallen = 0;
	private HashMap<String, Object> entityData = new HashMap<>();

	public int timeAlive = 0;

	public Entity( float x, float y ) {
		pos = new Point2D(x, y);
	}

	public abstract String getEntityDisplayName();

	public Point2D getEntityPostion() {
		return pos;
	}

	public void setEntityPosition( float x, float y ) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);

		int xL = Integer.toString((int) x).length() + 2;
		int yL = Integer.toString((int) y).length() + 2;

		if (Float.toString(x).length() > xL) {
			xL += 1;
		}

		if (Float.toString(y).length() > yL) {
			yL += 1;
		}

		x = Float.parseFloat(Float.toString(x).substring(0, xL));
		y = Float.parseFloat(Float.toString(y).substring(0, yL));

		pos.setLocation(x, y);
	}

	public void setEntityData( String tag, Object ob ) {
		entityData.put(tag, ob);
	}

	public Object getEntityData( String tag ) {
		return entityData.get(tag);
	}

	public abstract Rectangle2D getEntityBounds();

	public abstract int getEntityHealth();

	public abstract boolean shouldDamage( DamageSource source );

	public abstract void damageEntity( DamageSource source, DamageBase damage );

	public abstract void renderEntity( org.newdawn.slick.Graphics g2, int renderX, int renderY );

	public Block getBlockBelow() {
		return MainFile.getServer().getWorld().getBlock(Math.round(getEntityPostion().x), Math.round(getEntityPostion().y) + 1);
	}

	public boolean canMoveTo( float x, float y ) {
		if (x >= 0 && x < MainFile.getServer().getWorld().worldSize.xSize && y < MainFile.getServer().getWorld().worldSize.ySize) {
			float f = 0.38F;

			Block b1 = MainFile.getServer().getWorld().getBlock(Math.round(x), Math.round(y)), b2 = MainFile.getServer().getWorld().getBlock(Math.round(x - f), Math.round(y)), b3 = MainFile.getServer().getWorld().getBlock(Math.round(x), Math.round(y) - 1);
			Block b4 = MainFile.getServer().getWorld().getBlock(Math.round(x + f), Math.round(y));

			boolean t1 = b1 == null || b1 != null && b1.canPassThrough();
			boolean t2 = b2 == null || b2 != null && b2.canPassThrough();
			boolean t3 = b3 == null || b3 != null && b3.canPassThrough();
			boolean t4 = b4 == null || b4 != null && b4.canPassThrough();

			return t1 && t2 && t3 && t4;
		}
		return false;
	}

	public boolean moveTo( float x, float y ) {
		Block targetBlock = MainFile.getServer().getWorld().getBlock(Math.round(x), Math.round(y));

		if (x < MainFile.getServer().getWorld().worldSize.xSize && x >= 0 && y < MainFile.getServer().getWorld().worldSize.ySize)
			if (targetBlock != null && !targetBlock.blockBounds(Math.round(x), Math.round(y)).intersects(getEntityBounds()) || targetBlock != null && targetBlock.canPassThrough() || targetBlock == null) {
				if (canMoveTo(x, y)) {
					setEntityPosition(x, y);
					return true;
				}
			}

		return false;
	}

	public void updateEntity() {

		if(!MainFile.getServer().getWorld().generating) {
			timeAlive += 1;

			Block bl = getBlockBelow();
			isOnGround = bl != null && !bl.canPassThrough();

			if (bl != null && !bl.canPassThrough()) {
				isOnGround = true;
			}

			if (!isOnGround && bl == null || !isOnGround && bl != null && bl.canPassThrough()) {
				float x = pos.x;
				float y = Math.round(pos.y + (blocksFallen > 2 ? 2 : 1));

				if(canMoveTo(x, y)) {
					moveTo(x, y);
				}else if(blocksFallen > 2 && canMoveTo(x, y - 1)){
					moveTo(x, y - 1);
				}

				blocksFallen += 1;
			}

			if (isOnGround && getBlockBelow() != null && !getBlockBelow().canPassThrough()) {
				blocksFallen = 0;
			}
		}
	}


	@Override
	public boolean equals( Object o ) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Entity)) {
			return false;
		}

		Entity entity = (Entity) o;

		if (isOnGround != entity.isOnGround) {
			return false;
		}
		if (!pos.equals(entity.pos)) {
			return false;
		}
		return !(entityData != null ? !entityData.equals(entity.entityData) : entity.entityData != null);

	}

	@Override
	public int hashCode() {
		int result = (isOnGround ? 1 : 0);
		result = 31 * result + pos.hashCode();
		result = 31 * result + (entityData != null ? entityData.hashCode() : 0);
		return result;
	}
}


