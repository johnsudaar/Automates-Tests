import java.util.ArrayList;

/**
 * Permet de stocker un automate
 * Created by johnsudaar on 16/10/15.
 */
public class Automate {

    // Matrice représentant l'automate. A un point nous avons une liste des lettres possibles
    // graph[x][y] -> Liste des lettres de l'état X vers l'état Y
    private ArrayList<ArrayList<ArrayList<Character>>> transitions;
    private ArrayList<Integer> acceptants;
    private int initial;
    private int size; // For error detection

    public Automate(int initial, ArrayList<Integer> acceptants, int nb_etats){

        // Initialisation des transitions

        this.transitions = new ArrayList<>(size);
        for (int i = 0; i < this.transitions.size(); i++) {
            this.transitions.set(i, new ArrayList(size));
            for (int j = 0; j < size; j++) {
                this.transitions.get(i).set(j, new ArrayList());
            }
        }

        // Initialisation des acceptans

        this.acceptants = new ArrayList<Integer>(acceptants);

        // Divers

        this.initial = initial;
        this.size = nb_etats;

    }

    public void lier(int from, int to, char letter){
        this.transitions.get(from).get(to).add(letter);
    }

    public ArrayList<Character> lien(int from, int to) {
        return this.transitions.get(from).get(to);
    }
}
