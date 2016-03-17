package EntityFiles.DamageSourceFiles;

import EntityFiles.Entity;
import WorldFiles.World;

public enum DamageSource {

	//TODO Add damage sources
	Fall_Damage("Fall Damage"),
	UNSPECIFIED("UNSPECIFIED");



	public boolean defenceDecreaseDamage(){return false;}
	public boolean shouldDamage( Entity ent){return true;}
	public void doDamageEffects(Entity ent, World world){}

	public String damageName;
	DamageSource( String name ) {
		this.damageName = name;
	}
}
