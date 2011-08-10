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
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;
import java.io.IOException;

import jsyntaxpane.SyntaxDocument;

import java.net.URL;

class McHandler {
	public BigMcApp frame;

	public McHandler(BigMcApp f) {
		frame = f;
	}
}

public class McEvent {

	static public class NewEvent extends McHandler implements ActionListener {
		public NewEvent(BigMcApp f) { super(f); }

		public void actionPerformed(ActionEvent e) {
			if(frame.getDirty()) {
				if(!frame.confirmDiscard()) return;
			}

			frame.newDocument();
		}
	}

	static public class OpenEvent extends McHandler implements ActionListener {
		public OpenEvent(BigMcApp f) { super(f); }

		public void actionPerformed(ActionEvent e) {
			if(frame.getDirty()) {
				if(!frame.confirmDiscard()) return;
			}

			frame.newDocument();

			FileDialog f = new FileDialog(frame,"Open Model File");

			f.show();

			if(f.getFile() == null) return;

			System.out.println("File selected: " + f.getDirectory() + f.getFile());
			frame.loadFile(new File(f.getDirectory() + f.getFile()));
		}
	}
	static public class SaveEvent extends McHandler implements ActionListener {
		public SaveEvent(BigMcApp f) { super(f); }

		public void actionPerformed(ActionEvent e) {
			if(frame.getFileName() == null) {
				(new SaveAsEvent(frame)).actionPerformed(e);
				return;
			}

			frame.saveFile(frame.getFileName());
		}
	}

	static public class SaveAsEvent extends McHandler implements ActionListener {
		public SaveAsEvent(BigMcApp f) { super(f); }

		public void actionPerformed(ActionEvent e) {
			FileDialog f = new FileDialog(frame,"Save Model File As...",FileDialog.SAVE);

			f.show();

			if(f.getFile() == null) return;

			if((new File(f.getFile())).exists()) {
				int n = JOptionPane.showConfirmDialog(frame,
    				"That file already exists.  Would you like to overwrite it?",
				"File Exists",
    				JOptionPane.YES_NO_OPTION);

				if(n == JOptionPane.NO_OPTION) {
					actionPerformed(e);
					return;
				}
			}

			System.out.println("File selected: " + f.getDirectory() + f.getFile());
			frame.setFileName(new File(f.getDirectory() + f.getFile()));
			frame.saveFile(frame.getFileName());
		}
	}

	static public class UndoEvent extends McHandler implements ActionListener {
		public UndoEvent(BigMcApp f) { super(f); }
		public void actionPerformed(ActionEvent e) {
			frame.undo();
		}
	}

	static public class RedoEvent extends McHandler implements ActionListener {
		public RedoEvent(BigMcApp f) { super(f); }

		public void actionPerformed(ActionEvent e) {
			frame.redo();
		}
	}


	static public class CutEvent extends McHandler implements ActionListener {
		public CutEvent(BigMcApp f) { super(f); }

		public void actionPerformed(ActionEvent e) {

		}
	}

	static public class CopyEvent extends McHandler implements ActionListener {
		public CopyEvent(BigMcApp f) { super(f); }

		public void actionPerformed(ActionEvent e) {
			
		}
	}

	static public class PasteEvent extends McHandler implements ActionListener {
		public PasteEvent(BigMcApp f) { super(f); }

		public void actionPerformed(ActionEvent e) {

		}
	}

	static public class SelectAllEvent extends McHandler implements ActionListener {
		public SelectAllEvent(BigMcApp f) { super(f); }

		public void actionPerformed(ActionEvent e) {

		}
	}


	static public class VisualiseEvent extends McHandler implements ActionListener {
		public VisualiseEvent(BigMcApp f) { super(f); }

		public void actionPerformed(ActionEvent e) {
			frame.visualise(null);
		}

	}

	static public class RunEvent extends McHandler implements ActionListener {
		public RunEvent(BigMcApp f) { super(f); }

		public void actionPerformed(ActionEvent e) {
			frame.resetProgress();

			if(!frame.showRunOpts()) return;
		}

	}

	static public class ManualEvent extends McHandler implements ActionListener {
		public ManualEvent(BigMcApp f) { super(f); }

		public void actionPerformed(ActionEvent e) {
			String url = "http://bigraph.org/bigmc/manual";
			try {
				java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
			} catch (IOException error) {
				JOptionPane.showMessageDialog(frame, "Error launching browser: " + error.getMessage());
			}
		}
	}

	static public class EditorEvent extends McHandler implements DocumentListener {
		public EditorEvent(BigMcApp f) { super(f); }

		public void changedUpdate(DocumentEvent ev) {
			frame.setDirty(true);
		}
 
		public void insertUpdate(DocumentEvent ev) {
			frame.setDirty(true);
		}
 
		public void removeUpdate(DocumentEvent ev) {
			frame.setDirty(true);
		}
	}

	static public class CloseEvent extends WindowAdapter implements ActionListener {
		BigMcApp frame;

		public CloseEvent(BigMcApp f) { super(); frame = f; }

		public void windowClosing(WindowEvent e) {
			if(frame.getDirty()) {
				if(!frame.confirmDiscard()) return;
			}

        		System.exit(0);
      		}

		public void actionPerformed(ActionEvent e) {
			windowClosing(null);
		}
	}

	static public class ExampleEvent extends McHandler implements ActionListener {
		public ExampleEvent(BigMcApp f) { super(f); }

		public void actionPerformed(ActionEvent e) {
			if(frame.getDirty()) {
				if(!frame.confirmDiscard()) return;
			}

			frame.newDocument();

			File f = new File(BigMcApp.BIGMC_HOME + "/doc/examples/" + ((JMenuItem)e.getSource()).getText());

			frame.loadFile(f);

		}
	}
}
