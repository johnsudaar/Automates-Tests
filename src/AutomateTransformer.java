import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public static Automate minimize(Automate aut) {

        Automate a = AutomateTransformer.completeAndDeterminize(aut);

        ArrayList<Integer> groups = new ArrayList<Integer>();
        for (int i = 0; i < a.size(); i++) {
            if (a.isAcceptant(i)) {
                groups.add(i, 1);
            } else {
                groups.add(i, 0);
            }
        }

        boolean same;
        ArrayList<Map<Character, Integer>> links;
        ArrayList<Map<Integer, Map<Character, Integer>>> g_link;
        ArrayList<Integer> new_groups;
        int nb_groups;
        int old_nb_groups = 2;
        do{
            nb_groups = 0;
            links = new ArrayList<Map<Character, Integer>>();
            g_link = new ArrayList<Map<Integer, Map<Character, Integer>>>();
            new_groups = new ArrayList<Integer>();
            for (int i = 0; i < a.size(); i++) {
                links.add(i, new HashMap<Character, Integer>());
                for (Character c : a.alphabet()) {
                    int node = a.exec(i, c).get(0);
                    links.get(i).put(c, groups.get(node));
                }
            }

            for(int i = 0; i < old_nb_groups; i++){
                g_link.add(i, new HashMap<Integer, Map<Character,Integer>>());
            }

            for (int i = 0; i < a.size(); i++) {
                int node_group = -1;
                int old_node_group = groups.get(i);
                for(Map.Entry<Integer, Map<Character, Integer>> entry : g_link.get(old_node_group).entrySet()){
                    if(entry.getValue().equals(links.get(i))){
                        node_group = entry.getKey();
                    }
                }
                if (node_group == -1) {
                    node_group = nb_groups;
                    nb_groups++;
                    g_link.get(old_node_group).put(node_group, links.get(i));
                }

                new_groups.add(i, node_group);
            }

            same = true;

            for (int i = 0; i < a.size() && same; i++) {
                int n_g = new_groups.get(i);
                int o_g = new_groups.get(i);

                for (int j = 0; j < a.size(); j++) {
                    if (groups.get(j) == o_g && new_groups.get(j) != n_g) {
                        same = false;
                        break;
                    }
                }
            }

            groups = new_groups;
            old_nb_groups = nb_groups;

        }while(! same);

        for (int i = 0; i < a.size(); i++) {
            links.add(i, new HashMap<Character, Integer>());
            for (Character c : a.alphabet()) {
                int node = a.exec(i, c).get(0);
                links.get(i).put(c, groups.get(node));
            }
        }

        ArrayList<Integer> acc = new ArrayList<Integer>();

        for(int i = 0; i< a.size(); i++){
            if(a.isAcceptant(i)){
                if(! acc.contains(groups.get(i))) {
                    acc.add(groups.get(i));
                }
            }
        }

        int[] ac = Utilities.toIntArray(acc.toArray(new Integer[acc.size()]));

        Automate r = new Automate(groups.get(a.getInitialNode()),ac , nb_groups, Utilities.toCharArray(a.alphabet()));
        ArrayList<Integer> done = new ArrayList<Integer>();

        for(int i = 0; i < a.size(); i++){
            int group = groups.get(i);
            if(! done.contains(group)){
                for(Map.Entry<Character, Integer> entry : links.get(i).entrySet()){
                    r.lier(group, entry.getValue(), entry.getKey());
                }
            }
            done.add(group);
        }


        return r;
    }

/*    public static Automate minimize(Automate aut){

        // ---   PHASE 1 : INITIALISATION ---
        Automate a = AutomateTransformer.completeAndDeterminize(aut);

        // Associe un noeud a un groupe
        ArrayList<Integer> group = new ArrayList<Integer>();

        // Permet de donner le groupe atteint par un charactere depuis un noeud
        ArrayList<Map<Character, Integer>> links;

        // Condition d'arret pour la boucle principale (cf etape 4)
        boolean same = false;



        // Initialisation des groupes avec les noeuds acceptants
        for(int i = 0; i < a.size(); i++){
            if(a.isAcceptant(i)){
                group.add(i,0);
            } else {
                group.add(i,1);
            }
        }

        do{

            // --- PHASE 2 : CREATION DE LA LISTE DES NOEUDS ---

            // Permet de lier un noeud a un groupe en passant par un charactere
            links = new ArrayList<Map<Character, Integer>>();

            // On parcourt la liste des noeuds
            for(int i = 0; i < a.size(); i++){
                links.add(i, new HashMap<Character, Integer>());
                // Pour toutes les lettres de l'alphabet
                for(char c : a.alphabet()){
                    // On met ajoute le lien entre le noeud et le groupe
                    int l = a.exec(i,c).get(0);
                    links.get(i).put(c, group.get(l));
                }
            }

            // --- PHASE 3 : MISE A JOUR DES GROUPES ---

            // Creation d'un tableau temporaire de groupes

            ArrayList<Integer> new_groups = new ArrayList<Integer>();
            ArrayList<Map<Character, Integer>> signification = new ArrayList<Map<Character, Integer>>();

            // Pour tous les noeuds
            for(int i = 0 ; i < links.size(); i++){
                int g = -1;
                // On cherche si leurs associations ont déjà été trouvée a ce niveau
                for(int j = 0 ; j < signification.size(); j++){
                    // Si l'association a été trouvée on stocke le numero du groupe
                    if(links.get(i).equals(signification.get(j))){
                        g = j;
                        break;
                    }
                }
                // Sinon on le créé
                if( g == -1 ){
                    g = signification.size();
                    signification.add(g, links.get(i));
                }
                // On met a jour la liste des groupes
                new_groups.add(i, g);
            }

            // --- ETAPE 4 : Recherche de differences entre les nouveaux groupes et les anciens

            same = true;
            // Pour tous les noeuds
            for(int i = 0; i < group.size() && same; i++) {
                int g = group.get(i); // Ancien groupe
                int n_g = new_groups.get(i); // Nouveau groupe

                // On reparcours les autres groupes pour trouver une differece
                for(int j = 0; j < group.size(); j++){
                    if(group.get(j) == g && new_groups.get(j) != n_g){ // Si l'on a trouvé un noeud qui était dans le meme groupe avant mais qui ne l'est plus, on  a trouvé une incohérence il faut continuer.
                        same = false;
                        break;
                    }
                }
            }

            group = new ArrayList<Integer>(new_groups);
        }while(! same);

        // ETAPE 5 : Creation du nouvel automate

        int nb_node = 0; // Nombre de noeuds

        for(int g : group) {
            if(g > nb_node){
                nb_node = g;
            }
        }

        char[] alphabet = new char[a.alphabet().length];

        for(int i = 0; i < a.alphabet().length; i++) {
            alphabet[i] = a.alphabet()[i];
        }

        ArrayList<Integer> accepting = new ArrayList<Integer>(); // Acceptants


        for(int i = 0; i < a.size(); i++){
            if(a.isAcceptant(i)){
                if(! accepting.contains(group.get(i))) {
                    accepting.add(group.get(i));
                }
            }
        }

        Integer[] acc = accepting.toArray(new Integer[accepting.size()]);

        int initial = group.get(a.getInitialNode());

        Automate n = new Automate(initial, Utilities.toIntArray(acc), nb_node, alphabet);

        ArrayList<Integer> done = new ArrayList<Integer>();

        for(int i = 0; i < links.size(); i++){
            int g = group.get(i);
            if(! done.contains(i)){
                done.add(i);
                for(Map.Entry<Character, Integer> entry : links.get(0).entrySet()){
                    n.lier(i,entry.getValue(),entry.getKey());
                }
            }
        }

        return n;
    }*/
}
