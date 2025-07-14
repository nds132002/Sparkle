import java.util.*;
import org.apache.commons.math3.distribution.BinomialDistribution;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ShinyHunter {

    int generation;
    String method;
    boolean haveCharm;
    String target;
    boolean ongoing;
    double odds;
    int encounters = 0;
    int seconds = 0;
    boolean done = false;
    int systems = 0;
    Font customFont;
    ArrayList<String> gen2methods = new ArrayList<String>();
    ArrayList<String> gen3methods = new ArrayList<String>();
    ArrayList<String> gen4methods = new ArrayList<String>();
    ArrayList<String> gen5methods = new ArrayList<String>();
    ArrayList<String> gen6methods = new ArrayList<String>();
    ArrayList<String> gen7methods = new ArrayList<String>();
    ArrayList<String> filenames = new ArrayList<String>();
    ArrayList<String> savefiles = new ArrayList<String>();
    String[] gens = new String[6];
    String[] methods = new String[10];
    String[] pokemon = new String[827];

    private static double calculateOdds(int generation, String method, boolean haveCharm, int encounters) {
        if(generation == 2 || generation == 3) {
            if(method == "BR") {
                return 64;
            }
            else {
                return 8192;
            }
        }
        if(generation == 4) {
            if(method.equals("RE") || method.equals("SR")) {
                return 8192;
            }
            if(method.equals("MM")) {
                return 1638.4;
            }
            if(method.equals("PR")) {
                return pokeRadarCalc(encounters, generation);
            }
        }
        if(generation == 5) {
            if(haveCharm) {
                if(method.equals("RE") || method.equals("SR")) {
                    return 2730.7;
                }
                if(method.equals("MM")) {
                    return 1024;
                }
            }
            else { 
                if(method.equals("RE") || method.equals("SR")) {
                    return 8192;
                }
                if(method.equals("MM")) {
                    return 1365.3;
                }
            }
        }
        if(generation == 6) {
            if(haveCharm) {
                if(method.equals("RE") || method.equals("SR")) {
                    return 1365.3;
                }
                if(method.equals("MM")) {
                    return 512;
                }
                if(method.equals("FS")) {
                    return 585.1;
                }
                if(method.equals("HO")) {
                    return 273.1;
                }
                if(method.equals("CF")) {
                    return chainFishingCalc(encounters, haveCharm);
                }
                if(method.equals("DN")) {
                    return dexNavCalc(encounters, haveCharm);
                }
                if(method.equals("PR")) {
                    return pokeRadarCalc(encounters, generation);
                }
            }
            else {
                if(method.equals("RE") || method.equals("SR")) {
                    return 4096;
                }
                if(method.equals("MM")) {
                    return 682.7;
                }
                if(method.equals("FS")) {
                    return 819.2;
                }
                if(method.equals("HO")) {
                    return 819.2;
                }
                if(method.equals("CF")) {
                    return chainFishingCalc(encounters, haveCharm);
                }
                if(method.equals("DN")) {
                    return dexNavCalc(encounters, haveCharm);
                }
                if(method.equals("PR")) {
                    return pokeRadarCalc(encounters, generation);
                }
            }
        }
        if(generation == 7) {
            if(haveCharm) {
                if(method.equals("RE") || method.equals("SR")) {
                    return 1365.3;
                }
                if(method.equals("MM")) {
                    return 512;
                }
                if(method.equals("SOS")) {
                    return sosCalc(encounters, haveCharm);
                }
            }
            else {
                if(method.equals("RE") || method.equals("SR")) {
                    return 4096;
                }
                if(method.equals("MM")) {
                    return 682.7;
                }
                if(method.equals("SOS")) {
                    return sosCalc(encounters, haveCharm);
                }
            }
        }
        return 0;
    }

    private static double pokeRadarCalc(int encounters, int generation) {
        double radarOdds;
        if (encounters > 40) {
            radarOdds = 1 / (Math.ceil(65535 / (8200 - 40 * 200)) / 65536);
        }
        else {
            radarOdds = 1 / ((Math.ceil(65535 / (8200 - encounters * 200)) + 1) / 65536);
        }
        if(generation == 6)
        {
            radarOdds *= 2;
        }
        return radarOdds;
    }

    private static double chainFishingCalc(int encounters, boolean haveCharm) {
        int rolls = 1;

        if (haveCharm) {
            rolls += 2;
        }
        if(encounters < 20) {
            rolls += 2 * encounters;
        }
        else {
            rolls += 40;
        }

        return 4096 / rolls;
    }

    private static double dexNavCalc(int encounters, boolean haveCharm) {
        int rolls = 1;
        
        if(haveCharm)
        {
            rolls += 1;
        }
        for(int i = 1; i < encounters+1; i++) {
            if(i > 901) {
                continue;
            }
            if(i%17 == 0) {
                rolls += 0.475;
            }
        }

        return 4096 / rolls;
    }

    private static double sosCalc(int encounters, boolean haveCharm) {
        int rolls = 1;

        if(encounters >= 11 && encounters <= 20) {
            rolls += 4;
        }
        if(encounters >= 21 && encounters <= 30) {
            rolls += 8;
        }
        if(encounters >= 31) {
            rolls += 12;
        }

        if(haveCharm) {
            rolls += 2;
        }

        return 4096 / rolls;
    }

    private static double binomDist(double odds, int encounters) {
        BinomialDistribution binomialDist = new BinomialDistribution(encounters, 1/odds);
        return (1 - binomialDist.cumulativeProbability(0)) * 100;
    }

    private boolean methodGenCheck(int generation, String method) {
        if(generation == 2)
        {
            if(gen2methods.contains(method)){
                return true;
            }
            else {
                return false;
            }
        }
        if(generation == 3)
        {
            if(gen3methods.contains(method)){
                return true;
            }
            else {
                return false;
            }
        }
        if(generation == 4)
        {
            if(gen4methods.contains(method)) {
                return true;
            }
            else {
                return false;
            }
        }
        if(generation == 5)
        {
            if(gen5methods.contains(method)) {
                return true;
            }
            else {
                return false;
            }
        }
        if(generation == 6)
        {
            if(gen6methods.contains(method)) {
                return true;
            }
            else {
                return false;
            }
        }
        if(generation == 7)
        {
            if(gen7methods.contains(method)) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    private void populateMethodLists() {
        gen2methods.add("SR");
        gen2methods.add("RE");
        gen2methods.add("BR");

        gen3methods.add("SR");
        gen3methods.add("RE");

        gen4methods.add("SR");
        gen4methods.add("RE");
        gen4methods.add("MM");
        gen4methods.add("PR");

        gen5methods.add("SR");
        gen5methods.add("RE");
        gen5methods.add("MM");

        gen6methods.add("SR");
        gen6methods.add("RE");
        gen6methods.add("MM");
        gen6methods.add("FS");
        gen6methods.add("HO");
        gen6methods.add("CF");
        gen6methods.add("DN");
        gen6methods.add("PR");

        gen7methods.add("SR");
        gen7methods.add("RE");
        gen7methods.add("MM");
        gen7methods.add("SOS");
    }

    private void populateList(String listType) {
        File dir;
        int i = 0;
        if(listType == "pokemon") {
            dir = new File("sprites");
        } else {
            dir = new File("saves");
        }
        File[] files = dir.listFiles();

        if(files != null) {
            for(File file: files) {
                if(file.isFile()) {
                    if(listType == "pokemon") {
                        filenames.add(file.getName());
                        pokemon[i] = file.getName().substring(0, file.getName().length() - 4);
                        i++;
                    } else {
                        savefiles.add(file.getName().substring(0, file.getName().length() - 4));
                    }
                }
            }
        }
    }

    private void populateOptions() {
        gens[0] = "2";
        gens[1] = "3";
        gens[2] = "4";
        gens[3] = "5";
        gens[4] = "6";
        gens[5] = "7";

        methods[0] = "RE";
        methods[1] = "SR";
        methods[2] = "MM";
        methods[3] = "BR";
        methods[4] = "PR";
        methods[5] = "CF";
        methods[6] = "FS";
        methods[7] = "HO";
        methods[8] = "DN";
        methods[9] = "SOS";
    }

    private void save() {
        try {
            FileWriter saving = new FileWriter("saves/save-" + target + ".txt", false);
            saving.write("" + encounters + System.getProperty("line.separator"));
            saving.write("" + generation + System.getProperty("line.separator"));
            saving.write(method + System.getProperty("line.separator"));
            saving.write(String.valueOf(haveCharm) + System.getProperty("line.separator"));
            saving.write(target + System.getProperty("line.separator"));
            saving.write("" + seconds + System.getProperty("line.separator"));
            saving.write("" + systems + System.getProperty("line.separator"));
            saving.write(String.valueOf(done) + System.getProperty("line.separator"));
            saving.close();
        } catch (IOException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
    }

    private void load(String filename) {
        try {
            File saveFile = new File("saves/"+filename+".txt");
            Scanner loading = new Scanner(saveFile);
            encounters = Integer.parseInt(loading.nextLine());
            generation = Integer.parseInt(loading.nextLine());
            method = loading.nextLine();
            haveCharm = Boolean.valueOf(loading.nextLine());
            target = loading.nextLine();
            seconds = Integer.parseInt(loading.nextLine());
            systems = Integer.parseInt(loading.nextLine());
            done = Boolean.valueOf(loading.nextLine());
            System.out.println(done);
            loading.close();
        } catch (IOException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
    }

    private void loadFont() {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("font/PokemonGb-RAeo.ttf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        ShinyHunter obj = new ShinyHunter();

        obj.populateMethodLists();
        obj.populateList("pokemon");
        obj.populateList("savefiles");
        obj.populateOptions();
        obj.loadFont();

        System.out.println("Welcome to Sparkle, a Shiny Hunting Tracker!\n");

        int loadOrNot = JOptionPane.showConfirmDialog(null, "Do you wish to load a file?", "Load File", 0);
        if(loadOrNot == 0 && !(obj.savefiles.size() == 0)) {
            String[] saveFileNames = new String[obj.savefiles.size()];
            for(int i = 0; i < obj.savefiles.size(); i++) {
                saveFileNames[i] = obj.savefiles.get(i);
            }
            Object selectedSavefile = JOptionPane.showInputDialog(null, "Please select the save file you wish to load", "Load File", JOptionPane.QUESTION_MESSAGE, null, saveFileNames, saveFileNames[0]);
            obj.load(selectedSavefile.toString());
        } else if (loadOrNot == 1 || obj.savefiles.size() == 0) {
            Object selectedGeneration = JOptionPane.showInputDialog(null, "Please enter the generation of Pokémon games you are hunting in:", "Generation", JOptionPane.QUESTION_MESSAGE, null, obj.gens, obj.gens[0]);
            obj.generation = Integer.parseInt(selectedGeneration.toString());
            
            Object selectedMethod = JOptionPane.showInputDialog(null, "Please enter the method you are using to Shiny Hunt as follows:\n\nRandom Encounters: RE\nSoft Resets: SR\nMasuda Method: MM\nGen 2 Shiny Breeding: BR\nPoke Radar: PR\nChain Fishing: CF\nFriend Safari: FS\nHorde Encounter: HO\nDexNav: DN\nSOS Battles: SOS", "Method", JOptionPane.QUESTION_MESSAGE, null, obj.methods, obj.methods[0]);
            obj.method = selectedMethod.toString();

            while(!obj.methodGenCheck(obj.generation, obj.method)) {
                Object selectedMethodRetry = JOptionPane.showInputDialog(null, "Please enter the method you are using to Shiny Hunt as follows:\n\nRandom Encounters: RE\nSoft Resets: SR\nMasuda Method: MM\nGen 2 Shiny Breeding: BR\nPoke Radar: PR\nChain Fishing: CF\nFriend Safari: FS\nHorder Encounter: HO\nDexNav: DN\nSOS Battles: SOS", "Method", JOptionPane.QUESTION_MESSAGE, null, obj.methods, obj.methods[0]);
                obj.method = selectedMethodRetry.toString();
            }

            if(obj.generation == 2 || obj.generation == 3 || obj.generation == 4)
            {
                obj.haveCharm = false;
            }
            else {
                int charmCheck = JOptionPane.showConfirmDialog(null, "Do you have the Shiny Charm?", "Shiny Charm", 0);
                if(charmCheck == 0) {
                    obj.haveCharm = true;
                }
                else if(charmCheck == 1) {
                    obj.haveCharm = false;
                }
            }

            int selectedSystems = JOptionPane.showConfirmDialog(null, "Are you hunting on multiple systems?", "Systems", 0);
            if(selectedSystems == 0) {
                String systemCount = JOptionPane.showInputDialog("Enter the number of systems you are using:");
                obj.systems = Integer.parseInt(systemCount);
            } else if(selectedSystems == 1) {
                obj.systems = 1;
            }

            Object selectedTarget = JOptionPane.showInputDialog(null, "What is your Target Pokémon?", "Pokémon", JOptionPane.QUESTION_MESSAGE, null, obj.pokemon, obj.pokemon[0]);
            obj.target = selectedTarget.toString();
        }

        obj.odds = Math.round(calculateOdds(obj.generation, obj.method, obj.haveCharm, obj.encounters));

        JFrame frame = new JFrame("Sparkle");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                obj.save();
                frame.dispose();
                System.exit(0);
            }
        });

        try {

            Image background = ImageIO.read(new File("sparkle.png"));
            ShinyPanel panel = new ShinyPanel(background);

            ImageIcon targetIcon = new ImageIcon(obj.getClass().getResource("/sprites/" + obj.target + ".png"));
            Image targetImage = targetIcon.getImage();
            Image newTargetImage = targetImage.getScaledInstance(136, 112, java.awt.Image.SCALE_SMOOTH);
            JLabel targetLabel = new JLabel(new ImageIcon(newTargetImage));
            targetLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel encounterLabel = new JLabel("Encounters: " + obj.encounters);
            encounterLabel.setFont(new Font(obj.customFont.getFontName(), Font.BOLD, 15));
            encounterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            encounterLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JLabel oddsLabel = new JLabel("Odds: 1/" + obj.odds);
            oddsLabel.setFont(new Font(obj.customFont.getFontName(), Font.PLAIN, 8));
            oddsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            oddsLabel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

            String distStr = String.format("%.02f", binomDist(obj.odds, obj.encounters));
            JLabel binomLabel = new JLabel("Binomial Distribution: " + distStr);
            binomLabel.setFont(new Font(obj.customFont.getFontName(), Font.PLAIN, 8));
            binomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            binomLabel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

            JButton addEncounter = new JButton("+");
            addEncounter.setFont(new Font(obj.customFont.getFontName(), Font.PLAIN, 10));
            addEncounter.setBackground(Color.RED);
            addEncounter.setAlignmentX(Component.CENTER_ALIGNMENT);
            addEncounter.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    obj.encounters += obj.systems;
                    encounterLabel.setText("Encounters: " + obj.encounters);
                    obj.odds = Math.round(calculateOdds(obj.generation, obj.method, obj.haveCharm, obj.encounters));
                    oddsLabel.setText("Odds: 1/" + obj.odds);
                    String distStr = String.format("%.02f", binomDist(obj.odds, obj.encounters));
                    binomLabel.setText("Binomial Distribution: " + distStr);
                }
            });

            JButton subtractEncounter = new JButton("-");
            subtractEncounter.setFont(new Font(obj.customFont.getFontName(), Font.PLAIN, 10));
            subtractEncounter.setBackground(Color.WHITE);
            subtractEncounter.setAlignmentX(Component.CENTER_ALIGNMENT);
            subtractEncounter.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    if(obj.encounters > 0) {
                        obj.encounters -= obj.systems;
                        encounterLabel.setText("Encounters: " + obj.encounters);
                        obj.odds = Math.round(calculateOdds(obj.generation, obj.method, obj.haveCharm, obj.encounters));
                        oddsLabel.setText("Odds: 1/" + obj.odds);
                        String distStr = String.format("%.02f", binomDist(obj.odds, obj.encounters));
                        binomLabel.setText("Binomial Distribution: " + distStr);
                    }
                }
            });

            JLabel timerLabel = new JLabel("Time Elapsed: " + String.format("%d:%02d:%02d", obj.seconds / 3600, (obj.seconds % 3600) / 60, (obj.seconds % 60)));
            timerLabel.setFont(new Font(obj.customFont.getFontName(), Font.PLAIN, 8));
            timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            timerLabel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
            Timer secondsTimer = new Timer(1000, e -> obj.seconds++);
            Timer timer = new Timer(1000, e-> timerLabel.setText("Time Elapsed: " + String.format("%d:%02d:%02d", obj.seconds / 3600, (obj.seconds % 3600) / 60, (obj.seconds % 60))));
            if(!obj.done) {
                secondsTimer.start();
                timer.start();
            }


            JLabel congrats = new JLabel("");
            if(obj.done) {
                congrats.setText("Congratulations!");
            }
            congrats.setFont(new Font(obj.customFont.getFontName(), Font.BOLD, 15));
            congrats.setAlignmentX(Component.CENTER_ALIGNMENT);
            congrats.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            panel.add(targetLabel);
            panel.add(congrats);
            panel.add(encounterLabel);
            panel.add(addEncounter);
            panel.add(subtractEncounter);
            panel.add(oddsLabel);
            panel.add(timerLabel);
            panel.add(binomLabel);

            JButton done = new JButton("Done!");
            done.setBackground(Color.YELLOW);
            done.setFont(new Font(obj.customFont.getFontName(), Font.PLAIN, 10));
            done.setAlignmentX(Component.CENTER_ALIGNMENT);
            done.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    panel.remove(addEncounter);
                    panel.remove(subtractEncounter);
                    secondsTimer.stop();
                    timer.stop();
                    congrats.setText("Congratulations!");
                    panel.remove(done);
                    obj.done = true;
                    frame.repaint();
                }
            });

            panel.add(done);

            if(obj.done) {
                panel.remove(addEncounter);
                panel.remove(subtractEncounter);
                panel.remove(done);
            }

            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            frame.add(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.setSize(300,300);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}