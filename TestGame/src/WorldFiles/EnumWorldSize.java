package WorldFiles;

public enum EnumWorldSize {
	SMALL(128, 64),
	MEDIUM(512, 256),
	LARGE(2048, 1024);

	public int xSize, ySize;

	EnumWorldSize( int xSize, int ySize ) {
		this.xSize = xSize;
		this.ySize = ySize;
	}
}
