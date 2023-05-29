/*
 * @author  HSU
 */
package ncku.selab.rapi4jmeter.report;

import freemarker.template.TemplateException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    final String withMilisecondsFormatString = "yyyyMMdd HH:mm:ss:SSS";
    final DateTimeFormatter normalDateTimeFormatWithDash = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    final DateTimeFormatter withMillisecondsFormatter = DateTimeFormatter.ofPattern(withMilisecondsFormatString);
    LocalDateTime startTimeIntervalPointLocalDateTime;

    public void generate_report(String requestStats, JsonParse jsonParseFile, ArrayList<String> testResults,
                                ArrayList<String> command, String reportPath) {

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
        } catch (IOException e) {
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


    private int getTimeDiffDataPosition(String labelTime) {
        labelTime += ":000";
        String formattedDateTime = LocalDateTime.parse(labelTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"))
                .format(DateTimeFormatter.ofPattern(withMilisecondsFormatString));

        LocalDateTime labelLocalDate = getLocalDateTime(formattedDateTime);

        double timeDifference = calculateDuration(labelLocalDate, startTimeIntervalPointLocalDateTime);
        timeDifference /= 1000;
        timeDifference = Math.round(timeDifference);

        return (int) timeDifference;
    }

    private LocalDateTime getLocalDateTime(String timeStr) {
        // timeStr: 20230527 19:22:37
        LocalDate localDate = LocalDate.parse(timeStr.substring(0, 8), DateTimeFormatter.BASIC_ISO_DATE);
        LocalTime localTimeObj = LocalTime.parse(timeStr.substring(9), DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));

        return LocalDateTime.of(localDate, localTimeObj);
    }

    private long calculateDuration(LocalDateTime localDateOne, LocalDateTime localDateTwo) {
        Duration timeDifference = Duration.between(localDateOne, localDateTwo);
        return Math.abs(timeDifference.toMillis());
    }


    public void parse() throws ParseException {
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

        LocalDateTime startTimeLocalDateTime = getLocalDateTime(startTime);
        LocalDateTime endTimeLocalDateTime = getLocalDateTime(endTime);

        JSONObject json, cases, records;
        JSONArray recordsArray, casesArray;
        Long commandTime;


        //Data of User
        int userCount;
        String status;

        //Data of Hit, Error, ResponseTime
//        double timeDifference;

        int dataPosition, commandAmount, errorCount;


        // Original X point
        startTimeIntervalPointLocalDateTime = startTimeLocalDateTime.withSecond(0).withNano(0); // same with startCalendar
        LocalDateTime eachTimeIntervalPointLocalDateTime = startTimeLocalDateTime.withSecond(0).withNano(0); // same with eachTimeIntervalPoint

        // Final X point
        LocalDateTime endTimeLocalDateTimeAddOneMinute = endTimeLocalDateTime.plusMinutes(1).withSecond(0).withNano(0); // same with endCalendar


        // Add x point in every 30 seconds to create X axis points
        while (eachTimeIntervalPointLocalDateTime.isBefore(endTimeLocalDateTimeAddOneMinute)) {
            // 2023-05-24 13:48:00
            String eachTimeIntervalPointString = eachTimeIntervalPointLocalDateTime.format(normalDateTimeFormatWithDash);
            timeStamp.add(eachTimeIntervalPointString);
            eachTimeIntervalPointLocalDateTime = eachTimeIntervalPointLocalDateTime.plusSeconds(1);
        }

        // final time-point
        String eachTimeIntervalPointString = eachTimeIntervalPointLocalDateTime.format(normalDateTimeFormatWithDash);
        timeStamp.add(eachTimeIntervalPointString);


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
//            caseStartTime += ":000";

            LocalDateTime caseStartLocalDate = getLocalDateTime(caseStartTime);

            String caseEndTime = eachJson.get("endTime").toString();
//            caseEndTime += ":000";

            LocalDateTime caseEndLocalDate = getLocalDateTime(caseEndTime);

            casesArray = (JSONArray) eachJson.get("cases");
            cases = (JSONObject) casesArray.get(0);
            recordsArray = (JSONArray) cases.get("records");

            commandTimePoint.get(i).add(withMillisecondsFormatter.format(caseStartLocalDate)); // ok
            LocalDateTime willModifyCaseStartLocalDate = caseStartLocalDate;

            for (int j = 0; j < recordsArray.size(); j++) {
                records = (JSONObject) recordsArray.get(j);
                commandTime = (Long) records.get("time");
                willModifyCaseStartLocalDate = willModifyCaseStartLocalDate.plusNanos(commandTime.intValue() * 1_000_000L); // add the command milliseconds

                LocalDateTime willModifyCaseStartLocalDateAddSeconds = willModifyCaseStartLocalDate;

                int firstDigitMilliseconds = Integer.parseInt(String.valueOf(withMillisecondsFormatter.format(willModifyCaseStartLocalDateAddSeconds).charAt(18))); // If the time is 20230524 13:48:44:704, the firstDigitMilliseconds will be 7

                if (firstDigitMilliseconds > 4){
                    willModifyCaseStartLocalDateAddSeconds = willModifyCaseStartLocalDateAddSeconds.plusSeconds(1);
                }

                ////to plugin
                if (j == (recordsArray.size() - 1))
                    commandTimePoint.get(i).add(caseEndTime);
                else
                {
                    // will be 20230524 13:48:46:000
                    commandTimePoint.get(i).add(withMillisecondsFormatter.format(willModifyCaseStartLocalDateAddSeconds).substring(0, 18)+ "000");
                }

            }

            for (int j = 0; j < timeStamp.size(); j++) {

                //allUser
                eachTimeIntervalPointLocalDateTime = LocalDateTime.parse(timeStamp.get(j), normalDateTimeFormatWithDash);

                if (eachTimeIntervalPointLocalDateTime.isEqual(caseStartLocalDate) || eachTimeIntervalPointLocalDateTime.isEqual(caseEndLocalDate)) {
                    userCount = users.get(j);
                    userCount++;
                    users.set(j, userCount);
                }

                //commandUser
                if (eachTimeIntervalPointLocalDateTime.isBefore(caseStartLocalDate) || eachTimeIntervalPointLocalDateTime.isAfter(caseEndLocalDate)) {
                    continue;
                }

                // Active User for all
                if (caseStartLocalDate.isBefore(eachTimeIntervalPointLocalDateTime) && caseEndLocalDate.isAfter(eachTimeIntervalPointLocalDateTime)) {
                    userCount = users.get(j);
                    userCount++;
                    users.set(j, userCount);
                }

                //Active User for each command, 5 timepoints and 4 intervals
                for (int k = 0; k < recordsArray.size(); k++) {

                    String commandTimeOne = commandTimePoint.get(i).get(k);
                    String commandTimeTwo = commandTimePoint.get(i).get(k + 1);

                    LocalDateTime commandLocalDateOne = getLocalDateTime(commandTimeOne);
                    LocalDateTime commandLocalDateTwo = getLocalDateTime(commandTimeTwo);


                    if (eachTimeIntervalPointLocalDateTime.isBefore(commandLocalDateOne) || eachTimeIntervalPointLocalDateTime.isAfter(commandLocalDateTwo)) {
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
//            startTime += ":000";

            LocalDateTime caseStartLocalDate = getLocalDateTime(startTime);

            casesArray = (JSONArray) json.get("cases");
            cases = (JSONObject) casesArray.get(0);
            recordsArray = (JSONArray) cases.get("records");


            for (int j = 0; j < recordsArray.size(); j++) {

                records = (JSONObject) recordsArray.get(j);
                status = records.get("status").toString();
                commandTime = (Long) records.get("time");


                //Error
                double timeDifference = (double) calculateDuration(caseStartLocalDate, startTimeIntervalPointLocalDateTime);

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
                caseStartLocalDate = caseStartLocalDate.plusNanos(commandTime.intValue() * 1_000_000L);
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
        StringBuilder checkBoxFirstStringBuilder = new StringBuilder();
        checkBoxFirstStringBuilder.append("<div class=\"checkbox\">");

        for (int i = 0; i < 3; i++) {
            if (i == 0)
                checkBoxFirstStringBuilder.append("<label for=\"Virtual Users\"><b> Virtual Users</b></label><br>\r\n");
            else if (i == 1)
                checkBoxFirstStringBuilder.append("<label for=\"Hits\"><b> Hits</b></label><br>\r\n");
            else
                checkBoxFirstStringBuilder.append("<label for=\"Errors\"><b> Errors</b></label><br>\r\n");

            for (int j = 0; j < (hitTypeCount + 1); j++) {
                String commandName;

                if (j == 0)
                    commandName = "ALL";
                else
                    commandName = commandList.get(j - 1);

                checkBoxFirstStringBuilder.append("<input type=\"checkbox\" onclick=\"updateChart(this)\" value=\"")
                        .append(checkValue);

                if (j == 0)
                    checkBoxFirstStringBuilder.append("\" checked=\"\">");
                else
                    checkBoxFirstStringBuilder.append("\">");

                checkBoxFirstStringBuilder.append("<label>").append(commandName).append("</label><br>\r\n");

                checkValue++;
            }
        }

        String checkBoxFirstString = checkBoxFirstStringBuilder.toString();
        checkBox += checkBoxFirstString;

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
        StringBuilder userDataBuilder = new StringBuilder();
        userDataBuilder.append("const allUsers = [");

        for (String labelTime : xAxisLabel) {
            dataPosition = getTimeDiffDataPosition(labelTime);
            userDataBuilder.append(users.get(dataPosition)).append(", ");
        }

        userDataBuilder.append("]\r\n");

        for (int i = 0; i < hitTypeCount; i++) {
            userDataBuilder.append("const ").append(dataName.get(i + 1)).append(" = [");

            for (String labelTime : xAxisLabel) {
                dataPosition = getTimeDiffDataPosition(labelTime);
                userDataBuilder.append(commandUsers.get(dataPosition).get(i)).append(", ");
            }

            userDataBuilder.append("]\r\n");
        }

        StringBuilder hitDataBuilder = new StringBuilder();
        for (int i = 0; i <= hitTypeCount; i++) {
            hitDataBuilder.append("const ").append(dataName.get(hitTypeCount + 1 + i)).append(" = [");

            for (String labelTime : xAxisLabel) {
                int allCount = 0;
                dataPosition = getTimeDiffDataPosition(labelTime);

                if (i == 0) {
                    for (int k = 0; k < hitTypeCount; k++)
                        allCount += Hit.get(dataPosition).get(k);

                    if (allCount != 0)
                        hitDataBuilder.append(allCount).append(", ");
                    else
                        hitDataBuilder.append(", ");
                } else {
                    if (Hit.get(dataPosition).get(i - 1) != 0)
                        hitDataBuilder.append(Hit.get(dataPosition).get(i - 1)).append(", ");
                    else
                        hitDataBuilder.append(", ");
                }
            }

            hitDataBuilder.append("]\r\n");
        }

        StringBuilder errorDataBuilder = new StringBuilder();
        for (int i = 0; i <= hitTypeCount; i++) {
            errorDataBuilder.append("const ").append(dataName.get((hitTypeCount + 1) * 2 + i)).append(" = [");

            for (String labelTime : xAxisLabel) {
                int allCount = 0, hitCount = 0;
                dataPosition = getTimeDiffDataPosition(labelTime);

                if (i == 0) {
                    for (int k = 0; k < hitTypeCount; k++) {
                        allCount += Error.get(dataPosition).get(k);
                        hitCount += Hit.get(dataPosition).get(k);
                    }

                    if (allCount != 0)
                        errorDataBuilder.append(allCount).append(", ");
                    else {
                        if (hitCount != 0)
                            errorDataBuilder.append("0, ");
                        else
                            errorDataBuilder.append(", ");
                    }
                } else {
                    if (Error.get(dataPosition).get(i - 1) != 0)
                        errorDataBuilder.append(Error.get(dataPosition).get(i - 1)).append(", ");
                    else {
                        if (Hit.get(dataPosition).get(i - 1) != 0)
                            errorDataBuilder.append("0, ");
                        else
                            errorDataBuilder.append(", ");
                    }
                }
            }

            errorDataBuilder.append("]\r\n");
        }

        errorDataBuilder.append("\r\n\n");

        StringBuilder responseTimeDataBuilder = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            responseTimeDataBuilder.append("const all").append(statisticsName.get(i)).append(" = [");

            for (int j = 0; j < xAxisLabel.size(); j++) {
                if (i == 0)
                    responseTimeDataBuilder.append(Avg.get(j));
                else if (i == 1)
                    responseTimeDataBuilder.append(Min.get(j));
                else if (i == 2)
                    responseTimeDataBuilder.append(Max.get(j));
                else if (i == 3)
                    responseTimeDataBuilder.append(Median.get(j));
                else if (i == 4)
                    responseTimeDataBuilder.append(P90.get(j));
                else if (i == 5)
                    responseTimeDataBuilder.append(P95.get(j));
                else
                    responseTimeDataBuilder.append(P99.get(j));

                responseTimeDataBuilder.append(" ,");
            }

            responseTimeDataBuilder.append("]\r\n");
        }

        for (int i = 0; i < hitTypeCount; i++) {
            for (int j = 0; j < 7; j++) {
                responseTimeDataBuilder.append("const ").append(commandList.get(i)).append(statisticsName.get(j)).append(" = [");

                for (int k = 0; k < xAxisLabel.size(); k++) {
                    if (j == 0) {
                        if (commandAvg.get(k).get(i).size() != 0)
                            responseTimeDataBuilder.append(commandAvg.get(k).get(i).get(0));
                    } else if (j == 1) {
                        if (commandMin.get(k).get(i).size() != 0)
                            responseTimeDataBuilder.append(commandMin.get(k).get(i).get(0));
                    } else if (j == 2) {
                        if (commandMax.get(k).get(i).size() != 0)
                            responseTimeDataBuilder.append(commandMax.get(k).get(i).get(0));
                    } else if (j == 3) {
                        if (commandMedian.get(k).get(i).size() != 0)
                            responseTimeDataBuilder.append(commandMedian.get(k).get(i).get(0));
                    } else if (j == 4) {
                        if (commandP90.get(k).get(i).size() != 0)
                            responseTimeDataBuilder.append(commandP90.get(k).get(i).get(0));
                    } else if (j == 5) {
                        if (commandP95.get(k).get(i).size() != 0)
                            responseTimeDataBuilder.append(commandP95.get(k).get(i).get(0));
                    } else {
                        if (commandP99.get(k).get(i).size() != 0)
                            responseTimeDataBuilder.append(commandP99.get(k).get(i).get(0));
                    }

                    responseTimeDataBuilder.append(", ");
                }

                responseTimeDataBuilder.append("]\r\n");
            }
        }

        String userData = userDataBuilder.toString();
        String hitData = hitDataBuilder.toString();
        String errorData = errorDataBuilder.toString();
        String responseTimeData = responseTimeDataBuilder.toString();

        yAxisData += userData + hitData + errorData + responseTimeData;
        reportContentMap.put("yAxisData", yAxisData);

        //Dataset information
        reportContentMap.put("dataName", dataName);
        reportContentMap.put("labelName", labelName);
        reportContentMap.put("yaxis", yaxis);
    }

}
