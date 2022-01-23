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

    public FART() {
        JFileChooser j = new JFileChooser();
        j.setMultiSelectionEnabled(true);
        int returnVal = j.showOpenDialog(mainPanel);
        File[] selectedFiles = j.getSelectedFiles();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            contentMap = readFiles(selectedFiles);
            String[] files = contentMap.keySet().toArray(new String[0]);
            explorerList.setListData(files);
            explorerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            explorerList.addListSelectionListener(f -> {
                String selectedValue = explorerList.getSelectedValue();
                displayFile(contentMap.get(selectedValue));
            });
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Please select 1 or more files.");
            System.exit(0);
        }

        selectButton.addActionListener(e -> {
            String selectedValue = explorerList.getSelectedValue();
            if (!markedArray.contains(selectedValue)) {
                markedArray.add(selectedValue);
                Object[] tempObj = markedArray.toArray();
                selectedList.setListData(Arrays.copyOf(tempObj, tempObj.length, String[].class));
            }
        });

        deselectButton.addActionListener(e -> {
           String selectedValue = explorerList.getSelectedValue();
           markedArray.remove(selectedValue);
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

    public void displayFile(StringBuilder content) {
        fileArea.setText(String.valueOf(content));
    }

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
        frame.setContentPane(new FART().mainPanel);;
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // TODO: optimaliseer
    }
}
