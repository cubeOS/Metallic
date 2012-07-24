package net.cubeos.metallic;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;

import javax.swing.*;

public class MetallicGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2239551627744634009L;
	private JTabbedPane tabs;
	
	public MetallicGUI()
	{
		super("Metallic");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
		
		tabs = new JTabbedPane();
		
		MenuBar menu = new MenuBar();
		
		Menu fileMenu = new Menu();
		fileMenu.setLabel("File");
		MenuItem openButton = new MenuItem("Open");
		openButton.setShortcut(new MenuShortcut(KeyEvent.VK_O));
		
		openButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser("Open");
			int returnVal = chooser.showOpenDialog(getContentPane());
			if (returnVal==JFileChooser.APPROVE_OPTION)
			{
				tabs.addTab(chooser.getSelectedFile().getName(), openTab(chooser.getSelectedFile()));
			}
		}});
		fileMenu.add(openButton);
		
		
		MenuItem saveButton = new MenuItem("Save as...");
		saveButton.setShortcut(new MenuShortcut(KeyEvent.VK_S,true));
		
		saveButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser("Save as...");
			int returnVal = chooser.showSaveDialog(getContentPane());
			if (returnVal==JFileChooser.APPROVE_OPTION) System.out.print("User saved as " + chooser.getSelectedFile().getName());
		}});
		fileMenu.add(saveButton);
		
		menu.add(fileMenu);
		add(tabs);
		setMenuBar(menu);	
		setVisible(true);
	}
	
	public int getCurrentTab()
	{
		return tabs.getSelectedIndex();
	}
	
	public JPanel openTab(File f)
	{
		if (f.isFile() && f.canRead())
		{
			try
			{
				String buffer = "";
				FileReader fr = new FileReader(f);
				while (true)
				{
					int next = fr.read();
					if (next==-1) break;
					buffer += (char)next;
				}	//this can't be efficient...
				JPanel tabContent = new JPanel();
				JEditorPane editor = new JEditorPane();
				editor.setText(buffer);
				tabContent.add(editor);
				tabs.add(f.getName(),tabContent);
				return tabContent;
			} catch (FileNotFoundException e)
			{
				return null;
			} catch (IOException e) {
				return null;
			}
		} else return null;
	}
}
