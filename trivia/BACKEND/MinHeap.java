package trivia;

import java.io.*;
import java.util.*; 

public class MinHeap<Key, Value> implements PriorityQueue<Key, Value>{
    private Node root;
	private int numItems;

	private class Node{
		Key key;
		Value value;
		Node left;
		Node right;

		public Node(Key key, Value value){
			this.key = key;
			this.value = value;
			this.left = null;
			this.right =  null;
		}

	}

	public boolean isEmpty(){
		return this.root == null;

	}

	public int size(){
		return this.numItems;
	}

	public void put(Key key, Value value, Comparator<Key> comparator){

	}

	public Key remove(){

	}
}



