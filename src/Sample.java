/**
 * Simple automates and test methods
 * Created by johnsudaar on 26/10/15.
 */
public class Sample {

    // Detecte ABC
    public static Automate simple1(){
        Automate aut = new Automate(0, new int[]{3}, 4, new char[]{'a','b','c'});

        aut.lier(0,1,'a');
        aut.lier(1,2,'b');
        aut.lier(2,3,'c');
        return aut;
    }

    // Que des 0 puis des 1
    public static Automate zerosThenOnes(){
        Automate aut = new Automate(0, new int[]{0,1},2, new char[]{'0','1'});
        aut.lier(0,0,'0');
        aut.lier(0,1,'1');
        aut.lier(1,1,'1');
        return aut;
    }

    // N'importe quelle chaine qui se termine par abcaabc
    public static Automate digicode(){
        Automate aut = new Automate(0,new int[]{4},5, new char[]{'a','b','c'});
        // Cas d'un digicode, on autorise toutes les chaines qui se terminent par aabc
        aut.lier(0,0,'a');
        aut.lier(0,0,'b');
        aut.lier(0,0,'c');
        aut.lier(0,1,'a');
        aut.lier(1,2,'a');
        aut.lier(2,3,'b');
        aut.lier(3,4,'c');

        return aut;
    }

    // 011 ou 100
    public static Automate simpleEpsilon(){
        Automate aut = new Automate(0, new int[]{4,8}, 9, new char[]{'0','1', Automate.EPSILON});

        aut.lier(0,1,Automate.EPSILON);
        aut.lier(1,2,'0');
        aut.lier(2,3,'1');
        aut.lier(3,4,'1');
        aut.lier(0,5,Automate.EPSILON);
        aut.lier(5,6,'1');
        aut.lier(6,7,'0');
        aut.lier(7,8,'0');
        return aut;
    }

    public static Automate coursMinimize(){
        Automate a = new Automate(0,new int[]{0,1},6,new char[]{'0','1'});

        a.lier(0,1,'0');
        a.lier(1,1,'0');
        a.lier(3,1,'1');
        a.lier(3,3,'0');
        a.lier(1,2,'1');
        a.lier(2,3,'1');
        a.lier(2,4,'0');
        a.lier(4,2,'0');
        a.lier(0,4,'1');
        a.lier(4,5,'1');
        a.lier(5,5,'0');
        a.lier(5,0,'1');

        return a;
    }

    public static void testSimple1(Automate aut){
        Utilities.pass("abc", Runner.accept(aut,"abc"), true);
        Utilities.pass("a", Runner.accept(aut, "a"), false);
        Utilities.pass("dba", Runner.accept(aut, "dba"), false);
        Utilities.pass("adc", Runner.accept(aut, "adc"), false);
        Utilities.pass("ab", Runner.accept(aut, "ab"), false);
    }

    public static void testZerosThenOnes(Automate aut) {
        Utilities.pass("EMPTY", Runner.accept(aut, ""), true);
        Utilities.pass("1", Runner.accept(aut, "1"), true);
        Utilities.pass("0", Runner.accept(aut, "0"), true);
        Utilities.pass("000", Runner.accept(aut, "000"), true);
        Utilities.pass("0011", Runner.accept(aut, "0011"), true);
        Utilities.pass("00101", Runner.accept(aut, "00101"), false);
        Utilities.pass("10", Runner.accept(aut, "10"), false);
    }

    public static void testDigicode(Automate aut){
        Utilities.pass("EMPTY", Runner.accept(aut, ""), false);
        Utilities.pass("aabc", Runner.accept(aut, "aabc"), true);
        Utilities.pass("abcaabc", Runner.accept(aut, "abcaabc"), true);
        Utilities.pass("aabcabc", Runner.accept(aut, "aabcabc"), false);
    }

    public static void testSimpleEpsilon(Automate aut){
        Utilities.pass("EMPTY", Runner.accept(aut,""), false);
        Utilities.pass("011", Runner.accept(aut,"011"), true);
        Utilities.pass("100", Runner.accept(aut,"100"), true);
        Utilities.pass("0111", Runner.accept(aut,"0111"), false);
        Utilities.pass("0", Runner.accept(aut, "0"), false);
    }

}
