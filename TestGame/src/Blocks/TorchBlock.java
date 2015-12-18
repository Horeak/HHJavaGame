package Blocks;

import Blocks.Util.Block;
import Blocks.Util.ILightSource;
import org.newdawn.slick.Color;

public class TorchBlock extends Block implements ILightSource {
	@Override
	public String getBlockDisplayName() {
		return "Torch";
	}

	@Override
	public Color getDefaultBlockColor() {
		return Color.yellow;
	}

	@Override
	public int getOutputStrength() {
		return 16;
	}

}
