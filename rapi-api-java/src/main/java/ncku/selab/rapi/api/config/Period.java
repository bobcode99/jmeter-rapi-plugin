/*
 * @link    https://github.com/RapiTest/rapi-api
 * @author  fourcolor
 */

package ncku.selab.rapi.api.config;

public class Period {
    private int time = -1;
    private int maxNum = 1;

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
