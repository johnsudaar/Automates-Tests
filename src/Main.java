import java.util.ArrayList;

/**
 * Point d'entrée d'un programme
 * Created by johnsudaar on 24/10/15.
 */

public class Main {

    public static void main(String[] args){

        Automate aut = new Automate(0, new int[]{3}, 4);

        aut.lier(0,1,'a');
        aut.lier(1,2,'b');
        aut.lier(2,3,'c');

        pass("abc", Runner.accept(aut,"abc"));
        pass("a",   Runner.accept(aut, "a"));
        pass("dba", Runner.accept(aut,"dba"));
        pass("adc", Runner.accept(aut,"adc"));
        pass("ab",  Runner.accept(aut,"ab"));
    }

    public static void pass(String text, boolean state) {
        if (state)
            System.out.println(text+" :  OK!");
        else
            System.out.println(text+" :  FAILED!");
    }
}
