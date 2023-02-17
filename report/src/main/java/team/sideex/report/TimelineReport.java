/*
 * @author  HSU
 */
package team.sideex.report;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class TimelineReport {

    private final Html html = new Html();
    private final ArrayList<String> startTimeList = new ArrayList<>();
    private final ArrayList<String> endTimeList = new ArrayList<>();
    private final ArrayList<String> commandList = new ArrayList<>();
    private final ArrayList<ArrayList<String>> commandTimePoint = new ArrayList<>();
    private final ArrayList<Integer> users = new ArrayList<>();
    private final ArrayList<ArrayList<Integer>> commandUsers = new ArrayList<>();
    private final ArrayList<ArrayList<Integer>> Hit = new ArrayList<>();
    private final ArrayList<ArrayList<Integer>> Error = new ArrayList<>();
    private final ArrayList<String> statisticsName = new ArrayList<>();
    private final ArrayList<Double> Avg = new ArrayList<>();
    private final ArrayList<Long> Min = new ArrayList<>();
    private final ArrayList<Long> Max = new ArrayList<>();
    private final ArrayList<Double> Median = new ArrayList<>();
    private final ArrayList<Long> P90 = new ArrayList<>();
    private final ArrayList<Long> P95 = new ArrayList<>();
    private final ArrayList<Long> P99 = new ArrayList<>();
    private final ArrayList<ArrayList<ArrayList<Long>>> eachCommandTestResponseTime = new ArrayList<>();
    private final ArrayList<ArrayList<Long>> eachTestResponseTime = new ArrayList<>();
    private final ArrayList<ArrayList<ArrayList<Double>>> commandAvg = new ArrayList<>();
    private final ArrayList<ArrayList<ArrayList<Long>>> commandMin = new ArrayList<>();
    private final ArrayList<ArrayList<ArrayList<Long>>> commandMax = new ArrayList<>();
    private final ArrayList<ArrayList<ArrayList<Double>>> commandMedian = new ArrayList<>();
    private final ArrayList<ArrayList<ArrayList<Long>>> commandP90 = new ArrayList<>();
    private final ArrayList<ArrayList<ArrayList<Long>>> commandP95 = new ArrayList<>();
    private final ArrayList<ArrayList<ArrayList<Long>>> commandP99 = new ArrayList<>();
    private final ArrayList<String> timeStamp = new ArrayList<>();
    private final ArrayList<String> xAxisLabel = new ArrayList<>();


    // merge EachTestResponseTime into ResponseTime
    //for chartjs
    private final ArrayList<String> lineColor = new ArrayList<>();
    private final ArrayList<String> dataName = new ArrayList<>();
    private final ArrayList<String> labelName = new ArrayList<>();
    private final ArrayList<String> yaxis = new ArrayList<>();
    private JsonParse jsonFile = null;
    private ArrayList<String> jsonNames = new ArrayList<>();

    private String Timeline_Report_Content = "";
    private String Request_Statistics_Content = "";
    private String checkBox = "";
    private String userData = "";
    private String hitData = "";
    private String errorData = "";
    private String responseTimeData = "";
    private String dataset = "";
    private String yAxisData = "";
    private String javascript = "";

    private int checkValue = 0;

    private int hitTypeCount = 0;
    private int commandNumber = 0;

    public void generate_report(String requestStats, JsonParse jsonParseFile, ArrayList<String> testResults,
                                ArrayList<String> command, String reportPath) throws java.text.ParseException {

        jsonNames = testResults;
        Request_Statistics_Content = requestStats;
        jsonFile = jsonParseFile;


        for (int i = 1; i < command.size(); i++)
            commandList.add(command.get(i));

        try {

            parse();
            html.generate(Timeline_Report_Content, reportPath);

        } catch (ParseException e1) {

            e1.printStackTrace();
        }

    }

    public void preprocessing() {

        statisticsName.add("Avg");
        statisticsName.add("Min");
        statisticsName.add("Max");
        statisticsName.add("Median");
        statisticsName.add("P90");
        statisticsName.add("P95");
        statisticsName.add("P99");

        // data name
        dataName.add("allUsers");
        yaxis.add("y1");
        for (int i = 0; i < hitTypeCount; i++) {
            dataName.add(commandList.get(i) + "Users");
            yaxis.add("y1");
        }

        dataName.add("allHit");
        yaxis.add("y3");
        for (int i = 0; i < hitTypeCount; i++) {
            dataName.add(commandList.get(i) + "Hit");
            yaxis.add("y3");
        }

        dataName.add("allError");
        yaxis.add("y3");

        for (int i = 0; i < hitTypeCount; i++) {
            dataName.add(commandList.get(i) + "Error");
            yaxis.add("y3");
        }

        for (int i = 0; i < 7; i++)
            yaxis.add("y2");

        dataName.add("allAvg");
        dataName.add("allMin");
        dataName.add("allMax");
        dataName.add("allMedian");
        dataName.add("allP90");
        dataName.add("allP95");
        dataName.add("allP99");

        for (int i = 0; i < hitTypeCount; i++)
            for (int j = 0; j < 7; j++) {
                dataName.add(commandList.get(i) + statisticsName.get(j));
                yaxis.add("y2");
            }


        labelName.add("ALL - Users");

        for (int i = 0; i < hitTypeCount; i++)
            labelName.add(commandList.get(i) + " - " + "Users");

        labelName.add("ALL - Hit/s Total");

        for (int i = 0; i < hitTypeCount; i++)
            labelName.add(commandList.get(i) + " - Hit/s Total");

        labelName.add("ALL - Hit/s Errors");

        for (int i = 0; i < hitTypeCount; i++)
            labelName.add(commandList.get(i) + " - Hit/s Errors");

        labelName.add("ALL - Avg - Response Time");
        labelName.add("ALL - Min - Response Time");
        labelName.add("ALL - Max - Response Time");
        labelName.add("ALL - Median - Response Time");
        labelName.add("ALL - P90 - Response Time");
        labelName.add("ALL - P95 - Response Time");
        labelName.add("ALL - P99 - Response Time");

        for (int i = 0; i < hitTypeCount; i++)
            for (int j = 0; j < 7; j++)
                labelName.add(commandList.get(i) + " - " + statisticsName.get(j) + " - " + "Response Time");

    }


    @SuppressWarnings("StringConcatenationInLoop")
    public void parse() throws ParseException, java.text.ParseException {


        int fileSize = jsonNames.size();


        for (int i = 0; i < fileSize; i++) {

            JSONObject startJson = jsonFile.getJson(i);
            JSONObject endJson = jsonFile.getJson(i);

            startTimeList.add((String) startJson.get("startTime"));
            endTimeList.add((String) endJson.get("endTime"));
        }

        startTimeList.sort(null);
        endTimeList.sort(null);

        String startTime = startTimeList.get(0);
        String endTime = endTimeList.get(fileSize - 1);


        String startDate = startTime.substring(0, 4) + "-" + startTime.substring(4, 6) + "-" + startTime.substring(6, 8);
        String endDate = endTime.substring(0, 4) + "-" + endTime.substring(4, 6) + "-" + endTime.substring(6, 8);

        int startHour = Integer.parseInt(startTime.substring(9, 11));
        int endHour = Integer.parseInt(endTime.substring(9, 11));
        int startMinute = Integer.parseInt(startTime.substring(12, 14));
        int endMinute = Integer.parseInt(endTime.substring(12, 14));


        Random rgb = new Random();


        JSONObject json, cases, records;
        JSONArray recordsArray, casesArray;
        Long commandTime;


        //Calculate total cases HitTypeCount
        for (int i = 0; i < fileSize; i++)
            recordsArray = jsonFile.getRecordsArray(i);


        //Data of User
        int userCount;
        String status;

        //Data of Hit, Error, ResponseTime
        double timeDifference;
        int dataPosition, commandAmount, errorCount;

        String year, month, day, EachTimeIntervalPointString;

        String originTime = String.format("%02d", startHour);
        originTime += ":";
        originTime += String.format("%02d", startMinute);
        originTime += ":00";

        String endPointTime = String.format("%02d", endHour);
        endPointTime += ":";
        endPointTime += String.format("%02d", endMinute + 1);
        endPointTime += ":00";

        Date currentDate;
        Date endPointDate;
        Calendar startCalendar = Calendar.getInstance();
        Calendar eachTimeIntervalPoint = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        Date caseStartDate;
        Date caseEndDate;
        Date labelDate;
        Date commandDateOne;
        Date commandDateTwo;

        Calendar caseStartCalendar = Calendar.getInstance();
        Calendar caseEndCalendar = Calendar.getInstance();
        Calendar labelCalendar = Calendar.getInstance();
        Calendar commandCalendarOne = Calendar.getInstance();
        Calendar commandCalendarTwo = Calendar.getInstance();


        currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate + " " + originTime);
        endPointDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate + " " + endPointTime);


        // Original X point
        startCalendar.setTime(currentDate);
        eachTimeIntervalPoint.setTime(currentDate);
        // Final X point
        endCalendar.setTime(endPointDate);


        // Add x point in every 30 seconds to create X axis points
        int timeLength = eachTimeIntervalPoint.getTime().toString().length();


        while (eachTimeIntervalPoint.before(endCalendar)) {


            month = eachTimeIntervalPoint.getTime().toString().substring(4, 7);
            month = monthStringToString(month);
            year = eachTimeIntervalPoint.getTime().toString().substring(timeLength - 4, timeLength);
            day = eachTimeIntervalPoint.getTime().toString().substring(8, 19);

            EachTimeIntervalPointString = year + "-" + month + "-" + day;


            timeStamp.add(EachTimeIntervalPointString);

            eachTimeIntervalPoint.add(Calendar.SECOND, 1);
        }

        //final time-point
        month = eachTimeIntervalPoint.getTime().toString().substring(4, 7);
        month = monthStringToString(month);
        year = eachTimeIntervalPoint.getTime().toString().substring(timeLength - 4, timeLength);
        day = eachTimeIntervalPoint.getTime().toString().substring(8, 19);

        EachTimeIntervalPointString = year + "-" + month + "-" + day;

        timeStamp.add(EachTimeIntervalPointString);


        // initial Hit Data
        hitTypeCount = commandList.size();
        commandNumber = hitTypeCount + 1;
        int timePointCount = timeStamp.size();


        for (int i = 0; i < hitTypeCount; i++) {

            int commandCount = 2;
            String newCommand;


            for (int j = 0; j < hitTypeCount; j++) {

                if (commandList.get(i).equals(commandList.get(j)) && i != j) {

                    newCommand = commandList.get(j);
                    newCommand += Integer.toString(commandCount);
                    commandList.set(j, newCommand);
                    commandCount++;
                }

            }
        }

        preprocessing();


        for (int i = 0; i < timePointCount; i++) {

            commandUsers.add(new ArrayList<>());
            Hit.add(new ArrayList<>());
            Error.add(new ArrayList<>());

            eachTestResponseTime.add(new ArrayList<>());
            eachCommandTestResponseTime.add(new ArrayList<>());


            for (int j = 0; j < hitTypeCount; j++) {

                commandUsers.get(i).add(0);
                Hit.get(i).add(0);
                Error.get(i).add(0);

                eachCommandTestResponseTime.get(i).add(new ArrayList<>());

            }
        }


        for (int i = 0; i < timeStamp.size(); i++)
            users.add(0);


        for (int i = 0; i < fileSize; i++)
            commandTimePoint.add(new ArrayList<>());


        //Data of VirtualUser
        for (int i = 0; i < fileSize; i++) {


            JSONObject eachJson = jsonFile.getJson(i);


            String caseStartTime = eachJson.get("startTime").toString();
            caseStartTime += ":000";
            caseStartDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS").parse(caseStartTime);
            caseStartCalendar.setTime(caseStartDate);


            String caseEndTime = eachJson.get("endTime").toString();
            caseEndTime += ":000";
            caseEndDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS").parse(caseEndTime);
            caseEndCalendar.setTime(caseEndDate);

            casesArray = (JSONArray) eachJson.get("cases");
            cases = (JSONObject) casesArray.get(0);
            recordsArray = (JSONArray) cases.get("records");


            //Add each command time interval
            Calendar caseCommandTime = Calendar.getInstance();
            caseCommandTime.setTime(caseStartDate);

            SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS");
            commandTimePoint.get(i).add(simpleFormat.format(caseStartCalendar.getTime()));


            for (int j = 0; j < recordsArray.size(); j++) {

                records = (JSONObject) recordsArray.get(j);
                commandTime = (Long) records.get("time");
                caseCommandTime.add(Calendar.MILLISECOND, commandTime.intValue());

                Calendar round = Calendar.getInstance();
                Date roundDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS").parse(simpleFormat.format(caseCommandTime.getTime()));
                round.setTime(roundDate);

                int firstDigit = Integer.parseInt(simpleFormat.format(round.getTime()).substring(18, 19));

                if (firstDigit > 4)
                    round.add(Calendar.SECOND, 1);

                ////to plugin
                if (j == (recordsArray.size() - 1))
                    commandTimePoint.get(i).add(caseEndTime);
                else
                    commandTimePoint.get(i).add(simpleFormat.format(round.getTime()).substring(0, 18) + "000");

            }


            for (int j = 0; j < timeStamp.size(); j++) {

                //allUser

                currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStamp.get(j));
                eachTimeIntervalPoint.setTime(currentDate);

                if (eachTimeIntervalPoint.equals(caseStartCalendar) || eachTimeIntervalPoint.equals(caseEndCalendar)) {

                    userCount = users.get(j);
                    userCount++;
                    users.set(j, userCount);

                }


                //commandUser


                if (eachTimeIntervalPoint.before(caseStartCalendar) || eachTimeIntervalPoint.after(caseEndCalendar))
                    continue;

                // Active User for all
                if (caseStartCalendar.before(eachTimeIntervalPoint) && caseEndCalendar.after(eachTimeIntervalPoint)) {

                    userCount = users.get(j);
                    userCount++;
                    users.set(j, userCount);
                }

                //Active User for each command, 5 timepoints and 4 intervals
                for (int k = 0; k < recordsArray.size(); k++) {

                    String commandTimeOne = commandTimePoint.get(i).get(k);
                    String commandTimeTwo = commandTimePoint.get(i).get(k + 1);

                    commandDateOne = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS").parse(commandTimeOne);
                    commandDateTwo = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS").parse(commandTimeTwo);

                    commandCalendarOne.setTime(commandDateOne);
                    commandCalendarTwo.setTime(commandDateTwo);

                    if (eachTimeIntervalPoint.before(commandCalendarOne) || eachTimeIntervalPoint.after(commandCalendarTwo))
                        continue;
                    else {


                        userCount = commandUsers.get(j).get(k);
                        userCount++;
                        commandUsers.get(j).set(k, userCount);

                    }

                }
            }
        }


        for (int i = 0; i < fileSize; i++) {

            json = jsonFile.getJson(i);
//            


            startTime = json.get("startTime").toString();
            startTime += ":000";
            caseStartDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS").parse(startTime);
            caseStartCalendar.setTime(caseStartDate);

            casesArray = (JSONArray) json.get("cases");
            cases = (JSONObject) casesArray.get(0);
            recordsArray = (JSONArray) cases.get("records");


            for (int j = 0; j < recordsArray.size(); j++) {

                records = (JSONObject) recordsArray.get(j);
                status = records.get("status").toString();
                commandTime = (Long) records.get("time");


                //Error
                timeDifference = (double) (caseStartCalendar.getTimeInMillis() - startCalendar.getTimeInMillis());
                timeDifference /= 1000;
                timeDifference = Math.round(timeDifference);
                dataPosition = (int) timeDifference;
                eachTestResponseTime.get(dataPosition).add(commandTime);
                eachCommandTestResponseTime.get(dataPosition).get(j).add(commandTime);
                if (!xAxisLabel.contains(timeStamp.get(dataPosition)))
                    xAxisLabel.add(timeStamp.get(dataPosition));
                commandAmount = Hit.get(dataPosition).get(j);
                commandAmount++;
                Hit.get(dataPosition).set(j, commandAmount);
                if (status.equals("fail")) {

                    //Error Data
                    errorCount = Error.get(dataPosition).get(j);
                    errorCount++;
                    Error.get(dataPosition).set(j, errorCount);

                }
                caseStartCalendar.add(Calendar.MILLISECOND, commandTime.intValue());

            }
        }


        xAxisLabel.sort(null);


        //AllResponseTime statistics
        for (ArrayList<Long> longs : eachTestResponseTime) {


            longs.sort(null);

            if (longs.size() != 0) {

                int medianPosition = (longs.size() / 2),
                        p90Position = (int) Math.round(longs.size() * 0.9),
                        p95Position = (int) Math.round(longs.size() * 0.95),
                        p99Position = (int) Math.round(longs.size() * 0.99);

                p90Position--;
                p95Position--;
                p99Position--;

                if (p90Position == longs.size())
                    p90Position--;
                if (p95Position == longs.size())
                    p95Position--;
                if (p99Position == longs.size())
                    p99Position--;

                double average = 0.0, medianValue;

                Long minValue = longs.get(0),
                        maxValue = longs.get(longs.size() - 1),
                        p90Value = longs.get(p90Position),
                        p95Value = longs.get(p95Position),
                        p99Value = longs.get(p99Position);


                if (longs.size() % 2 == 0)
                    medianValue = ((double) (longs.get(medianPosition)
                            + longs.get(medianPosition - 1)) / 2);
                else
                    medianValue = longs.get(medianPosition);


                Min.add(minValue);
                Max.add(maxValue);
                Median.add(medianValue);
                P90.add(p90Value);
                P95.add(p95Value);
                P99.add(p99Value);


                for (Long aLong : longs) average += aLong;

                if (longs.size() != 0)
                    average = average / longs.size();

                Avg.add(average);

            }

        }

        for (int i = 0; i < xAxisLabel.size(); i++) {

            commandAvg.add(new ArrayList<>());
            commandMin.add(new ArrayList<>());
            commandMax.add(new ArrayList<>());
            commandMedian.add(new ArrayList<>());
            commandP90.add(new ArrayList<>());
            commandP95.add(new ArrayList<>());
            commandP99.add(new ArrayList<>());

            for (int j = 0; j < hitTypeCount; j++) {

                commandAvg.get(i).add(new ArrayList<>());
                commandMin.get(i).add(new ArrayList<>());
                commandMax.get(i).add(new ArrayList<>());
                commandMedian.get(i).add(new ArrayList<>());
                commandP90.get(i).add(new ArrayList<>());
                commandP95.get(i).add(new ArrayList<>());
                commandP99.get(i).add(new ArrayList<>());

            }
        }


        dataPosition = -1;

        // Data of response time for each command

        for (int i = 0; i < eachCommandTestResponseTime.size(); i++) {


            if (eachTestResponseTime.get(i).size() != 0)
                dataPosition++;

            for (int j = 0; j < hitTypeCount; j++) {

                eachCommandTestResponseTime.get(i).get(j).sort(null);

                if (eachCommandTestResponseTime.get(i).get(j).size() != 0) {

                    int medianPosition = (eachCommandTestResponseTime.get(i).get(j).size() / 2),
                            p90Position = (int) Math.round(eachCommandTestResponseTime.get(i).get(j).size() * 0.9),
                            p95Position = (int) Math.round(eachCommandTestResponseTime.get(i).get(j).size() * 0.95),
                            p99Position = (int) Math.round(eachCommandTestResponseTime.get(i).get(j).size() * 0.99);

                    p90Position--;
                    p95Position--;
                    p99Position--;

                    if (p90Position == eachCommandTestResponseTime.get(i).get(j).size())
                        p90Position--;
                    if (p95Position == eachCommandTestResponseTime.get(i).get(j).size())
                        p95Position--;
                    if (p99Position == eachCommandTestResponseTime.get(i).get(j).size())
                        p99Position--;

                    double average = 0.0, medianValue;

                    Long minValue = eachCommandTestResponseTime.get(i).get(j).get(0),
                            maxValue = eachCommandTestResponseTime.get(i).get(j)
                                    .get(eachCommandTestResponseTime.get(i).get(j).size() - 1),
                            p90Value = eachCommandTestResponseTime.get(i).get(j).get(p90Position),
                            p95Value = eachCommandTestResponseTime.get(i).get(j).get(p95Position),
                            p99Value = eachCommandTestResponseTime.get(i).get(j).get(p99Position);

                    if (eachCommandTestResponseTime.get(i).get(j).size() % 2 == 0)
                        medianValue = ((double) (eachCommandTestResponseTime.get(i).get(j).get(medianPosition)
                                + eachCommandTestResponseTime.get(i).get(j).get(medianPosition - 1)) / 2);
                    else
                        medianValue = eachCommandTestResponseTime.get(i).get(j).get(medianPosition);

                    for (int k = 0; k < eachCommandTestResponseTime.get(i).get(j).size(); k++)
                        average += eachCommandTestResponseTime.get(i).get(j).get(k);

                    average = average / eachCommandTestResponseTime.get(i).get(j).size();


                    commandAvg.get(dataPosition).get(j).add(average);
                    commandMin.get(dataPosition).get(j).add(minValue);
                    commandMax.get(dataPosition).get(j).add(maxValue);
                    commandMedian.get(dataPosition).get(j).add(medianValue);
                    commandP90.get(dataPosition).get(j).add(p90Value);
                    commandP95.get(dataPosition).get(j).add(p95Value);
                    commandP99.get(dataPosition).get(j).add(p99Value);

                }
            }

        }


        // checkbox
        checkBox += "<div class=\"checkbox\">";

        for (int i = 0; i < 3; i++) {

            if (i == 0)
                checkBox += "<label for=\"Virtual Users\"><b> Virtual Users</b></label><br>\r\n";
            else if (i == 1)
                checkBox += "<label for=\"Hits\"><b> Hits</b></label><br>\r\n";
            else checkBox += "<label for=\"Errors\"><b> Errors</b></label><br>\r\n";

            for (int j = 0; j < (hitTypeCount + 1); j++) {

                String commandName;

                if (j == 0)
                    commandName = "ALL";
                else
                    commandName = commandList.get(j - 1);

                if (j == 0)
                    checkBox += "<input type=\"checkbox\" onclick=\"updataChart(this)\" value=\"" + checkValue
                            + "\"checked=\"\">";

                else
                    checkBox += "<input type=\"checkbox\" onclick=\"updataChart(this)\" value=\"" + checkValue
                            + "\">";

                checkBox += "<label>" + commandName + "</label><br>\r\n";

                checkValue++;
            }
        }


        //checkbox of Response Time

        checkBox += "<label for=\"Response Time\"><b> Response Time</b></label><br>\r\n";

        for (int i = 0; i < (hitTypeCount + 1); i++) {

            if (i == 0)
                checkBox += "<label><b> ALL</b></label><br>\r\n";
            else
                checkBox += "<label><b> " + commandList.get(i - 1) + "</b></label><br>\r\n";

            for (int j = 0; j < 7; j++) {

                if (i == 0 && j == 0)
                    checkBox += "<input type=\"checkbox\" onclick=\"updataChart(this)\" value=\"" + checkValue
                            + "\"checked=\"\">";

                else
                    checkBox += "<input type=\"checkbox\" onclick=\"updataChart(this)\" value=\"" + checkValue
                            + "\">";

                if (j == 0)
                    checkBox += "<label>" + statisticsName.get(j) + "</label>\r\n";
                else if (j == 1)
                    checkBox += "<label>" + statisticsName.get(j) + "</label>\r\n";
                else if (j == 2)
                    checkBox += "<label>" + statisticsName.get(j) + "</label>\r\n";
                else if (j == 3)
                    checkBox += "<label>" + statisticsName.get(j) + "</label><br>\r\n";
                else if (j == 4)
                    checkBox += "<label>" + statisticsName.get(j) + "</label>\r\n";
                else if (j == 5)
                    checkBox += "<label>" + statisticsName.get(j) + "</label>\r\n";
                else checkBox += "<label>" + statisticsName.get(j) + "</label><br>\r\n";

                checkValue++;

            }
        }

        checkBox += "</div>\r\n\n";


        userData += "const allUsers = [";


        for (String labelTime : xAxisLabel) {

            labelDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(labelTime);
            labelCalendar.setTime(labelDate);

            timeDifference = (double) (labelCalendar.getTimeInMillis() - startCalendar.getTimeInMillis());
            timeDifference /= 1000;
            timeDifference = Math.round(timeDifference);
            dataPosition = (int) timeDifference;

            userData += users.get(dataPosition) + ", ";

        }

        userData += "]\r\n";

        for (int i = 0; i < hitTypeCount; i++) {

            userData += "const " + dataName.get(i + 1) + " = [";

            for (String labelTime : xAxisLabel) {

                labelDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(labelTime);
                labelCalendar.setTime(labelDate);

                timeDifference = (double) (labelCalendar.getTimeInMillis() - startCalendar.getTimeInMillis());
                timeDifference /= 1000;
                timeDifference = Math.round(timeDifference);
                dataPosition = (int) timeDifference;

                userData += commandUsers.get(dataPosition).get(i) + ", ";

            }

            userData += "]\r\n";

        }


        //hitData to js
        for (int i = 0; i <= hitTypeCount; i++) {

            hitData += "const " + dataName.get(hitTypeCount + 1 + i) + " = [";

            for (String s : xAxisLabel) {

                int allCount = 0;

                labelDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
                labelCalendar.setTime(labelDate);

                timeDifference = (double) (labelCalendar.getTimeInMillis() - startCalendar.getTimeInMillis());
                timeDifference /= 1000;
                timeDifference = Math.round(timeDifference);
                dataPosition = (int) timeDifference;


                if (i == 0) {

                    for (int k = 0; k < hitTypeCount; k++)
                        allCount += Hit.get(dataPosition).get(k);

                    if (allCount != 0)
                        hitData += allCount + ", ";
                    else
                        hitData += ", ";

                } else {
                    if (Hit.get(dataPosition).get(i - 1) != 0)
                        hitData += Hit.get(dataPosition).get(i - 1) + ", ";
                    else
                        hitData += ", ";
                }
            }

            hitData += "]\r\n";
        }


        //errorData to js
        for (int i = 0; i <= hitTypeCount; i++) {


            errorData += "const " + dataName.get((hitTypeCount + 1) * 2 + i) + " = [";

            for (String s : xAxisLabel) {

                int allCount = 0, hitCount = 0;


                labelDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
                labelCalendar.setTime(labelDate);

                timeDifference = (double) (labelCalendar.getTimeInMillis() - startCalendar.getTimeInMillis());
                timeDifference /= 1000;
                timeDifference = Math.round(timeDifference);
                dataPosition = (int) timeDifference;

                if (i == 0) {

                    for (int k = 0; k < hitTypeCount; k++) {
                        allCount += Error.get(dataPosition).get(k);
                        hitCount += Hit.get(dataPosition).get(k);
                    }

                    if (allCount != 0)
                        errorData += allCount + ", ";
                    else {

                        if (hitCount != 0)
                            errorData += "0, ";
                        else
                            errorData += ", ";
                    }

                } else {

                    if (Error.get(dataPosition).get(i - 1) != 0)
                        errorData += Error.get(dataPosition).get(i - 1) + ", ";

                    else {

                        if (Hit.get(dataPosition).get(i - 1) != 0)
                            errorData += "0, ";
                        else
                            errorData += ", ";
                    }
                }

            }

            errorData += "]\r\n";
        }


        errorData += "\r\n\n";


        //ResponseTime Data for all
        for (int i = 0; i < 7; i++) {

            responseTimeData += "const all" + statisticsName.get(i) + " = [";

            for (int j = 0; j < xAxisLabel.size(); j++) {

                if (i == 0)
                    responseTimeData += Avg.get(j);
                else if (i == 1)
                    responseTimeData += Min.get(j);
                else if (i == 2)
                    responseTimeData += Max.get(j);
                else if (i == 3)
                    responseTimeData += Median.get(j);
                else if (i == 4)
                    responseTimeData += P90.get(j);
                else if (i == 5)
                    responseTimeData += P95.get(j);
                else responseTimeData += P99.get(j);

                responseTimeData += " ,";
            }

            responseTimeData += "]\r\n";

        }


        //ResponseTime Data for each

        for (int i = 0; i < hitTypeCount; i++)
            for (int j = 0; j < 7; j++) {

                responseTimeData += "const " + commandList.get(i) + statisticsName.get(j) + " = [";

                for (int k = 0; k < xAxisLabel.size(); k++) {

                    if (j == 0) {
                        if (commandAvg.get(k).get(i).size() != 0)
                            responseTimeData += commandAvg.get(k).get(i).get(0);
                    } else if (j == 1) {
                        if (commandMin.get(k).get(i).size() != 0)
                            responseTimeData += commandMin.get(k).get(i).get(0);
                    } else if (j == 2) {
                        if (commandMax.get(k).get(i).size() != 0)
                            responseTimeData += commandMax.get(k).get(i).get(0);
                    } else if (j == 3) {
                        if (commandMedian.get(k).get(i).size() != 0)
                            responseTimeData += commandMedian.get(k).get(i).get(0);
                    } else if (j == 4) {
                        if (commandP90.get(k).get(i).size() != 0)
                            responseTimeData += commandP90.get(k).get(i).get(0);
                    } else if (j == 5) {
                        if (commandP95.get(k).get(i).size() != 0)
                            responseTimeData += commandP95.get(k).get(i).get(0);
                    } else {
                        if (commandP99.get(k).get(i).size() != 0)
                            responseTimeData += commandP99.get(k).get(i).get(0);
                    }

                    responseTimeData += ", ";

                }

                responseTimeData += "]\r\n";

            }


        yAxisData += userData + hitData + errorData + responseTimeData;


        //Generate line color
        for (int i = 0; i < ((hitTypeCount + 1) * 3 + (hitTypeCount + 1) * 7); i++) {

            String hex = "";

            hex += "#" + Integer.toHexString(rgb.nextInt(255)) + Integer.toHexString(rgb.nextInt(255))
                    + Integer.toHexString(rgb.nextInt(255));

            lineColor.add(hex);

        }


        //Dataset information
        dataset += "datasets: [\r\n";

        int numberOfShow = 0;

        for (int i = 0; i < dataName.size(); i++) {

            dataset += "\t{\r\n";

            dataset += "\t\tdata: " + dataName.get(i) + ",\r\n";
            dataset += "\t\tlabel: \"" + labelName.get(i) + "\",\r\n";
            dataset += "\t\tbackgroundColor: \"" + lineColor.get(i) + "\",\r\n";
            dataset += "\t\tborderColor: \"" + lineColor.get(i) + "\",\r\n";
            dataset += "\t\tfill: false,\r\n";

            if (i % (hitTypeCount + 1) == 0 && numberOfShow < 4) {
                dataset += "\t\thidden: false,\r\n";
                numberOfShow++;
            } else
                dataset += "\t\thidden: true,\r\n";

            dataset += "\t\tpointRadius: 3,\r\n";
            //axis0 or axis1
            dataset += "\t\tyAxisID: \"" + yaxis.get(i) + "\"\r\n";

            if (i != dataName.size() - 1)
                dataset += "\t},\r\n";
            else
                dataset += "\t}\r\n";
        }


        dataset += "\t]\r\n";

        generateHtml();


    }


    public void generateHtml() {


        javascript += "<div>\r\n"
                + "      <canvas id=\"line-chart\" class=\"chart\" width=\"1250\" height=\"900\"></canvas>\r\n"
                + "    </div>"
                + "<script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script> \r\n <script>\r\n"
                + "var  commandNumber = "
                + commandNumber
                + ";\n"
                + yAxisData
                + "var report = new Chart(document.getElementById(\"line-chart\"), {\r\n"
                + "  type: 'line',\r\n"
                + "  data: {\r\n";


        javascript += "labels: [";

        for (int i = 0; i < xAxisLabel.size(); i++) {

            javascript += "\"" + xAxisLabel.get(i) + "\",";

            if (i == xAxisLabel.size() / 2)
                javascript += "\n\t\t";

        }

        javascript += "],\r\n";

        javascript += dataset
                + "  },\r\n"
                + "  options: {\r\n"
                + "	   responsive: false,"
                + "    spanGaps: true,\r\n"
                + "    plugins: {\r\n"
                + "  legend: {\r\n"
                + "		   onClick: (e) => e.stopPropagation(),\r\n"
                + "		   position: 'bottom',\r\n"
                + "		   labels: {\r\n"
                + "			filter: function(legendItem, chartData) {\r\n"
                + "                if (legendItem.datasetIndex === 0 || legendItem.datasetIndex === (commandNumber) || \r\n"
                + "					legendItem.datasetIndex === (commandNumber * 2) || legendItem.datasetIndex === (commandNumber * 3))\r\n"
                + "                    return true;\r\n"
                + "            return false;\r\n"
                + "            }\r\n"
                + "		   }\r\n"
                + "      }\r\n"
                + "    },"
                + "    scales: {\r\n"
                + "	  y1: {\r\n"
                + "        type: 'linear',\r\n"
                + "        display: true,\r\n"
                + "        position: 'left',\r\n"
                + "	  title: {\r\n"
                + "			display: true,\r\n"
                + "	   font: {\r\n"
                + "			size: 20,\r\n"
                + "            family: \"Times New Roman\"\r\n"
                + "        },"
                + "			text: \"Virtual  Users\"\r\n"
                + "		},"
                + "		beginAtZero: true\r\n"
                + "      },\r\n"
                + "	  y2: {\r\n"
                + "        type: 'linear',\r\n"
                + "        display: true,\r\n"
                + "        position: 'right',\r\n"
                + "	  title: {\r\n"
                + "			display: true,\r\n"
                + "	   font: {\r\n"
                + "			size: 20,\r\n"
                + "            family: \"Times New Roman\"\r\n"
                + "        },"
                + "			text: \"Response  Time (ms)\"\r\n"
                + "		},"
                + "		beginAtZero: true\r\n"
                + "      },\r\n"
                + "	  y3: {\r\n"
                + "        type: 'linear',\r\n"
                + "        display: true,\r\n"
                + "        position: 'right',\r\n"
                + "	  title: {\r\n"
                + "			display: true,\r\n"
                + "	   font: {\r\n"
                + "			size: 20,\r\n"
                + "            family: \"Times New Roman\"\r\n"
                + "        },"
                + "			text: \"Hit/s\"\r\n"
                + "		},"
                + "		beginAtZero: true\r\n"
                + "      }"
                + "	}\r\n"
                + "  }\r\n"
                + "});\r\n\n";


        javascript += "var index;\r\n"
                + "\r\n"
                + "var filterLegend = function(item, chart) {\r\n"
                + "\r\n"
                + "	\r\n"
                + "\r\n"
                + "	if(report.data.datasets[item.datasetIndex].hidden == true)		\r\n"
                + "		return false;\r\n"
                + "	\r\n"
                + "    else if(report.data.datasets[item.datasetIndex].hidden == false) \r\n"
                + " 		return true;\r\n"
                + "\r\n"
                + "}\r\n"
                + "\r\n"
                + "\r\n"
                + "\r\n"
                + "function updataChart(data){\r\n"
                + "  \r\n"
                + "	index = data.value;\r\n"
                + "\r\n"
                + "	report.data.datasets[index].hidden = !(report.data.datasets[index].hidden);\r\n"
                + "	\r\n"
                + "	report.options.plugins.legend.labels.filter = filterLegend;\r\n"
                + "\r\n"
                + "    \r\n"
                + "  report.update();\r\n"
                + "} "
                + "</script>\r\n";


        Timeline_Report_Content += "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "  <head>\r\n"
                + "    <!-- Required meta tags -->\r\n"
                + "    <meta charset=\"utf-8\">\r\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
                + "    <title>Chart.js Integration</title>\r\n"
                + "    <!--Chart.js JS CDN--> \r\n"
                + "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js\"></script> \r\n"
                + "\r\n"
                + "  \r\n"
                + "  <style>\r\n"

                + "	 .reportTitle{\r\n"
                + "		position: absolute;\r\n"
                + "\r\n"
                + "		font-size: 25px;\r\n"
                + "	  }\r\n"
                + "	  	img {\r\n"
                + "		position: absolute;\r\n"
                + "		margin-top: 30px;\r\n"
                + "		width: 200px;\r\n"
                + "  		height: 200px;\r\n"
                + "		\r\n"
                + "	}"

                + " .requestTitle{\r\n"
                + "		margin-top: 20px;\r\n"
                + "		margin-left: 700px;\r\n"
                + "		margin-bottom: 20px;\r\n"
                + "		font-size: 30px;\r\n"
                + "	}\r\n"
                + "\r\n"
                + "	.timelineTitle{\r\n"
                + "		margin-top: 20px;\r\n"
                + "		margin-left: 730px;\r\n"
                + "		margin-bottom: 20px;\r\n"
                + "		font-size: 30px;\r\n"
                + "	}"
                + "	  .request {\r\n"
                + "    margin-left: 240px;\r\n"
                + "	  border-top: 30px solid DodgerBlue;\r\n"
                + "	  border-bottom: 1px solid gray;\r\n"
                + "	  border-left: 1px solid gray;\r\n"
                + "	  border-right: 1px solid gray;\r\n"
                + "      border-radius: 10px;\r\n"
                + "	  \r\n"
                + "	}\r\n"
                + "    .checkbox {\r\n"
                + "	\r\n"
                + "  	  margin-top: 40px;"
                + "      position: absolute;\r\n"
                + "      left: 0px;\r\n"
                + "      width: 100%;\r\n"
                + "    }\r\n"
                + "    .chart {\r\n"
                + "		background-color: white;\r\n"
                + "	  border-top: 50px solid DodgerBlue;\r\n"
                + "	  border-bottom: 1px solid gray;\r\n"
                + "	  border-left: 1px solid gray;\r\n"
                + "	  border-right: 1px solid gray;\r\n"
                + "   border-radius: 10px;\r\n"
                + "	  margin-top: 20px;\r\n"
                + "	  margin-left: 240px;\r\n"
                + "\r\n"
                + "      position: absolute;\r\n"
                + "      width: 100%;\r\n"
                + "    }\r\n"
                + "\r\n"
                + "	table, th, td {\r\n"
                + "		background-color: white;\r\n"
                + "  		border: 1px solid black;\r\n"
                + "	}\r\n"
                + "\r\n"
                + "body {\r\n"
                + "  background-color: rgb(246, 239, 239);\r\n"
                + "}"
                + "  </style>\r\n"
                + "\r\n"
                + "</head>\n"
                + "<body>\n\n\n"

                + "<div class=\"reportTitle\">\r\n"
                + "	<label><b>SideeX-JMeter Report</b></label>\r\n"
                + "</div>\r\n"
                + "<div class=\"img\">\r\n"
                + "	<img src=\"https://sideex.io/static/media/sideex_logo.2728ffac.png\" >\r\n"
                + "</div>"


                + "<div  class=\"requestTitle\">\r\n"
                + "\r\n"
                + "	<label><b> Request Stats Report</b></label><br>\r\n"
                + "\r\n"
                + "</div>\n"

                + Request_Statistics_Content

                + "\n<div  class=\"timelineTitle\">\r\n"
                + "\r\n"
                + "	<label><b> Timeline Report</b></label><br>\r\n"
                + "\r\n"
                + "</div>\n"

                + checkBox
                + javascript

                + "</body>\r\n"
                + "</html>";


    }


    public String monthStringToString(String month) {

        switch (month) {
            case "Jan":
                return "01";
            case "Feb":

                return "02";
            case "Mar":
                return "03";
            case "Apr":
                return "04";
            case "May":
                return "05";
            case "Jun":
                return "06";
            case "Jul":
                return "07";
            case "Aug":
                return "08";
            case "Sep":
                return "09";
            case "Oct":
                return "10";
            case "Nov":
                return "11";
            case "Dec":
                return "12";
        }


        return "";

    }


}
