package trivia.BACKEND;

import java.util.*;

public class MaxHeap {
    private Node root;
    private int numItems;

    // Node class to store questions in the heap
    private class Node {
        Question question;
        Node left;
        Node right;

        public Node(Question question) {
            this.question = question;
            this.left = null;
            this.right = null;
        }
    }

    // Constructor
    public MaxHeap() {
        this.root = null;
        this.numItems = 0;
    }

    // Check if the heap is empty
    public boolean isEmpty() {
        return this.root == null;
    }

    // Get the size of the heap
    public int size() {
        return this.numItems;
    }

    // Push a new question into the heap
    public void push(Question question) {
        root = pushRecursive(root, question);
        numItems++;
    }

    // Helper method for recursive insertion
    private Node pushRecursive(Node node, Question question) {
        if (node == null) {
            return new Node(question);
        }

        // Compare difficulty levels and place the new question in the right position
        if (question.getDifficultyLevel() > node.question.getDifficultyLevel()) {
            // Swap the current node with the new question
            Question temp = node.question;
            node.question = question;
            question = temp;
        }

        // Recursively insert into left or right subtree based on comparison
        if (question.getDifficultyLevel() > node.question.getDifficultyLevel()) {
            node.left = pushRecursive(node.left, question);
        } else {
            node.right = pushRecursive(node.right, question);
        }
        
        return node;
    }

    // Remove and return the question with the highest difficulty
    public Question pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        
        Question removedQuestion = root.question;
        root = popRecursive(root);
        numItems--;
        return removedQuestion;
    }

    // Helper method to recursively remove the root and maintain the heap property
    private Node popRecursive(Node node) {
        if (node == null) {
            return null;
        }

        // Wremove the root and replace it with the largest child
        if (node.left == null && node.right == null) {
            return null;
        }

        if (node.left != null && node.left.question.getDifficultyLevel() > node.right.question.getDifficultyLevel()) {
            // Swap with left child
            Question temp = node.question;
            node.question = node.left.question;
            node.left.question = temp;
            node.left = popRecursive(node.left);
        } else if (node.right != null) {
            // Swap with right child
            Question temp = node.question;
            node.question = node.right.question;
            node.right.question = temp;
            node.right = popRecursive(node.right);
        }
        
        return node;
    }
}
