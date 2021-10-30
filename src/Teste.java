import java.io.FileNotFoundException;

public class Teste {

    public static void main(String[] args) {
        for (int i = 0; i < 25; i++ ){
            String[] context = new String[1];
            context[0] = String.format("entradas/teste%d.txt", i);
            try {
                Main.main(context);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }
}
