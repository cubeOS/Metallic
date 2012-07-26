package net.cubeos.metallic;

import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.text.*;

import net.cubeos.metallic.FileInfo.FileModifiedException;

public class MetallicGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2239551627744634009L;
	private JTabbedPane tabs;
	private ArrayList<FileInfo> fileByIndex;
	private ArrayList<JEditorPane> editorWindows;
	
	public MetallicGUI()
	{
		super("Metallic");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
		fileByIndex = new ArrayList<FileInfo>();
		tabs = new JTabbedPane();
		editorWindows = new ArrayList<JEditorPane>();
		
		MenuBar menu = new MenuBar();
		
		Menu fileMenu = new Menu();
		fileMenu.setLabel("File");
		
		MenuItem newButton = new MenuItem("New");
		newButton.setShortcut(new MenuShortcut(KeyEvent.VK_N));
		newButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser("New file...");
			int returnVal = chooser.showSaveDialog(getContentPane());
			if (returnVal==JFileChooser.APPROVE_OPTION)
			{
				JPanel newTab = openTab(chooser.getSelectedFile());
			}
		}});
		fileMenu.add(newButton);
		
		MenuItem openButton = new MenuItem("Open...");
		openButton.setShortcut(new MenuShortcut(KeyEvent.VK_O));
		
		openButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
			doOpenFile();
		}});
		fileMenu.add(openButton);
		
		fileMenu.addSeparator();
		
		MenuItem saveButton = new MenuItem("Save");
		saveButton.setShortcut(new MenuShortcut(KeyEvent.VK_S));	//TODO add save handler
		
		saveButton.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {
			doSaveFile();
		}});
		fileMenu.add(saveButton);
		
		MenuItem saveAsButton = new MenuItem("Save as...");
		saveAsButton.setShortcut(new MenuShortcut(KeyEvent.VK_S,true));
		
		saveAsButton.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser("Save as...");
			int returnVal = chooser.showSaveDialog(getContentPane());
			if (returnVal==JFileChooser.APPROVE_OPTION) System.out.print("User saved as " + chooser.getSelectedFile().getName());
		}});
		fileMenu.add(saveAsButton);
		
		menu.add(fileMenu);
		add(tabs);
		setMenuBar(menu);	
		setVisible(true);
	}
	
	private void doOpenFile()
	{
		JFileChooser chooser = new JFileChooser("Open");
		int returnVal = chooser.showOpenDialog(getContentPane());
		if (returnVal==JFileChooser.APPROVE_OPTION)
		{
			JPanel newTab = openTab(chooser.getSelectedFile());
			if (newTab != null) tabs.addTab(chooser.getSelectedFile().getName(), newTab);
			else JOptionPane.showMessageDialog(getContentPane(), "Unable to open file.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void doSaveFile()
	{
		try {
			getCurrentFileInfo().save(getCurrentTextPane().getText());
		} catch(FileModifiedException fme)
		{
			int overwriteReturn = JOptionPane.showConfirmDialog(getContentPane(), "This file has been modified since you opened it. Are you sure you want to continue?", "Confirm file overwrite",JOptionPane.OK_CANCEL_OPTION);
			if (overwriteReturn==JOptionPane.OK_OPTION)
			{
				try {
					getCurrentFileInfo().forceSave(getCurrentTextPane().getText());
				} catch (SecurityException e) {
					JOptionPane.showMessageDialog(getContentPane(), "Unable to save file: Permission denied.", "Error", JOptionPane.ERROR_MESSAGE);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(getContentPane(), "Unable to open file.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (SecurityException se) {
			JOptionPane.showMessageDialog(getContentPane(), "Unable to save file: Permission denied.", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(getContentPane(), "Unable to open file.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public FileInfo getCurrentFileInfo()
	{
		return fileByIndex.get(getCurrentTabIndex());
	}
	
	public JEditorPane getCurrentTextPane()
	{
		return editorWindows.get(getCurrentTabIndex());
	}
	
	public boolean saveTab()
	{
		return saveTab(getCurrentTabIndex());
	}
	
	public boolean saveTab(int index)
	{
		return false;
	}
	
	public FileInfo getFileInfo(String filePath)
	{
		return FileManager.getFileInfo(filePath);
	}
	
	public int getCurrentTabIndex()
	{
		return tabs.getSelectedIndex();
	}
	
	public JPanel openTab(File f)
	{
		if (f.isFile() && f.canRead())
		{
			try
			{
				FileInfo opened = FileManager.loadFile(f);
				JPanel tabContent = new JPanel();
				tabContent.setName(f.getAbsolutePath());
				tabContent.setBounds(getBounds());
				JEditorPane editor = new JEditorPane();
				editor.setEditorKit(new NumberedEditorKit());
				editor.setText(opened.getContents());
				editor.setBounds(tabContent.getBounds());
				editor.validate();
				editorWindows.add(editor);
				fileByIndex.add(opened);
				tabContent.add(editor);
				tabContent.validate();
				tabs.add(f.getName(),tabContent);
				
				return tabContent;
			
			} catch (FileNotFoundException e)
			{
				return null;
			} catch (IOException e)
			{
				return null;
			}
		} else return null;
	}
}

class NumberedEditorKit extends StyledEditorKit
{
    public ViewFactory getViewFactory() {
        return new NumberedViewFactory();
    }
}

class NumberedViewFactory implements ViewFactory
{
    public View create(Element elem) {
        String kind = elem.getName();
        if (kind != null)
            if (kind.equals(AbstractDocument.ContentElementName)) {
                return new LabelView(elem);
            }
            else if (kind.equals(AbstractDocument.
                             ParagraphElementName)) {
//              return new ParagraphView(elem);
                return new NumberedParagraphView(elem);
            }
            else if (kind.equals(AbstractDocument.
                     SectionElementName)) {
                return new BoxView(elem, View.Y_AXIS);
            }
            else if (kind.equals(StyleConstants.
                     ComponentElementName)) {
                return new ComponentView(elem);
            }
            else if (kind.equals(StyleConstants.IconElementName)) {
                return new IconView(elem);
            }
        // default to text display
        return new LabelView(elem);
    }
}

class NumberedParagraphView extends ParagraphView
{
    public static short NUMBERS_WIDTH=25;

    public NumberedParagraphView(Element e) {
        super(e);
        short top = 0;
        short left = 0;
        short bottom = 0;
        short right = 0;
        this.setInsets(top, left, bottom, right);
    }

    protected void setInsets(short top, short left, short bottom,
                             short right) {super.setInsets
                             (top,(short)(left+NUMBERS_WIDTH),
                             bottom,right);
    }

    public void paintChild(Graphics g, Rectangle r, int n) {
        super.paintChild(g, r, n);
        int previousLineCount = getPreviousLineCount();
        int numberX = r.x - getLeftInset();
        int numberY = r.y + r.height - 5;
        g.drawString(Integer.toString(previousLineCount + n + 1),
                                      numberX, numberY);
    }

    public int getPreviousLineCount() {
        int lineCount = 0;
        View parent = this.getParent();
        int count = parent.getViewCount();
        for (int i = 0; i < count; i++) {
            if (parent.getView(i) == this) {
                break;
            }
            else {
                lineCount += parent.getView(i).getViewCount();
            }
        }
        return lineCount;
    }
}
