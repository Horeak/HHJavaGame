package WorldFiles;

public enum EnumWorldSize {
	SMALL(256, 128, 2),
	MEDIUM(1024, 512, 2),
	LARGE(4096, 2048, 2);

	public int xSize, ySize;
	public int div;

	EnumWorldSize( int xSize, int ySize, int div ) {
		this.xSize = xSize;
		this.ySize = ySize;

		this.div = div;
	}

	@Override
	public String toString() {
		return "EnumWorldSize{" +
				"ySize=" + ySize +
				", xSize=" + xSize +
				'}';
	}
}
