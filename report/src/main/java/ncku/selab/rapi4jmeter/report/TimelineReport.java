/*
 * @author  HSU
 */
package ncku.selab.rapi4jmeter.report;

import freemarker.template.TemplateException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    //for Chart.js
    private final ArrayList<String> dataName = new ArrayList<>();
    private final ArrayList<String> labelName = new ArrayList<>();
    private final ArrayList<String> yaxis = new ArrayList<>();
    private JsonParse jsonFile = null;
    private ArrayList<String> jsonNames = new ArrayList<>();
    private String checkBox = "";
    private String userData = "";
    private String hitData = "";
    private String errorData = "";
    private String responseTimeData = "";
    private String yAxisData = "";

    private int checkValue = 0;

    private int hitTypeCount = 0;
    private final Map<String, Object> reportContentMap = new HashMap<>();
    Calendar startCalendar = Calendar.getInstance();
    Calendar labelCalendar = Calendar.getInstance();



    public void generate_report(String requestStats, JsonParse jsonParseFile, ArrayList<String> testResults,
                                ArrayList<String> command, String reportPath) throws java.text.ParseException {

        jsonNames = testResults;
        jsonFile = jsonParseFile;

        for (int i = 1; i < command.size(); i++)
            commandList.add(command.get(i));

        reportContentMap.put("requestStatisticsContent", requestStats);


        try {
            parse();

            // start generate report at here
            html.generate(reportPath, reportContentMap);

        } catch (ParseException e1) {

            e1.printStackTrace();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }

    }

    public void preprocessing() {

        statisticsName.addAll(Arrays.asList("Avg", "Min", "Max", "Median", "P90", "P95", "P99"));
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

        dataName.addAll(Arrays.asList("allAvg", "allMin", "allMax", "allMedian", "allP90", "allP95", "allP99"));

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

    private String getDateWithFormat(String timeNeedParse) {
        return timeNeedParse.substring(0, 4) + "-" + timeNeedParse.substring(4, 6) + "-" + timeNeedParse.substring(6, 8);
    }

    private int getTimeDiffDataPosition(String labelTime) throws java.text.ParseException {
        labelTime += ":000";
        Date labelDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").parse(labelTime);
        labelCalendar.setTime(labelDate);

        double timeDifference = (double) (labelCalendar.getTimeInMillis() - startCalendar.getTimeInMillis());
        timeDifference /= 1000;
        timeDifference = Math.round(timeDifference);
        return (int) timeDifference;
    }

    @SuppressWarnings("StringConcatenationInLoop")
    public void parse() throws ParseException, java.text.ParseException {


        int fileSize = jsonNames.size(); // Means that the amount of Rapi json report that .csv have.


        for (int i = 0; i < fileSize; i++) {

            JSONObject startJson = jsonFile.getJson(i);
            JSONObject endJson = jsonFile.getJson(i);

            startTimeList.add((String) startJson.get("startTime"));
            endTimeList.add((String) endJson.get("endTime"));
        }

        startTimeList.sort(null);
        endTimeList.sort(null);

        // startTime, endTime is from the Rapi json report. The format will be YYYYMMDD HH:MM:SS
        String startTime = startTimeList.get(0); // Ex: 20230527 19:22:37
        String endTime = endTimeList.get(fileSize - 1); // Ex: 20230527 19:27:37

        String startDate = getDateWithFormat(startTime);
        String endDate = getDateWithFormat(endTime);

        int startHour = Integer.parseInt(startTime.substring(9, 11));
        int endHour = Integer.parseInt(endTime.substring(9, 11));
        int startMinute = Integer.parseInt(startTime.substring(12, 14));
        int endMinute = Integer.parseInt(endTime.substring(12, 14));

        JSONObject json, cases, records;
        JSONArray recordsArray, casesArray;
        Long commandTime;


        //Data of User
        int userCount;
        String status;

        //Data of Hit, Error, ResponseTime
//        double timeDifference;

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

        Calendar eachTimeIntervalPoint = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        Date caseStartDate;
        Date caseEndDate;
        Date commandDateOne;
        Date commandDateTwo;

        Calendar caseStartCalendar = Calendar.getInstance();
        Calendar caseEndCalendar = Calendar.getInstance();
        Calendar commandCalendarOne = Calendar.getInstance();
        Calendar commandCalendarTwo = Calendar.getInstance();


        Date currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate + " " + originTime);
        Date endPointDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate + " " + endPointTime);


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
        reportContentMap.put("hitTypeCount", hitTypeCount);
        int commandNumber = hitTypeCount + 1;
        reportContentMap.put("commandNumber", commandNumber);


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

                    if (eachTimeIntervalPoint.before(commandCalendarOne) || eachTimeIntervalPoint.after(commandCalendarTwo)) {
                        continue;
                    } else {


                        userCount = commandUsers.get(j).get(k);
                        userCount++;
                        commandUsers.get(j).set(k, userCount);

                    }

                }
            }
        }


        for (int i = 0; i < fileSize; i++) {

            json = jsonFile.getJson(i);

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
                double timeDifference = (double) (caseStartCalendar.getTimeInMillis() - startCalendar.getTimeInMillis());
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

        StringJoiner stringJoiner = new StringJoiner(", ", "[", "]");
        for (String element : xAxisLabel) {
            stringJoiner.add("\"" + element + "\"");
        }
        String listXAxisLabel = stringJoiner.toString();
//        System.out.println("listXAxisLabel: " + listXAxisLabel);
        reportContentMap.put("listXAxisLabel", listXAxisLabel);


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

        // for what ?
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
                    checkBox += "<input type=\"checkbox\" onclick=\"updateChart(this)\" value=\"" + checkValue
                            + "\"checked=\"\">";

                else
                    checkBox += "<input type=\"checkbox\" onclick=\"updateChart(this)\" value=\"" + checkValue
                            + "\">";

                checkBox += "<label>" + commandName + "</label><br>\r\n";

                checkValue++;
            }
        }


        //checkbox of Response Time

        StringBuilder checkBoxResponseTimeBuilder = new StringBuilder();
        checkBoxResponseTimeBuilder.append("<label for=\"Response Time\"><b> Response Time</b></label><br>\r\n");

        for (int i = 0; i < (hitTypeCount + 1); i++) {
            checkBoxResponseTimeBuilder.append("<label><b> ").append((i == 0) ? "ALL" : commandList.get(i - 1)).append("</b></label><br>\r\n");

            for (int j = 0; j < 7; j++) {
                checkBoxResponseTimeBuilder.append("<input type=\"checkbox\" onclick=\"updateChart(this)\" value=\"").append(checkValue).append("\"");

                if (i == 0 && j == 0) {
                    checkBoxResponseTimeBuilder.append(" checked=\"\">");
                } else {
                    checkBoxResponseTimeBuilder.append(">");
                }

                checkBoxResponseTimeBuilder.append("<label>").append(statisticsName.get(j)).append("</label>");

                if (j == 3 || j == 6) {
                    checkBoxResponseTimeBuilder.append("<br>\r\n");
                } else {
                    checkBoxResponseTimeBuilder.append("\r\n");
                }

                checkValue++;
            }
        }

        String checkBoxResponseTimeString = checkBoxResponseTimeBuilder.toString();
        checkBox += checkBoxResponseTimeString;

        checkBox += "</div>\r\n\n";

        reportContentMap.put("checkBox", checkBox);

        // userData
        userData += "const allUsers = [";

        for (String labelTime : xAxisLabel) {
            dataPosition = getTimeDiffDataPosition(labelTime);
            userData += users.get(dataPosition) + ", ";

        }

        userData += "]\r\n";

        for (int i = 0; i < hitTypeCount; i++) {

            userData += "const " + dataName.get(i + 1) + " = [";

            for (String labelTime : xAxisLabel) {

                dataPosition = getTimeDiffDataPosition(labelTime);


                userData += commandUsers.get(dataPosition).get(i) + ", ";

            }

            userData += "]\r\n";

        }


        //hitData to js
        for (int i = 0; i <= hitTypeCount; i++) {

            hitData += "const " + dataName.get(hitTypeCount + 1 + i) + " = [";

            for (String labelTime : xAxisLabel) {

                int allCount = 0;

                dataPosition = getTimeDiffDataPosition(labelTime);


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

            for (String labelTime : xAxisLabel) {

                int allCount = 0, hitCount = 0;


                dataPosition = getTimeDiffDataPosition(labelTime);

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
        reportContentMap.put("yAxisData", yAxisData);

        //Dataset information
        reportContentMap.put("dataName", dataName);
        reportContentMap.put("labelName", labelName);
        reportContentMap.put("yaxis", yaxis);
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
