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

import java.io.*;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.SyntaxDocument;

import com.apple.eawt.Application;

public class BigMcApp extends JFrame {
	final JEditorPane codeEditor;
	JScrollPane scrPane;
	JPanel appPanel;
	JMenuBar menuBar;
	JTextArea console;
	JScrollPane consoleScr;
	JSplitPane splitPane;
	public static String BIGMC_HOME;
	boolean modified;
	McToolbar toolBar;
	RunOpts runOpts;
	boolean isDirty;
	File fileName;

	public BigMcApp() {
		super("Untitled - BigMC");

		fileName = null;

		setPreferredSize(new Dimension(800, 600));

		menuBar = new BigMcMenu(this);
		appPanel = new JPanel(new BorderLayout()); 
		codeEditor = new JEditorPane();
		codeEditor.setFont(new Font("Monospaced",Font.PLAIN,13));
		scrPane = new JScrollPane(codeEditor);
		console = new JTextArea();
		console.setEditable(false);
		console.setFont(new Font("Monospaced",Font.PLAIN,11));
		console.setText("Welcome to BigMC!\n");
		console.setLineWrap(true);

		toolBar = new McToolbar(this);
		
		consoleScr = new JScrollPane(console);

		// Mac things to make it look better
		scrPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		consoleScr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		consoleScr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrPane, consoleScr);
		splitPane.setDividerLocation(450);
	
		appPanel.add(splitPane, BorderLayout.CENTER);

        	DefaultSyntaxKit.initKit();

		codeEditor.setContentType("text/bgm");
		codeEditor.setText("");

		codeEditor.getDocument().addDocumentListener(new McEvent.EditorEvent(this));

		isDirty = false;

		setJMenuBar(menuBar);

		final Container c = getContentPane();
		c.setLayout(new BorderLayout());

		c.add(toolBar, BorderLayout.PAGE_START);

		c.add(appPanel, BorderLayout.CENTER);

		c.doLayout();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	
		runOpts = new RunOpts(this);
	
		setLocationRelativeTo(null);
	}

	public void loadFile(File fp) {
		if(!fp.canRead()) {
			JOptionPane.showMessageDialog(this, "Error reading from file: " + fp);
			return;
		}

		try {
			codeEditor.read(new FileReader(fp), fp.getAbsolutePath());
			codeEditor.getDocument().addDocumentListener(new McEvent.EditorEvent(this));
			fileName = fp;
			setDirty(true);
			setDirty(false);
		} catch (java.io.IOException e) {
			JOptionPane.showMessageDialog(this, "Error reading from file: " + fp);
		}
	}
	
	public void saveFile(File fp) {
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(fp));
			String s = codeEditor.getText();
			w.write(s);
			w.close();
			fileName = fp;
			setDirty(false);
		} catch(java.io.IOException e) {
			JOptionPane.showMessageDialog(this, "Error writing to file: " + fp);
		}
	}

	public void newDocument() {
		codeEditor.setText("");
		fileName = null;
		setDirty(true);
		setDirty(false);
	}

	public void runModel(int maxSteps, int reportSteps, boolean local, boolean printmode, boolean verbose) {
		final File tmp;

		try {
			tmp = File.createTempFile("bigmc_model",".bgm");

		} catch(java.io.IOException e) {
			JOptionPane.showMessageDialog(this, "Error creating temporary files");
			return;
		}
		System.out.println("Tmp file: " + tmp.getAbsolutePath());
		saveFile(tmp);
		
		String cmdLine = BIGMC_HOME + "/bin/bigmc";
	
		toolBar.setMaxSteps(maxSteps);

		cmdLine += " -m " + maxSteps;
		cmdLine += " -r " + reportSteps;

		if(local) cmdLine += " -l";
		if(printmode) cmdLine += " -p";
		if(verbose) cmdLine += " -V";

		cmdLine += " " + tmp.getAbsolutePath();
		System.err.println("CmdLine:" + cmdLine);

		final String cl = cmdLine;

		console.append("> " + cmdLine + "\n");
		console.setCaretPosition(console.getDocument().getLength());

		(new Thread() {

			public void run() {

				try {
					String[] envp = {"BIGMC_HOME="+BIGMC_HOME};
					final Process process = Runtime.getRuntime().exec(cl, envp);

					System.out.println("Process call has returned");

					InputStream os = process.getInputStream();
					final BufferedReader br = new BufferedReader(new InputStreamReader(os));
					System.out.println("Got a reader");


					String c;
					while((c = br.readLine()) != null) {
						console.append(c + "\n");
						console.setCaretPosition(console.getDocument().getLength());
						if(c.startsWith("[mc::report]")) {
							String prog[] = c.split(" @ ");
							int pr = Integer.parseInt(prog[1]);
							toolBar.setProgress(pr);
						}
					}
			
					System.out.println("Waitfor");

					process.waitFor();

					toolBar.setComplete("Checker process terminated");

					System.out.println("Process terminated: " + process.exitValue());

					tmp.delete();

				} catch (Exception e) {
					System.err.println("There was a problem starting the BigMC checker:\n" +
						e.getMessage());
				}
			}
		}).start();
	}

	public void undo() {
		((SyntaxDocument)codeEditor.getDocument()).doUndo();
	}

	public void redo() {
		((SyntaxDocument)codeEditor.getDocument()).doRedo();
	}

	public void resetProgress() {
		toolBar.resetProgress();
	}

	public boolean showRunOpts() {
		runOpts.successful = false;

		runOpts.show();

		if(!runOpts.successful) return false;

		runModel(runOpts.rMaxSteps, 
			runOpts.rReportSteps, 
			runOpts.rLocal, 
			runOpts.rPrint, 
			runOpts.rVerbose);

		return true;

	}

	public void setDirty(boolean dirty) {
		if(!isDirty && dirty) {
			setTitle(((fileName != null) ? fileName.getName() : "Untitled") + "* - BigMC");
		}

		if(isDirty && !dirty) {
			setTitle(((fileName != null) ? fileName.getName() : "Untitled") + " - BigMC");
		}
		
		isDirty = dirty;
	}

	public boolean getDirty() {
		return isDirty;
	}

	public boolean confirmDiscard() {
		int n = JOptionPane.showConfirmDialog(this,
    		"The current document has been modified.  Would you like to save it?",
		"Confirm",
    		JOptionPane.YES_NO_CANCEL_OPTION);
	

		if(n == JOptionPane.YES_OPTION) {
			// Do save, then proceed

			(new McEvent.SaveEvent(this)).actionPerformed(null);

			return true;
		}

		if(n == JOptionPane.NO_OPTION) {
			// Discard current text, then proceed

			return true;
		}

		return false;
	}

	public File getFileName() {
		return fileName;
	}

	public static void main(String[] args) {
		/*try {
			URL url = BigMcApp.class.getResource("/bigmc-small.png");
			Toolkit kit = Toolkit.getDefaultToolkit();
			Image img = kit.createImage(url);
			Application.getApplication().setDockIconImage(img);
		} catch (Exception e) {

		}*/

		if(args.length >= 1) {
			System.out.println("Setting BIGMC_HOME to " + args[0]);
			BIGMC_HOME = args[0];
		} else {
			// Try the default
			BIGMC_HOME = "/usr/local/bigmc";

			String os = System.getProperty("os.name").toLowerCase();
			if(os.indexOf( "win" ) >= 0) {
				BIGMC_HOME="C:\\Progra~1\\BigMC";
			}
		}

		

		JFrame frame = new BigMcApp();

		frame.setVisible(true);
	}
}


