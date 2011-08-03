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

import com.apple.eawt.Application;

public class BigMcMenu extends JMenuBar {
	BigMcApp window;

	public BigMcMenu(BigMcApp win) {
		window = win;
		addMenus(this);
	}

	protected void addMenus(JMenuBar menuBar) {
		JMenu m = null;
		JMenuItem i = null;

		int mod = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		// FILE MENU

		m = new JMenu("File");
		menuBar.add(m);

		i = new JMenuItem("New"); m.add(i);
		i.addActionListener(new McEvent.NewEvent(window));

		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,mod));
		i = new JMenuItem("Open"); m.add(i);
		i.addActionListener(new McEvent.OpenEvent(window));
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,mod));
		m.addSeparator();
		i = new JMenuItem("Save"); m.add(i);
		i.addActionListener(new McEvent.SaveEvent(window));
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,mod));
		i = new JMenuItem("Save As..."); m.add(i);
		i.addActionListener(new McEvent.SaveAsEvent(window));
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,mod | InputEvent.SHIFT_DOWN_MASK));
		m.addSeparator();
		i = new JMenuItem("Page Setup"); m.add(i);
		i = new JMenuItem("Print"); m.add(i);
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,mod));

		// EDIT MENU

		m = new JMenu("Edit");
		menuBar.add(m);

		i = new JMenuItem("Undo"); m.add(i);
		i.addActionListener(new McEvent.UndoEvent(window));
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,mod));

		i = new JMenuItem("Redo"); m.add(i);
		i.addActionListener(new McEvent.RedoEvent(window));
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,mod | InputEvent.SHIFT_DOWN_MASK));
		m.addSeparator();

		i = new JMenuItem("Cut"); m.add(i);
		i.addActionListener(new McEvent.CutEvent(window));
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,mod));

		i = new JMenuItem("Copy"); m.add(i);
		i.addActionListener(new McEvent.CopyEvent(window));
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,mod));
	
		i = new JMenuItem("Paste"); m.add(i);
		i.addActionListener(new McEvent.PasteEvent(window));
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,mod));

		i = new JMenuItem("Select All"); m.add(i);
		i.addActionListener(new McEvent.SelectAllEvent(window));
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,mod));

		// RUN MENU

		m = new JMenu("Run");
		menuBar.add(m);
		i = new JCheckBoxMenuItem("Local Checking Mode"); m.add(i);
		
		i = new JMenuItem("Run Check..."); m.add(i);
		i.addActionListener(new McEvent.RunEvent(window));
		i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,mod));

	}
}

