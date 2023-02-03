/*
 * @author  HSU
 */
package team.sideex.report;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class RequestStatsReport {

    private final TimelineReport report = new TimelineReport();
    private final JsonParse jsonParse = new JsonParse();
    private final ArrayList<String> commandList = new ArrayList<String>();
    private final ArrayList<Integer> commandSamplesCount = new ArrayList<Integer>();
    private final ArrayList<Long> commandTotalTime = new ArrayList<Long>();
    private final ArrayList<ArrayList<Long>> commandTimeData = new ArrayList<ArrayList<Long>>();
    private final ArrayList<ArrayList<Long>> commandTime = new ArrayList<ArrayList<Long>>();
    private final ArrayList<Long> All = new ArrayList<Long>();
    private final ArrayList<Integer> AvgTime = new ArrayList<Integer>();
    private final ArrayList<Double> Hit = new ArrayList<Double>();
    private final ArrayList<Long> line_90 = new ArrayList<Long>();
    private final ArrayList<Long> line_95 = new ArrayList<Long>();
    private final ArrayList<Long> line_99 = new ArrayList<Long>();
    private final ArrayList<Long> Min = new ArrayList<Long>();
    private final ArrayList<Long> Max = new ArrayList<Long>();
    private final ArrayList<Integer> error = new ArrayList<Integer>();
    private final ArrayList<Double> errorPercentage = new ArrayList<Double>();
    private final ArrayList<Long> commandTimeDifference = new ArrayList<Long>();
    private final String chromeVersionName = "";
    private String reportPath = "";
    private File folder = null;
    private ArrayList<String> testResults;
    private ArrayList<String> browserVersions;
    private int commandAmount = 0;
    private long timeDifference = 0, AllCommandTimeSum = 0;
    private String Request_Statistics_Content = "";

    public void startGenerateReport(String path, ArrayList<String> testResults) throws IOException, ParseException, java.text.ParseException {
        this.reportPath = path;
        File jsonFolder = new File(this.reportPath);


        this.folder = jsonFolder;
        this.testResults = testResults;

        parse();
        report.generate_report(Request_Statistics_Content, jsonParse, this.testResults, commandList, path);
    }

    public void preprocessing() throws IOException, ParseException {


        for (int i = 0; i < testResults.size(); i++) {

            JSONArray recordsArray = jsonParse.getRecordsArray(i);

            if (commandAmount < recordsArray.size())
                commandAmount = recordsArray.size();

        }


        for (int j = 0; j < commandAmount; j++)
            commandSamplesCount.add(0);


        String command = "";
        int commandCount = 0, totalCommandCount = 0;
        JSONObject records = null;

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


    public void parse() throws IOException, ParseException, java.text.ParseException {


        jsonParse.addTestResults(this.testResults);


        browserVersions = jsonParse.getBrowserVersion();
//        


        preprocessing();

        int position = 0, errorCount = 0;
        double percentage = 0;

        for (int j = 0; j < commandAmount; j++) {
            commandTimeData.add(new ArrayList<Long>());
            commandTime.add(new ArrayList<Long>());
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
            Long time = 0L;
            String status = "";

            String startTime = (String) json.get("startTime");
            String endTime = (String) json.get("endTime");
            startTime += ":000";
            Date commandDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS").parse(startTime);
            Calendar currentCalendar = Calendar.getInstance();

            currentCalendar.setTime(commandDate);
            commandTime.get(0).add(currentCalendar.getTimeInMillis());
            commandDate = null;


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


        for (int i = 0; i < commandTime.size(); i++)
            commandTime.get(i).sort(null);

        int commandCount = commandTime.get(commandTime.size() - 1).size();

        //Calculate allhit time
        timeDifference = commandTime.get(commandTime.size() - 1).get(commandCount - 1) - commandTime.get(0).get(0);
        timeDifference /= 1000;
        if (timeDifference == 0)
            timeDifference = 1;
        commandTimeDifference.add(timeDifference);

        for (int i = 0; i < commandTime.size(); i++) {

            commandCount = commandTime.get(i).size();

            //Calculate commandhit time
            timeDifference = commandTime.get(i).get(commandCount - 1) - commandTime.get(i).get(0);
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


        for (int i = 0; i < commandTimeData.size(); i++)
            Collections.sort(commandTimeData.get(i));
        Collections.sort(All);

        for (int i = 0; i < All.size(); i++)
            AllCommandTimeSum += All.get(i);


        commandTotalTime.add(0, AllCommandTimeSum);
        commandTimeData.add(0, All);


        errorCount = 0;
        //Calculate total error count
        for (int i = 0; i < error.size(); i++)
            errorCount += error.get(i);
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


    public void generateHtml() throws java.text.ParseException {

        String DataContent = "";


        for (int i = 0; i < commandList.size(); i++) {

            DataContent += (
                    "  </tr>\r\n" +
                            "  <tr>\r\n" +
                            "    <td>" + commandList.get(i) + "</td>\r\n" +
                            "    <td>" + AvgTime.get(i) + "</td>\r\n" +
                            "    <td>" + Hit.get(i) + "</td>\r\n" +
                            "    <td>" + commandTimeData.get(i).size() + "</td>\r\n" +
                            "    <td>" + line_90.get(i) + "</td>\r\n" +
                            "    <td>" + line_95.get(i) + "</td>\r\n" +
                            "    <td>" + line_99.get(i) + "</td>\r\n" +
                            "    <td>" + Min.get(i) + "</td>\r\n" +
                            "    <td>" + Max.get(i) + "</td>\r\n" +
                            "    <td>" + errorPercentage.get(i) + "%</td>\r\n"
            );

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
