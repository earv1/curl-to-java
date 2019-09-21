package libraries;

import codeexecutor.UrlToClassName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.SingleRootFileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.recording.SnapshotRecordResult;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;


public class WiremockWrapper {
    private final WireMockServer wireMockServer;
    private final int port;
    private final File customStubsFolder = new File("tmp/wiremock/");

    //I actually wanted to manage saving myself, so I could organize it better.
    //But unfortunately it's not possible to disable it, so instead I put it in a tmp directory
    private final File defaultStubsFolder = customStubsFolder;


    public WiremockWrapper() {


        WireMockConfiguration config = options();
        config.extensions(new ResponseTemplateTransformer(false));
        config.dynamicPort();


        new File(defaultStubsFolder + "/mappings").mkdirs();
        config.fileSource(new SingleRootFileSource(defaultStubsFolder));


        wireMockServer = new WireMockServer(config);
        List<File> jsonStubFileList = Arrays.stream(customStubsFolder.listFiles()).filter((file)->file.getAbsolutePath().matches("\\.json$")).collect(Collectors.toList());
        try {
            for (File jsonStubFile : jsonStubFileList) {
                wireMockServer.addStubMapping(new ObjectMapper().readValue(jsonStubFile, StubMapping.class));
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read wiremock files!", e);
        }


        wireMockServer.start();
        wireMockServer.startRecording("http://jsonplaceholder.typicode.com/");
        port = wireMockServer.port();
    }

    public void getAllRecordings() {
        SnapshotRecordResult snapshotRecordResult = wireMockServer.stopRecording();
        try {
            for (StubMapping stubMapping : snapshotRecordResult.getStubMappings()) {
                new ObjectMapper().writeValue(new File(customStubsFolder + "/" + UrlToClassName.urlToClassName(stubMapping.getRequest().getUrl()) + ".json"), stubMapping);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not write wiremock files!", e);
        } finally {
            wireMockServer.stop();
        }
    }

    public int getPort() {
        return port;
    }
}
