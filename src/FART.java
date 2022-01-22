import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class FART{
    private JTextArea explorerArea;
    private JScrollPane explorerScroll;
    private JButton markButton;
    private JButton writeButton;
    private JPanel explorerPanel;
    private JTextArea fileArea;
    private JScrollPane fileScroll;
    private JPanel filePanel;
    private JPanel mainPanel;
    private JScrollPane selectedScroll;
    private JTextArea selectedArea;

    public FART() {
        markButton.addActionListener( e -> System.out.println("cringe"));

        writeButton.addActionListener( e -> System.out.println("cringe"));


        JFileChooser j = new JFileChooser();
        j.setMultiSelectionEnabled(true);
        int returnVal = j.showOpenDialog(mainPanel);
        File[] selectedFiles = j.getSelectedFiles();
        System.out.println(Arrays.toString(selectedFiles));

        if (returnVal == 0) {
            HashMap<String, StringBuilder> contentMap = readFiles(selectedFiles);
            // System.out.println(contentMap)
        }

    }

    public static HashMap<String, StringBuilder> readFiles(File[] selectedFiles) {
        HashMap<String, StringBuilder> contentMap = new HashMap<>();
        StringBuilder definition = new StringBuilder();
        StringBuilder fileContent = null;
        boolean nextLine = true;
        for (File selectedFile : selectedFiles) {
            try {
                Scanner fileReader = new Scanner(selectedFile);
                fileContent = new StringBuilder();
                while (fileReader.hasNextLine()) {
                    String line = fileReader.nextLine();
                    if (line.startsWith("DEFINITION")) {
                        definition.append(line.split("\s{2}")[1].strip());
                        fileContent.append(line).append('\n');
                        while (nextLine) {
                            line = fileReader.nextLine();
                            if (!line.startsWith("ACCESSION")) {
                                definition.append(line.strip());
                                fileContent.append(line).append('\n');
                            } else {
                                nextLine = false;
                            }
                        }
                    }
                    fileContent.append(line).append('\n');
                }
            } catch (FileNotFoundException e) {
                System.out.println("The file - " + selectedFile + " - has not been found, Has it been deleted?");
            }
            contentMap.put(definition.toString(), fileContent);
            definition.setLength(0);
        }

        return contentMap;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("FART - Forensic Application for Reading Text");
        frame.setContentPane(new FART().mainPanel);;
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // TODO: Fix de GUI, voeg logica toe aan de GUI, optimaliseer
    }
}
