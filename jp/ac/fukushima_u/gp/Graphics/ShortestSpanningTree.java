package jp.ac.fukushima_u.gp.Graphics;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

public class ShortestSpanningTree extends JFrame {
	
	private class Node {
		int X;
		int Y;
		int NODE_GROUP;
		
		private Node() {
		}
		
		private Node(int X, int Y, int G) {
			this.X = X;
			this.Y = Y;
			this.NODE_GROUP = G;
		}
	}
	
	private class Edge {
		Node N1;
		Node N2;
		double DISTANCE;
		
		private Edge() {
			
		}
		
		private Edge(Node n1, Node n2) {
			this.N1 = n1;
			this.N2 = n2;
			this.DISTANCE = Math.sqrt(Math.pow(n1.X - n2.X, 2) + Math.pow(n1.Y - n2.Y, 2));
		}
	}
	
	int NODE_NUM = 40;
	ArrayList<Node> NODE_SET = new ArrayList<Node>();
	ArrayList<Edge> EDGE_GROUP = new ArrayList<Edge>();
	int W = 600;
	int H = 400;
	
	public static void main() {
		ShortestSpanningTree sst = new ShortestSpanningTree();
		
	}
	
	private void initNode() {
		Random r = new Random();
		Dimension d = this.getContentPane().getSize();
		
		for (int i = 0; i < this.NODE_NUM; i++) {
			this.NODE_SET.add(new Node(r.nextInt(this.W - 30) + 15, r.nextInt(this.H - 50) + 35, i));
			
		}
	}
	
	private void allEdge() {
		for (int i = 0; i < this.NODE_NUM - 1; i++) {
			for (int l = i + 1; l < this.NODE_NUM; l++) {
				this.EDGE_GROUP.add(new Edge(this.NODE_SET.get(i), this.NODE_SET.get(l)));
			}
		}
	}
	
	private void edgeSort() {
		int EdgeNum = this.EDGE_GROUP.size();
		Edge[] ea = new Edge[EdgeNum];
		
		for (int i = 0; i < EdgeNum; i++) {
			ea[i] = EDGE_GROUP.get(i);
		}
		
		for (int i = 0; i < EdgeNum - 1; i++) {
			for (int l = i + 1; l < EdgeNum; l++) {
				if (ea[i].DISTANCE > ea[l].DISTANCE) {
					Edge e = ea[i];
					ea[i] = ea[l];
					ea[l] = e;
				}
			}
		}
		
		this.EDGE_GROUP.clear();
		
		for (int i = 0; i < EdgeNum; i++) {
			this.EDGE_GROUP.add(ea[i]);
		}
		
	}
	
	private void shortestEdgeCalc() {
		
	}
	
}
