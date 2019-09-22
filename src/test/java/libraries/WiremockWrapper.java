package libraries;

import codeexecutor.UrlToClassName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.common.SingleRootFileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.recording.RecordingStatus;
import com.github.tomakehurst.wiremock.recording.SnapshotRecordResult;
import com.github.tomakehurst.wiremock.stubbing.StubImport;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;


public class WiremockWrapper {
    private static final Logger logger = LoggerFactory.getLogger(WiremockWrapper.class);

    private final File customStubsFolder = new File("src/main/resources/wiremock/mappings");
    private final String customStubsFolderAccessFromResources = "wiremock/mappings";


    //I actually wanted to manage saving myself, so I could organize it better.
    //But unfortunately it's not possible to disable it, so instead I put it in a tmp directory
    private final File defaultStubsFolder = new File("tmp/wiremock/");
    private final static ObjectMapper objectMapper;
    private static int port;//we don't want this to change
    private WireMockServer wireMockServer;


    static {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);

    }
    private void reinitializeMappingsDirectory() {
        try {
            FileUtils.deleteDirectory((new File(defaultStubsFolder + "/mappings")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new File(defaultStubsFolder + "/mappings").mkdirs();
    }


    public WiremockWrapper() {
        customStubsFolder.mkdirs();

        startServerWithFileMocks();
    }

    private void startServerWithFileMocks() {
        reinitializeMappingsDirectory();
        WireMockConfiguration config = options();
        config.extensions(new ResponseTemplateTransformer(false));
        config.notifier(new ConsoleNotifier(true));
        config.fileSource(new SingleRootFileSource(defaultStubsFolder));


        if(port <= 0) {
            config.dynamicPort();
        } else {
            config.port(port);
        }

        wireMockServer = new WireMockServer(config);


        //Arrays.stream(customStubsFolder.listFiles()).
        wireMockServer.importStubs(new StubImport(getStubMappings(), StubImport.Options.DEFAULTS));

        wireMockServer.start();
        this.port = wireMockServer.port();

    }

    List<StubMapping> getStubMappings() {
        List<StubMapping> jsonStubFileList = null;
        try {
            if(isJar()){
                List<String> filesInWiremockStubResourceDirectory = IOUtils.readLines(WiremockWrapper.class.getClassLoader().getResourceAsStream(customStubsFolderAccessFromResources), Charsets.UTF_8);
                jsonStubFileList = filesInWiremockStubResourceDirectory
                        .stream().filter((file)->file.matches(".*\\.json$")).
                                map(jsonStubFileName -> {
                                    try {
                                        File jsonStubFile = new File(getClass().getClassLoader().getResource(customStubsFolderAccessFromResources + "/" + jsonStubFileName).getFile());
                                        StubMapping stubMapping = objectMapper.readValue(jsonStubFile, StubMapping.class);
                                        return stubMapping;
                                    } catch (IOException e) {
                                        throw new RuntimeException("Could not read wiremock files!", e);
                                    }
                                })
                        .collect(Collectors.toList());
            } else {
                jsonStubFileList =  Arrays.stream(customStubsFolder.listFiles())
                        .filter((file)->file.getAbsolutePath().matches(".*\\.json$")).
                                map(jsonStubFile -> {
                                    try {
                                        StubMapping stubMapping = objectMapper.readValue(jsonStubFile, StubMapping.class);
                                        return stubMapping;
                                    } catch (IOException e) {
                                        throw new RuntimeException("Could not read wiremock files!", e);
                                    }
                                })
                        .collect(Collectors.toList());
            }
            return jsonStubFileList;
        } catch (IOException e) {
            throw new RuntimeException("Could not load wiremock mappings");
        }
    }

    void startRecording(){
        wireMockServer.startRecording("http://jsonplaceholder.typicode.com/");
    }

    public void saveAllRecordings() {
        if(!isJar() && !isBuildServer()) {
            if(wireMockServer.getRecordingStatus().getStatus().equals(RecordingStatus.Recording)) {
                SnapshotRecordResult snapshotRecordResult = wireMockServer.stopRecording();
                try {
                    for (StubMapping stubMapping : snapshotRecordResult.getStubMappings()) {
                        if(stubMapping.getResponse().wasConfigured()) {
                            //this allows us to make the matcher not worry about the required state
                            stubMapping.setRequiredScenarioState(null);
                            objectMapper.writeValue(new File(customStubsFolder + "/" + UrlToClassName.urlToClassName(stubMapping.getRequest().getUrl() + "-" + stubMapping.getRequest().getMethod()) + ".json"), stubMapping);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Could not write wiremock files!", e);
                } finally {
                    wireMockServer.stop();
                }
            }
            reinitializeMappingsDirectory();
        } else {
            throw new RuntimeException(
                    "You are recording with wiremock, but you are doing it while running a jar. This means that we can't save the new mocks. \n" +
                    "To rectify this, run `./gradlew test`, then commit any new wiremock wrappings in resources");
        }

    }

    public void rerunAndRecordWiremockOnHttpError(final Runnable runnable) {

        try {
            logger.info("Trying to run code with wiremock as proxys");
            runnable.run();
        } catch (Exception e) {
            logger.info("Got an exception, let's see if it's an http one");
            if (ExceptionUtils.indexOfThrowable(e, HttpClientErrorException.class) != -1) {
                logger.info("Http exception caught, handling http exception");

                logger.info("Start recording new wiremock mappings");
                //We have an http exception, so let's record again
                startRecording();
                runnable.run();
                saveAllRecordings();
                logger.info("Recording saved");

                logger.info("Try again");
                startServerWithFileMocks();
                //Run again, while not recording to make sure it has recorded successfully
                runnable.run();

            } else {
                throw e;
            }
        }
    }

    public int getPort() {
        return port;
    }

    /**
     * There is no point in saving wiremock mappings to a place that is deleted.
     * The solution to this, is to save it to resources when it is able to
     * and throw a a clear error message when it is not
     * @return
     */
    public boolean isJar(){
        return WiremockWrapper.class.getResource("WiremockWrapper.class").toString().startsWith("jar");
    }

    /**
     *
     * @return
     */
    public boolean isBuildServer(){
        return System.getProperty("build-server").equals("true");
    }
}
