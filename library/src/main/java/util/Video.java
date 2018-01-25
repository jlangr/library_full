package util;

public class Video implements Media {

	private String filename;

	public Video(String filename) {
		this.filename = filename;
	}
	
	public Video() {
		this.filename = "defaultFilename";
	}

	public String getFilename() { return filename; }

}
