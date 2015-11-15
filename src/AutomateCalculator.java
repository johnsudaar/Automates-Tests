import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Node count
 * Created by johnsudaar on 15/11/15.
 */
public class AutomateCalculator {

    public static Automate product(Automate a, Automate b){
        Map<Integer, Map<Integer, Integer>> nodes = new HashMap<Integer, Map<Integer, Integer>>();
        ArrayList<Integer> accepting = new ArrayList<Integer>();
        ArrayList<Character> alphabet = new ArrayList<Character>();

        for(char c : a.alphabet()){
            if(! alphabet.contains(c)){
                alphabet.add(c);
            }
        }

        for(char c : b.alphabet()){
            if( ! alphabet.contains(c)){
                alphabet.add(c);
            }
        }

        if(a.acceptEpsilon() || b.acceptEpsilon()){
            alphabet.add(Automate.EPSILON);
        }

        int initial = -1;
        int node_count = 0;
        for(int i = 0; i < a.size(); i++){
            nodes.put(i, new HashMap<Integer, Integer>());
            for(int j = 0; j < b.size(); j++){
                nodes.get(i).put(j,node_count);
                if(a.isAcceptant(i) || b.isAcceptant(j)){
                    accepting.add(node_count);
                }
                if(i == a.getInitialNode() && j == b.getInitialNode()){
                    initial = node_count;
                }
                node_count++;
            }
        }

        int[] acc = Utilities.toIntArray(accepting.toArray(new Integer[accepting.size()]));
        char[] al = Utilities.toCharArray(alphabet.toArray(new Character[accepting.size()]));
        Automate r = new Automate(initial,acc,node_count, al);

        for(int i = 0; i < a.size(); i++){
            for(Character c : a.alphabet()){
                for(int node : a.exec(i,c)){
                    for(int j = 0 ; j < b.size(); j++){
                        r.lier(nodes.get(i).get(j), nodes.get(node).get(j), c);
                    }
                }
            }
        }

        for(int i = 0; i < b.size(); i++){
            for(Character c : b.alphabet()){
                for(int node : b.exec(i,c)){
                    for(int j = 0 ; j < a.size(); j++){
                        r.lier(nodes.get(j).get(i), nodes.get(j).get(node), c);
                    }
                }
            }
        }

        return r;

    }
}
