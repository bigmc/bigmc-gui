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
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

import java.net.URL;

import java.io.*;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.SyntaxDocument;

import com.apple.eawt.Application;

public class McVisualiser extends JFrame implements ActionListener {
	BigMcApp frame;
	String text;
	BgDisplay disp;
	JButton but;
	JTextArea termEntry;

	public McVisualiser(BigMcApp f, String s) {
		super("Bigraph Visualisation");
		frame = f;
		text = s;

		JPanel p = new JPanel(new BorderLayout());

		getContentPane().add(p);

		disp = new BgDisplay(this);

		p.add(new JScrollPane(disp), BorderLayout.CENTER);
		JPanel entryPanel = new JPanel(new BorderLayout());
		termEntry = new JTextArea();

		if(s != null) {
			termEntry.setText(s);
			disp.display(s);
		}

		termEntry.setPreferredSize(new Dimension(450,100));
		entryPanel.add(termEntry, BorderLayout.CENTER);
		but = new JButton("Display");
		entryPanel.add(but, BorderLayout.PAGE_END);

		but.addActionListener(this);

		entryPanel.setPreferredSize(new Dimension(450,100));

		entryPanel.setBorder(new EmptyBorder(10, 10, 10, 10) );

		p.add(entryPanel, BorderLayout.PAGE_END);

		setPreferredSize(new Dimension(550,550));

		pack();
	}

	public void actionPerformed(ActionEvent e) {
		disp.display(termEntry.getText());
	}
	

}

