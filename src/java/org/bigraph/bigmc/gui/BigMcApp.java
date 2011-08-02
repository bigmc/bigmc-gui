/*******************************************************************************
*
* Copyright (C) 2011 Gian Perrone (http://itu.dk/~gdpe)
* 
* BigMC - A bigraphical model checker (http://bigraph.org/bigmc).
* 
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307,
* USA.
*********************************************************************************/

package org.bigraph.bigmc.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

import java.net.URL;

import jsyntaxpane.DefaultSyntaxKit;

public class BigMcApp extends JFrame implements ActionListener {
	final JEditorPane codeEditor;
	JScrollPane scrPane;
	JPanel appPanel;
	JMenuBar menuBar;
	JTextArea console;
	JScrollPane consoleScr;
	JSplitPane splitPane;

	public BigMcApp() {
		super("Untitled - BigMC");

		setPreferredSize(new Dimension(800, 600));

		menuBar = new JMenuBar();
		appPanel = new JPanel(new BorderLayout()); 
		codeEditor = new JEditorPane();
		scrPane = new JScrollPane(codeEditor);
		console = new JTextArea();
		console.setEditable(false);
		console.setFont(new Font("Monospaced",Font.BOLD,11));
		console.setText("Welcome to BigMC!");
		consoleScr = new JScrollPane(console);

		// Mac things to make it look better
		scrPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		consoleScr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		consoleScr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		addMenus(menuBar);

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrPane, consoleScr);
		splitPane.setDividerLocation(450);
	
		appPanel.add(splitPane, BorderLayout.CENTER);

        	DefaultSyntaxKit.initKit();

		codeEditor.setContentType("text/bgm");
		codeEditor.setText("\n\n\n%check;\n");

		setJMenuBar(menuBar);

		final Container c = getContentPane();
		c.setLayout(new BorderLayout());

		c.add(appPanel, BorderLayout.CENTER);

		c.doLayout();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}

	protected void addMenus(JMenuBar menuBar) {
		JMenu m = null;
		JMenuItem i = null;

		int mod = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		m = new JMenu("File");
		menuBar.add(m);

		i = new JMenuItem("New"); m.add(i);
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,mod));
		i = new JMenuItem("Open"); m.add(i);
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,mod));
		m.addSeparator();
		i = new JMenuItem("Save"); m.add(i);
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,mod));
		i = new JMenuItem("Save As..."); m.add(i);
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,mod | InputEvent.SHIFT_DOWN_MASK));
		m.addSeparator();
		i = new JMenuItem("Page Setup"); m.add(i);
		i = new JMenuItem("Print"); m.add(i);
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,mod));

		m = new JMenu("Run");
		menuBar.add(m);
		i = new JCheckBoxMenuItem("Local Checking Mode"); m.add(i);
		
		JMenu submenu = new JMenu("Maximum steps");
		ButtonGroup group = new ButtonGroup();

		JMenuItem n = new JRadioButtonMenuItem("10"); group.add(n); 
		n = new JRadioButtonMenuItem("50"); group.add(n); 
		n = new JRadioButtonMenuItem("100"); group.add(n); 
		n = new JRadioButtonMenuItem("500"); group.add(n); 
		n = new JRadioButtonMenuItem("1000"); group.add(n); n.setSelected(true); 
		n = new JRadioButtonMenuItem("2000"); group.add(n); 
		n = new JRadioButtonMenuItem("5000"); group.add(n); 
		n = new JRadioButtonMenuItem("10000"); group.add(n); 
		n = new JRadioButtonMenuItem("25000"); group.add(n); 
		n = new JRadioButtonMenuItem("50000"); group.add(n); 
		n = new JRadioButtonMenuItem("100000"); group.add(n);
		n = new JRadioButtonMenuItem("500000"); group.add(n); 
		n = new JRadioButtonMenuItem("1000000"); group.add(n); 
		n = new JRadioButtonMenuItem("1500000"); group.add(n);
		m.add(submenu);
		
		i = new JMenuItem("Run Check"); m.add(i);
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,mod));

	}

	public void actionPerformed(ActionEvent e) {

	}

	public static void main(String[] args) {
		JFrame frame = new BigMcApp();

		frame.setVisible(true);
	}
}


