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

public class McToolbar extends JToolBar {
	BigMcApp window;
	JButton runButton;
	JProgressBar progressBar;
	int mMax;

	public McToolbar(BigMcApp win) {
		window = win;
		addControls(this);
	}

	protected void addControls(JToolBar toolBar) {
		runButton = new JButton("Check Model...");

		runButton.addActionListener(new McEvent.RunEvent(window));

		toolBar.add(runButton);
		toolBar.addSeparator();
		progressBar = new JProgressBar(0,1000);
		toolBar.add(progressBar);
		progressBar.setStringPainted(true);
		progressBar.setString("Not Running");
	}

	public void setMaxSteps(int max) {
		mMax = max;
		progressBar.setMaximum(max);
		progressBar.setValue(0);
		progressBar.setString("(0 / " + max + ")");
	}

	public void setProgress(int value) {
		progressBar.setValue(value);
		progressBar.setString("("+value+" / " + mMax + ")");
	}

	public void setComplete(String msg) {
		progressBar.setValue(mMax);
		progressBar.setString(msg);
	}

	public void resetProgress() {
		progressBar.setMaximum(1000);
		progressBar.setValue(0);
		progressBar.setString("Not Running");
	}
}


