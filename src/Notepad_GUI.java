import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

public class Notepad_GUI extends JFrame {

    private JFileChooser fileChooser;

    private JTextArea textArea;

    public JTextArea getTextArea(){
        return textArea;
    }
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
        textArea.getDocument().addUndoableEditListener((UndoableEditEvent e) -> {
            undoManager.addEdit(e.getEdit());
        });
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JMenuBar menuBar = new JMenuBar();
        toolBar.add(menuBar);

        menuBar.add(addFileMenu());
        menuBar.add(addEditMenu());
        menuBar.add(addFormatMenu());
        menuBar.add(addViewMenu());

        add(toolBar, BorderLayout.NORTH);
    }

    private JMenu addFileMenu() {
        
        JMenu fileMenu = new JMenu("File");

        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener((ActionEvent e) -> {
            setTitle("Notepad");
            textArea.setText("getName()");
            
            currentFile = null;
        });
        fileMenu.add(newMenuItem);

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener((ActionEvent e) -> {
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
                    fileText.append(readText).append("\n");
                }
                
                textArea.setText(fileText.toString());
            } catch (IOException e1) {
            }
        });
        fileMenu.add(openMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener((ActionEvent e) -> {
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
        });
        fileMenu.add(saveAsMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener((ActionEvent e) -> {
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
        });
        fileMenu.add(saveMenuItem);

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener((ActionEvent e) -> {
            Notepad_GUI.this.dispose();
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }


    private JMenu addEditMenu(){
        JMenu editMenu = new JMenu("Edit");

        JMenuItem undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.addActionListener((ActionEvent e) -> {
            if(undoManager.canUndo()){
                undoManager.undo();
            }
        });
        editMenu.add(undoMenuItem);

        JMenuItem redoMenuItem = new JMenuItem("Redo");
        redoMenuItem.addActionListener((ActionEvent e) -> {
            if(undoManager.canRedo()){
                undoManager.redo();
            }
        });
        editMenu.add(redoMenuItem);

        return editMenu;
    }


    private JMenu addFormatMenu(){
        JMenu formatMenu = new JMenu("Format");

        JCheckBoxMenuItem wordWraMenuItem = new JCheckBoxMenuItem("Word Wrap");
        wordWraMenuItem.addActionListener((ActionEvent e) -> {
            boolean isChecked = wordWraMenuItem.getState();
            if(isChecked){
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
            }
            else{
                textArea.setLineWrap(false);
                textArea.setWrapStyleWord(false);
            }
        });
        formatMenu.add(wordWraMenuItem);

        JMenu aligntextMenu = new JMenu("Align Text");

        JMenuItem alignTextLeftMenuItem = new JMenuItem("Left");
        alignTextLeftMenuItem.addActionListener((ActionEvent e) -> {
            textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        });
        aligntextMenu.add(alignTextLeftMenuItem);

        JMenuItem alignTextRightMenuItem = new JMenuItem("Right");
        alignTextRightMenuItem.addActionListener((ActionEvent e) -> {
            textArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        });
        aligntextMenu.add(alignTextRightMenuItem);

        
        formatMenu.add(aligntextMenu);

        JMenuItem fontMenuItem = new JMenuItem("Font");
        fontMenuItem.addActionListener((ActionEvent e) -> {
            new FontMenu(Notepad_GUI.this).setVisible(true);
        });
        formatMenu.add(fontMenuItem);

        return formatMenu;
    }


    private JMenu addViewMenu(){
        JMenu viewMenu = new JMenu("View");

        JMenu zoomMenu = new JMenu("Zoom");

        JMenuItem zoomInMenu = new JMenuItem("Zoom in");
        zoomInMenu.addActionListener((ActionEvent e) -> {
            Font currentFont = textArea.getFont();
            textArea.setFont(new Font(
                    currentFont.getName(),
                    currentFont.getStyle(),
                    currentFont.getSize() + 4
            ));
        });
        zoomMenu.add(zoomInMenu);

        JMenuItem zoomOutMenu = new JMenuItem("Zoom out");
        zoomOutMenu.addActionListener((ActionEvent e) -> {
            Font currentFont = textArea.getFont();
            textArea.setFont(new Font(
                    currentFont.getName(),
                    currentFont.getStyle(),
                    currentFont.getSize() - 4
            ));
        });
        zoomMenu.add(zoomOutMenu);

        JMenuItem zoomRestoreMenuItem = new JMenuItem("Restore Default Zoom");
        zoomRestoreMenuItem.addActionListener((ActionEvent e) -> {
            Font currentFont = textArea.getFont();
            textArea.setFont(new Font(
                    currentFont.getName(),
                    currentFont.getStyle(),
                    12
            ));
        });
        zoomMenu.add(zoomRestoreMenuItem);


        viewMenu.add(zoomMenu);

        return viewMenu;
    }
}
