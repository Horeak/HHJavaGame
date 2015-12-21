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
	int blocksFallen = 0;
	private HashMap<String, Object> entityData = new HashMap<>();

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

		x = Float.parseFloat(df.format(x).replace(",", "."));
		y = Float.parseFloat(df.format(y).replace(",", "."));

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
		return MainFile.currentWorld.getBlock(Math.round((int) getEntityPostion().x), (int) getEntityPostion().y + 1);
	}

	public boolean canMoveTo( float x, float y ) {
		if (x >= 0 && x < MainFile.currentWorld.worldSize.xSize && y >= 0 && y < MainFile.currentWorld.worldSize.ySize) {
			return MainFile.currentWorld.getBlock(Math.round(x), Math.round(y)) == null && MainFile.currentWorld.getBlock(Math.round(x - 0.6F), Math.round(y)) == null && MainFile.currentWorld.getBlock(Math.round(x), Math.round(y) - 1) == null;
		}
		return false;
	}

	public boolean moveTo( float x, float y ) {
		Block targetBlock = MainFile.currentWorld.getBlock(Math.round(x), Math.round(y));

		if ((int) x == MainFile.currentWorld.worldSize.xSize) x = Float.floatToIntBits(x);
		if ((int) y == MainFile.currentWorld.worldSize.ySize) y = Float.floatToIntBits(y);

		if (x < MainFile.currentWorld.worldSize.xSize && x >= 0 && y < MainFile.currentWorld.worldSize.ySize && y >= 0)
			if (targetBlock != null && !targetBlock.blockBounds().contains(x, y, getEntityBounds().getBounds().getWidth(), getEntityBounds().getBounds().getHeight()) && targetBlock != null && !targetBlock.blockBounds().intersects(getEntityBounds()) || targetBlock != null && targetBlock.canPassThrough() || targetBlock == null) {
				if (canMoveTo(x, y)) {
					setEntityPosition(x, y);
					return true;
				}
			}

		return false;
	}

	public void updateEntity() {
		Block bl = getBlockBelow();
		isOnGround = bl != null && !bl.canPassThrough();

		if (bl != null && !bl.canPassThrough()) {
			isOnGround = true;
		}

		if (!isOnGround && bl == null || !isOnGround && bl != null && bl.canPassThrough()) {
			moveTo(pos.x, Math.round(pos.y + blocksFallen));

			blocksFallen += 1;
		}

		if (isOnGround && getBlockBelow() != null && !getBlockBelow().canPassThrough()) {
			blocksFallen = 0;
		} else {
			blocksFallen = 1;
		}
	}

}


