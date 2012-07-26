package net.cubeos.metallic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

public class FileManager
{
	private static HashMap<String,FileInfo> files = new HashMap<String,FileInfo>();
	
	public static FileInfo loadFile(File f) throws FileNotFoundException,IOException,IllegalArgumentException
	{
		if (f.isFile() && f.canRead())
		{
			if (files.containsKey(f.getAbsolutePath())) return files.get(f.getAbsolutePath());
			FileInfo fileInfo = new FileInfo(f);
			files.put(f.getAbsolutePath(), fileInfo);
				
			return fileInfo;
		} else throw new IllegalArgumentException("File must not be a directory.");
	}
	
	public static FileInfo getFileInfo(String filePath)
	{
		return files.get(filePath);
	}
}
