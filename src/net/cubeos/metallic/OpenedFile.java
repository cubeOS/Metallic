package net.cubeos.metallic;

public class OpenedFile
{
	private String path;
	private int index;
	public OpenedFile(String path, int index)
	{
		this.path = path;
		this.index = index;
	}
	//will hold attributes like last modified, opened, etc.
}
