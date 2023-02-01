# SideeX API Document (Java)

> java api repo: https://github.com/fourcolor/sideex-api-java

```
以下功能需要將 runner 切到 feat/api-support 這個 branch 才能使用
```

## 將Runner轉成執行檔

1. 安裝 [pkg](https://www.npmjs.com/package/pkg)
    * 指令 npm install -g pkg
2. runner build 完後到 modules/main/dist/runner
3. pkg sideex-runner.js
4. 資料夾內會出現 sideex-runner-linux, sideex-runner-win.exe, sideex-runner-macos

---

## Class

### Driver

* 為主要驅動 Runner 的 Class

#### Constructor

* `Driver(String path, Config config)`
    * path: runner 的路徑
    * config: sideex config object 包含 sideex runner config file 的所有資訊

#### Methods

* `run()`
    * 根據傳入的 runner path ,及 config
      物件執行，會回傳[JsonNode](https://fasterxml.github.io/jackson-databind/javadoc/2.7/com/fasterxml/jackson/databind/JsonNode.html)
      格式的report

<pre>
以下 Class 對應 <a href="https://hackmd.io/@sideex/book/%2F%40sideex%2Frunner#Appendix-A---SideeX-Runner-Config-File-Format">SideeX Runner Document Config File Format</a>
每個欄位都有對應的 get, set function 
ex: Config 的 input 欄位有 setInput(Input input) 及 getInput()
因此下面指列每個 class 的成員變數
</pre>

### Config

* Member
    * `Input input`
    * `Report report`
    * `WebDriver webdriver`
    * `Play play`

### Input

* Member
    * `ArrayList<String> testSuites`
    * `ArrayList<String> variables`
    * `ArrayList<String> dataDriven`

### Report

* Member
    * `String type`
    * `int snapshot`
    * `int snapshotQuality`

### WebDriver

* Member
    * `ArrayList<WebDriverConfig> configs`
    * `Map<String, String> i18n`

### Play

* Member
    * `int mode`
    * `String entry`
    * `int autoWaitTimeout`
    * `int speed`
    * `Period period`

### WebDriverConfig

* Member
    * `String type`
    * `String serverUrl`
    * `ArrayList<Browser> browsers`
    * `ArrayList<Session> sessions`

### Browser

* Member
    * `Map<String, Object> capbility`
    * ~~`boolean keepSessionAlive`~~ 此功能還尚未整合進去

### Session

```
Session 功能還尚未整合進去 feat/api-support 
```

## Example

```java
    public static void main(String[] args)  {

        ArrayList<String> testSuites = new ArrayList<String>();
        testSuites.add("/path/to/testSuite.json");

        Browser browser = new Browser();
        Map<String, Object> caps = new HashMap<>();
        caps.put("browserName", "chrome");
        browser.setCapbility(caps);
        ArrayList<Browser> browsers = new ArrayList<Browser>();
        browsers.add(browser);

        WebDriverConfig webDriverConfig = new WebDriverConfig();
        webDriverConfig.setBrowsers(browsers);
        webDriverConfig.setServerUrl("http://url.to.selenium.grid");
        ArrayList<WebDriverConfig> webDriverConfigs = new ArrayList<WebDriverConfig>();
        webDriverConfigs.add(webDriverConfig);

        Config config = new Config();
        config.getInput().setTestSuites(testSuites);
        config.getWebdriver().setConfigs(webDriverConfigs);

        Driver driver = new Driver(
                "/path/to/runner", config);

        JsonNode report = driver.run();
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(report));
    }
```
