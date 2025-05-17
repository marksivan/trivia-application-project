package trivia.BACKEND;

import java.util.Comparator;

public interface PriorityQueue<T>{

	/* Returns true if the priority queue is empty */
	public abstract boolean isEmpty();
    
    /* Returns the number of elements in the priority queue  */
	public abstract int size();

    /*Inserts a question object into the priority queue*/
	public abstract void push(T question, Comparator<T> comparator);
    
    /*Removes the element with the highest priority*/
	public abstract T pop();
}