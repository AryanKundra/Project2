package datastructures.dictionaries;

import cse332.datastructures.trees.BinarySearchTree;
import datastructures.worklists.ArrayStack;
import java.util.NoSuchElementException;

/**
 * AVLTree must be a subclass of BinarySearchTree<E> and must use
 * inheritance and calls to superclass methods to avoid unnecessary
 * duplication or copying of functionality.
 * <p>
 * 1. Create a subclass of BSTNode, perhaps named AVLNode.
 * 2. Override the insert method such that it creates AVLNode instances
 * instead of BSTNode instances.
 * 3. Do NOT "replace" the children array in BSTNode with a new
 * children array or left and right fields in AVLNode.  This will
 * instead mask the super-class fields (i.e., the resulting node
 * would actually have multiple copies of the node fields, with
 * code accessing one pair or the other depending on the type of
 * the references used to access the instance).  Such masking will
 * lead to highly perplexing and erroneous behavior. Instead,
 * continue using the existing BSTNode children array.
 * 4. Ensure that the class does not have redundant methods
 * 5. Cast a BSTNode to an AVLNode whenever necessary in your AVLTree.
 * This will result a lot of casts, so we recommend you make private methods
 * that encapsulate those casts.
 * 6. Do NOT override the toString method. It is used for grading.
 * 7. The internal structure of your AVLTree (from this.root to the leaves) must be correct
 */

public class AVLTree<K extends Comparable<? super K>, V> extends BinarySearchTree<K, V> {
    private class AVLNode extends BSTNode {
        private int balanceFactor;

        public AVLNode(K key, V value) {
            super(key, value);
            this.balanceFactor = 0;
        }
    }
        public AVLTree() {
            super();
        }

    private AVLNode scanForInsertion(K key) {
        ArrayStack<AVLNode> path = new ArrayStack<AVLNode>();
        if (this.root == null) {
            this.root = new AVLNode(key, null);
            this.size++;
            return (AVLNode) this.root;
        }
        AVLNode current = (AVLNode) this.root;
        int direction = 0;
        int child = -1;
        AVLNode problemParent = null;

        while (current != null) {
            path.add(current);
            direction = Integer.signum(key.compareTo(current.key));
            if (direction == 0) {
                return current;
            }

            child = Integer.signum(direction + 1);
            current = (AVLNode) current.children[child];
        }

        current = new AVLNode(key, null);
        this.size++;
        AVLNode parent = path.peek();
        path.add(current);
        parent.children[child] = current;
        if (parent.children[1 - child] != null) {
            parent.balanceFactor += direction;

        } else {
            problemParent = this.updateBalanceFactor(path);
        }
        if (problemParent != null) {
            int subTreeDirection = Integer.signum(key.compareTo(problemParent.key));
            int subTree = Integer.signum(subTreeDirection + 1);
            problemParent.children[subTree] = this.rotate(path);
        }
        return current;

    }
        public V insert(K key, V value) {
            if (value == null || key == null) {
                throw new IllegalArgumentException();
            }
            AVLNode current = this.scanForInsertion(key);
            V old = current.value;
            current.value = value;
            return old;

        }


    private boolean isOrdered(K firstEdge, K middle, K secondEdge) {
        return firstEdge.compareTo(middle) < 0 && middle.compareTo(secondEdge) < 0;

    }

    private void generateRotations(ArrayStack<AVLNode> path, AVLNode parent,
                                   AVLNode child, AVLNode grandChild) {

        path.clear();
        path.add(parent);
        path.add(child);
        path.add(grandChild);
    }

        private AVLNode rotate(ArrayStack<AVLNode> path) {
            AVLNode grandChild = path.next();
            AVLNode child = path.next();
            AVLNode parent = path.next();
            K firstKey = parent.key;
            K secondKey = child.key;
            K thirdKey = grandChild.key;
            int direction = Integer.signum(thirdKey.compareTo(firstKey));
            int side = Integer.signum(direction + 1);
            AVLNode remainder = null;

            if (isOrdered(firstKey, thirdKey, secondKey) || isOrdered(secondKey, thirdKey, firstKey)) {
                parent.children[side] = grandChild;
                remainder = (AVLNode) grandChild.children[side];
                child.children[1 - side] = remainder;
                grandChild.children[side] = child;
                AVLNode temp = child;
                child = grandChild;
                grandChild = temp;
                child.balanceFactor += direction;
                grandChild.balanceFactor += direction;
            }

            remainder = (AVLNode) child.children[1 - side];
            parent.children[side] = remainder;
            child.children[1 - side] = parent;

            child.balanceFactor += (direction * -1);
            parent.balanceFactor += (direction * -2);
            return child;


        }

        private AVLNode updateBalanceFactor(ArrayStack<AVLNode> path) {
            AVLNode parent = null;
            AVLNode child = null;
            AVLNode grandChild = null;
            while (path.size() > 1) {
                grandChild = child;
                child = path.next();
                parent = path.peek();
                if (child == parent.children[1]) {
                    parent.balanceFactor++;
                } else {
                    parent.balanceFactor--;
                }
                if (Math.abs(parent.balanceFactor) == 2) {
                    if (path.size() == 1) {
                        this.generateRotations(path, parent, child, grandChild);
                        this.root = this.rotate(path);
                        return null;
                    } else {
                        path.next();
                        AVLNode parentProblem = path.peek();
                        this.generateRotations(path, parent, child, grandChild);
                        return parentProblem;
                    }
                }
            }
            return null;
        }
    }
