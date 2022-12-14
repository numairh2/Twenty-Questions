import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

//import MyBST.BSTNode;

//import GameTreeSol.BinaryTreeNode;
//import MyBST.BSTNode;

public class GameTree {
	/**
	 * Will you need to create an inner class?
	 */
	// TODO?
	private class Node {
		String data;
		Node left, right;

		public Node(String val) {
			this.data = val;
			left = right = null;
		}

		@Override
		public String toString() {
			return "" + this.data;
		}
	}

	/**
	 * Will you need any instance variables?
	 */
	// TODO?
	private Node current;
	private Node overallRoot;

	/**
	 * Constructor needed to create the game.
	 *
	 * @param fileName this is the name of the file we need to import the game
	 *                 questions and answers from.
	 */
	public GameTree(String fileName) {
		try {
			Scanner scan = new Scanner(new File(fileName));

			this.current = bulidTree(scan);
			this.overallRoot = this.current;

		} catch (FileNotFoundException s) {
			System.out.println("File does Not Exist Please Try Again: ");
		}
	}

	private Node bulidTree(Scanner sc) {
		String line = sc.nextLine();
		Node node = new Node(line);
		if (node.data.contains("?")) {
			node.left = bulidTree(sc);
			node.right = bulidTree(sc);
		}
		return node;
	}

	/*
	 * Add a new question and answer to the currentNode. If the current node has the
	 * answer chicken, theGame.add("Does it swim?", "goose"); should change that
	 * node like this:
	 */
	// -----------Feathers?-----------------Feathers?------
	// -------------/----\------------------/-------\------
	// ------- chicken horse-----Does it swim?-----horse--
	// -----------------------------/------\---------------
	// --------------------------goose--chicken-----------
	/**
	 * @param newQ The question to add where the old answer was.
	 * @param newA The new Yes answer for the new question.
	 */
	public void add(String newQ, String newA) {
		Node temp = current;
		current = new Node(newQ);
		current.right = new Node(newA);
		current.left = temp;
	}

	

	/**
	 * True if getCurrent() returns an answer rather than a question.
	 *
	 * @return False if the current node is an internal node rather than an answer
	 *         at a leaf.
	 */
	public boolean foundAnswer() {
		return answer(current);
	}

	private boolean answer(Node node) {
		if (!this.current.data.endsWith("?") && node.right == null && node.left == null)
			return true;
		return false;
	}

	/**
	 * Return the data for the current node, which could be a question or an answer.
	 * Current will change based on the users progress through the game.
	 *
	 * @return The current question or answer.
	 */
	public String getCurrent() {
		return current(current);
	}

	private String current(Node node) {
		if (node != null) {
			this.c = node;
			return node.data;
		}
		return "";
	}

	/**
	 * Ask the game to update the current node by going left for Choice.yes or right
	 * for Choice.no Example code: theGame.playerSelected(Choice.Yes);
	 *
	 * @param yesOrNo
	 */
	public void playerSelected(Choice yesOrNo) {
		if (yesOrNo.equals(Choice.Yes))
			this.current = this.current.left;

		else if (yesOrNo.equals(Choice.No))
			this.current = this.current.right;
	}

	/**
	 * Begin a game at the root of the tree. getCurrent should return the question
	 * at the root of this GameTree.
	 */
	public void reStart() {
		this.current = this.overallRoot;
	}

	@Override
	public String toString() {
		return postOrder(current, 0);
	}

	private String postOrder(Node node, int l) {
		String displayNodes = "";
		if (node != null) {
			displayNodes += this.postOrder(node.right, l + 1);
			for (int i = 0; i < l; i++) {
				displayNodes += "- ";
			}
			displayNodes += node;
			displayNodes += "\n";
			displayNodes += this.postOrder(node.left, l + 1);

		}
		return displayNodes;

	}

	/**
	 * Overwrite the old file for this gameTree with the current state that may have
	 * new questions added since the game started.
	 *
	 */
	public void saveGame() {
		String outputFileName = "output.txt";
		PrintWriter diskFile = null;
		try {
			diskFile = new PrintWriter(new File(outputFileName));
			save(current, diskFile);
			diskFile.close();
		} catch (IOException io) {
			System.out.println("Could not create file: " + outputFileName);
		}
	}

	private void save(Node node, PrintWriter disk) {
		if (node != null) {
			disk.println(node.data);
			save(node.right, disk);
			save(node.left, disk);

		}
	}
}
