import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Programming utilities
 * Created by johnsudaar on 26/10/15.
 */
public class Utilities {

    public static void pass(String text, boolean state, boolean expected) {
        if (state == expected)
            System.out.println(text+" ("+state+"):  OK !");
        else{
            System.out.println(text+" ("+state+"):  FAILED!");
            System.out.println("/!\\ ERRORED ! /!\\ ");
            try {
                System.in.read();
            }catch(IOException e){

            }
            System.exit(1);
        }
    }

    public static boolean equalLists(List<Integer> a, List<Integer> b){
        // Check for sizes and nulls
        if ((a.size() != b.size()) || (a == null && b!= null) || (a != null && b== null)){
            return false;
        }

        if (a == null && b == null) return true;

        // Sort and compare the two lists
        Collections.sort(a);
        Collections.sort(b);
        return a.equals(b);
    }
}
