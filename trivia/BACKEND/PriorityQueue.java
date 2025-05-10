package trivia;

import java.util.Comparator;

 

public interface PriorityQueue<Key, Value>{
	public abstract boolean isEmpty();

	public abstract int size();

	public abstract void put(Key key, Value val, Comparator<Key> comparator);

	public abstract Key remove();

	public abstract peek();
}