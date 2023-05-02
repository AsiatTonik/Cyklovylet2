import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CyklovyletFrame extends JFrame{
    private JTextField textField1;
    private JTextArea textArea1;
    private JButton deleteBtn;
    private JPanel mainPanel;
    private JMenuBar bar;
    private JMenu menu;
    private JMenuItem nacti;
    private JMenuItem refresh;
    private int indexSeznamu = 0;
    private JFileChooser vyberSouboru = new JFileChooser(".");
    private File selectedFile;
    private List<Vylet> cyklovylet = new ArrayList<>();
    public static final String oddelovac = ",";


    public CyklovyletFrame() {
        menu();
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                smaz();
            }
        });
    }


    public void menu(){
        bar = new JMenuBar();
        setJMenuBar(bar);
        menu = new JMenu("Soubor");
        bar.add(menu);
        nacti = new JMenuItem("Načti");
        refresh = new JMenuItem("Refresh");
        menu.add(nacti);
        menu.add(refresh);
        nacti.addActionListener(ActionListener -> openFile());
        refresh.addActionListener(ActionListener -> refreshSoubor());


    }


    private void openFile(){
        int vysledek = vyberSouboru.showOpenDialog(this);
        if (vysledek == JFileChooser.APPROVE_OPTION) {
            selectedFile = vyberSouboru.getSelectedFile();
            nactiZeSouboru();
        }
        else{
            JOptionPane.showMessageDialog(mainPanel, "Neplatný soubor");
        }
    }

    public void nactiZeSouboru(){
        String[] polozky;
        String line;
        int poradi = 1;

        try{
            Scanner scanner = new Scanner(new BufferedReader(new FileReader(selectedFile)));
            while (scanner.hasNextLine()){
                line = scanner.nextLine();
                polozky = line.split(oddelovac);
                String cil = polozky[0].trim();
                int delka = Integer.parseInt(polozky[1].trim());
                LocalDate datum = LocalDate.parse(polozky[2]);

                Vylet vylet = new Vylet(cil,delka,datum);
                cyklovylet.add(vylet);

                textArea1.append(poradi + "." + vylet.getCil()+ "(" + vylet.getDelka() + " km)" + "\n");
                poradi++;

            }
            scanner.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void smaz(){
        int index = Integer.parseInt(textField1.getText()) - 1;

        if(index >= 0 && index <= cyklovylet.size()){
            cyklovylet.remove(index);
            zapis();
            refreshSoubor();
        }
        else{
            JOptionPane.showMessageDialog(mainPanel,"Špatně zadaný index");
        }
    }

    private void zapis(){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));
            for (Vylet v : cyklovylet){
                writer.write(v.getCil() + oddelovac + v.getDelka() + oddelovac + v.getDatum() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshSoubor(){
        textArea1.setText("");
        int poradi = 1;

        for(Vylet vylet : cyklovylet){
            textArea1.append(poradi + "." + vylet.getCil() + "(" + vylet.getDelka() + "km)" + "\n");
            poradi++;
        }

    }

    public static void main(String[] args) {
        CyklovyletFrame f = new CyklovyletFrame();
        f.setContentPane(f.mainPanel);
        f.setVisible(true);
        f.pack();
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}




