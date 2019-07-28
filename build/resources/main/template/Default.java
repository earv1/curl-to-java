import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class {class-name} {

    final static RestTemplate restTemplate = new RestTemplate();
    final static Logger logger = LoggerFactory.getLogger({class-name}.class);
    final static ObjectMapper mapper = new ObjectMapper();


    public static boolean execute() throws Exception {
        {main-logic}

        return true;
    }
}

