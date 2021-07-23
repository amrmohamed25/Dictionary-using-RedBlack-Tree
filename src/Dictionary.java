import java.io.*;
import java.util.Scanner;

class Node {
    String data;
    Node parent;
    Node left;
    Node right;
    int color;  // 0 --> Black, 1 --> Red

    public Node(String key) {
        color = 1;  //red
        left = null;
        right = null;
        parent = null;
        data = key;
    }
}

class RedBlackTree {
    private Node root;
    private Node TNULL;

    public RedBlackTree() {
        TNULL = new Node(null);
        TNULL.color = 0;
        root = TNULL;
    }

    public Node getRoot() {
        return this.root;
    }

    // insert the key to the tree in its appropriate position and fix the tree
    public void insert(String key) {
        // Ordinary Binary Search Insertion
        Node node = new Node(key);
        node.left = TNULL;
        node.right = TNULL;

        Node parentTraverse = null;
        Node traverse = this.root;

        while (traverse != TNULL) {
            parentTraverse = traverse;
            if (node.data.compareToIgnoreCase(traverse.data) < 0) {
                traverse = traverse.left;
            } else {
                traverse = traverse.right;
            }
        }

        // parentTraverse is parent of traverse
        node.parent = parentTraverse;
        if (parentTraverse == null) {
            root = node;
        } else if (node.data.compareToIgnoreCase(parentTraverse.data) < 0) {
            parentTraverse.left = node;
        } else {
            parentTraverse.right = node;
        }

        // if new node is a root node, simply return
        if (node.parent == null) {
            node.color = 0;
            return;
        }

        // if the grandparent is null, simply return
        if (node.parent.parent == null) {
            return;
        }

        // Fix the tree
        fixInsert(node);
    }

    // fix the red-black tree
    private void fixInsert(Node node) {
        Node uncle;
        while (node.parent.color == 1) { //while parent color is red
            if (node.parent == node.parent.parent.right) { //if node's parent is the right child of the grandparent
                uncle = node.parent.parent.left; // uncle is the left child of the grandparent
                if (uncle.color == 1) { //if uncle is red
                    //case: Parent and Uncle are red then parent and uncle will be black while grandparent will be red
                    uncle.color = 0;
                    node.parent.color = 0;
                    node.parent.parent.color = 1;
                    node = node.parent.parent; //node = grandparent
                } else { //if uncle is black
                    if (node == node.parent.left) { //if node is the left child of the parent
                        // case: Right-Left
                        node = node.parent;
                        rightRotate(node);
                    }
                    // case: Right-Right
                    node.parent.color = 0;
                    node.parent.parent.color = 1;
                    leftRotate(node.parent.parent);
                }
            } else {  //if node's parent is the left child of the grandparent
                uncle = node.parent.parent.right;

                if (uncle.color == 1) { //if uncle is red
                    // case: Parent and Uncle are red
                    uncle.color = 0;
                    node.parent.color = 0;
                    node.parent.parent.color = 1;
                    node = node.parent.parent;
                } else { //if uncle is black
                    if (node == node.parent.right) { //if node is the right child of the parent
                        // Left-Right
                        node = node.parent;
                        leftRotate(node);
                    }
                    // Left-Left
                    node.parent.color = 0;
                    node.parent.parent.color = 1;
                    rightRotate(node.parent.parent);
                }
            }
            if (node == root) {
                break;
            }
        }
        root.color = 0;
    }

    // rotate left at node x
    public void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != TNULL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    // rotate right at node x
    public void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }


    private Node searchTree(Node node, String key) {
        if (node == TNULL || key.equalsIgnoreCase(node.data)) {
            return node;
        }

        if (key.compareToIgnoreCase(node.data) < 0) {
            return searchTree(node.left, key);
        }
        return searchTree(node.right, key);
    }

    int max(int x, int y) {
        return Math.max(x, y);
    }

    int heightOfTree(Node Root) {  //number of edges in the longest path
        if (Root == TNULL)
            return -1;
        else
            return 1 + max(heightOfTree(Root.left), heightOfTree(Root.right));
    }

    int countNodes(Node Root) {
        if (Root == TNULL)
            return 0;
        else
            return 1 + countNodes(Root.left) + countNodes(Root.right);
    }

    public static void main(String[] args) throws IOException {
        RedBlackTree rbTree = new RedBlackTree();
        FileInputStream stream = new FileInputStream("EN-US-Dictionary.txt");

        DataInputStream inputStream = new DataInputStream(stream);

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String string;

        while ((string = br.readLine()) != null) {
            rbTree.insert(string);
        }
        inputStream.close();
        
        System.out.println("Dictionary Loaded Successfully...!\n");
        Scanner scan = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.println("Choose one of the following:\n1. Search\n2. Insert\n3. Print tree height\n4. Print tree size\n5. Exit\n");
            System.out.print("Please enter your Choice 1-5: ");
            try {
                choice = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                choice = 6;  //go to the last else
            }
            if (choice == 1) {
                System.out.print("Please enter the word you want to search for: ");
                String str = scan.nextLine();
                Node temp = rbTree.searchTree(rbTree.getRoot(), str);
                if (temp == rbTree.TNULL) {
                    System.out.println("NO");
                } else {
                    System.out.println("YES");
                }
            } else if (choice == 2) {
                System.out.print("Please enter the word you want to insert: ");
                String str = scan.nextLine();
                Node temp = rbTree.searchTree(rbTree.getRoot(), str);
                if (temp == rbTree.TNULL) {
                    rbTree.insert(str);
                    System.out.println("Size of dictionary (Tree) after insertion: " + rbTree.countNodes(rbTree.getRoot()));
                    System.out.println("Height of tree after insertion: " + rbTree.heightOfTree(rbTree.getRoot()));
                } else {
                    System.out.println("ERROR: Word already in the dictionary!");
                }
            } else if (choice == 3) {
                System.out.println("Tree height: " + rbTree.heightOfTree(rbTree.getRoot()));

            } else if (choice == 4) {
                System.out.println("Tree size (count of nodes): " + rbTree.countNodes(rbTree.getRoot()));
            } else if (choice == 5) {
                System.exit(0);
            } else {
                System.out.println("Invalid ! Please enter a correct choice.");
            }
            System.out.println("---------------------------------------------------------");
        }
    }
}