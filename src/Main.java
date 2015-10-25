
/**
 * Point d'entrée d'un programme
 * Created by johnsudaar on 24/10/15.
 */

public class Main {

    public static void main(String[] args){
        testAll();
    }

    public static void simple1(){
        Automate aut = new Automate(0, new int[]{3}, 4, new char[]{'a','b','c'});

        aut.lier(0,1,'a');
        aut.lier(1,2,'b');
        aut.lier(2,3,'c');

        pass("abc", Runner.accept(aut,"abc"), true);
        pass("a",   Runner.accept(aut, "a"),  false);
        pass("dba", Runner.accept(aut,"dba"), false);
        pass("adc", Runner.accept(aut,"adc"), false);
        pass("ab",  Runner.accept(aut,"ab"),  false);
    }

    public static void zeroThenOne(){
        Automate aut = new Automate(0, new int[]{0,1},2, new char[]{'0','1'});
        aut.lier(0,0,'0');
        aut.lier(0,1,'1');
        aut.lier(1,1,'1');

        pass("EMPTY", Runner.accept(aut,""),     true);
        pass("1",     Runner.accept(aut,"1"),    true);
        pass("0",     Runner.accept(aut,"0"),    true);
        pass("000",   Runner.accept(aut,"000"),  true);
        pass("0011",  Runner.accept(aut,"0011"), true);
        pass("00101", Runner.accept(aut,"00101"),false);
        pass("10",    Runner.accept(aut,"10"),   false);
    }

    public static void digicode(){
        Automate aut = new Automate(0,new int[]{4},5, new char[]{'a','b','c'});
        // Cas d'un digicode, on autorise toutes les chaines qui se terminent par aabc
        aut.lier(0,0,'a');
        aut.lier(0,0,'b');
        aut.lier(0,0,'c');
        aut.lier(0,1,'a');
        aut.lier(1,2,'a');
        aut.lier(2,3,'b');
        aut.lier(3,4,'c');

        pass("EMPTY",   Runner.accept(aut,""),        false);
        pass("aabc",    Runner.accept(aut,"aabc"),    true);
        pass("abcaabc", Runner.accept(aut, "abcaabc"),true);
        pass("aabcabc", Runner.accept(aut, "aabcabc"),false);
    }

    public static void testAll(){
        System.out.println("************************");
        System.out.println("*       TESTING        *");
        System.out.println("************************");

        System.out.println("PART 1 : DETERMINIST");

        System.out.println("---- SIMPLE TEST 1 ----");
        simple1();
        System.out.println("---- ZERO THEN ONE ----");
        zeroThenOne();

        System.out.println("PART 2 : NON DETERMINIST");
        System.out.println("----    DIGICODE    ----");
        digicode();


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
