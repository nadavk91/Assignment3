import java.util.*;

/**
 * Cuckoo hash table implementation of hash tables.
 */
public class CuckooHashing {
    private static final int DEFAULT_TABLE_SIZE = 101;

    private final HashMethods hashFunctions;
    private final int numHashFunctions;

    private String[] array; // The array of elements
    private int currentSize; // The number of occupied cells
    private ArrayList<String> stash; //List of items that couldn't find a place

    //Backtrack
    private Stack<StackOperation> stack;

    /**
     * Construct the hash table.
     */
    public CuckooHashing(HashMethods hf) {
        this(hf, DEFAULT_TABLE_SIZE);
    }

    /**
     * Construct the hash table.
     *
     * @param hf   the hash family
     * @param size the approximate initial size.
     */
    public CuckooHashing(HashMethods hf, int size) {
        allocateArray(nextPrime(size));
        stash = new ArrayList<String>();
        makeEmpty();
        hashFunctions = hf;
        numHashFunctions = hf.getNumberOfFunctions();
        stack = new Stack<StackOperation>();
    }

    /**
     * Insert into the hash table. If the item is
     * already present, return false.
     *
     * @param x the item to insert.
     */
    public boolean insert(String x) {
        if (this.capacity() == this.size()) {
            return false;
        }
        if (find(x))
            return false;

        return insertHelper1(x);
    }

    private boolean insertHelper1(String x) {
        while (true) {
            int pos = -1;
            int kick_pos = -1;
            // indicates how many action we did during the insert
            // will be used in the undo
            int operation_count = 0;

            //This is NOT part of a real cuckoo hash implementation
            //but is necessary to avoid randomization so we can test your work
            ArrayList<ArrayList<String>> cycle_tester = new ArrayList<ArrayList<String>>();
            for (int i = 0; i < this.capacity(); i++) {
                cycle_tester.add(i, new ArrayList<String>());
            }
            boolean cycle = false;

            int MAXTRIES = this.size();
            for (int count = 0; count <= MAXTRIES; count++) {
                for (int i = 0; i < numHashFunctions; i++) {
                    pos = myhash(x, i);
                    if (isCycle(cycle_tester, x, i)) {
                        cycle = true;
                        break;
                    }
                    cycle_tester.get(pos).add(x);
                    if (array[pos] == null) {
                        array[pos] = x;
                        currentSize++;
                        // inserting to the array to a 'free' space, so the value in the index is null
                        operation_count++;
                        stack.push(new StackOperation(pos, null, operation_count));
                        return true;
                    }

                }
                if (cycle)
                    break;
                if (pos == kick_pos || kick_pos == -1)
                    kick_pos = myhash(x, 0);
                else
                    kick_pos = pos;
                // none of the spots are available, kick out item in kick_pos
                String tmp = array[kick_pos];
                array[kick_pos] = x;
                x = tmp;
                // x which now holds the last value, pushed to the stack with its' original location
                operation_count++;
                stack.push(new StackOperation(kick_pos, x, operation_count));
            }
            //insertion got into a cycle use overflow list
            this.stash.add(x);
            // if index is -1 therefore we need to remove the item from the stash
            operation_count++;
            stack.push(new StackOperation(-1, x, operation_count));
            return true;
        }
    }

    private boolean isCycle(ArrayList<ArrayList<String>> cycle_tester, String x, int i) {
        return cycle_tester.get(i).contains(x);
    }

    public void undo() {
        if (!stack.isEmpty()) {
            StackOperation operation = stack.pop();
            int operationCount = operation.getOperationCount();
            // operation count indicates how many actions to the stack occurred during the last insert
            for (int i = operationCount; i > 0; i--) {
                // if the index is -1 therefore the value inserted to the stack so we need to remove it
                if (operation.getIndex() == -1) {
                    stash.remove(operation.getLastValue());
                } else {
                    // setting the last value in its' original index
                    array[operation.getIndex()] = operation.getLastValue();
                }
                // making sure we are not doing 'extra' pop from the stack
                if (i != 1) {
                    operation = stack.pop();
                }
            }
        }
    }

    /**
     * @param x the item
     * @param i index of hash function in hash family
     * @return hash value of x using hash function(i) mod table size
     */
    private int myhash(String x, int i) {
        long hashVal = hashFunctions.hash(x, i);

        hashVal %= array.length;
        if (hashVal < 0)
            hashVal += array.length;

        return (int) hashVal;
    }

    /**
     * Finds an item in the hash table.
     *
     * @param x the item to search for.
     * @return True iff item is in the table.
     */
    public boolean find(String x) {
        return findPos(x) != -1;
    }

    /**
     * Method that searches all hash function places.
     *
     * @param x the item to search for.
     * @return the position where the search terminates or capacity+1 if item is in overflow list, or -1 if not found.
     */
    private int findPos(String x) {
        for (int i = 0; i < numHashFunctions; i++) {
            int pos = (int) myhash(x, i);
            if (array[pos] != null && array[pos].equals(x))
                return pos;
        }
        for (String s : stash) {
            if (s.equals(x)) {
                return this.capacity() + 1;
            }
        }

        return -1;
    }

    /**
     * Gets the size of the table.
     *
     * @return number of items in the hash table.
     */
    public int size() {
        return currentSize;
    }

    /**
     * Gets the length (potential capacity) of the table.
     *
     * @return length of the internal array in the hash table.
     */
    public int capacity() {
        return array.length;
    }

    /**
     * Remove from the hash table.
     *
     * @param x the item to remove.
     * @return true if item was found and removed
     */
    public boolean remove(String x) {
        int pos = findPos(x);
        if (pos == -1)
            return false;
        if (pos < this.capacity()) {
            array[pos] = null;
        } else {
            this.stash.remove(x);
        }
        currentSize--;

        // emptying the backtrack stack after successful deletion
        stack.empty();

        return true;
    }

    /**
     * Make the hash table logically empty.
     */
    public void makeEmpty() {
        currentSize = 0;
        for (int i = 0; i < array.length; i++)
            array[i] = null;
        this.stash.clear();
    }

    public String toString() {
        String ans = "";
        for (int i = 0; i < capacity(); i++) {
            if (array[i] != null)
                ans = ans.concat("Index: " + i + " ,String: " + array[i] + "\n");
        }
        int i = 0;
        for (String s : stash) {
            ans = ans + "Overflow[" + i + "] ,String: " + s + "\n";
            i++;
        }
        return ans;
    }

    /**
     * Method to allocate array.
     */
    private void allocateArray(int arraySize) {
        array = new String[arraySize];
    }

    /**
     * Method to find a prime number at least as large as n.
     */
    protected static int nextPrime(int n) {
        if (n % 2 == 0)
            n++;

        while (!isPrime(n)) {
            n += 2;
        }
        return n;
    }

    /**
     * Method to test if a number is prime.
     */
    private static boolean isPrime(int n) {
        if (n == 2 || n == 3)
            return true;

        if (n == 1 || n % 2 == 0)
            return false;

        for (int i = 3; i * i <= n; i += 2)
            if (n % i == 0)
                return false;

        return true;
    }

}