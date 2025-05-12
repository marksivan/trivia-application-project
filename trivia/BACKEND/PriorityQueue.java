package trivia.BACKEND;

import java.util.Comparator;

public interface PriorityQueue<T>{
	public abstract boolean isEmpty();

	public abstract int size();

	public abstract void push(T question, Comparator<T> comparator);

	public abstract T pop();
}