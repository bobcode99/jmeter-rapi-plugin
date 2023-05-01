/*
 * @author  HSU
 */
package ncku.selab.rapi4jmeter.report;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

//import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class RequestStatsReport {

    private final TimelineReport report = new TimelineReport();
    private final JsonParse jsonParse = new JsonParse();
    private final ArrayList<String> commandList = new ArrayList<>();
    private final ArrayList<Integer> commandSamplesCount = new ArrayList<>();
    private final ArrayList<Long> commandTotalTime = new ArrayList<>();
    private final ArrayList<ArrayList<Long>> commandTimeData = new ArrayList<>();
    private final ArrayList<ArrayList<Long>> commandTime = new ArrayList<>();
    private final ArrayList<Long> All = new ArrayList<>();
    private final ArrayList<Integer> AvgTime = new ArrayList<>();
    private final ArrayList<Double> Hit = new ArrayList<>();
    private final ArrayList<Long> line_90 = new ArrayList<>();
    private final ArrayList<Long> line_95 = new ArrayList<>();
    private final ArrayList<Long> line_99 = new ArrayList<>();
    private final ArrayList<Long> Min = new ArrayList<>();
    private final ArrayList<Long> Max = new ArrayList<>();
    private final ArrayList<Integer> error = new ArrayList<>();
    private final ArrayList<Double> errorPercentage = new ArrayList<>();
    private final ArrayList<Long> commandTimeDifference = new ArrayList<>();


    // all test results
    // [{"sideex": [4, 0, 0 ], "format": [1, 0, 1 ] }, "reports": [{"title": "date 16-13-30", "browserName": "chrome 112.0.5615.137", ...}] },{...}]
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


    public void parse() throws ParseException, java.text.ParseException {


        jsonParse.addTestResults(this.testResults);


//        ArrayList<String> browserVersions = jsonParse.getBrowserVersion(); // no use this

        preprocessing();

        int position, errorCount;
        double percentage;

        for (int j = 0; j < commandAmount; j++) {
            commandTimeData.add(new ArrayList<>());
            commandTime.add(new ArrayList<>());
            error.add(0);
            commandTotalTime.add(0L);
        }


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
            startTime += ":000";
            Date commandDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS").parse(startTime);
            Calendar currentCalendar = Calendar.getInstance();

            currentCalendar.setTime(commandDate);
            commandTime.get(0).add(currentCalendar.getTimeInMillis());

            for (int commandIndex = 0; commandIndex < recordsArray.size(); commandIndex++) {

                records = (JSONObject) recordsArray.get(commandIndex);
                status = (String) records.get("status");


                time = (Long) records.get("time");
                commandTimeData.get(commandIndex).add(time);
                // All records first row data
                All.add(time);

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
        double hit = (double) All.size() / commandTimeDifference.get(0);
        hit = Math.round(hit * 100.0) / 100.0;
        Hit.add(hit);

        for (int i = 1; i <= commandTime.size(); i++) {

            hit = (double) commandTimeData.get(i - 1).size() / commandTimeDifference.get(i);
            hit = Math.round(hit * 100.0) / 100.0;
            Hit.add(hit);

        }


        for (ArrayList<Long> commandTimeDatum : commandTimeData) Collections.sort(commandTimeDatum);
        Collections.sort(All);

        for (Long aLong : All) AllCommandTimeSum += aLong;


        commandTotalTime.add(0, AllCommandTimeSum);
        commandTimeData.add(0, All);


        errorCount = 0;
        //Calculate total error count
        for (Integer integer : error) errorCount += integer;
        error.add(0, errorCount);


        for (int i = 0; i < commandList.size(); i++) {


            //Avg
            int avg = (int) Math.round((double) commandTotalTime.get(i) / commandSamplesCount.get(i));
            AvgTime.add(avg);


            //P90
            position = (int) Math.round(commandTimeData.get(i).size() * 0.9);
            position--;
            if (position >= commandTimeData.get(i).size())
                position--;
            if (position < 0)
                position = 0;
            line_90.add(commandTimeData.get(i).get(position));

            //P95
            position = (int) Math.round(commandTimeData.get(i).size() * 0.95);
            position--;
            if (position >= commandTimeData.get(i).size())
                position--;
            if (position < 0)
                position = 0;
            line_95.add(commandTimeData.get(i).get(position));


            //P99
            position = (int) Math.round(commandTimeData.get(i).size() * 0.99);
            position--;
            if (position >= commandTimeData.get(i).size())
                position--;
            if (position < 0)
                position = 0;
            line_99.add(commandTimeData.get(i).get(position));

            Min.add(commandTimeData.get(i).get(0));
            Max.add(commandTimeData.get(i).get(commandTimeData.get(i).size() - 1));


            percentage = (float) error.get(i) / commandSamplesCount.get(i);
            percentage = Math.round(percentage * 1000.0);
            percentage /= 10;
            errorPercentage.add(percentage);

        }


        generateHtml();

    }


    public void generateHtml() {

        StringBuilder DataContent = new StringBuilder();


        for (int i = 0; i < commandList.size(); i++) {

            DataContent.append("  </tr>\r\n" + "  <tr>\r\n" + "    <td>").append(commandList.get(i)).append("</td>\r\n").append("    <td>").append(AvgTime.get(i)).append("</td>\r\n").append("    <td>").append(Hit.get(i)).append("</td>\r\n").append("    <td>").append(commandTimeData.get(i).size()).append("</td>\r\n").append("    <td>").append(line_90.get(i)).append("</td>\r\n").append("    <td>").append(line_95.get(i)).append("</td>\r\n").append("    <td>").append(line_99.get(i)).append("</td>\r\n").append("    <td>").append(Min.get(i)).append("</td>\r\n").append("    <td>").append(Max.get(i)).append("</td>\r\n").append("    <td>").append(errorPercentage.get(i)).append("%</td>\r\n");

        }

        Request_Statistics_Content +=
                "<table width = \"1250\"  class=\"request\">\r\n" +
                        "  <tr>\r\n" +
                        "    <td>Element Label&nbsp;&nbsp;&nbsp;&nbsp;</td>\r\n" +
                        "    <td>Avg. Time(ms)&nbsp;&nbsp;&nbsp;&nbsp;</td>\r\n" +
                        "    <td>Avg. Hits/s&nbsp;&nbsp;&nbsp;&nbsp;</td>\r\n" +
                        "    <td>#Samples&nbsp;&nbsp;&nbsp;&nbsp;</td>\r\n" +
                        "    <td>90% line(ms)&nbsp;&nbsp;&nbsp;&nbsp;</td>\r\n" +
                        "    <td>95% line(ms)&nbsp;&nbsp;&nbsp;&nbsp;</td>\r\n" +
                        "    <td>99% line(ms)&nbsp;&nbsp;&nbsp;&nbsp;</td>\r\n" +
                        "    <td>Min Time(ms)&nbsp;&nbsp;&nbsp;&nbsp;</td>\r\n" +
                        "    <td>Max Time(ms)&nbsp;&nbsp;&nbsp;&nbsp;</td>\r\n" +
                        "    <td>Error Percentage&nbsp;&nbsp;&nbsp;&nbsp;</td>\r\n" +
                        DataContent +
                        "  </tr>\r\n" +
                        "</table>";


    }

}
