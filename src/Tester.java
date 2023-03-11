import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

public class Tester {
    private static final String MAIN_CLASS = "Main";

    public static void main(String[] args) {
        boolean passed = true;

        //standard
        passed &= passedTestNetzwerk("", "Configurations: Random: false, Pfad: src\\spanningTreeInputs\\spanningTreeInput.txtOutput of Spannig tree:Root: BA-BC-DD-EE-BF-E");

        //eigene datei
        passed &= passedTestNetzwerk("src\\spanningTreeInputs\\tests\\spanningTreeInputTest1.txt", "src\\spanningTreeInputs\\tests\\spanningTreeInputTest1.txtConfigurations: Random: false, Pfad: src\\spanningTreeInputs\\tests\\spanningTreeInputTest1.txtOutput of Spannig tree:Root: BRoot: CA-BC-DD-CE-DF-EA-BB-ED-CE-DF-E");

        if (passed) {
            System.out.println("Alle Tests bestanden :-)");
        } else {
            System.out.println("Leider nicht alle Tests bestanden :-(");
        }

    }


    /**
     * Ueberprueft ob Aufruf den erwarteten Ausgabestring beinhaltet.
     *
     * @param arg          Programmargument
     * @param resultString String, welcher als Ausgabe erwartet wird
     * @return
     */
    private static boolean passedTestNetzwerk(String arg, String resultString) {
        // Der System.out Stream muss umgebogen werden, damit dieser spaeter ueberprueft werden kann.
        PrintStream normalerOutput = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        String[] args = {arg};
        try {
            // MainClass mittels Reflection bekommen und main Methode aufrufen
            Class<?> mainClass = Class.forName(MAIN_CLASS);
            Method mainMethod = mainClass.getDeclaredMethod("main", String[].class);
            mainMethod.invoke(null, (Object) args);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Main-Klasse konnte nicht geladen werden, bitte Konfiguration pruefen.");
            System.exit(1);
        } finally {
            // System.out wieder zuruecksetzen
            System.setOut(normalerOutput);
        }

        // Ergebnisse ueberpruefen.
        String output = baos.toString();
        String[] lines = output.split(System.lineSeparator());
        // Pryefe ob eine Zeile in der Ausgabe dem Format entspricht
        StringBuilder conclusion = new StringBuilder();
        for (String line : lines) {
            conclusion.append(line);
            conclusion.append(" ");
            //System.out.println("-" + line + "-");
            //System.out.println("-" + resultString + "-");
            // keine Leerzeichen beachten
        }

        if (conclusion.toString().replace(" ", "").strip().equals(resultString.replace(" ", "").strip())) {
            return true;
        }
        System.err.println("Feher bei: '" + arg + "'. Erwartetes Ergebnis: '" + resultString + "', erhaltenes Ergebnis: '" + output.replace(System.lineSeparator(), "") + "'");
        return false;
    }
}
