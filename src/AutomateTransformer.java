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
        // S'il n'est pas complet : Ajout d'un noeud poubelle
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

        Automate a = AutomateTransformer.completeAndDeterminize(aut); // On part d'un automate complet et deterministe

        ArrayList<Integer> groups = new ArrayList<Integer>(); // Permet de stocker le groupe correspondant a un noeud
        for (int i = 0; i < a.size(); i++) { // Initialisation de la liste des groupes (acceptant => 1, pas acceptant => 0)
            if (a.isAcceptant(i)) {
                groups.add(i, 1);
            } else {
                groups.add(i, 0);
            }
        }

        boolean same; // Condition d'arret
        ArrayList<Map<Character, Integer>> links; // Permet de stocker les associations suivantes : Noeuds -> transition -> groupe
        ArrayList<Map<Integer, Map<Character, Integer>>> g_link; // Permet de stocker les associations suivantes Groupe -> transition -> groupe (pour chacun des anciens groupes)
        ArrayList<Integer> new_groups; // Variable de travail pour groups
        int nb_groups;
        int old_nb_groups = 2;
        do{
            nb_groups = 0; // Permet de savoir combien de groupes ont été créé lors de cette itération
            // Initialisations
            links = new ArrayList<Map<Character, Integer>>();
            g_link = new ArrayList<Map<Integer, Map<Character, Integer>>>();
            new_groups = new ArrayList<Integer>();

            // Recherche du groupe pointé par un charactere depuis tous les noeuds de l'automate
            for (int i = 0; i < a.size(); i++) {
                links.add(i, new HashMap<Character, Integer>());
                for (Character c : a.alphabet()) {
                    int node = a.exec(i, c).get(0);
                    links.get(i).put(c, groups.get(node));
                }
            }


            // Initialisation de g_link : On crée une table d'association groupe -> Transition -> groupe pour chacun des anciens groupes
            for(int i = 0; i < old_nb_groups; i++){
                g_link.add(i, new HashMap<Integer, Map<Character,Integer>>());
            }

            // Mise a jour de g_link
            for (int i = 0; i < a.size(); i++) {
                int node_group = -1;
                int old_node_group = groups.get(i);
                // Pour chacun des noeuds, nous cherchons si la sequence visée par celui ci existe déjà dans notre branche
                for(Map.Entry<Integer, Map<Character, Integer>> entry : g_link.get(old_node_group).entrySet()){
                    if(entry.getValue().equals(links.get(i))){
                        node_group = entry.getKey(); // Si oui on attribue le groupe au noeud trouvé
                    }
                }
                if (node_group == -1) { // Sinon on créé le groupe en fonction de la séquence du noeud
                    node_group = nb_groups;
                    nb_groups++;
                    g_link.get(old_node_group).put(node_group, links.get(i));
                }

                new_groups.add(i, node_group);
            }

            same = true;
            // Calcul de la condition d'arret : On regarde si il y a une différences entre les nouveaux groupes et ceux del'étape N-1
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

        // Une fois finit nous calculons le groupe de chacun des noeuds
        for (int i = 0; i < a.size(); i++) {
            links.add(i, new HashMap<Character, Integer>());
            for (Character c : a.alphabet()) {
                int node = a.exec(i, c).get(0);
                links.get(i).put(c, groups.get(node));
            }
        }

        ArrayList<Integer> acc = new ArrayList<Integer>();
        // Récuperation de la liste des groupes acceptants
        for(int i = 0; i< a.size(); i++){
            if(a.isAcceptant(i)){
                if(! acc.contains(groups.get(i))) {
                    acc.add(groups.get(i));
                }
            }
        }

        int[] ac = Utilities.toIntArray(acc.toArray(new Integer[acc.size()]));
        // Initialisation de l'automate
        Automate r = new Automate(groups.get(a.getInitialNode()),ac , nb_groups, Utilities.toCharArray(a.alphabet()));
        ArrayList<Integer> done = new ArrayList<Integer>();

        // Ajout des liens
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
}
