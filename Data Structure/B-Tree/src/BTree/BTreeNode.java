package BTree;
import java.util.LinkedList;
import java.util.function.Consumer;

import javax.management.RuntimeErrorException;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;
import javax.swing.tree.TreeNode;

public class BTreeNode {
	//	define variables
	int m;
	BTreeNode parent;
	LinkedList<Integer> keyValues;
	LinkedList<BTreeNode> children;
	
	private BTreeNode()
	{
		this.keyValues = new LinkedList<Integer>();
		this.children = new LinkedList<BTreeNode>();
		m = 0;
	}
	public BTreeNode( int m )
	{
		this();
		if( m<3 )
		{
			throw new RuntimeException("M mujst greater than 2 !");
		}
		this.m = m;
	}
	public BTreeNode( BTreeNode parent )
	{
		this(parent.m);
		this.parent = parent;		
	}
	
	/**
	 * 
	* @Title: search
	* @Description: 
	* @param @param x
	* @param @return    
	* @return BTreeNode
	 */
	public BTreeNode search( int x )
	{
		if( isEmpty() )
		{
			return this;
		}
		int index = 0;
		// find the correct pointer
		while( index<keyValues.size() && keyValues.get(index) <= x )
		{
			// move forward
			if( keyValues.get(index) == x )
				return this;
			index++;
		}
		//	recursion
		return children.get(index).search(x);
	}
	
	
	private boolean isEmpty() {
		// TODO Auto-generated method stub
		if( keyValues == null || keyValues.size()==0 )
			return true;
		else
			return false;
	}

	/**
	 * 
	* @Title: insert
	* @Description: insert new key
	* 				(1) First find the correct position
	* 				(2) Then add the new node and split( if necessary )
	* 				(3) Finally recurse
	* @param e the key to be added
	* @return BTreeNode( root )
	 */
	public BTreeNode insert( int e )
	{
		//	empty node condition
		if( isEmpty() )
		{
			keyValues.add(e);
			children.add(new BTreeNode(this));
			children.add(new BTreeNode(this));
			return this;
		}
		
		// find the correct position
		BTreeNode p = getRoot().search( e );
		if( !p.isEmpty() )
		{
			throw new RuntimeException("Cannot insert same keys!");
		}
		
		//	insert node
		insertNode( p.parent,e,new BTreeNode(p.m) );
		return getRoot();
	}
	
	/**
	 * 
	* @Title: insertNode
	* @Description: TODO
	* @param node: nodes to be inserted
	* @param e: key to be inserted
	* @param eNode: e -> right child    
	* @return void
	 */
	public void insertNode( BTreeNode node,int e,BTreeNode eNode )
	{
		int index = 0;
		//	find the position
		while( index<node.keyValues.size() && node.keyValues.get(index)<e )
		{
			index++;
		}
		//	insert key value
		node.keyValues.add(index,e);
		eNode.parent = node;
		node.children.add(index+1,eNode);
		//	check if overflow
		if( node.keyValues.size()>m-1 )
		{
			//	overflow -> split
			int middle = m/2;
			int midVal = node.keyValues.get(middle);
			
			//	define right part
			BTreeNode rightTreeNode = new BTreeNode( m );
			rightTreeNode.keyValues = new LinkedList<Integer>( node.keyValues.subList(middle+1,m) );
			rightTreeNode.children = new LinkedList<BTreeNode>( node.children.subList(middle+1,m+1) );
			for( BTreeNode element:rightTreeNode.children )
			{
				element.parent = rightTreeNode;
			}
			
			//	define left part
			node.keyValues = new LinkedList<Integer>( node.keyValues.subList(0,middle) );
			node.children = new LinkedList<BTreeNode>( node.children.subList(0,middle+1) );
			
			//	 right part upward
			if( node.parent == null )
			{
				node.parent = new BTreeNode(m);
				node.parent.keyValues.add(midVal);
				node.parent.children.add(node);
				node.parent.children.add(rightTreeNode);
				rightTreeNode.parent = node.parent;
				return;
			}
			
			//	recursion
			insertNode(node.parent, midVal , rightTreeNode );
		}
	}
	
	/**
	 * 
	* @Title: delete
	* @Description: 
	* @param @param node: node to be deleted
	* @param @param index: index of the key value
	* @param @param childIndex: index of the children
	* @param @return    
	* @return BTreeNode( root )
	 */
	private BTreeNode delete( BTreeNode node,int index,int childIndex )
	{
		//	delete
		node.keyValues.remove( index );
		node.children.remove( childIndex );
		
		//	case 1: still maintain B-tree structure
		if( node.children.size() >= Math.ceil(m/2.0) )
		{
			return node.getRoot();
		}
		if( node.isRoot() )
		{
			if( node.children.size()>1 )
			{
				return node;
			}
			else {
				BTreeNode newRoot = node.children.get(0);
				newRoot.parent = null;
				return newRoot;
			}
		}
		
		//	case 2: the structure is destroyed after deletion
		//	get the pointer that points to the node
		int pcIndex = 0;
		while( pcIndex<node.parent.children.size() && node.parent.children.get(pcIndex) != node )
		{
			pcIndex++;
		}
		//	get brother index
		if( pcIndex>0 && node.parent.children.get(pcIndex-1).keyValues.size() >= Math.ceil(m/2.0) )
		{
			//	borrow from the left brother node
			int downKey = node.parent.keyValues.get(pcIndex-1);
			BTreeNode leftBrotherNode = node.parent.children.get(pcIndex-1);
			int upKey = leftBrotherNode.keyValues.remove( leftBrotherNode.keyValues.size()-1 );
			//	modify node and left brother
			BTreeNode mergeNode = leftBrotherNode.children.remove( leftBrotherNode.children.size()-1 );
			node.keyValues.add(0,downKey);
			node.children.add(0,mergeNode);
			node.parent.keyValues.set(pcIndex-1, upKey);
			return node.getRoot();
		}
		else if( pcIndex<node.parent.children.size()-1 && 
				 node.parent.children.get(pcIndex+1).keyValues.size() >= Math.ceil(m/2.0) )
		{
			//	borrow from the right brother
			int downKey = node.parent.keyValues.get(pcIndex);
			BTreeNode rightBrotherNode = node.parent.children.get(pcIndex+1);
			int upKey = rightBrotherNode.keyValues.remove(0);
			BTreeNode mergeNode = rightBrotherNode.children.remove(0);
			//	modify node and right brother node
			node.keyValues.add(downKey);
			node.children.add(mergeNode);
			node.parent.keyValues.set(pcIndex, upKey);
			return node.getRoot();
		}
		else
		{
			int parentIndex = 0;
			//	Both left and right brother cannot borrow
			//	Merge left or right
			if( pcIndex > 0 )
			{
				//	merge (node,left brother)
				BTreeNode leftBrotherNode = node.parent.children.get(pcIndex-1);
				parentIndex = pcIndex-1;
				int downKey = node.parent.keyValues.get(pcIndex-1);
				leftBrotherNode.keyValues.add(downKey);
				leftBrotherNode.keyValues.addAll( node.keyValues );
				node.children.forEach( c -> c.parent = leftBrotherNode );
				leftBrotherNode.children.addAll( node.children );
			}
			else
			{
				//	merge right node and node
				BTreeNode rightBrotherNode = node.parent.children.get(pcIndex+1);
				parentIndex = pcIndex;
				int downKey = node.parent.keyValues.get(parentIndex);
				rightBrotherNode.keyValues.add(0,downKey);
				//	(node+right) -> right
				rightBrotherNode.keyValues.addAll(0,node.keyValues);
				node.children.forEach( c -> c.parent = rightBrotherNode );
				rightBrotherNode.children.addAll(0,node.children);				
			}
			return delete(node.parent, parentIndex, pcIndex);
		}
	}
	
	public BTreeNode delete( int e )
	{
		if( isEmpty() )
		{
			return this;
		}
		BTreeNode pBTreeNode = getRoot().search(e);
		if( pBTreeNode.isEmpty() )
		{
			throw new RuntimeException( "The key does not exist!\n" );  
		}
		int index = 0;
		while( index<pBTreeNode.keyValues.size() && pBTreeNode.keyValues.get(index) <e )
		{
			index++;
		}
		
		if( !pBTreeNode.children.get(0).isEmpty() )
		{
			//	non-leaf node
			BTreeNode rightMin = pBTreeNode.children.get(index);
			while( !rightMin.children.get(0).isEmpty() )
			{
				rightMin = rightMin.children.get(0);
			}
			//	right-min -> node
			pBTreeNode.keyValues.set(index, rightMin.keyValues.get(0));
			return delete(rightMin,index,0);
		}
		return delete(pBTreeNode,index,0);
	}
	
	/**
	 * 
	* @Title: getRoot
	* @Description: find the root of the Tree
	* @param  
	* @return BTreeNode
	 */
	private BTreeNode getRoot() {
		BTreeNode p = this;
		while ( !p.isRoot() ) {
			p = p.parent;
		}
		return p;
	}
	private boolean isRoot() {
		return parent == null;
	}
	
	/**
	 * 
	* @Title: printTree
	* @Description: output the results
	* @param    
	* @return void
	 */
	public void printTree() {
		printNode(this, 0);
	}
	
	private void printNode(BTreeNode node, int depth) {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < depth; i++) {
            sb.append("|    ");
        }
        if(depth > 0) {
            sb.append("|----");
        }
        sb.append(node.keyValues);
        System.out.println(sb.toString());
        for(BTreeNode child : node.children) {
            printNode(child, depth+1);
        }
    }

}
