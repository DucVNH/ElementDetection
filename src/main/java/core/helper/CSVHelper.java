package core.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static core.helper.FileUtils.fileExists;

public class CSVHelper {
    protected static final LogHelper logger = LogHelper.getInstance();
    public static boolean compareCSV(String strExpectedPath, String strActualPath) throws IOException, InterruptedException {
        boolean result = true;
        while (!fileExists(strActualPath)) {
            Thread.sleep(1000);
        }
        File expectedCSV = new File(strExpectedPath);
        File actualCSV = new File(strActualPath);

        FileReader expectedFR = new FileReader(expectedCSV);
        FileReader actualFR = new FileReader(actualCSV);
        BufferedReader expectedBR = new BufferedReader(expectedFR);
        BufferedReader actualBR = new BufferedReader(actualFR);

        String lineExpected = "";
        String lineActual = "";
        String[] tempExpected;
        String[] tempActual;

        try {
            while((lineExpected = expectedBR.readLine()) != null && (lineActual = actualBR.readLine()) != null) {
                tempExpected = lineExpected.split(",");
                tempActual = lineActual.split(",");
                for(int i = 0; i < tempExpected.length; i++) {
                    if(!tempExpected[i].contains("Tested at") && !tempExpected[i].contains("Analyzed at")) {
                        if (!tempActual[i].equals(tempExpected[i])) {
                            logger.debug("E: " + tempExpected[i] + " A: " + tempActual[i]);
                            result = false;
                        }
                    } else {
                        break;
                    }
                }
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            logger.info("CSV files format do not match");
            result = false;
        }

        // attached csv files to RP in case csv file is not match
        if(!result){
            logger.info("2 csv files do not match!");
            sentCSVFileToRP(expectedCSV,actualCSV);
        }

        return result;
    }

    /**
     *
     * @param expectedFile
     * @param actualFile
     */
    public static void sentCSVFileToRP(File expectedFile, File actualFile){
        logger.logFile(expectedFile,"expected CSV file");
        logger.logFile(actualFile,"actualCSV CSV file");
    }
}
