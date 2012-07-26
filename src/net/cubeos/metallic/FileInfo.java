package net.cubeos.metallic;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.WriteAbortedException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileInfo
{
	private File file;
	private long openedOn;
	private long lastSaved;
	private boolean isModified;
	
	public FileInfo(File f)
	{
		file = f;
		openedOn = System.currentTimeMillis();
		lastSaved = openedOn; 	
	}
	
	public boolean wasModifiedExternally()
	{
			//call getAbsoluteFile so that lastModified information is updated
		return file.getAbsoluteFile().lastModified() > lastSaved;
	}
	
	public void setModified(boolean modified)
	{
		isModified = modified;
	}
	
	public String getContents() throws IOException, SecurityException
	{
		//List<String> lines = Files.readAllLines(Files., null)
		String buffer = "";
		FileReader fr = new FileReader(file);
		while (true)
		{
			int next = fr.read();
			if (next==-1) break;
			buffer += (char)next;
		}	//this can't be the best way to do this...
		fr.close();
		return buffer;
	}
	
	public void forceSave(String contents) throws IOException, SecurityException
	{
		try {
			save(contents,true);
		} catch (FileModifiedException e)
		{
			//do nothing cause we don't care about this and it shouldn't actually happen
		}
	}
	
	public void save(String contents) throws SecurityException, IOException, FileModifiedException
	{
		save(contents,false);
	}
	
	private void save(String contents, boolean overwrite) throws IOException, SecurityException, FileModifiedException
	{
		if (wasModifiedExternally() && !overwrite) throw new FileModifiedException("File was modified by another program");
		FileWriter fw = new FileWriter(file);
		fw.write(contents);
		fw.close();
		lastSaved = file.getAbsoluteFile().lastModified();
	}
	
	public class FileModifiedException extends Exception
	{
		public FileModifiedException(String m)
		{
			super(m);
		}
	}
	
	public class FileExistsException extends Exception
	{
		public FileExistsException(String m)
		{
			super(m);
		}
	}
}
