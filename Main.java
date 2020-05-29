public class Main {
    public static void main(String[] args) {
//        BTree<Character> tree = new BTree<Character>(2);
//        tree.insert('a');
//        tree.insert('b');
//        System.out.println(tree);
//        tree.insert('c');
//        System.out.println(tree);
//        tree.insert('d');
//        System.out.println(tree);
//        tree.insert('g');
//        tree.insert('h');
//        tree.insert('k');
//        tree.insert('m');
//        tree.insert('r');
//        tree.insert('w');
//        tree.insert('z');
//        System.out.println(tree);
//
//        System.out.println("Now deleting");
//        tree.delete('g');
//        System.out.println(tree);
//        tree.delete('m');
//        System.out.println(tree);
//        BTree<Integer> generatedTree = new BTree<Integer>(2);
//        generatedTree.insert(59);
//        generatedTree.insert(97);
//        generatedTree.insert(71);
//        generatedTree.insert(66);
//        generatedTree.insert(34);
//        generatedTree.insert(41);
//        generatedTree.insert(99);
//        generatedTree.insert(48);
//        generatedTree.insert(96);
//        generatedTree.insert(29);
//        generatedTree.insert(81);
//        generatedTree.insert(92);
//        generatedTree.insert(89);
//        generatedTree.insert(83);
//        generatedTree.insert(63);
//        generatedTree.insert(24);
//        generatedTree.insert(67);
//        generatedTree.insert(64);
//        generatedTree.insert(23);
//        generatedTree.insert(38);
//
//        System.out.println(generatedTree);
//        generatedTree.delete(64);
//        System.out.println(generatedTree);
//        generatedTree.delete(99);
//        System.out.println(generatedTree);
//        generatedTree.delete(66);
//        System.out.println(generatedTree);
        BTree bt = new BTree(2);
        bt.insert(44);
        bt.insert(44);
        bt.insert(44);
        bt.insert(50);
        bt.insert(44);
        bt.insert(44);
        System.out.println(bt.validate());
        System.out.println(bt.toString());
        bt.insert(44);
        System.out.println(bt.validate());
        System.out.println(bt.toString());

    }
}
