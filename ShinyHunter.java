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
    double odds;
    int encounters = 0;
    int seconds = 0;
    boolean done = false;
    int systems = 0;
    Boolean newFile = true;
    String loadedFileName;
    Font customFont;
    Boolean pause = false;

    private static double binomDist(double odds, int encounters) {
        BinomialDistribution binomialDist = new BinomialDistribution(encounters, 1/odds);
        return (1 - binomialDist.cumulativeProbability(0)) * 100;
    }

    private void save(Populator populator) {
        try {
            String currentDir = System.getProperty("user.dir");
            File directory = new File(currentDir + "/saves/");
            if(!directory.exists()) {
                if(directory.mkdirs()) {
                    System.out.println("Saves directory successfully created.");
                }
                else {
                    System.out.println("Failed to create Saves directory.");
                }
            }
            else {
                System.out.println("Directory already exists.");
            }
            FileWriter saving;
            if(populator.samename.size() >= 1) {
                if(newFile) {
                    saving = new FileWriter("saves/save-" + target + populator.samename.size() + ".txt", false);
                    newFile = false;
                } else {
                    saving = new FileWriter(loadedFileName, false);
                }
            } else {
                saving = new FileWriter("saves/save-" + target + ".txt", false);
                if(newFile) {
                    newFile = false;
                }
            }
            saving.write("" + encounters + System.getProperty("line.separator"));
            saving.write("" + generation + System.getProperty("line.separator"));
            saving.write(method + System.getProperty("line.separator"));
            saving.write(String.valueOf(haveCharm) + System.getProperty("line.separator"));
            saving.write(target + System.getProperty("line.separator"));
            saving.write("" + seconds + System.getProperty("line.separator"));
            saving.write("" + systems + System.getProperty("line.separator"));
            saving.write(String.valueOf(done) + System.getProperty("line.separator"));
            saving.write(String.valueOf(newFile) + System.getProperty("line.separator"));
            saving.close();
        } catch (IOException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
    }

    private void load(String filename) {
        try {
            loadedFileName = "saves/" + filename + ".txt";
            File saveFile = new File(loadedFileName);
            Scanner loading = new Scanner(saveFile);
            encounters = Integer.parseInt(loading.nextLine());
            generation = Integer.parseInt(loading.nextLine());
            method = loading.nextLine();
            haveCharm = Boolean.valueOf(loading.nextLine());
            target = loading.nextLine();
            seconds = Integer.parseInt(loading.nextLine());
            systems = Integer.parseInt(loading.nextLine());
            done = Boolean.valueOf(loading.nextLine());
            newFile = Boolean.valueOf(loading.nextLine());
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

    private Boolean checkNumberString(String numberString) {
        try {
            Integer.parseInt(numberString);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {

        ShinyHunter obj = new ShinyHunter();
        Populator populator = new Populator();

        populator.populateMethodLists();
        populator.populateList("pokemon");
        populator.populateList("savefiles");
        populator.populateOptions();
        obj.loadFont();

        System.out.println("Welcome to Sparkle, a Shiny Hunting Tracker!\n");

        int loadOrNot = JOptionPane.showConfirmDialog(null, "Do you wish to load a file?", "Load File", 0);
        if(loadOrNot == 0 && !(populator.savefiles.size() == 0)) {
            String[] saveFileNames = new String[populator.savefiles.size()];
            for(int i = 0; i < populator.savefiles.size(); i++) {
                saveFileNames[i] = populator.savefiles.get(i);
            }
            Object selectedSavefile = JOptionPane.showInputDialog(null, "Please select the save file you wish to load", "Load File", JOptionPane.QUESTION_MESSAGE, null, saveFileNames, saveFileNames[0]);
            if(selectedSavefile == null) {
                System.exit(0);
            }
            obj.load(selectedSavefile.toString());
            populator.setTarget(obj.target);
            populator.populateList("names");
        } else if (loadOrNot == 1 || (populator.savefiles.size() == 0 && loadOrNot == 0)) {
            Object selectedGeneration = JOptionPane.showInputDialog(null, "Please enter the generation of Pokémon games you are hunting in:", "Generation", JOptionPane.QUESTION_MESSAGE, null, populator.gens, populator.gens[0]);
            if(selectedGeneration == null) {
                System.exit(0);
            }
            obj.generation = Integer.parseInt(selectedGeneration.toString());
            
            Object selectedMethod = JOptionPane.showInputDialog(null, "Please enter the method you are using to Shiny Hunt as follows:\n\nRandom Encounters: RE\nSoft Resets: SR\nMasuda Method: MM\nGen 2 Shiny Breeding: BR\nPoke Radar: PR\nChain Fishing: CF\nFriend Safari: FS\nHorde Encounter: HO\nDexNav: DN\nSOS Battles: SOS\nDynamax Adventures: DA\nBrilliant Pokémon: BL", "Method", JOptionPane.QUESTION_MESSAGE, null, populator.methods, populator.methods[0]);
            if(selectedMethod == null) {
                System.exit(0);
            }
            obj.method = selectedMethod.toString();

            while(!populator.methodGenCheck(obj.generation, obj.method)) {
                selectedMethod = JOptionPane.showInputDialog(null, "Please enter the method you are using to Shiny Hunt as follows:\n\nRandom Encounters: RE\nSoft Resets: SR\nMasuda Method: MM\nGen 2 Shiny Breeding: BR\nPoke Radar: PR\nChain Fishing: CF\nFriend Safari: FS\nHorder Encounter: HO\nDexNav: DN\nSOS Battles: SOS\nDynamax Adventures: DA\nBrilliant Pokémon: BL", "Method", JOptionPane.QUESTION_MESSAGE, null, populator.methods, populator.methods[0]);
                if(selectedMethod == null) {
                    System.exit(0);
                }
                obj.method = selectedMethod.toString();
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
                } else if(charmCheck == -1) {
                    System.exit(0);
                }
            }

            int selectedSystems = JOptionPane.showConfirmDialog(null, "Are you hunting on multiple systems?", "Systems", 0);
            if(selectedSystems == 0) {
                String systemCount = JOptionPane.showInputDialog("Enter the number of systems you are using:");
                while(!obj.checkNumberString(systemCount)) {
                    systemCount = JOptionPane.showInputDialog("Enter the number of systems you are using:");
                }
                obj.systems = Integer.parseInt(systemCount);
            } else if(selectedSystems == 1) {
                obj.systems = 1;
            } else if(selectedSystems == -1) {
                System.exit(0);
            }

            Object selectedTarget = JOptionPane.showInputDialog(null, "What is your Target Pokémon?", "Pokémon", JOptionPane.QUESTION_MESSAGE, null, populator.pokemon, populator.pokemon[0]);
            if(selectedTarget == null) {
                System.exit(0);
            }
            obj.target = selectedTarget.toString();
            populator.setTarget(obj.target);
            populator.populateList("names");
        } else if(loadOrNot == -1) {
            System.exit(0);
        }

        obj.odds = Math.round(OddsCalculator.calculateOdds(obj.generation, obj.method, obj.haveCharm, obj.encounters));

        JFrame frame = new JFrame("Sparkle");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                obj.save(populator);
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
            encounterLabel.setFont(new Font(obj.customFont.getFontName(), Font.BOLD, 14));
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
                    obj.odds = Math.round(OddsCalculator.calculateOdds(obj.generation, obj.method, obj.haveCharm, obj.encounters));
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
                        obj.odds = Math.round(OddsCalculator.calculateOdds(obj.generation, obj.method, obj.haveCharm, obj.encounters));
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

            JButton pauseButton = new JButton("Pause");
            pauseButton.setFont(new Font(obj.customFont.getFontName(), Font.PLAIN, 10));
            pauseButton.setBackground(Color.LIGHT_GRAY);
            pauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            pauseButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    obj.pause = !obj.pause;
                    if(obj.pause) {
                        secondsTimer.stop();
                        timer.stop();
                        pauseButton.setText("Unpause");
                    } else {
                        secondsTimer.start();
                        timer.start();
                        pauseButton.setText("Pause");
                    }
                }
            });

            panel.add(targetLabel);
            panel.add(congrats);
            panel.add(encounterLabel);
            panel.add(addEncounter);
            panel.add(subtractEncounter);
            panel.add(oddsLabel);
            panel.add(timerLabel);
            panel.add(binomLabel);
            panel.add(pauseButton);

            JButton done = new JButton("Done!");
            done.setBackground(Color.YELLOW);
            done.setFont(new Font(obj.customFont.getFontName(), Font.PLAIN, 10));
            done.setAlignmentX(Component.CENTER_ALIGNMENT);
            done.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    panel.remove(addEncounter);
                    panel.remove(subtractEncounter);
                    panel.remove(pauseButton);
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
                panel.remove(pauseButton);
                panel.remove(done);
            }

            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            frame.add(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.setSize(320,320);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}