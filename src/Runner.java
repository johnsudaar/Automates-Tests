import java.util.ArrayList;

/**
 * Permet de travailler sur un automate
 * Created by johnsudaar on 16/10/15.
 */
public class Runner {

    private Automate automate;
    private int current_state;

    public Runner(Automate aut) {
        this.automate =  aut;
        this.current_state = this.automate.getInitialNode();
    }

    public Runner(Automate aut, int state) {
        this.automate = aut;
        this.current_state = state;
    }

    public boolean exec(char symb){
        ArrayList<Integer> listes = this.automate.exec(this.current_state, symb);
        if(listes.isEmpty()) {
            return false;
        } else if(listes.size() == 1) {
            this.current_state = listes.get(0);
        } else {
            // TODO : Automates non deterministes
        }
        return true;
    }

    public static boolean accept(Automate aut, String mot) {
        Runner r = new Runner(aut);
        for(char c : mot.toCharArray()) {
            if(! r.exec(c)) {
                return false;
            }
        }
        
        return r.automate.isAcceptant(r.current_state);
    }

}
