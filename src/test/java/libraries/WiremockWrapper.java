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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;


public class WiremockWrapper {
    private final WireMockServer wireMockServer;
    private final File customStubsFolder = new File("tmp/wiremock/");

    //I actually wanted to manage saving myself, so I could organize it better.
    //But unfortunately it's not possible to disable it, so instead I put it in a tmp directory
    private final File defaultStubsFolder = customStubsFolder;
    private final static ObjectMapper objectMapper;
    private static int port;//we don't want this to change

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

        List<StubMapping> jsonStubFileList = Arrays.stream(customStubsFolder.listFiles()).
                filter((file)->file.getAbsolutePath().matches(".*\\.json$")).
                map(jsonStubFile -> {
                    try {
                        StubMapping stubMapping = objectMapper.readValue(jsonStubFile, StubMapping.class);
                        return stubMapping;
                    } catch (IOException e) {
                        throw new RuntimeException("Could not read wiremock files!", e);
                    }
                })
        .collect(Collectors.toList());

        wireMockServer.importStubs(new StubImport(jsonStubFileList, StubImport.Options.DEFAULTS));

        wireMockServer.start();
        this.port = wireMockServer.port();
    }

    public void startRecording(){
        wireMockServer.startRecording("http://jsonplaceholder.typicode.com/");
    }

    public void saveAllRecordings() {
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
    }

    public int getPort() {
        return port;
    }
}
