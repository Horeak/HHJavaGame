package EntityFiles;

import EntityFiles.DamageSourceFiles.DamageBase;
import EntityFiles.DamageSourceFiles.DamageSource;
import com.sun.javafx.geom.Point2D;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public abstract class Entity {

	Point2D pos;
	public Entity(float x, float y){
		pos = new Point2D(x, y);
	}

	public abstract String getEntityDisplayName();

	public Point2D getEntityPostion(){return pos;}
	public void setEntityPosition(float x, float y){
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);

		//TODO Use this when adding float position
		x = Float.parseFloat(df.format(x).replace(",", "."));
		y = Float.parseFloat(df.format(y).replace(",", "."));

		pos.setLocation(x, y);
	}

	public abstract int getEntityHealth();
	public abstract boolean shouldDamage(DamageSource source);
	public abstract void damageEntity(DamageSource source, DamageBase damage);

	public abstract void renderEntity(JFrame frame, Graphics2D g2, int renderX, int renderY);

	public void updateEntity(){}
}


