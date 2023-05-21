/*
 * @author  HSU
 */
package ncku.selab.rapi4jmeter.report;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class RequestStatsReport {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    // experimental use
    final JSONObject webVitalsAnalyzeResult = new JSONObject();
    // Combine allResults and rating data for each metric
    final Map<String, List<Double>> wvAllResultsMap = new HashMap<>();
    final Map<String, Map<String, Integer>> wvRatingMap = new HashMap<>();
    // experimental use

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
    /**
     * record each Command error
     */
    private final ArrayList<Integer> error = new ArrayList<>();
    private final ArrayList<Double> errorPercentage = new ArrayList<>();
    private final ArrayList<Long> commandTimeDifference = new ArrayList<>();
    // experimental use
    private final ArrayList<Long> allAmountOfRequest = new ArrayList<>();
    private String webVitalsAnalyzeResultTable = "";
    // experimental use
<<<<<<< main

=======
>>>>>>> Reformat code
    /**
     * all test results
     * [{"sideex": [4, 0, 0 ], "format": [1, 0, 1 ] }, "reports": [{"title": "date 16-13-30", "browserName": "chrome 112.0.5615.137", ...}] },{...}]
     */
    private ArrayList<String> testResults;
    private int commandAmount = 0;
    private long AllCommandTimeSum = 0;
    private String Request_Statistics_Content = "";

    // experimental use
    public static String wvObjToTable(JSONObject jsonObj) {
        df.setRoundingMode(RoundingMode.UP);

        StringBuilder sb = new StringBuilder();

        // loop through each metric in the JSON object
        for (Object key : jsonObj.keySet()) {
            String metric = key.toString();
            JSONObject values = (JSONObject) jsonObj.get(key);
            String avg = df.format(values.get("avg"));
            String min = df.format(values.get("min"));
            String med = df.format(values.get("med"));
            String max = df.format(values.get("max"));
            String p90 = df.format(values.get("p90"));
            String p95 = df.format(values.get("p95"));
            String p99 = df.format(values.get("p99"));

            // get the rating values
            JSONObject rating = (JSONObject) values.get("rating");
            String good = rating.containsKey("good") ? rating.get("good").toString() : "";
            String needsImp = rating.containsKey("needs-improvement") ? rating.get("needs-improvement").toString() : "";
            String poor = rating.containsKey("poor") ? rating.get("poor").toString() : "";

            // build the HTML table row
            sb.append("<tr>");
            sb.append("<td>").append(metric).append("</td>");
            sb.append("<td>").append(avg).append("</td>");
            sb.append("<td>").append(min).append("</td>");
            sb.append("<td>").append(med).append("</td>");
            sb.append("<td>").append(max).append("</td>");
            sb.append("<td>").append(p90).append("</td>");
            sb.append("<td>").append(p95).append("</td>");
            sb.append("<td>").append(p99).append("</td>");
            StringBuilder ratingString = new StringBuilder();
            ratingString.append("<td>");
            boolean needAddBreak = false;
            if (!good.isEmpty()) {
                needAddBreak = true;
                ratingString.append("good: ").append(good);
            }
            if (!needsImp.isEmpty()) {
                if (needAddBreak) {
                    ratingString.append("<br/>");
                }
                needAddBreak = true;
                ratingString.append("needs-improvement: ").append(needsImp);
            }
            if (!poor.isEmpty()) {
                if (needAddBreak) {
                    ratingString.append("<br/>");
                }
                ratingString.append("poor: ").append(poor);
            }
            ratingString.append("</td>");
            sb.append(ratingString);
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
    // experimental use


    public void startGenerateReport(String path, ArrayList<String> testResults) throws ParseException, java.text.ParseException {
        this.testResults = testResults;

        parse();
        report.generate_report(Request_Statistics_Content, webVitalsAnalyzeResultTable, jsonParse, this.testResults, commandList, this.allAmountOfRequest, path);
    }

    public void preprocessing() throws ParseException {

        // experimental use
        boolean haveAmountOfRequest = true;
        try {
            jsonParse.getAmountOfRequest(0);
        } catch (Exception e) {
            haveAmountOfRequest = false;
        }
        // experimental use

        for (int i = 0; i < testResults.size(); i++) {

            JSONArray recordsArray = jsonParse.getRecordsArray(i);

            // experimental use
            if (haveAmountOfRequest) {
                allAmountOfRequest.add(jsonParse.getAmountOfRequest(i));
            }
            // experimental use

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

    // experimental use
    private void analyzeWebVitals(JSONObject needAnalyzeWVObject) {

        for (String metric : new String[]{"FCP", "LCP", "CLS", "TTFB", "FID", "INP"}) {
            if (needAnalyzeWVObject.containsKey(metric)) {
                JSONObject metricObject = (JSONObject) needAnalyzeWVObject.get(metric);
                JSONArray allResultsArray = (JSONArray) metricObject.get("allResults");
                var allResultsList = wvAllResultsMap.computeIfAbsent(metric, k -> new ArrayList<>());
                allResultsList.addAll((Collection<? extends Double>) allResultsArray.stream().map(obj2 -> ((Number) obj2).doubleValue()).collect(Collectors.toList()));

//                System.out.println("allResultsList: " + allResultsList);
                JSONObject ratingObject = (JSONObject) metricObject.get("rating");
                Map<String, Integer> ratingCountMap = wvRatingMap.computeIfAbsent(metric, k -> new HashMap<>());
                for (Object key : ratingObject.keySet()) {
                    String rating = (String) key;
                    Integer count = ratingCountMap.getOrDefault(rating, 0);
                    ratingCountMap.put(rating, count + ((Long) ratingObject.get(key)).intValue());
                }
            }
        }
    }
    // experimental use

    private int getPercentilePosition(double rankFloat, int arraySize) {
        int position = (int) Math.round(arraySize * rankFloat);
        position--;
        if (position >= arraySize)
            position--;
        if (position < 0)
            position = 0;

        return position;
    }

    public void parse() throws ParseException, java.text.ParseException {
        jsonParse.addTestResults(this.testResults);

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


        for (int i = 0; i < testResults.size(); i++) {
            JSONObject json = jsonParse.getJson(i);

            // experimental use
            JSONObject webVitalsResultAnalyze = (JSONObject) json.get("webVitalsResultAnalyze");
//            System.out.println("webVitalsResultAnalyze: " + webVitalsResultAnalyze.toString());
            analyzeWebVitals(webVitalsResultAnalyze);
            // experimental use


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

        // experimental use
        // Calculate average, min, med, max, p90, p95, p99 for each metric
        Map<String, Double> avgMap = new HashMap<>();
        Map<String, Double> minMap = new HashMap<>();
        Map<String, Double> medMap = new HashMap<>();
        Map<String, Double> maxMap = new HashMap<>();
        Map<String, Double> p90Map = new HashMap<>();
        Map<String, Double> p95Map = new HashMap<>();
        Map<String, Double> p99Map = new HashMap<>();
        for (String metric : wvAllResultsMap.keySet()) {
            List<Double> allResultsList = wvAllResultsMap.get(metric);
            Collections.sort(allResultsList);
            double avg = allResultsList.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
            double min = allResultsList.get(0);
            double med = allResultsList.get(allResultsList.size() / 2);
            double max = allResultsList.get(allResultsList.size() - 1);
            double p90 = allResultsList.get((int) Math.ceil(allResultsList.size() * 0.9) - 1);
            double p95 = allResultsList.get((int) Math.ceil(allResultsList.size() * 0.95) - 1);
            double p99 = allResultsList.get((int) Math.ceil(allResultsList.size() * 0.99) - 1);
            avgMap.put(metric, avg);
            minMap.put(metric, min);
            medMap.put(metric, med);
            maxMap.put(metric, max);
            p90Map.put(metric, p90);
            p95Map.put(metric, p95);
            p99Map.put(metric, p99);
        }

        // Add metrics data to final JSON object
        for (String metric : wvAllResultsMap.keySet()) {
            JSONObject metricObject = new JSONObject();
            metricObject.put("avg", avgMap.get(metric));
            metricObject.put("min", minMap.get(metric));
            metricObject.put("med", medMap.get(metric));
            metricObject.put("max", maxMap.get(metric));
            metricObject.put("p90", p90Map.get(metric));
            metricObject.put("p95", p95Map.get(metric));
            metricObject.put("p99", p99Map.get(metric));
            Map<String, Integer> ratingCountMap = wvRatingMap.get(metric);
            if (ratingCountMap != null) {
                JSONObject ratingObject = new JSONObject();
                for (String rating : ratingCountMap.keySet()) {
                    ratingObject.put(rating, ratingCountMap.get(rating));
                }
                metricObject.put("rating", ratingObject);
            }
            webVitalsAnalyzeResult.put(metric, metricObject);
        }
//        System.out.println("webVitalsAnalyzeResult: " + webVitalsAnalyzeResult);
        // experimental use


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
            requestStatisticsContentStringBuilder.append("  </tr>\r\n" + "  <tr>\r\n" + "    <td>").append(commandList.get(i)).append("</td>\r\n").append("    <td>").append(AvgTime.get(i)).append("</td>\r\n").append("    <td>").append(Hit.get(i)).append("</td>\r\n").append("    <td>").append(commandTimeData.get(i).size()).append("</td>\r\n").append("    <td>").append(line_90.get(i)).append("</td>\r\n").append("    <td>").append(line_95.get(i)).append("</td>\r\n").append("    <td>").append(line_99.get(i)).append("</td>\r\n").append("    <td>").append(Min.get(i)).append("</td>\r\n").append("    <td>").append(Max.get(i)).append("</td>\r\n").append("    <td>").append(errorPercentage.get(i)).append("%</td>\r\n");
        }

        // experimental use
        webVitalsAnalyzeResultTable = wvObjToTable(this.webVitalsAnalyzeResult);
        // experimental use

<<<<<<< main
=======
        Request_Statistics_Content = requestStatisticsContentStringBuilder.toString();


>>>>>>> Reformat code
    }

}
