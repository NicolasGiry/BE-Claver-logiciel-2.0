import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

public class Tree implements Comparable<Tree>{
    private int poids;
    private List<Tree> enfants = new ArrayList<>();
    private Tree parent;
    private String val;

    public Tree (String val, Tree parent) {
        poids = 1;
        this.val = val;
        this.parent = parent;
    }

    public Tree addLettre(String lettre, Tree parent) {
        Tree tree = new Tree(lettre, parent);
        if (enfants.contains(tree)) {
            tree = enfants.get(enfants.indexOf(tree));
            tree.addPoids();
        } else {
            enfants.add(tree);
        }
        return tree;
    }

    public void addPoids() {
        poids ++;
    }

    public int getPoids() {
        return poids;
    }

    public String getVal() {
        return val;
    }

    public Tree getparent() {
        return parent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tree) {
            Tree tree = (Tree) obj;
            return val.equals(tree.val);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31*val.hashCode();
    }

    public void printTree(int indent) {
        for (int i=0; i<indent; i++) {
            System.out.print("-");
        } 
        System.out.println(val + " (" + poids +")");
        for (Tree t : enfants) {
            t.printTree(indent+1);
        }
    }

    public List<String> predictNext(boolean display) {
        // trier enfants par poids
        NavigableSet<Tree> listeTrie = new TreeSet<>();
        listeTrie.addAll(enfants);
        List<String> lettres = new ArrayList<>();
        for (Tree t : listeTrie) {
            lettres.add(t.val);
        }

        if (display) {
            for (String s : lettres) {
                if (s!="  ")
                    System.out.print(s + "  ");
            }
            System.out.println("");
        }
        return lettres;
    }

    public void afficherEnfants() {
        for (Tree t : enfants) {
            System.out.print(t.val + "  ");
        }
        System.out.println("");
    }

    public Tree goTo(String letter) {
        Tree tree = new Tree(letter, this);
        if (enfants.contains(tree)) {
            tree = enfants.get(enfants.indexOf(tree));
            return tree;
        }
        return null;
    }

    @Override
    public int compareTo(Tree o) {
        return o.poids-poids;
    }
}
