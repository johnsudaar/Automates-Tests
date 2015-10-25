import java.util.ArrayList;

/**
 * Permet de stocker un automate
 * Created by johnsudaar on 16/10/15.
 */
public class Automate {

    // Matrice représentant l'automate. A un point nous avons une liste des lettres possibles
    // graph[x][y] -> Liste des lettres de l'état X vers l'état Y
    private ArrayList<ArrayList<ArrayList<Character>>> transitions;
    private int[] acceptants;
    private int initial;
    private int size; // For error detection

    public Automate(int initial, int[] acceptants, int nb_etats){

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

        this.initial = initial;

    }

    // Permet d'ajouter une liaison entre deux noeuds de l'automate

    public void lier(int from, int to, char letter){
        this.transitions.get(from).get(to).add(letter);
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
}
