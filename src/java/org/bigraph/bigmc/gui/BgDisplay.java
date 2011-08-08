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
import java.awt.geom.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

import java.net.URL;

import java.io.*;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.ArrayList;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.SyntaxDocument;

import com.apple.eawt.Application;

abstract class BgShape {
	public double x;
	public double y;
	public double w;
	public double h;

	public static HashMap<String,Point2D.Double> names;
	
	LinkedList<BgShape> children;

	public static double PADDING = 30.0;
	
	public BgShape() {
		x = 0.0;
		y = 0.0;
		w = 0.0;
		h = 0.0;
		children = new LinkedList<BgShape>();
	}

	public void drawText(Graphics2D g, String s, double x, double y) {
		FontMetrics metrics = g.getFontMetrics(g.getFont());
   		int hgt = metrics.getHeight();
		int adv = (int)(metrics.stringWidth(s) / 2.0);
		
		g.drawString(s, (int)x + 7, (int)y + hgt + 2);
	}

	public abstract void draw(Graphics2D g, double xOff, double yOff);

	public abstract void layout(Graphics2D g);

	public void addChild(BgShape b) {
		children.add(b);
	}
}

class BgRegion extends BgShape {
	int mIndex;

	public BgRegion(int index) {
		super();
		mIndex = index;

	}

	public void draw(Graphics2D g, double xOff, double yOff) {
		final float dash1[] = {5.0f};
		final BasicStroke dashed = new BasicStroke(1.0f,
                                          BasicStroke.CAP_BUTT,
                                          BasicStroke.JOIN_MITER,
                                          10.0f, dash1, 0.0f);
		g.setStroke(dashed);
		g.setPaint(Color.black);

		g.draw(new RoundRectangle2D.Double(x + xOff, y + yOff,
                                   w,
                                   h,
                                   10, 10));
	

		drawText(g,"" + mIndex,x + xOff,y + yOff);

		
		double drX = PADDING;
		double drY = PADDING;

		for(BgShape c : children) {
			c.draw(g, drX + xOff, drY + yOff);
			drX += PADDING + c.w;
		}

		for(String s : BgShape.names.keySet()) {
			g.drawString(s,(int)(BgShape.names.get(s).getX() + xOff),(int)BgShape.names.get(s).getY());
		}
		
	}

	public void layout(Graphics2D g) {
		double maxX = 0.0;
		double maxY = 0.0;
		double newW = PADDING;
		double newH = 0.0;		

		for(BgShape c : children) {
			c.layout(g);

			if(c.h + 2*PADDING > newH) newH = c.h + 2*PADDING;
			newW += c.w + PADDING;

		}

		w = newW;
		h = newH;

		if(w < 50) w = 50;
		if(h < 50) h = 50;
		System.out.println("BgRegion layout: " + w + " / " + h);

		for(String s : BgShape.names.keySet()) {
			System.out.println("Name: " + s);
		}

		// Once we've established the size of the region, we layout the names.

		if(BgShape.names.size() == 0) return;


		int nm = BgShape.names.size() - 1;

		if(nm == 0) nm = 1;

		double interval = (w - 2*PADDING) / nm;
		int i = 0;

		for(String s : BgShape.names.keySet()) {
			names.put(s, new Point2D.Double(PADDING + interval * i++, 20.0));
		}
	}
}

class BgNode extends BgShape {
	String mControl;
	ArrayList<String> ports;

	public BgNode(String control) {
		super();
		mControl = control;
		ports = new ArrayList<String>();
	}

	public void draw(Graphics2D g, double xOff, double yOff) {
		final float dash1[] = {};
		final BasicStroke dashed = new BasicStroke(1.0f,
                                          BasicStroke.CAP_BUTT,
                                          BasicStroke.JOIN_MITER);
		g.setStroke(dashed);
		g.setPaint(Color.black);

		g.draw(new RoundRectangle2D.Double(x + xOff, y + yOff,
                                   w,
                                   h,
                                   10, 10));
	

		drawText(g,mControl,x + xOff,y + yOff);
		
		double drX = PADDING;
		double drY = PADDING;

		for(BgShape c : children) {
			c.draw(g, drX + xOff, drY + yOff);
			drX += PADDING + c.w;
		}

		if(ports.size() == 0) return;

		int sz = ports.size() - 1;

		if(sz == 0) sz = 1;

		double interval = (w - PADDING) / sz;
		int i = 0;
		double px = PADDING / 2.0;
		for(String s : ports) {
			if(s == "") {
				g.draw(new Ellipse2D.Double(px+xOff-2, y+yOff-2, 5, 5));
			
				px += interval;
				
				continue;
			}

			Point2D targ = BgShape.names.get(s);
			if(targ == null) continue;

			double ctrlX = ((px + xOff) + (targ.getX() + PADDING)) / 2.0;

			QuadCurve2D q = new QuadCurve2D.Double();
			q.setCurve(px + xOff, y + yOff, ctrlX, (y + yOff) - 25.0, 
				targ.getX() + PADDING, 
				targ.getY() + 5);
			g.draw(q);
			px += interval;
		}
		
	}

	public void layout(Graphics2D g) {
		double maxX = 0.0;
		double maxY = 0.0;
		double newW = PADDING;
		double newH = 0.0;		

		for(BgShape c : children) {
			c.layout(g);

			if(c.h + 2*PADDING > newH) newH = c.h + 2*PADDING;
			newW += c.w + PADDING;
		}

		FontMetrics metrics = g.getFontMetrics(g.getFont());
   		int hgt = metrics.getHeight();
		int adv = (int)(metrics.stringWidth(mControl));

		w = newW;
		h = newH;

		if(w < adv + PADDING*2) w = adv+PADDING;
		if(h < 50) h = w;
		System.out.println("BgNode " + mControl + " layout: " + w + " / " + h);
	}

	public void linkPort(String p) {
		ports.add(p);
	}
}

class BgParser {
	String s;
	int index;

	public BgParser(String inp) {
		s = inp;
		index = 0;
	}

	public char peek() {
		if(index < s.length()) return s.charAt(index);

		return 0;
	}

	public void consume() {
		char c = peek();

		index++;
	}

	public boolean startsWith(String t) {
		return s.startsWith(t,index);
	}

	public void consume(String t) {
		if(startsWith(t)) index += t.length();

		else throw new RuntimeException("BgParser: Expected '" + t + "'");
	}

	public void ws() {
		char c = peek();

		if(c == 0) return;

		while(c == ' ' || c == '\t' || c == '\n') {
			consume(); 
			c = peek();
		}
	}

	public boolean isId() {
		char c = peek();

		return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
	}

	public boolean isDigit() {
		char c = peek();

		return (c >= '0' && c <= '9');
	}

	public String getId() {
		if(!isId())
			throw new RuntimeException("Expected name!");

		String n = "";
		n += Character.toString(peek());
		consume();

		while(isId() || isDigit()) {
			n += Character.toString(peek());
			consume();
		}

		System.out.println("getId(): " + n);

		return n;
	}

	public void links(BgNode b) {
		ws();

		if(peek() == ']') return;

		if(peek() == '-') {
			consume();
			b.linkPort("");
		}

		if(isId()) {
			String i = getId();
			b.linkPort(i);
			BgShape.names.put(i,new Point2D.Double(0.0,0.0));
		}

		ws();

		if(peek() == ',') {
			consume();
			links(b);
		}

		return;
	}

	public LinkedList<BgShape> exp() {
		LinkedList<BgShape> res;
		ws();

		if(peek() == '(') {
			consume();
			res = exp();
			ws();
			consume(")");
			return res;
		}

		LinkedList<BgShape> n = exp_el();

		ws();

		if(peek() == 0) return n;

		if(peek() == '|') {
			consume();
			res = exp();
			n.addAll(res);
			return n;
		}

		throw new RuntimeException("Unexpected expression at position " + index);
	}

	public LinkedList<BgShape> exp_el() {
		LinkedList<BgShape> res;
	
		System.out.println("exp_el: " + s.substring(index));

		ws();


		res = new LinkedList<BgShape>();

		
		if(isId()) {
			if(startsWith("nil")) { 
				consume("nil");
				return res;
			}

			String id = getId();
			BgNode b = new BgNode(id);
			res.add(b);
			ws();
			if(peek() == '[') {
				consume();
				links(b);
				ws();
				consume("]");
			}
			ws();

			if(peek() == '.') {
				consume();
				LinkedList<BgShape> ch = exp_el();
				for(BgShape cc : ch)
					b.addChild(cc);

				return res;
			}


		}

		if(peek() == 0) return res;

		throw new RuntimeException("Unknown expression at position " + index);
	}

	LinkedList<BgShape> parse() {
		return exp();
	}
}

public class BgDisplay extends JPanel {
	private LinkedList<BgShape> shapes;
	private BigMcApp frame;

	public BgDisplay(BigMcApp app, String bgstring) {
		super(true);

		frame = app;

		BgShape.names = new HashMap<String,Point2D.Double>();
		shapes = new LinkedList<BgShape>();

		BgParser bgp = new BgParser(bgstring);

		LinkedList<BgShape> n = bgp.parse();

		/*BgShape.names.put("a", new Point2D.Double(0,0));
		BgShape.names.put("b", new Point2D.Double(0,0));
		BgShape.names.put("c", new Point2D.Double(0,0));*/

		BgRegion r = new BgRegion(0);

		for(BgShape nn : n)
			r.addChild(nn);

		shapes.add(r);

		setPreferredSize(new Dimension(500,500));
	}

	public void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;

		g.setPaint(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());

		for(BgShape s : shapes) {
			s.layout(g);
			s.draw(g, 25.0, 100.0);
			System.out.println("Drawing g: " + s);
		}
	}	
}


