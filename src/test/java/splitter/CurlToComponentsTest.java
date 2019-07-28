package splitter;

import codeexecutor.CodeExecutor;
import component.ComponentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CurlToComponentsTest {
    //curl 'https://clients5.google.com/pagead/drt/dn/' -H 'User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0' -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8' -H 'Accept-Language: en-US,en;q=0.5' --compressed -H 'Referer: https://www.google.com/' -H 'Connection: keep-alive' -H 'Cookie: NID=188=ySHtktver1CqJ6BfB7HT9eqfSlJpTwnFuLEfngN0V4mZtMWrsILU5dvO3XvtkGAKdpKMr7bJxboDD_Rbc17IDqU2onOrPhbY2-GEFZ3N70k7ttJeIYxr9KCsXJHTVL2AMxJnwkWAkIdP8lvCC1rloXrOKttntML2YYLN5boIvrh1yQJvKupQODOXC499fHMD8FhUOz92QkwXz1l8hPq6apM5ctTENozSYHBMeZK4yEqmVnAvjghPfMys1B6MgtgYELdSL0G8fvAQA8-k_Fd7cocEF9hM8OuDP10x0kv8U1eforRXxjNTLuJYipEBKU_tdDbJQXLX; ANID=AHWqTUmm3X9lPeP2KtoVqDWmpnbNjA67oA1gGXV3fSK276TQzJkWn74xDVfavB1F; SID=dQcjZqS4Bf69cgho3ye0xIbMxrCJabbNNvlpiutDNKSgxlY2Tz8PkywJCm1dwr6meZsw0A.; HSID=Amo4nM8SA_AvUTHZQ; SSID=AHNwOOXI-rcR7OvOr; APISID=scXIYN2pXK4iG6F-/APtNNvffeM9eeXcxb; SAPISID=jBx3xtEuG_5VhOtB/AqT09dRwOauqEm14C; SIDCC=AN0-TYse3gjH65VR78e-acy8TaErD0h_7L-STPAOCfr7_xwJjLI6I5GnDJLxs3OfI5QSAi9DF9w; 1P_JAR=2019-7-26-20; OGP=-5061451:; SEARCH_SAMESITE=CgQIrI0B' -H 'Upgrade-Insecure-Requests: 1'
    @Test
    public void extractGetComponentsTest() throws Exception {
        Map<ComponentType, List<String>> componentList = CurlToComponents.extractComponents(
                "curl -d \"param1=value1&param2=value2&param3=http://test.com\" " +
                        "-H 'Accept-Language: en-US,en;q=0.5' " +
                        "-H \"Content-Type: application/x-www-form-urlencoded\" " +
                        "-H 'Referer: https://www.google.com/' " +
                        "-X GET " +
                        "https://jsonplaceholder.typicode.com/users");

        List<String> requestTypes = componentList.get(ComponentType.REQUEST_TYPE);
        assertEquals(1, requestTypes.size());
        assertEquals("GET", requestTypes.get(0));

        List<String> urls = componentList.get(ComponentType.URL);
        assertEquals(1, urls.size());
        assertEquals("https://jsonplaceholder.typicode.com/users", urls.get(0));

        String restTemplateBlock = String.format(
                "RestTemplate restTemplate = new RestTemplate();\n"+
                        "HttpEntity<String> requestEntity = new HttpEntity<String>(\"\");\n" +
                        "ResponseEntity<String> responseEntity = restTemplate.exchange(\"%s\", HttpMethod.%s, requestEntity, String.class);\n",
                urls.get(0), requestTypes.get(0));

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<String>("");
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://jsonplaceholder.typicode.com/users", HttpMethod.GET, requestEntity, String.class);

        String restResponse = CodeExecutor.runCode(restTemplateBlock, "responseEntity.getBody()");
    }

    @Test
    public void extractPOSTComponentsTest() throws Exception {
        Map<ComponentType, List<String>> componentList = CurlToComponents.extractComponents(
                "curl -d \"param1=value1&param2=value2&param3=http://test.com\" " +
                        "-H 'Accept-Language: en-US,en;q=0.5' " +
                        "-H \"Content-Type: application/x-www-form-urlencoded\" " +
                        "-H 'Referer: https://www.google.com/' " +
                        "-X POST " +
                        "https://jsonplaceholder.typicode.com/users");

        List<String> requestTypes = componentList.get(ComponentType.REQUEST_TYPE);
        assertEquals(1, requestTypes.size());
        assertEquals("POST", requestTypes.get(0));
    }
}