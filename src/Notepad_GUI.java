import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

public class Notepad_GUI extends JFrame {

    private JFileChooser fileChooser;

    private JTextArea textArea;
    private File currentFile;

    private UndoManager undoManager;

    public Notepad_GUI() {
        super("Notepad");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("src/assets"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        undoManager = new UndoManager();

        addGuiComponents();
    }

    private void addGuiComponents() {
        addToolbar();

        textArea = new JTextArea();
        textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e){
                undoManager.addEdit(e.getEdit());
            }
        });
        add(textArea, BorderLayout.CENTER);
    }

    private void addToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JMenuBar menuBar = new JMenuBar();
        toolBar.add(menuBar);

        menuBar.add(addFileMenu());
        menuBar.add(addEditMenu());

        add(toolBar, BorderLayout.NORTH);
    }

    private JMenu addFileMenu() {
        
        JMenu fileMenu = new JMenu("File");

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTitle("Notepad");
                textArea.setText("getName()");

                currentFile = null;
            }
        });
        fileMenu.add(newMenuItem);

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(Notepad_GUI.this);

                if (result != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                try {
                    newMenuItem.doClick();

                    File selectedFile = fileChooser.getSelectedFile();

                    currentFile = selectedFile;

                    setTitle(selectedFile.getName());

                    FileReader fileReader = new FileReader(selectedFile);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    StringBuilder fileText = new StringBuilder();
                    String readText;
                    while ((readText = bufferedReader.readLine()) != null) {
                        fileText.append(readText + "\n");
                    }

                    textArea.setText(fileText.toString());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
        });
        fileMenu.add(openMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showSaveDialog(Notepad_GUI.this);
                if (result != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                try {
                    File selectedFile = fileChooser.getSelectedFile();

                    String fileName = selectedFile.getName();
                    if (!fileName.substring(fileName.length() - 4).equalsIgnoreCase(".txt")) {
                        selectedFile = new File(selectedFile.getAbsoluteFile() + ".txt");

                        selectedFile.createNewFile();

                        FileWriter fileWriter = new FileWriter(selectedFile);
                        BufferedWriter bufferedWriter;
                        bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write(textArea.getText());

                        bufferedWriter.close();
                        fileWriter.close();

                        setTitle(fileName);

                        currentFile = selectedFile;

                        JOptionPane.showMessageDialog(Notepad_GUI.this, "Saved File !");
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        fileMenu.add(saveAsMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentFile == null) {
                    saveAsMenuItem.doClick();
                }

                if(currentFile == null){
                    return;
                }

                try {
                    FileWriter fileWriter = new FileWriter(currentFile);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(textArea.getText());
                    bufferedWriter.close();
                    fileWriter.close();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        fileMenu.add(saveMenuItem);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Notepad_GUI.this.dispose();
            }

        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }


    private JMenu addEditMenu(){
        JMenu editMenu = new JMenu("Edit");

        JMenuItem undoMenuItem = new JMenuItem("Undo");
        editMenu.add(undoMenuItem);

        JMenuItem redoMenuItem = new JMenuItem("Redo");
        editMenu.add(redoMenuItem);

        return editMenu;
    }

}
