import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class FART{
    private JScrollPane explorerScroll;
    private JButton selectButton;
    private JButton writeButton;
    private JPanel explorerPanel;
    private JTextArea fileArea;
    private JScrollPane fileScroll;
    private JPanel filePanel;
    private JPanel mainPanel;
    private JScrollPane selectedScroll;
    private JList<String> explorerList;
    private JList<String> selectedList;
    private JButton deselectButton;
    private JPanel selectedPanel;
    private JLabel explorerLabel;
    private JLabel selectedLabel;
    private final ArrayList<String> markedArray = new ArrayList<>();
    private static HashMap<String, StringBuilder> contentMap = new HashMap<>();

    /**
     * This is the constructor of the application
     */
    public FART() {

        JFileChooser j = new JFileChooser();
        j.setMultiSelectionEnabled(true);
        int returnVal = j.showOpenDialog(mainPanel);
        File[] selectedFiles = j.getSelectedFiles();
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            // Read the selected files and store the content
            contentMap = readFiles(selectedFiles);

            // Load the array of keys into the left JList object
            explorerList.setListData(contentMap.keySet().toArray(new String[0]));
            explorerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        } else {
            JOptionPane.showMessageDialog(mainPanel, "Selecteer tenminste 1 bestand.");
            System.exit(0);
        }

        explorerList.addListSelectionListener(f -> {
            // This check is needed because a JList's value adjusts twice when the user selects something, this check
            // prevents events from happening while the value is still being adjusted.
            if (!explorerList.getValueIsAdjusting()) {
                String selectedValue = explorerList.getSelectedValue();
                displayFile(contentMap.get(selectedValue));
                // The setCaretPosition function is called to set the scroll position of the middle text field
                // to the top after the content has been loaded.
                fileArea.setCaretPosition(0);
            }
        });

        selectButton.addActionListener(e -> {
            String selectedValue = explorerList.getSelectedValue();
            // Check to see if the selected value in the JList isn't already in the array of selected keys
            if (!markedArray.contains(selectedValue)) {
                markedArray.add(selectedValue);
                // A temporary object has to be made to prevent index exceptions
                Object[] tempObj = markedArray.toArray();
                selectedList.setListData(Arrays.copyOf(tempObj, tempObj.length, String[].class));
            }
        });

        deselectButton.addActionListener(e -> {
           markedArray.remove(explorerList.getSelectedValue());
           // A temporary object has to be made to prevent index exceptions
           Object[] tempObj = markedArray.toArray();
           selectedList.setListData(Arrays.copyOf(tempObj, tempObj.length, String[].class));
        });

        writeButton.addActionListener( e -> {
            j.setDialogTitle("Specificeer een bestand om naartoe te schrijven.");
            int returnVal2 = j.showSaveDialog(mainPanel);
            if (returnVal2 == JFileChooser.APPROVE_OPTION) {
                try {
                    writeFile(j.getSelectedFile());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * This function writes the user-selected data to a user-selected file
     *
     * @param writeToFile the user-selected file where the data must be written
     * @throws IOException throws an error if something goes wrong while writing
     */
    public void writeFile(File writeToFile) throws IOException {
        FileWriter writer = new FileWriter(writeToFile);
        BufferedWriter buffWriter = new BufferedWriter(writer);
        for (String selected: markedArray) {
            buffWriter.append(contentMap.get(selected));
            buffWriter.append("\n");
        }
        buffWriter.flush();
        JOptionPane.showMessageDialog(mainPanel, "Data geschreven naar bestand");
    }

    /**
     * This function will display the content of the file, once the user selects it in the list.
     *
     * @param content The content of the selected file in the application
     */
    public void displayFile(StringBuilder content) {
        fileArea.setText(String.valueOf(content));
    }

    /**
     * This function reads all the files selected by the user, then stores all necessary information in a HashMap
     *
     * @param selectedFiles, an array of File objects containing all the files the user selected
     * @return contentMap, a HashMap<String, StringBuilder> containing the DEFINITION in the selected files as keys,
     * and the content of the files as values.
     */
    public static HashMap<String, StringBuilder> readFiles(File[] selectedFiles) {
        contentMap = new HashMap<>();
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
                JOptionPane.showMessageDialog(null, "The file - " + selectedFile + " - has not been found, Has it been deleted?");
            }

            contentMap.put(definition.toString(), fileContent);
            definition.setLength(0);
        }

        return contentMap;
    }

    public static void main(String[] args) {
        // Set look and feel according to system look and feel. If the method fails, just continue because the option
        // is not required
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
        }

        JFrame frame = new JFrame("FART - Forensic Application for Reading Text");
        frame.setContentPane(new FART().mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
