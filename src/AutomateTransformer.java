import java.util.ArrayList;

/**
 * Perform transformations on automates
 * Created by johnsudaar on 26/10/15.
 */
public class AutomateTransformer {

    public static Automate determinize(Automate aut){
        Character[] alphabet = aut.alphabet(); // Alphabet de travail
        ArrayList<ArrayList<Integer>> assoc = new ArrayList<ArrayList<Integer>>(); // Table d'association (permet de savoir a quels noeuds du graph originel correspond le noeud de ce graph)
        ArrayList<Triplet<Integer, Integer, Character>> links = new ArrayList<Triplet<Integer, Integer, Character>>(); // Table des liens a ajouter
        ArrayList<Integer> todo = new ArrayList<Integer>(); // Noeuds restant a analyser

        // On initialise notre algorithme avec le noeud courrant
        assoc.add(0, new ArrayList<Integer>(1));
        assoc.get(0).add(aut.getInitialNode());
        todo.add(0);

        // Dernier id attribué
        int latest = 0;

        do{
            // Pour chacun des lettres de l'alphabet on regarde les noeuds atteind depuis la liste d'association de todo[0]
            for(char c : alphabet) {
                ArrayList<Integer> destinations = new ArrayList<Integer>();
                for(int node : assoc.get(todo.get(0))){
                    ArrayList<Integer> d = aut.exec(node, c);
                    for(int k : d){
                        destinations.add(k);
                    }
                }
                // S'il y a des noeuds trouvés
                if( ! destinations.isEmpty()){
                    int node_index = -1;
                    // On cherche si il y a un noeud qui correspond a cette liste
                    for (int i = 0; i < assoc.size(); i++) {
                        if (Utilities.equalLists(assoc.get(i), destinations)) {
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
                if(aut.isAcceptant(node)) {
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

    public static Automate completeAndDeterminize(Automate aut){
        Automate a = AutomateTransformer.determinize(aut);
        int new_node = -1;
        for(int i = 0; i < a.size(); i++){
            for(char c : a.alphabet()){
                if(a.exec(i,c).isEmpty()){
                    if(new_node == -1){
                        new_node = a.addNode(false);
                    }
                    a.lier(i,new_node, c);
                }
            }
        }

        return a;
    }
}
