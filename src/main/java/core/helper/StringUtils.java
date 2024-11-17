package core.helper;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class StringUtils {

    private StringUtils() {
    }

    private static final LogHelper logger = LogHelper.getInstance();

    /**
     * Generate an new email base on exist email.
     * For ex:
     * exist email: test@yahoo.com
     * new email:   test_{random character}@yahoo.com
     *
     * @param email
     * @return new email with random character
     */
    public static String generateEmail(String email) {
        String delimeter = "@";
        List<String> value = Arrays.asList(email.split(delimeter));
        //get random string for email
        String string1 = value.get(0) + "_" + randomString(6);
        return string1 + delimeter + value.get(1);
    }



    public static List<String> getDataFromString(String input, String spliter) {
        return Arrays.asList(input.split(spliter));
    }

    public static String generateEmailAliasAddress(String emailAddress, String suffixString) {
        String outEmailAddress = emailAddress.split("@")[0];
        outEmailAddress = outEmailAddress + "+" + suffixString + "@";
        outEmailAddress = outEmailAddress + emailAddress.split("@")[1];

        return outEmailAddress;
    }

    public static String updateUrlBaseEnv(String baseUrl, String regex, String replaceString) {
        return baseUrl.replaceAll(regex, replaceString);
    }

    public static List<String> convertToList(String inputString, String delimiter) {
        if(inputString.equals("")){
            return null;
        }
        return new LinkedList<>(Arrays.asList(inputString.split("\\s?" + delimiter + "\\s?", -1)));
    }

    public static List<String> convertToList(String inputString) {
        return convertToList(inputString, "\\r?\\n");
    }

    /*
     * This method receive a list as input and return a list of all possible combinations
     * For Example:
     * Input: [A, B, C]
     * Output: list with 7 combinations
     * [[C], [B], [B, C], [A], [A, C], [A, B], [A, B, C]]
     */
    public static List<List<String>> generateCombinations(List<String> listElements) {
        List<List<String>> listCombinations = new ArrayList<>();

        int totalElements = listElements.size();
        int totalCombinations = (int) Math.pow(2.0, totalElements);

        for (int i = 1; i < totalCombinations; i++) {
            List<String> combination = new ArrayList<>();
            String code = Integer.toBinaryString(totalCombinations | i).substring(1);

            for (int j = 0; j < totalElements; j++) {
                if (code.charAt(j) == '1') {
                    combination.add(listElements.get(j));
                }
            }

            listCombinations.add(combination);
        }

        return listCombinations;
    }

    public static String containsClass(String className) {
        return String.format("contains(concat(' ', normalize-space(@class), ' '), ' %s ')", className);
    }

    public static String containsClass(List<String> lstClassName) {
        List<String> resXpath = new ArrayList<>();
        for (String className : lstClassName) {
            resXpath.add(containsClass(className));
        }
        return String.join(" and ", resXpath);
    }

    public static String convertStringToDecimalNumber(String format, String number) {
        return String.format(format, new BigDecimal(number));
    }

    public static String convertStringTo2DigitNumber(String number) {
        return convertStringToDecimalNumber("%.2f", number);
    }

    public static String getRandomElementFromList(List<String> list) {
        SecureRandom rand = new SecureRandom();
        return list.get(rand.nextInt(list.size())).trim();
    }

    public static String randomEmail(String userEmail, String emailDomain, long randomNum) {
        return userEmail + randomNum + emailDomain;
    }

    public static String xpathExpression(String value) {
        if (!value.contains("'"))
            return '\'' + value + '\'';
        else if (!value.contains("\""))
            return '"' + value + '"';
        else
            return "concat('" + value.replace("'", "',\"'\",'") + "')";
    }

    public static String generateRandomPhoneNumberString() {
        SecureRandom rand = new SecureRandom();
        int num1 = (rand.nextInt(7) + 1) * 100 + (rand.nextInt(8) * 10) + rand.nextInt(8);
        int num2 = rand.nextInt(743);
        int num3 = rand.nextInt(10000);

        DecimalFormat df3 = new DecimalFormat("000"); // 3 zeros
        DecimalFormat df4 = new DecimalFormat("0000"); // 4 zeros

        String phoneNumber = df3.format(num1) + df3.format(num2) + df4.format(num3);
        logger.info(String.format("Generate phone number %s", phoneNumber));
        return phoneNumber;
    }

    private static String randomString(int count, boolean letters, boolean numbers) {
        String numberChrs = "0123456789";
        String alphabetChrs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String chrs = "";
        chrs = numbers ? chrs + numberChrs : chrs;
        chrs = letters ? chrs + alphabetChrs : chrs;
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.ints(count, 0, chrs.length()).mapToObj(chrs::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

    public static String randomString(int count) {
        return randomString(count, true, true);
    }

    public static String randomNumberic(int count) {
        return randomString(count, false, true);
    }

    public static String randomAlphabetic(int count) {
        return randomString(count, true, false);
    }

    public static String scrollToId(String id) {
        return String.format("new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().resourceIdMatches(\"%s\"))", id);
    }

    public static String scrollToVisibleText(String text, boolean isExact) {
        return String.format("new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().%s(\"%s\"))", isExact ? "text" : "textContains", text);
    }

    public static String scrollToVisibleText(String text) {
        return scrollToVisibleText(text, false);
    }

    public static String scrollToIdChildText(String id, String text) {
        return String.format("new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().resourceIdMatches(\"%s\").childSelector(text(\"%s\")))", id, text);
    }

    public static String scrollToIdAndText(String id, String text) {
        return String.format("new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().resourceIdMatches(\"%s\").text(\"%s\"))", id, text);
    }

    public static String scrollToIdHorizontalChildText(String id, String text) {
        return String.format("new UiScrollable(new UiSelector().scrollable(true).resourceId(\"%s\")).setAsHorizontalList().scrollIntoView(new UiSelector().childSelector(text(\"%s\")))", id, text);
    }

    public static String removeSpecialCharacters(String text) {
        return text.replaceAll("[^a-zA-Z0-9]", "");
    }

    public static String generateFolderName(String strProjectName, String verifyTime) {
        String strFolderName = "";
        String[] splited = verifyTime.split("/");
        if(splited[1].equals("1")){
            strFolderName = strProjectName;
        } else {
            strFolderName = strProjectName + "_" + splited[0];
        }
        return strFolderName;
    }


    // This method compares two strings lexicographically without using library functions
    public static int compareString(String str1, String str2){
        int l1 = str1.length();
        int l2 = str2.length();
        int lMin = Math.min(l1, l2);

        for(int i = 0; i < lMin; i++){
            int str1_ch = (int)str1.charAt(i);
            int str2_ch = (int)str2.charAt(i);

            if (str1_ch != str2_ch) {
                return str1_ch - str2_ch;
            }
        }
        if (l1 != l2) {
            return l1 - l2;
        } else {
            return 0;
        }
    }

    public static boolean isAllFullWidth(String str) {
        for (char c : str.toCharArray())
            if ((c & 0xff00) != 0xff00)
                return false;
        return true;
    }
}