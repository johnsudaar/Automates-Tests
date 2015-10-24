import java.util.ArrayList;

/**
 * Point d'entrée d'un programme
 * Created by johnsudaar on 24/10/15.
 */

public class Main {

    public static void main(String[] args){
        testAll();
    }

    public static void simple1(){
        Automate aut = new Automate(0, new int[]{3}, 4);

        aut.lier(0,1,'a');
        aut.lier(1,2,'b');
        aut.lier(2,3,'c');

        pass("abc", Runner.accept(aut,"abc"), true);
        pass("a",   Runner.accept(aut, "a"),  false);
        pass("dba", Runner.accept(aut,"dba"), false);
        pass("adc", Runner.accept(aut,"adc"), false);
        pass("ab",  Runner.accept(aut,"ab"),  false);
    }

    public static void testAll(){
        System.out.println("************************");
        System.out.println("*       TESTING        *");
        System.out.println("************************");
        System.out.println("---- SIMPLE TEST 1 ----");
        simple1();


        System.out.println("************************");
        System.out.println("*     ALL OK (GG)      *");
        System.out.println("************************");
    }

    public static void pass(String text, boolean state, boolean expected) {
        if (state == expected)
            System.out.println(text+" ("+state+"):  OK !");
        else{
            System.out.println(text+" ("+state+"):  FAILED!");
            System.out.println("/!\\ ERRORED ! /!\\ ");
            System.exit(1);
        }
    }
}
