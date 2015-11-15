import javax.rmi.CORBA.Util;
import java.awt.*;
import java.io.IOException;
import java.util.Scanner;

/**
 * Point d'entrée d'un programme
 * Created by johnsudaar on 24/10/15.
 */

public class Main {

    public static void main(String[] args) {
        System.out.println("****************************");
        System.out.println("*     ILSF : AUTOMATES     *");
        System.out.println("* Hurter Jonathan - ENSIIE *");
        System.out.println("****************************");
        System.out.println("\nActions : ");
        System.out.println(" 1) Lancer les tests unitaires (sans GUI)");
        System.out.println(" 2) Lancer les tests unitaires (avec GUI)");
        System.out.print("\nChoix : ");

        Scanner in = new Scanner(System.in);

        int choice = in.nextInt();

        switch(choice){
            case 1:
                testAll(false);
                break;
            case 2:
                testAll(true);
                break;
            default:
                System.out.println("Action inconnue");
                break;
        }
    }



    public static void testAll(boolean showGUI){

        if(showGUI) {
            new AutomateViewer(AutomateTransformer.determinize(Sample.zerosThenOnes()), "0t1 d");
            new AutomateViewer(AutomateTransformer.completeAndDeterminize(Sample.zerosThenOnes()), "0t1 cd");
            new AutomateViewer(Sample.zerosThenOnes(), "0t1");
            new AutomateViewer(AutomateTransformer.determinize(Sample.simple1()), "simple1 d");
            new AutomateViewer(AutomateTransformer.completeAndDeterminize(Sample.simple1()), "simple1 cd");
            new AutomateViewer(Sample.simple1(), "simple1");
            new AutomateViewer(AutomateTransformer.determinize(Sample.digicode()), "digicode d");
            new AutomateViewer(AutomateTransformer.completeAndDeterminize(Sample.digicode()), "digicode cd");
            new AutomateViewer(Sample.digicode(), "digicode");
            new AutomateViewer(AutomateTransformer.determinize(Sample.simpleEpsilon()), "epsilons d");
            new AutomateViewer(AutomateTransformer.completeAndDeterminize(Sample.simpleEpsilon()), "epsilons cd");
            new AutomateViewer(Sample.simpleEpsilon(),"epsilons");
        }


        System.out.println("************************");
        System.out.println("*       TESTING        *");
        System.out.println("************************");

        System.out.println("\nPART 1 : No transform");

        System.out.println("---- SIMPLE TEST 1 ----");
        Sample.testSimple1(Sample.simple1());
        System.out.println("---- ZERO THEN ONE ----");
        Sample.testZerosThenOnes(Sample.zerosThenOnes().clone());
        System.out.println("----    DIGICODE    ----");
        Sample.testDigicode(Sample.digicode());
        System.out.println("----    EPSILONS    ----");
        Sample.testSimpleEpsilon(Sample.simpleEpsilon().clone());


        System.out.println("\nPART 2 : Determninize");

        System.out.println("\nTesting determinize function\n");
        Utilities.pass("Simple 1 : NOT MODIFIED", Sample.simple1().isDeterminist(), true);
        Utilities.pass("Simple 1 : DETERMINIZED", AutomateTransformer.determinize(Sample.simple1()).isDeterminist(), true);
        Utilities.pass("ZerosThenOnes : NOT MODIFIED", Sample.zerosThenOnes().isDeterminist(), true);
        Utilities.pass("ZerosThenOnes : DETERMINIZED", AutomateTransformer.determinize(Sample.zerosThenOnes()).isDeterminist(), true);
        Utilities.pass("Digicode : NOT MODIFIED", Sample.digicode().isDeterminist(), false);
        Utilities.pass("Digicode : DETERMINIZED", AutomateTransformer.determinize(Sample.digicode()).isDeterminist(), true);
        Utilities.pass("SimpleEpsilon : NOT MODIFIED", Sample.simpleEpsilon().isDeterminist(), false);
        Utilities.pass("SimpleEpsilon : DETERMINIZED", AutomateTransformer.determinize(Sample.simpleEpsilon()).isDeterminist(), true);
        System.out.println("\nUnit testing determinized automates\n");
        System.out.println("---- SIMPLE TEST 1 ----");
        Sample.testSimple1(AutomateTransformer.determinize(Sample.simple1()));
        System.out.println("---- ZERO THEN ONE ----");
        Sample.testZerosThenOnes(AutomateTransformer.determinize(Sample.zerosThenOnes()));
        System.out.println("----    DIGICODE    ----");
        Sample.testDigicode(AutomateTransformer.determinize(Sample.digicode()));
        System.out.println("----    EPSILONS    ----");
        Sample.testSimpleEpsilon(AutomateTransformer.determinize(Sample.simpleEpsilon()));

        System.out.println("\nPART 3 : Complete and determinize");

        System.out.println("\nTesting Complete and determinized function\n");

        Utilities.pass("Simple 1", AutomateTransformer.completeAndDeterminize(Sample.simple1()).isDeterminist(), true);
        Utilities.pass("ZerosThenOnes", AutomateTransformer.completeAndDeterminize(Sample.zerosThenOnes()).isDeterminist(), true);
        Utilities.pass("Digicode", AutomateTransformer.completeAndDeterminize(Sample.digicode()).isDeterminist(), true);
        Utilities.pass("SimpleEpsilon : DETERMINIZED", AutomateTransformer.completeAndDeterminize(Sample.simpleEpsilon()).isDeterminist(), true);
        System.out.println("\nUnit testing determinized automates\n");
        System.out.println("---- SIMPLE TEST 1 ----");
        Sample.testSimple1(AutomateTransformer.completeAndDeterminize(Sample.simple1()));
        System.out.println("---- ZERO THEN ONE ----");
        Sample.testZerosThenOnes(AutomateTransformer.completeAndDeterminize(Sample.zerosThenOnes()));
        System.out.println("----    DIGICODE    ----");
        Sample.testDigicode(AutomateTransformer.completeAndDeterminize(Sample.digicode()));
        System.out.println("----    EPSILONS    ----");
        Sample.testSimpleEpsilon(AutomateTransformer.completeAndDeterminize(Sample.simpleEpsilon()));


        System.out.println("\n************************");
        System.out.println("*     ALL OK (GG)      *");
        System.out.println("************************");
    }
}
