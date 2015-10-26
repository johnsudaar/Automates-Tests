import javax.rmi.CORBA.Util;

/**
 * Point d'entrée d'un programme
 * Created by johnsudaar on 24/10/15.
 */

public class Main {

    public static void main(String[] args){
        testAll(false);
    }



    public static void testAll(boolean showGUI){
        System.out.println("************************");
        System.out.println("*       TESTING        *");
        System.out.println("************************");

        System.out.println("\nPART 1 : No transform");

        System.out.println("---- SIMPLE TEST 1 ----");
        Sample.testSimple1(Sample.simple1());
        System.out.println("---- ZERO THEN ONE ----");
        Sample.testZerosThenOnes(Sample.zerosThenOnes());
        System.out.println("----    DIGICODE    ----");
        Sample.testDigicode(Sample.digicode());

        System.out.println("\nPART 2 : Determninize");

        System.out.println("\nTesting determinize function\n");
        Utilities.pass("Simple 1 : NOT MODIFIED", Sample.simple1().isDeterminist(), true);
        Utilities.pass("Simple 1 : DETERMINIZED", AutomateTransformer.determinize(Sample.simple1()).isDeterminist(), true);
        Utilities.pass("ZerosThenOnes : NOT MODIFIED", Sample.zerosThenOnes().isDeterminist(), true);
        Utilities.pass("ZerosThenOnes : DETERMINIZED", AutomateTransformer.determinize(Sample.zerosThenOnes()).isDeterminist(), true);
        Utilities.pass("Digicode : NOT MODIFIED", Sample.digicode().isDeterminist(), false);
        Utilities.pass("Digicode : DETERMINIZED", AutomateTransformer.determinize(Sample.digicode()).isDeterminist(), true);

        System.out.println("\nUnit testing determinized automates\n");
        System.out.println("---- SIMPLE TEST 1 ----");
        Sample.testSimple1(AutomateTransformer.determinize(Sample.simple1()));
        System.out.println("---- ZERO THEN ONE ----");
        Sample.testZerosThenOnes(AutomateTransformer.determinize(Sample.zerosThenOnes()));
        System.out.println("----    DIGICODE    ----");
        Sample.testDigicode(AutomateTransformer.determinize(Sample.digicode()));

        // Test avec affichage graphique
        if(showGUI) {
            new AutomateViewer(AutomateTransformer.determinize(Sample.zerosThenOnes()), "0t1 d");
            new AutomateViewer(Sample.zerosThenOnes(), "0t1");
            new AutomateViewer(AutomateTransformer.determinize(Sample.simple1()), "simple1 d");
            new AutomateViewer(Sample.simple1(), "simple1");
            new AutomateViewer(AutomateTransformer.determinize(Sample.digicode()), "digicode d");
            new AutomateViewer(Sample.digicode(), "digicode");
        }

        System.out.println("\n************************");
        System.out.println("*     ALL OK (GG)      *");
        System.out.println("************************");
    }
}
