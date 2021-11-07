import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Teste {

    public static void main(String[] args) throws FileNotFoundException {

        System.setOut(new PrintStream(new FileOutputStream("log_file.txt")));
        List<Integer> blackList = new ArrayList<>();
        blackList.add(4);
        blackList.add(5);
        blackList.add(8);
        blackList.add(11);
        blackList.add(15);
        blackList.add(20);
        blackList.add(21);
        blackList.add(22);

        final int INIT = 1;
        final int END = 24;
        for (int i = END; i >= INIT; i-- ){
            if(!(blackList.contains(i))){
                String[] context = new String[1];
                context[0] = String.format("entradas/teste%d.txt", i);
                System.out.printf("PROXIMO TESTE \n\n Teste %d: \n", i);
                try {

                    Main.main(context);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
