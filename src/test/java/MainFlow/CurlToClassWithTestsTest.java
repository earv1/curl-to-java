package MainFlow;

import libraries.WiremockWrapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class CurlToClassWithTestsTest {

    private static WiremockWrapper wiremockWrapper;
    private static int wiremockPort;

    @BeforeAll
    private static void beforeAll() {
        wiremockWrapper = new WiremockWrapper();
        wiremockPort = wiremockWrapper.getPort();
    }

    @AfterAll
    private static void afterAll() {
        wiremockWrapper.saveAllRecordings();
    }


    @Test
    void endToEndTestNoQuotesUrlAtFront() throws Exception {
        String [] curls = {
                "curl http://localhost:" + wiremockPort + "/posts -d '{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}' -H \"Content-type: application/json; charset=UTF-8\"",
        };

        wiremockWrapper.rerunAndRecordWiremockOnHttpError(() -> CurlToClassWithTests.generateClassWithDependencies(curls));
    }

    @Test
    void endToEndTestNoQuotesUrlAtBack () throws Exception {
        String [] curls = {
                "curl -d '{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}' -H \"Content-type: application/json; charset=UTF-8\" http://localhost:" + wiremockPort + "/posts",
        };

        wiremockWrapper.rerunAndRecordWiremockOnHttpError(() -> CurlToClassWithTests.generateClassWithDependencies(curls));
    }

    @Test
    void endToEndTestPostUrlAtFront () throws Exception {
        //wiremockWrapper.startRecording();
        String [] curls = {
                "curl 'http://localhost:" + wiremockPort + "/posts' -H \"Content-type: application/json; charset=UTF-8\" -d '{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}'",
        };

        wiremockWrapper.rerunAndRecordWiremockOnHttpError(() -> CurlToClassWithTests.generateClassWithDependencies(curls));
    }

    @Test
    void endToEndTestPostUrlAtBack () throws Exception {
        String [] curls = {
                "curl -H \"Content-type: application/json; charset=UTF-8\" -d '{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}' 'http://localhost:" + wiremockPort + "/posts'",
        };

        wiremockWrapper.rerunAndRecordWiremockOnHttpError(() -> CurlToClassWithTests.generateClassWithDependencies(curls));
    }

    @Test
    void endToEndTestGetWithHeaders () throws Exception {
        String [] curls = {
                "curl -H 'Accept-Language: en-US,en;q=0.5' -H \"Content-Type: application/x-www-form-urlencoded\" -H 'Referer: https://www.google.com/' -X 'GET' 'http://localhost:" + wiremockPort + "/users'",
        };

        wiremockWrapper.rerunAndRecordWiremockOnHttpError(() -> CurlToClassWithTests.generateClassWithDependencies(curls));
    }

    @Test
    void endToEndTestSimpleGet () throws Exception {
        String [] curls = {
                "curl 'http://localhost:" + wiremockPort + "/todos'"
        };

        wiremockWrapper.rerunAndRecordWiremockOnHttpError(() -> CurlToClassWithTests.generateClassWithDependencies(curls));
    }

    @Test
    void endToEndTestMultipleCreation () throws Exception {
        String [] curls = {
                "curl 'http://localhost:" + wiremockPort + "/todos'",
                "curl http://localhost:" + wiremockPort + "/posts -d '{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}' -H \"Content-type: application/json; charset=UTF-8\"",
                "curl 'http://localhost:" + wiremockPort + "/posts' -H \"Content-type: application/json; charset=UTF-8\" -d '{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}'",
                "curl -H 'Accept-Language: en-US,en;q=0.5' -H \"Content-Type: application/x-www-form-urlencoded\" -H 'Referer: https://www.google.com/' -X 'GET' 'http://localhost:" + wiremockPort + "/users'",
        };
        wiremockWrapper.rerunAndRecordWiremockOnHttpError(() -> CurlToClassWithTests.generateClassWithDependencies(curls));
    }


}