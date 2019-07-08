package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

/**
 * This class accepts a list of people to determine the winner in a tournament randomly.
 * @author Chauncey Brown-Castro
 * @version 2.0
 */
public class Tournament {

    //fields
    private Queue<Node> bracket = new LinkedList<>();
    private StringBuilder winOrder = new StringBuilder();
    private Node root;

    /**
     * This is my constructor, that creates only leafs that have data for my tree
     * @param fileName is the name of the file to get the list of names
     */
    public Tournament(String fileName) {
        //construct leafs from file name, so read them in from a file
        try (Scanner input = new Scanner(new File(fileName))) {

            //while there is a line, add to the bracket
            while(input.hasNext()) {
                bracket.add(new Node(input.nextLine()));
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method creates the tree structure from all of the people in the tournament
     */
    public void createTree() {
        root = createTree(bracket.size());
    }

    private Node createTree(int size) {
        //once size of the bracket == 1, we have a winner in the queue
        if(size == 1) {
            return bracket.peek(); //this would be the root
        }

        //else, get/remove the first two elements and set a parent node
        Node item = bracket.poll();
        Node item2 = bracket.poll();

        Node item3 = new Node(null);

        //set the children
        item3.left = item;
        item3.right = item2;

        //add it to the bracket
        bracket.add(item3);

        //then recurse
        return createTree(bracket.size());
    }

    /**
     * This method goes through the leaf nodes, and sets the parent node
     * to the winner of its children, randomly
     */
    public void determineWinners() {
        //get the final winner
        root.data = determineWinners(root);
        root.isVictor = true;
    }

    private String determineWinners(Node current) {
        //base case found the winner
        if(current.data != null) {
            return current.data;
        }

        //otherwise look for the next winner
        String leftWinner = determineWinners(current.left);
        String rightWinner = determineWinners(current.right);

        //once a winner is found, set a winner randomly
        Random rand = new Random();
        if(rand.nextBoolean()) {
            current.data = leftWinner;
            current.left.isVictor = true;
            current.right.isVictor = false;
            return leftWinner;
        } else {
            current.data = rightWinner;
            current.isVictor = true;
            current.left.isVictor = false;
            current.right.isVictor = true;
            return rightWinner;
        }
    }

    /**
     * This method prints out a string built together with a list of winners/losers in every round
     */
    public void printRounds() {
        //now go and set the round numbers for when i print out the bracket rounds
        setRoundNumbers(root);

        //print out the rounds
        System.out.println(getRounds());
    }

    private StringBuilder getRounds() {
        buildRounds(bracket);
        return winOrder;
    }

    private void buildRounds(Queue<Node> rounds) {
        //for every round, i need to get to the bottom, and not stop until the queue is empty
        if(rounds.isEmpty()) {
            return;
        }

        //is it the winner?
        if(rounds.peek() == root) {
            winOrder.append("Winner: ").append(root.data).append("\n");

            rounds.poll();
            rounds.add(root.left);
            rounds.add(root.right);
        } else {
            Node item = rounds.peek();
            if(item != null) {
                concatStringFromNode(item, rounds);
            }
        }
        //then recurse again through the queue
        buildRounds(rounds);
    }

    private void concatStringFromNode(Node item, Queue<Node> rounds) {
        //get the round number
        winOrder.append("Round #").append(item.round).append(": ");
        Queue<Node> tempQueue = new LinkedList<>();

        int size = rounds.size(); //for the length of the current queue
        for(int i = 0; i < size; i++) {

            //look through the item
            item = rounds.poll();

            if(item != null) {
                if(item.isVictor && rounds.size() % 2 == 0) {
                    //if winner is the right node
                    winOrder.append(item.data).append(" (W), ");

                } else if (item.isVictor) {
                    //if winner is left node
                    winOrder.append(item.data).append(" (W) - ");

                } else if(rounds.size() % 2 == 0) {
                    //if loser is the right node
                    winOrder.append(item.data).append(", ");

                } else {
                    //if loser is the left node
                    winOrder.append(item.data).append(" - ");

                }
                //then add to the tempQueue, so i can cycle through than and add the children
                tempQueue.add(item);
            }
        }
        //at the end of the string, remove the comma and start a new line
        winOrder.deleteCharAt(winOrder.length()-2);
        winOrder.append("\n");

        //cycle through than and add the children
        for(Node node : tempQueue) {
            if(node.left != null) {
                rounds.add(node.left);
            }
            if(node.right != null) {
                rounds.add(node.right);
            }
        }
    }

    private int setRoundNumbers(Node current) {
        //if i've gone past the leaf nodes (base case)
        if(current == null) {
            return 0;
        }

        //look left then right
        int leftRound = setRoundNumbers(current.left);
        int rightRound = setRoundNumbers(current.right);

        //stick with the higher number
        if(leftRound > rightRound) {
            current.round = leftRound + 1;
            return leftRound + 1;
        } else {
            current.round = rightRound + 1;
            return rightRound +1;
        }
    }

    private class Node {

        //fields
        private String data; //name
        private int round; //what round the data fought at
        private boolean isVictor = false; //did they win
        private Node left;
        private Node right;

        private Node(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            String dataString = (data == null) ? "null" : data;
            String leftChild = (left.data == null) ? "null" : left.data;
            String rightChild = (right.data == null) ? "null" : right.data;

            return leftChild + " <-- " + dataString + " --> " + rightChild;
        }
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "bracket=" + bracket +
                ", winOrder=" + winOrder +
                ", root=" + root +
                '}';
    }
}
