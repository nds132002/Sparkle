import java.io.File;
import java.util.ArrayList;

public class Populator {

    ArrayList<String> gen2methods = new ArrayList<String>();
    ArrayList<String> gen3methods = new ArrayList<String>();
    ArrayList<String> gen4methods = new ArrayList<String>();
    ArrayList<String> gen5methods = new ArrayList<String>();
    ArrayList<String> gen6methods = new ArrayList<String>();
    ArrayList<String> gen7methods = new ArrayList<String>();
    ArrayList<String> gen8methods = new ArrayList<String>();
    ArrayList<String> filenames = new ArrayList<String>();
    ArrayList<String> savefiles = new ArrayList<String>();
    ArrayList<String> samename = new ArrayList<String>();
    String[] gens = new String[7];
    String[] methods = new String[12];
    String[] pokemon = new String[958];
    String target;

    public void populateMethodLists() {
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

        gen8methods.add("SR");
        gen8methods.add("RE");
        gen8methods.add("MM");
        gen8methods.add("DA");
        gen8methods.add("PR");
        gen8methods.add("BL");
    }

    public void populateList(String listType) {
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
                    } else if(listType == "savefiles") {
                        savefiles.add(file.getName().substring(0, file.getName().length() - 4));
                    } else {
                        if(file.getName().startsWith("save-" + target)) {
                            samename.add(file.getName());
                        }
                    }
                }
            }
        }
    }

    public void populateOptions() {
        gens[0] = "2";
        gens[1] = "3";
        gens[2] = "4";
        gens[3] = "5";
        gens[4] = "6";
        gens[5] = "7";
        gens[6] = "8";

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
        methods[10] = "DA";
        methods[11] = "BL";
    }

    public boolean methodGenCheck(int generation, String method) {
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
        if(generation == 8) {
            if(gen8methods.contains(method)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
