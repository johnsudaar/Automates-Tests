import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Permet de stocker un automate
 * Created by johnsudaar on 16/10/15.
 */
public class Automate {

    // Matrice représentant l'automate. A un point nous avons une liste des lettres possibles
    // graph[x][y] -> Liste des lettres de l'état X vers l'état Y
    private ArrayList<ArrayList<ArrayList<Character>>> transitions;
    private ArrayList<Character> alphabet;
    private int[] acceptants;
    private int initial;
    private int size; // For error detection

    public Automate(int initial, int[] acceptants, int nb_etats, char[] alphabet){

        this.size = nb_etats;

        // Initialisation des transitions
        this.transitions = new ArrayList<>(this.size);
        for (int i = 0; i < this.size; i++) {
            this.transitions.add(i, new ArrayList<ArrayList<Character>>(this.size));
            this.transitions.get(i).ensureCapacity(this.size);
            for (int j = 0; j < this.size; j++) {
                this.transitions.get(i).add(j, new ArrayList<Character>());
            }
        }

        // Initialisation des acceptans

        this.acceptants = acceptants.clone();

        // Divers
        this.alphabet = new ArrayList<Character>();
        for(char c : alphabet) {
            this.alphabet.add(c);
        }
        this.initial = initial;

    }

    // Permet d'ajouter une liaison entre deux noeuds de l'automate

    public void lier(int from, int to, char letter){
        if(this.alphabet.contains(letter))
            this.transitions.get(from).get(to).add(letter);
        else
            throw new IllegalArgumentException(letter+" is not in the automate alphabet");
    }

    // Récupère la liste des caractères possibles entre l'état 1 et l'état 2.
    public ArrayList<Character> lien(int from, int to) {
        return this.transitions.get(from).get(to);
    }

    // Retourne la liste des noeuds possibles en partant du noeud from et en lisant la lettre C
    public ArrayList<Integer> exec(int from, char c) {
        ArrayList<Integer> liste = new ArrayList<Integer>();
        for(int i = 0; i < this.size; i++)  {
            if (this.lien(from, i).contains(c)) {
                liste.add(i);
            }
        }
        return liste;
    }

    public boolean isAcceptant(int state){
        for (int i = 0; i < this.acceptants.length; i++) {
            if(this.acceptants[i] == state) {
                return true;
            }
        }
        return false;
    }

    public int getInitialNode() {
        return this.initial;
    }

    public int size() {
        return this.size;
    }

    public Character[] alphabet(){
        Character[] d = new Character[this.alphabet.size()];
        this.alphabet.toArray(d);
        return d;
    }

    public Automate determinize(){
        Character[] alphabet = this.alphabet(); // Alphabet de travail
        ArrayList<ArrayList<Integer>> assoc = new ArrayList<ArrayList<Integer>>(); // Table d'association (permet de savoir a quels noeuds du graph originel correspond le noeud de ce graph)
        ArrayList<Triplet<Integer, Integer, Character>> links = new ArrayList<Triplet<Integer, Integer, Character>>(); // Table des liens a ajouter
        ArrayList<Integer> todo = new ArrayList<Integer>(); // Noeuds restant a analyser

        // On initialise notre algorithme avec le noeud courrant
        assoc.add(0, new ArrayList<Integer>(1));
        assoc.get(0).add(this.initial);
        todo.add(0);

        // Dernier id attribué
        int latest = 0;

        do{
            // Pour chacun des lettres de l'alphabet on regarde les noeuds atteind depuis la liste d'association de todo[0]
            for(char c : alphabet) {
                ArrayList<Integer> destinations = new ArrayList<Integer>();
                for(int node : assoc.get(todo.get(0))){
                    ArrayList<Integer> d = this.exec(node, c);
                    for(int k : d){
                        destinations.add(k);
                    }
                }
                // S'il y a des noeuds trouvés
                if( ! destinations.isEmpty()){
                    int node_index = -1;
                    // On cherche si il y a un noeud qui correspond a cette liste
                    for (int i = 0; i < assoc.size(); i++) {
                        if (equalLists(assoc.get(i), destinations)) {
                            node_index = i;
                            break;
                        }
                    }
                    // S'il n'y en a pas on le créé
                    if(node_index == -1) {
                        latest++;
                        assoc.add(latest, (ArrayList<Integer>) destinations.clone());
                        todo.add(latest);
                        node_index = latest;
                    }
                    // On lie les deux noeuds
                    links.add(new Triplet<Integer, Integer, Character>(todo.get(0), node_index, c));
                }
            }
            todo.remove(0);
        }while(! todo.isEmpty());


        // Recherche des noeuds acceptants
        ArrayList<Integer> acceptants = new ArrayList<Integer>();
        for(int i = 0; i <= latest; i++) {
            for(int node : assoc.get(i)){
                if(this.isAcceptant(node)) {
                    if(! acceptants.contains(i)) {
                        acceptants.add(i);
                    }
                }
            }
        }

        // Transformations des listes pour la création de l'automate
        char[] al = new char[alphabet.length];
        for(int i = 0 ; i < alphabet.length; i++) {
            al[i] = alphabet[i];
        }

        int[] acc = new int[acceptants.size()];
        for(int i = 0; i < acceptants.size(); i++) {
            acc[i] = acceptants.get(i);
        }


        Automate a = new Automate(0, acc,latest + 1, al);

        // Création des liaisons entre les différents noeuds
        for(Triplet<Integer, Integer, Character> t : links) {
            a.lier(t.getA(), t.getB(), t.getC());
        }

        return a;
    }

    public static  boolean equalLists(List<Integer> a, List<Integer> b){
        // Check for sizes and nulls
        if ((a.size() != b.size()) || (a == null && b!= null) || (a != null && b== null)){
            return false;
        }

        if (a == null && b == null) return true;

        // Sort and compare the two lists
        Collections.sort(a);
        Collections.sort(b);
        return a.equals(b);
    }
}
