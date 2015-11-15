import java.util.ArrayList;

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
    private int size;
    private boolean acceptEpsilon = false;

    public static char EPSILON = '\0';

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
            if(c == EPSILON)
                this.acceptEpsilon = true;
            else
                this.alphabet.add(c);
        }
        this.initial = initial;

    }

    public int addNode(boolean acceptant){
        this.transitions.add(this.size, new ArrayList<ArrayList<Character>>());
        for (int j = 0; j < this.size + 1; j++) {
            this.transitions.get(this.size).add(j, new ArrayList<Character>());
            this.transitions.get(j).add(this.size, new ArrayList<Character>());
        }
        this.size++;
        return this.size - 1;
    }

    // Permet d'ajouter une liaison entre deux noeuds de l'automate
    public void lier(int from, int to, char letter){
        if(this.alphabet.contains(letter) || (letter == EPSILON && this.acceptEpsilon))
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

        ArrayList<Integer> epsilonBacklog = new ArrayList<Integer>();
        ArrayList<Integer> marqued = new ArrayList<Integer>();

        ArrayList<Integer> liste = new ArrayList<Integer>();
        for(int i = 0; i < this.size; i++)  {
            if (this.lien(from, i).contains(c)) {
                liste.add(i);
            }
            if(this.lien(from, i).contains(EPSILON)){
                epsilonBacklog.add(i);
            }
        }

        while(! epsilonBacklog.isEmpty()){
            int currentNode = epsilonBacklog.get(0);
            epsilonBacklog.remove(0);

            marqued.add(currentNode);
            for(int i = 0 ; i < this.size ; i++) {
                if(this.lien(currentNode, i).contains(c)){
                    if(! liste.contains(i)){
                        liste.add(i);
                    }
                }

                if(! marqued.contains(i)){
                    if(this.lien(currentNode, i).contains(EPSILON)){
                        epsilonBacklog.add(i);
                    }
                }
            }
        }

        return liste;
    }

    private boolean isDirectlyAcceptant(int state){
        for (int i = 0; i < this.acceptants.length; i++) {
            if(this.acceptants[i] == state) {
                return true;
            }
        }
        return false;
    }

    public boolean isAcceptant(int state){
        ArrayList<Integer> marqued = new ArrayList<Integer>();
        ArrayList<Integer> epsilonBacklog = new ArrayList<Integer>();

        if(this.isDirectlyAcceptant(state)){
            return true;
        }

        marqued.add(state);

        for(int i = 0; i < this.size; i++){
            if(this.lien(state, i).contains(EPSILON)) {
                epsilonBacklog.add(i);
            }
        }

        while(! epsilonBacklog.isEmpty()){
            int currentNode = epsilonBacklog.get(0);
            epsilonBacklog.remove(0);
            marqued.add(currentNode);
            if(this.isDirectlyAcceptant(currentNode)){
                return true;
            }
            for(int i = 0 ; i < this.size; i++){
                if(! marqued.contains(i) && this.lien(currentNode,i).contains(EPSILON)){
                    epsilonBacklog.add(i);
                }
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

    // Permet de savoir si un automate est deterministe
    public boolean isDeterminist(){
        for(int i = 0; i < this.size(); i++) {
            for(char c : this.alphabet()) {
                if(this.exec(i,c).size() > 1 ) {
                    return false;
                }
            }
            for(int j = 0 ; j < this.size; j++) {
                if (this.lien(i,j).contains(EPSILON)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Automate clone(){
        int size = this.acceptEpsilon ? this.alphabet.size() + 1 : this.alphabet.size();
        char[] a = new char[size];
        for(int i = 0; i < this.alphabet.size(); i++){
            a[i] = this.alphabet.get(i);
        }

        if(this.acceptEpsilon){
            a[this.alphabet.size()] = EPSILON;
        }

        Automate n = new Automate(this.initial, this.acceptants.clone(), this.size, a);

        for(int i = 0; i < this.size; i++){
            for(int j = 0; j < this.size; j++){
                for(Character c : this.lien(i,j)){
                    n.lier(i,j,c);
                }
            }
        }

        return n;
    }

    public boolean acceptEpsilon(){
        return this.acceptEpsilon;
    }
}
