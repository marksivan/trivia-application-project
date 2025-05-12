package trivia.BACKEND;

import java.util.Comparator;

 

public interface PriorityQueue<Question>{
	public abstract boolean isEmpty();

	public abstract int size();

	public abstract void push(Question question, Comparator<Question> comparator);

	public abstract Question pop();

}