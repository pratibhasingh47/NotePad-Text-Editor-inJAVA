
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try{
                UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
                new Notepad_GUI().setVisible(true);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
    }
}
