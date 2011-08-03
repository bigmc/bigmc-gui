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

import jsyntaxpane.DefaultSyntaxKit;

import com.apple.eawt.Application;

public class RunOpts extends JDialog implements ActionListener {
	BigMcApp frame;
	JPanel panel;
	JButton runButton;
	JButton cancelButton;
	JTextField maxSteps;
	JTextField reportSteps;
	JCheckBox localCheck;
	JCheckBox printMode;
	JCheckBox verboseMode;

	public int rMaxSteps;
	public int rReportSteps;
	public boolean rLocal;
	public boolean rPrint;
	public boolean rVerbose;
	public boolean successful;

	public RunOpts(BigMcApp parent) {
		super(parent,"Check Model Options", true);

		frame = parent;
	
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(20, 20, 20, 20) );

		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		JPanel p2 = new JPanel(new BorderLayout());
		JLabel l = new JLabel("Max. Steps", JLabel.TRAILING);
		p2.add(l, BorderLayout.LINE_START);
		maxSteps = new JTextField("1000", 10);
		l.setLabelFor(maxSteps);
		p2.add(maxSteps, BorderLayout.LINE_END);

		panel.add(p2);

		p2 = new JPanel(new BorderLayout());
		l = new JLabel("Reporting Frequency", JLabel.TRAILING);
		p2.add(l, BorderLayout.LINE_START);
		reportSteps = new JTextField("50", 10);
		l.setLabelFor(reportSteps);
		p2.add(reportSteps, BorderLayout.LINE_END);
		
		panel.add(p2);

		p2 = new JPanel(new BorderLayout());
		l = new JLabel("Local Checking Only", JLabel.TRAILING);
		p2.add(l, BorderLayout.LINE_START);
		localCheck = new JCheckBox();
		l.setLabelFor(localCheck);
		p2.add(localCheck, BorderLayout.LINE_END);
		
		panel.add(p2);

		p2 = new JPanel(new BorderLayout());
		l = new JLabel("Print new states", JLabel.TRAILING);
		p2.add(l, BorderLayout.LINE_START);
		printMode = new JCheckBox();
		printMode.setSelected(true);
		l.setLabelFor(printMode);
		p2.add(printMode, BorderLayout.LINE_END);
		
		panel.add(p2);

		p2 = new JPanel(new BorderLayout());
		l = new JLabel("Verbose Mode", JLabel.TRAILING);
		p2.add(l, BorderLayout.LINE_START);
		verboseMode = new JCheckBox();
		l.setLabelFor(verboseMode);
		p2.add(verboseMode, BorderLayout.LINE_END);
		
		panel.add(p2);

		p2 = new JPanel(new BorderLayout());
		l = new JLabel("Graph Output File:", JLabel.TRAILING);
		p2.add(l, BorderLayout.LINE_START);
		JTextField textField = new JTextField("", 15);
		l.setLabelFor(textField);
		p2.add(textField, BorderLayout.LINE_END);
		
		panel.add(p2);


		p2 = new JPanel(new BorderLayout());
		p2.setBorder(new EmptyBorder(10, 10, 10, 10) );

		runButton = new JButton("Run Now!");
		runButton.addActionListener(this);

		JButton cancelBut = new JButton("Cancel");
		cancelBut.addActionListener(this);

		p2.add(cancelBut, BorderLayout.LINE_START);
		p2.add(runButton, BorderLayout.CENTER);

		panel.add(p2);

		add(panel);

		pack();
		runButton.requestFocusInWindow();
		getRootPane().setDefaultButton(runButton);

		setLocationRelativeTo(frame);

		successful = false;
	}


	private void saveState() {
		rLocal = localCheck.isSelected();
		rPrint = printMode.isSelected();
		rVerbose = verboseMode.isSelected();
		rMaxSteps = Integer.parseInt(maxSteps.getText());
		rReportSteps = Integer.parseInt(reportSteps.getText());
		successful = true;
	}

	public void actionPerformed(ActionEvent e) {
		// We're only wired to the run button
		if(e.getSource() == runButton) {
			saveState();
		} else {
			successful = false;
		}

		setVisible(false);
	}


}
