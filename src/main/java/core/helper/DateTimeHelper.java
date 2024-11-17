package core.helper;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Driver;
import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeHelper {
    public static long ONE_SECOND = 1000;
    public static long THREE_SECOND = 3000;
    private DateTimeHelper() {
        // Do nothing
    }

    private static final LogHelper logger = LogHelper.getInstance();

    public static String getTimeStamp(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static long getCurrentTimeInMillis() {
        return new Date().getTime() / 10000;
    }

    public static String getTimeStamp() {
        return getTimeStamp("yyyyMMddHHmmss").trim()
                .replace("/", "")
                .replace(":", "")
                .replace(" ", "");
    }

    public static boolean validateTime(String time) {
        Pattern p = Pattern.compile("(1[012]|[0-9]):[0-5][0-9](\\s)?(?i)(AM|PM)");
        Matcher m = p.matcher(time);
        return m.matches();
    }

    public static String generateUniqueString() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    public static int calculateAge(String year, String month, String day) {
        LocalDate birthDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        LocalDate now = LocalDate.now();
        return Period.between(birthDate, now).getYears();
    }

    public static Date getYesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static String getDateTimeFormat(String format, String timezone) {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getTimeZone(timezone));
        return df.format(date);
    }

    private static int randBetween(int start, int end) {
        SecureRandom rand = new SecureRandom();
        return start + rand.nextInt((end - start) + 1);
    }

    public static GregorianCalendar randomDateOfBirth() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        int year = randBetween(1900, 2010);
        gregorianCalendar.set(Calendar.YEAR, year);
        int dayOfYear = randBetween(1, gregorianCalendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        gregorianCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        logger.info((gregorianCalendar.get(Calendar.MONTH) + 1) + "-" + gregorianCalendar.get(Calendar.DAY_OF_MONTH) + "-" + gregorianCalendar.get(Calendar.YEAR));
        return gregorianCalendar;
    }

    public static String convertMonthToText(int month) {
        return Month.of(month).getDisplayName(TextStyle.FULL, Locale.US);
    }

    public static int convertMonthToNumber(String month) {
        try {
            Date date = new SimpleDateFormat("MMMM").parse(month);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.MONTH);
        } catch (ParseException e) {
            logger.fail(e.getMessage());
            return -1;
        }
    }

    public static int getCurrentYear() {
        LocalDate date = LocalDate.now();
        return date.getYear();
    }

    // Calculate time difference between start time and end time
    public static String timeDiff(String strStart, String strEnd){
        String[] startSplit = strStart.split(":");
        String[] endSplit = strEnd.split(":");

        BigDecimal bdMin = new BigDecimal(endSplit[0]).subtract(new BigDecimal(startSplit[0]));
        BigDecimal bdSec = new BigDecimal(endSplit[1]).subtract(new BigDecimal(startSplit[1]));
        if(bdSec.compareTo(BigDecimal.ZERO) < 0){
            bdSec = bdSec.add(new BigDecimal(60));
            bdMin = bdMin.subtract(new BigDecimal(1));
        }

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        formatter.applyPattern("00.00");

        String result = String.format("%02d:%s", bdMin.intValue(), formatter.format(bdSec.floatValue()));
        return result;
    }

    // Calculate time sum between time 1 and time 2
    public static String timeSum(String strTime1, String strTime2){
        String[] startSplit = strTime1.split(":");
        String[] endSplit = strTime2.split(":");

        BigDecimal bdMin = new BigDecimal(endSplit[0]).add(new BigDecimal(startSplit[0]));
        BigDecimal bdSec = new BigDecimal(endSplit[1]).add(new BigDecimal(startSplit[1]));
        if(bdSec.compareTo(new BigDecimal(60)) > 0){
            bdSec = bdSec.subtract(new BigDecimal(60));
            bdMin = bdMin.add(new BigDecimal(1));
        }

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        formatter.applyPattern("00.00");

        String result = String.format("%02d:%s", bdMin.intValue(), formatter.format(bdSec.floatValue()));
        return result;
    }

    //Compare time and return True if diviation <=2
    public static boolean compareTimeByPixel(String strExpect, String strActual){
        Boolean flag = Boolean.FALSE;
        if(strExpect.substring(0, 5).equals(strActual.substring(0, 5))){
            if (Math.abs(Integer.parseInt(strExpect.substring(6, 8)) - Integer.parseInt(strActual.substring(6, 8))) <= 2){
                flag = Boolean.TRUE;
            }
        }
        return flag;
    }

    // Compare time 1 and time 2 and return whether time 2 are "Bigger", "Smaller" or "Equal" to time 1
    public static String compareTime(String strTime1, String strTime2){
        String[] time1Split = strTime1.split(":");
        String[] time2Split = strTime2.split(":");

        BigDecimal bdMin = new BigDecimal(time2Split[0]).subtract(new BigDecimal(time1Split[0]));
        BigDecimal bdSec = new BigDecimal(time2Split[1]).subtract(new BigDecimal(time1Split[1]));

        if(bdSec.compareTo(BigDecimal.ZERO) < 0){
            bdSec.add(new BigDecimal(60));
            bdMin.subtract(new BigDecimal(1));
        }

        if(bdMin.compareTo(BigDecimal.ZERO) < 0) {
            return "Smaller";
        } else if (bdMin.compareTo(BigDecimal.ZERO) == 0) {
            if(bdSec.compareTo(BigDecimal.ZERO) < 0){
                return "Smaller";
            } else if(bdSec.compareTo(BigDecimal.ZERO) == 0) {
                return "Equal";
            } else {
                return "Bigger";
            }
        } else {
            return "Bigger";
        }
    }

    // Return whether strTime is within time range (inclusive)
    public static boolean isWithinTimeRange(String[] ranges, String strTime){
        if(!compareTime(ranges[0], strTime).equals("Smaller") && !compareTime(ranges[1], strTime).equals("Bigger")){
            return true;
        } else {
            return false;
        }
    }

    // Compare date 1 and date 2 and return whether date 2 are "Bigger", "Smaller" or "Equal" to date 1
    public static String compareDate(String strDate1, String strDate2) throws ParseException {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(strDate1);
        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(strDate2);
        int result = date1.compareTo(date2);
        if (result > 0) {
            return "Smaller";
        } else if (result < 0) {
            return "Bigger";
        } else {
            return "Equal";
        }
    }

    // format time string
    public static String formatTime(String strTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        LocalDateTime dt = LocalDateTime.parse(strTime, formatter);
        String result = dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return result;
    }

    // calculate total millisecond from input string time (px to ms)
    public static Double calculateMillisecond(String strTime){
        int intTimeMinute = Integer.parseInt(strTime.split(":")[0]);
        Double dblTimeSecond = Double.parseDouble(strTime.split(":")[1]);
        return (intTimeMinute*60 + dblTimeSecond)*100;
    }

    // calculate string time from millisecond (ms to px)
    public static String calculateTime(Double dblMillisecond){
        Double dblSecond = dblMillisecond/100;
        int intMinute = 0;
        if(dblSecond > 60){
            intMinute = (int) (dblSecond/60);
            dblSecond -= intMinute*60;
        }
        if(intMinute > 9){
            return intMinute + ":" + Math.floor(dblSecond * 100.0) / 100.0;
        } else{
            return "0" + intMinute + ":" + Math.floor(dblSecond * 100.0) / 100.0;
        }
    }

    public static String convertTimeZone(String strInput, TimeZone timeZone) {
        String result = "";
        try{
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            utcFormat.setTimeZone(timeZone);
            Date date = utcFormat.parse(strInput);

            DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            pstFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            result = pstFormat.format(date);

        }catch (Exception exception){
            logger.info("Fail parse datetime: " + exception);
        }
        return result;
    }

    public static long differenceTime(String start, String end){
         return differenceTime(start,end,"mm:ss.S");
    }

    /***
     * Return diff time in long as milliseconds
     * @param start
     * @param end
     * @return
     */

    public static long differenceTime(String start, String end,String pattern)  {
        String time1 = start;
        String time2 = end;

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date1 = null;
        try {
            date1 = format.parse(time1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date date2 = null;
        try {
            date2 = format.parse(time2);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long difference = date2.getTime() - date1.getTime();
        return difference;
    }
}
