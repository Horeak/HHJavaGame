package Utils.TexutrePackFiles;


import Main.MainFile;
import org.newdawn.slick.Image;

public class TexturePack {

	public String name;
	public String fileLocation;
	public Image image;

	public TexturePack(String name, String fileLocation){
		this.name = name;
		this.fileLocation = fileLocation;
	}

	public void loadImage(){
		image = MainFile.game.imageLoader.getImage(this, null, "packImage_" + name);
	}

	@Override
	public boolean equals( Object o ) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof TexturePack)) {
			return false;
		}

		TexturePack that = (TexturePack) o;

		if (name != null ? !name.equals(that.name) : that.name != null) {
			return false;
		}
		if (fileLocation != null ? !fileLocation.equals(that.fileLocation) : that.fileLocation != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (fileLocation != null ? fileLocation.hashCode() : 0);
		return result;
	}


	@Override
	public String toString() {
		return "TexturePack{" +
				"name='" + name + '\'' +
				", fileLocation='" + fileLocation + '\'' +
				'}';
	}
}
