import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Teste {

    public static void main(String[] args) throws FileNotFoundException {

        System.setOut(new PrintStream(new FileOutputStream("log_file.txt")));
        List<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(5);
        list.add(8);
        list.add(11);
        list.add(15);
        list.add(20);
        list.add(21);
        list.add(22);

        final int INIT = 0;
        final int END = 24;
        for (int i = INIT; i <= END; i++ ){
            String[] context = new String[1];
            context[0] = String.format("entradas/teste%d.txt", i);
            System.out.printf("PROXIMO TESTE \n\n Teste %d: \n", i);
            try {
                if(!(list.contains(i)))
                    Main.main(context);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }
}
