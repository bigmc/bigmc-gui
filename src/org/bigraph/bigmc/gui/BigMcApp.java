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
import java.awt.Container;
import java.awt.BorderLayout;
import jsyntaxpane.DefaultSyntaxKit;

public class BigMcApp {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager
				.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
		}
		
		JFrame frame = new JFrame("BigMC GUI");

		final Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());

        	DefaultSyntaxKit.initKit();

		final JEditorPane codeEditor = new JEditorPane();
		JScrollPane scrPane = new JScrollPane(codeEditor);
		c.add(scrPane, BorderLayout.CENTER);
		c.doLayout();

		codeEditor.setContentType("text/bgm");
		codeEditor.setText("public static void main(String[] args) {\n}");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}


