import java.util.ArrayList;

/**
 * Permet de travailler sur un automate
 * Created by johnsudaar on 16/10/15.
 */
public class Runner {

    private Automate automate;
    private int current_state;
    private boolean deterministe;
    private ArrayList<Runner> states;

    public Runner(Automate aut) {
        this.automate =  aut;
        this.current_state = this.automate.getInitialNode();
        this.deterministe = true;
    }

    public Runner(Automate aut, int state) {
        this.automate = aut;
        this.current_state = state;
        this.deterministe = true;
    }

    public boolean exec(char symb){
        if(this.deterministe) {
            // Si l'on croit toujours que l'automate est déterministe

            // On récupère la liste des noeuds possibles
            ArrayList<Integer> listes = this.automate.exec(this.current_state, symb);

            // S'il n'y en a pas on s'arrète
            if (listes.isEmpty()) {
                return false;
            } else if (listes.size() == 1) {
                // S'il n'y en a qu'un seul, on continue dans l'idée d'un automate deterministe
                this.current_state = listes.get(0);
            } else {
                // S'il y a plus d'un noeud dans lequel on peut aller, l'automate n'est plus deterministe (OH DEAR WE ARE IN TROUBLE)
                this.deterministe = false;
                // On empile les différentes états possibles
                this.states = new ArrayList<Runner>();
                for(int state : listes) {
                    this.states.add(new Runner(this.automate, state));
                }
            }
            return true;
        } else {
            // Si l'on sait que l'algorithme n'est pas deterministe, plus aucun calcul n'est fait dans cet état,
            // on délègue aux autres instances de Runner.

            int count = 0; // Nombre d'instances pouvant executer l'instruction

            for(int i = 0; i < this.states.size(); i++) {
                // On execute l'instruction sur chacun des automates empilé
                boolean ret = this.states.get(i).exec(symb);
                if(ret){
                   count ++;
                } else {
                    // Si il ne peut pas executer l'instruction on l'enlève de la pile.
                    this.states.remove(i);
                }
            }
            // Si aucun Runner ne peut executer la commande, on s'arrète la.
            if(count == 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    // Est ce que l'automate est dans un état ecceptant ?
    public boolean isAccepting(){
        //  - Dans le cas d'un automate deterministe il suffit de tester l'état dans lequel nous somme
        //  - Sinon il faut tester toutes les instances.

        if(this.deterministe) {
            return this.automate.isAcceptant(this.current_state);
        } else {
            for(Runner r : this.states){
                if(r.isAccepting()){
                    return true;
                }
            }
            return false;
        }

    }

    public static boolean accept(Automate aut, String mot) {

        // On essaie de faire tourner l'automate pour chaque lettre du mot
        Runner r = new Runner(aut);
        for(char c : mot.toCharArray()) {
            if(! r.exec(c)) {
                return false;
            }
        }

        // Si l'on y ai arrivé, il faut tester qu'on est dans un état accéptant.

        return r.isAccepting();
    }

}
