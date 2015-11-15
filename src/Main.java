import java.util.Scanner;

/**
 * L'intégralité du code est disponible sur github : https://github.com/johnsudaar/Automates-Tests
 * La visualisation d'un automate necessite la librairie JUNG : http://jung.sourceforge.net/
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
        System.out.println(" 3) Tester les operations entre graph (GUI)");
        System.out.println(" 4) Lancer le code d'exemple");
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
            case 3:
                testOperations();
                break;
            case 4:
                sample();
                break;
            default:
                System.out.println("Action inconnue");
                break;
        }
    }

    public static void sample(){
        // Ordre des parametres : Initial, Acceptants, nb_etats, Alphabet
        Automate a = new Automate(0,new int[]{1}, 2, new char[]{'0','1'});
        a.lier(0,1,'1');
        a.lier(1,1,'1');
        a.lier(1,0,'0');
        a.lier(0,0,'0');
        // A devrait reconnaitre que les mots qui se terminent par 1

        // Pour tester un mot il nous faut un runner
        if(Runner.accept(a,"110")){
            System.out.println("On a bien lu le mot 110 (Il y a un probleme)");
        } else {
            System.out.println("Le mot 110 n'est pas reconnu par l'automate (OK)");
        }

        if(Runner.accept(a,"001")){
            System.out.println("On a bien lu le mot 001 (OK)");
        } else {
            System.out.println("Le mot 001 n'est pas reconnu par l'automate (Il y a un probleme)");
        }

        // Des automates d'exemples sont disponibles dans la classe Sample.

        // AutomateViewer nous permet de visualiser un automate. L'etat initial est en gros et les etats acceptants sont en vert.
        new AutomateViewer(Sample.digicode(),"Titre de la fenetre");

        // Determinisation :

        new AutomateViewer(AutomateTransformer.determinize(Sample.digicode()), "Digicode determinisé");

        // Minification :

        new AutomateViewer(Sample.coursMinimize(), "Avant");
        new AutomateViewer(AutomateTransformer.minimize(Sample.coursMinimize()), "Apres");

        // Produit d'automate

        Automate i = Sample.product1();
        Automate j = Sample.product2();
        Automate ij = AutomateCalculator.product(i,j);
        new AutomateViewer(ij, "Produit de deux automates");
    }

    public static void testOperations(){
        new AutomateViewer(Sample.product1(), "A");
        new AutomateViewer(Sample.product2(), "B");
        new AutomateViewer(AutomateCalculator.product(Sample.product1(), Sample.product2()), "A * B");
    }

    public static void testAll(boolean showGUI){

        if(showGUI) {
            new AutomateViewer(AutomateTransformer.determinize(Sample.zerosThenOnes()), "0t1 d");
            new AutomateViewer(AutomateTransformer.completeAndDeterminize(Sample.zerosThenOnes()), "0t1 cd");
            new AutomateViewer(AutomateTransformer.minimize(Sample.zerosThenOnes()), "0t1 m");
            new AutomateViewer(Sample.zerosThenOnes(), "0t1");
            new AutomateViewer(AutomateTransformer.determinize(Sample.simple1()), "simple1 d");
            new AutomateViewer(AutomateTransformer.completeAndDeterminize(Sample.simple1()), "simple1 cd");
            new AutomateViewer(AutomateTransformer.minimize(Sample.simple1()), "simple1 m");
            new AutomateViewer(Sample.simple1(), "simple1");
            new AutomateViewer(AutomateTransformer.determinize(Sample.digicode()), "digicode d");
            new AutomateViewer(AutomateTransformer.completeAndDeterminize(Sample.digicode()), "digicode cd");
            new AutomateViewer(AutomateTransformer.minimize(Sample.digicode()), "digicode m");
            new AutomateViewer(Sample.digicode(), "digicode");
            new AutomateViewer(AutomateTransformer.determinize(Sample.simpleEpsilon()), "epsilons d");
            new AutomateViewer(AutomateTransformer.completeAndDeterminize(Sample.simpleEpsilon()), "epsilons cd");
            new AutomateViewer(AutomateTransformer.minimize(Sample.simpleEpsilon()), "epsilons m");
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

        System.out.println("\nPART 4 : Minimize");

        System.out.println("---- SIMPLE TEST 1 ----");
        Sample.testSimple1(AutomateTransformer.minimize(Sample.simple1()));
        System.out.println("---- ZERO THEN ONE ----");
        Sample.testZerosThenOnes(AutomateTransformer.minimize(Sample.zerosThenOnes().clone()));
        System.out.println("----    DIGICODE    ----");
        Sample.testDigicode(AutomateTransformer.minimize(Sample.digicode()));
        System.out.println("----    EPSILONS    ----");
        Sample.testSimpleEpsilon(AutomateTransformer.minimize(Sample.simpleEpsilon().clone()));

        System.out.println("\n************************");
        System.out.println("*     ALL OK (GG)      *");
        System.out.println("************************");
    }
}
