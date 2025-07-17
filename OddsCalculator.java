public class OddsCalculator {
    public static double calculateOdds(int generation, String method, boolean haveCharm, int encounters) {
        int rolls = 1;

        if(generation == 2 || generation == 3) {
            if(method == "BR") {
                rolls += 127;
            }
            return 8192 / rolls;
        }
        if(generation == 4) {
            if(method.equals("MM")) {
                rolls += 4;
            }
            if(method.equals("PR")) {
                return pokeRadarCalc(encounters, generation);
            }
            return 8192 / rolls;
        }
        if(generation == 5) {
            if(haveCharm) {
                rolls += 2;
            }
            if(method.equals("MM")) {
                rolls += 5;
            }
            return 8192 / rolls;
        }
        if(generation == 6) {
            if(haveCharm) {
                rolls += 2;
            }
            if(method.equals("MM")) {
                rolls += 5;
            }
            if(method.equals("FS")) {
                rolls += 4;
            }
            if(method.equals("HO")) {
                if(haveCharm) {
                    rolls += 8;
                }
                rolls += 4;
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
            return 4096 / rolls;
        }
        if(generation == 7) {
            if(haveCharm) {
                rolls += 2;
            }
            if(method.equals("MM")) {
                rolls += 5;
            }
            if(method.equals("SOS")) {
                return sosCalc(encounters, haveCharm);
            }
            return 4096 / rolls;
        }
        if(generation == 8) {
            if(haveCharm) {
                rolls += 2;
            }
            if(method.equals("MM")) {
                rolls += 5;
            }
            if(method.equals("DA")) {
                if(haveCharm) {
                    return 100;
                } else {
                    return 300;
                }
            }
            if(method.equals("PR")) {
                return pokeRadarCalc(encounters, generation);
            }
            if(method.equals("BL")) {
                return brilliantCalc(encounters, haveCharm);
            }
            return 4096 / rolls;
        }
        return 0;
    }

    public static double pokeRadarCalc(int encounters, int generation) {
        double radarOdds;
        if (encounters > 40) {
            radarOdds = 1 / (Math.ceil(65535 / (8200 - 40 * 200)) / 65536);
        }
        else {
            radarOdds = 1 / ((Math.ceil(65535 / (8200 - encounters * 200)) + 1) / 65536);
        }
        if(generation == 6 || generation == 8)
        {
            radarOdds *= 2;
        }
        return radarOdds;
    }

    public static double chainFishingCalc(int encounters, boolean haveCharm) {
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

    public static double dexNavCalc(int encounters, boolean haveCharm) {
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

    private static double brilliantCalc(int encounters, boolean haveCharm) {
        int rolls = 1;

        if(encounters >= 1 && encounters < 50) {
            rolls += 1;
        }
        if(encounters >= 50 && encounters < 100) {
            rolls += 2;
        }
        if(encounters >= 100 && encounters < 200) {
            rolls += 3;
        }
        if(encounters >= 200 && encounters < 300) {
            rolls += 4;
        }
        if(encounters >= 300 && encounters < 500) {
            rolls += 5;
        }
        if(encounters >= 500) {
            rolls += 6;
        }

        if(haveCharm) {
            rolls += 2;
        }

        return 4096 / rolls;

    }
}
