package net.cubeos.metallic;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class MetallicGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2239551627744634009L;
	
	public MetallicGUI()
	{
		super("Metallic");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		java.awt.Container content = getContentPane();
		content.add(new JEditorPane());
		setExtendedState(MAXIMIZED_BOTH);
		
		MenuBar menu = new MenuBar();
		
		Menu fileMenu = new Menu();
		fileMenu.setLabel("File");
		MenuItem openButton = new MenuItem("Open");
		openButton.setShortcut(new MenuShortcut(KeyEvent.VK_O));
		fileMenu.add(openButton);
		
		MenuItem saveButton = new MenuItem("Save");
		saveButton.setShortcut(new MenuShortcut(KeyEvent.VK_S));
		/*saveButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser("Open...");
			int returnVal = chooser.showOpenDialog();
		}});*/
		fileMenu.add(saveButton);
		
		menu.add(fileMenu);
		
		setMenuBar(menu);
		pack();
		setVisible(true);
	}
}
