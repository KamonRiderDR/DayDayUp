package BTree;

import BTree.BTreeNode;

public class main {

	public static void main(String[] args)
	{
		System.out.println("Testing!");
		testInsert(3);
		
	}
	
	public static void testInsert(int m)
	{
		 	BTreeNode node = new BTreeNode(m);
	        node = node.insert(3);
	        node = node.insert(24);
	        node = node.insert(37);
	        node = node.insert(45);
	        node = node.insert(50);
	        node = node.insert(53);
	        node = node.insert(61);
	        node = node.insert(90);
	        node = node.insert(100);
	        node = node.insert(70);
	        System.out.println("--------Ô­Ê÷£º---------");
	        node.printTree();
	        System.out.println("--------É¾³ý50ºó£º---------");
	        node = node.delete(50);
	        node.printTree();
	        System.out.println("--------É¾³ý53ºó£º---------");
	        node = node.delete(53);
	        node.printTree();
	        System.out.println("--------É¾³ý37ºó£º---------");
	        node = node.delete(37);
	        node.printTree();
	}
	
}
