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

        // Initialisation des transitions
        this.size = nb_etats;
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

    public void lier(int from, int to, char letter){
        //this.transitions.get(from).get(to).ensureCapacity(this.transitions.get(from).get(to).size()+1);
        this.transitions.get(from).get(to).add(letter);
    }

    public ArrayList<Character> lien(int from, int to) {
        return this.transitions.get(from).get(to);
    }

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
}
