/*
 * @author  HSU
 */
package ncku.selab.rapi4jmeter.report;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class RequestStatsReport {

    private final TimelineReport report = new TimelineReport();
    private final JsonParse jsonParse = new JsonParse();
    private final ArrayList<String> commandList = new ArrayList<>();
    private final ArrayList<Integer> commandSamplesCount = new ArrayList<>();

    /**
     * Sum of individual times per command
     * [openCommandTimeSum, clickAtCommandTimeSum1, ...]
     * [10190, 678, 5072, 804, 162, 3666, 3936, 93, 3230, 4085, 609, 20062, 2229, 68]
     */
    private final ArrayList<Long> commandTotalTime = new ArrayList<>();

    /**
     * Record each command data time
     * [[openCommandTime1, openCommandTime2],[clickAtCommandTime1, clickAtCommandTime2], [...]]
     */
    private final ArrayList<ArrayList<Long>> commandTimeData = new ArrayList<>();

    /**
     * record each Command Hit timestamp
     * Command Hit means timestamp that command start
     */
    private final ArrayList<ArrayList<Long>> commandTime = new ArrayList<>();
    private final ArrayList<Long> AllTime = new ArrayList<>();
    private final ArrayList<Integer> AvgTime = new ArrayList<>();
    private final ArrayList<Double> Hit = new ArrayList<>();
    private final ArrayList<Long> line_90 = new ArrayList<>();
    private final ArrayList<Long> line_95 = new ArrayList<>();
    private final ArrayList<Long> line_99 = new ArrayList<>();
    private final ArrayList<Long> Min = new ArrayList<>();
    private final ArrayList<Long> Max = new ArrayList<>();
    private final ArrayList<Long> Median = new ArrayList<>();
    /**
     * record each Command error
     */
    private final ArrayList<Integer> error = new ArrayList<>();
    private final ArrayList<Double> errorPercentage = new ArrayList<>();
    private final ArrayList<Long> commandTimeDifference = new ArrayList<>();

    /**
     * all test results
     * [{"sideex": [4, 0, 0 ], "format": [1, 0, 1 ] }, "reports": [{"title": "date 16-13-30", "browserName": "chrome 112.0.5615.137", ...}] },{...}]
     */
    private ArrayList<String> testResults;
    private int commandAmount = 0;
    private long AllCommandTimeSum = 0;
    private String Request_Statistics_Content = "";

    public void startGenerateReport(String path, ArrayList<String> testResults) throws ParseException, java.text.ParseException {
//        File jsonFolder = new File(path);

        this.testResults = testResults;

        parse();
        report.generate_report(Request_Statistics_Content, jsonParse, this.testResults, commandList, path);
    }

    public void preprocessing() throws ParseException {


        for (int i = 0; i < testResults.size(); i++) {

            JSONArray recordsArray = jsonParse.getRecordsArray(i);

            if (commandAmount < recordsArray.size())
                commandAmount = recordsArray.size();

        }


        for (int j = 0; j < commandAmount; j++)
            commandSamplesCount.add(0);


        String command;
        int commandCount, totalCommandCount = 0;
        JSONObject records;

        for (int i = 0; i < testResults.size(); i++) {

            JSONArray recordsArray = jsonParse.getRecordsArray(i);

            if (recordsArray.size() == commandAmount && commandList.size() == 0) {

                for (int j = 0; j < commandAmount; j++) {

                    records = (JSONObject) recordsArray.get(j);

                    if (j == 0)
                        commandList.add("ALL");

                    command = (String) records.get("name");
                    commandList.add(command);
                }

            }

            for (int j = 0; j < recordsArray.size(); j++) {

                commandCount = commandSamplesCount.get(j);
                commandCount++;
                totalCommandCount++;
                commandSamplesCount.set(j, commandCount);

            }

        }

        commandSamplesCount.add(0, totalCommandCount);


    }

    private int getPercentilePosition(double rankFloat, int arraySize) {
        int position = (int) Math.round(arraySize * rankFloat);
        position--;
        if (position >= arraySize)
            position--;
        if (position < 0)
            position = 0;

        return position;
    }

    private static long findMedian(ArrayList<Long> sortedArray) {
        int n = sortedArray.size();
        int middleIndex = n / 2;
        if (n % 2 == 1) {
            // Array has odd number of elements, return the middle element
            return sortedArray.get(middleIndex);
        } else {
            // Array has even number of elements, return the average of the two middle elements
            return (sortedArray.get(middleIndex - 1) + sortedArray.get(middleIndex) / 2);
        }
    }

    public void parse() throws ParseException, java.text.ParseException {


        jsonParse.addTestResults(this.testResults);


//        ArrayList<String> browserVersions = jsonParse.getBrowserVersion(); // no use this

        preprocessing();

        int errorCount;
        double percentage;

        for (int j = 0; j < commandAmount; j++) {

            // generate empty array for each command data time
            // [[3924, 3170, 3096], [81, 397, 200], [1260, 2018, 1794], [237, 380, 187], [28, 106, 28], [1245, 1315, 1106], [1006, 1151, 1779], [33, 31, 29], [1072, 1037, 1121], [1952, 1063, 1070], [492, 38, 79], [10030, 10032], [973, 1256], [31, 37]]
            commandTimeData.add(new ArrayList<>()); // [[], [], [], [], [], [], [], [], [], [], [], [], [], []]
            commandTime.add(new ArrayList<>()); // [[], [], [], [], [], [], [], [], [], [], [], [], [], []]
            error.add(0); // [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
            commandTotalTime.add(0L); // [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss:SSS");
        for (int i = 0; i < testResults.size(); i++) {
            JSONObject json = jsonParse.getJson(i);
            JSONArray casesArray = (JSONArray) json.get("cases");
            JSONObject cases = (JSONObject) casesArray.get(0);
            JSONArray recordsArray = (JSONArray) cases.get("records");
            // Change records from JSONArray to JSONObject && records has many command
            // element, recordsArray.size() records amount of command
            JSONObject records;
            Long time;
            String status;

            String startTime = (String) json.get("startTime");
//            String endTime = (String) json.get("endTime");
            startTime += ":000"; // need add back if no milliseconds

            LocalDateTime commandDateTime = LocalDateTime.parse(startTime, formatter);

            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTimeInMillis(commandDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            commandTime.get(0).add(currentCalendar.getTimeInMillis());

            for (int commandIndex = 0; commandIndex < recordsArray.size(); commandIndex++) {

                records = (JSONObject) recordsArray.get(commandIndex);
                status = (String) records.get("status");


                time = (Long) records.get("time");
                commandTimeData.get(commandIndex).add(time);
                // AllTime records first row data
                AllTime.add(time);

                time = (Long) records.get("time");
                currentCalendar.add(Calendar.MILLISECOND, time.intValue());

                time += commandTotalTime.get(commandIndex);
                commandTotalTime.set(commandIndex, time);
                // Data of all command time
                if ((commandIndex + 1) < recordsArray.size())
                    commandTime.get(commandIndex + 1).add(currentCalendar.getTimeInMillis());


                if (status.equals("fail")) {

                    errorCount = error.get(commandIndex);
                    errorCount++;

                    error.set(commandIndex, errorCount);
                }

            }

        }


        for (ArrayList<Long> longArrayList : commandTime) {
            longArrayList.sort(null);
        }

        int commandCount = commandTime.get(commandTime.size() - 1).size();

        //Calculate all hit time
        long timeDifference = commandTime.get(commandTime.size() - 1).get(commandCount - 1) - commandTime.get(0).get(0);
        timeDifference /= 1000;
        if (timeDifference == 0)
            timeDifference = 1;
        commandTimeDifference.add(timeDifference);

        for (ArrayList<Long> longs : commandTime) {

            commandCount = longs.size();

            //Calculate command hit time
            timeDifference = longs.get(commandCount - 1) - longs.get(0);
            timeDifference /= 1000;

            if (timeDifference == 0)
                timeDifference = 1;

            commandTimeDifference.add(timeDifference);


        }


        // Calculate Hit Data
        // AllTime.size() same as All samples
        // hit = All samples / time
        double hit = (double) AllTime.size() / commandTimeDifference.get(0);
        hit = Math.round(hit * 100.0) / 100.0;
        Hit.add(hit);

        for (int i = 1; i <= commandTime.size(); i++) {

            hit = (double) commandTimeData.get(i - 1).size() / commandTimeDifference.get(i);
            hit = Math.round(hit * 100.0) / 100.0;
            Hit.add(hit);

        }


        for (ArrayList<Long> commandTimeDatum : commandTimeData) Collections.sort(commandTimeDatum);
        Collections.sort(AllTime);

        for (Long aLong : AllTime) AllCommandTimeSum += aLong;


        commandTotalTime.add(0, AllCommandTimeSum);
        commandTimeData.add(0, AllTime);


        errorCount = 0;
        //Calculate total error count
        for (Integer integer : error) errorCount += integer;
        error.add(0, errorCount);


        for (int i = 0; i < commandList.size(); i++) {

            int sizeCommandTimeDataArrayNow = commandTimeData.get(i).size();

            //Avg
            int avg = (int) Math.round((double) commandTotalTime.get(i) / commandSamplesCount.get(i));
            AvgTime.add(avg);

            //P90
            int positionP90 = getPercentilePosition(0.9, sizeCommandTimeDataArrayNow);
            line_90.add(commandTimeData.get(i).get(positionP90));

            //P95
            int positionP95 = getPercentilePosition(0.95, sizeCommandTimeDataArrayNow);
            line_95.add(commandTimeData.get(i).get(positionP95));

            //P99
            int positionP99 = getPercentilePosition(0.99, sizeCommandTimeDataArrayNow);
            line_99.add(commandTimeData.get(i).get(positionP99));

            Min.add(commandTimeData.get(i).get(0));
            Max.add(commandTimeData.get(i).get(sizeCommandTimeDataArrayNow - 1));
            Median.add(findMedian(commandTimeData.get(i)));

            percentage = (float) error.get(i) / commandSamplesCount.get(i);
            percentage = Math.round(percentage * 1000.0);
            percentage /= 10;

            errorPercentage.add(percentage);
        }

        generateHtml();

    }


    public void generateHtml() {

        StringBuilder requestStatisticsContentStringBuilder = new StringBuilder();


        for (int i = 0; i < commandList.size(); i++) {

            requestStatisticsContentStringBuilder.append("  </tr>\r\n" + "  <tr>\r\n" + "    <td>");
            requestStatisticsContentStringBuilder.append(commandList.get(i));
            requestStatisticsContentStringBuilder.append("</td>\r\n");

            requestStatisticsContentStringBuilder.append("    <td>");
            requestStatisticsContentStringBuilder.append(AvgTime.get(i));
            requestStatisticsContentStringBuilder.append("</td>\r\n");

            requestStatisticsContentStringBuilder.append("    <td>");
            requestStatisticsContentStringBuilder.append(Hit.get(i));
            requestStatisticsContentStringBuilder.append("</td>\r\n");

            requestStatisticsContentStringBuilder.append("    <td>");
            requestStatisticsContentStringBuilder.append(commandTimeData.get(i).size());
            requestStatisticsContentStringBuilder.append("</td>\r\n");

            requestStatisticsContentStringBuilder.append("    <td>");
            requestStatisticsContentStringBuilder.append(Median.get(i));
            requestStatisticsContentStringBuilder.append("</td>\r\n");

            requestStatisticsContentStringBuilder.append("    <td>");
            requestStatisticsContentStringBuilder.append(line_90.get(i));
            requestStatisticsContentStringBuilder.append("</td>\r\n");

            requestStatisticsContentStringBuilder.append("    <td>");
            requestStatisticsContentStringBuilder.append(line_95.get(i));
            requestStatisticsContentStringBuilder.append("</td>\r\n");

            requestStatisticsContentStringBuilder.append("    <td>");
            requestStatisticsContentStringBuilder.append(line_99.get(i));
            requestStatisticsContentStringBuilder.append("</td>\r\n");

            requestStatisticsContentStringBuilder.append("    <td>");
            requestStatisticsContentStringBuilder.append(Min.get(i));
            requestStatisticsContentStringBuilder.append("</td>\r\n");

            requestStatisticsContentStringBuilder.append("    <td>");
            requestStatisticsContentStringBuilder.append(Max.get(i));
            requestStatisticsContentStringBuilder.append("</td>\r\n");

            requestStatisticsContentStringBuilder.append("    <td>");
            requestStatisticsContentStringBuilder.append(errorPercentage.get(i));
            requestStatisticsContentStringBuilder.append("%</td>\r\n");

        }

        Request_Statistics_Content = requestStatisticsContentStringBuilder.toString();


    }

}
