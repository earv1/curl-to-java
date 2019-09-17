package testgenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurlToClassWithDependenciesTest {

    @Test
    public void endToEndTestCreation () throws Exception {
        String [] curls = {
                "curl http://jsonplaceholder.typicode.com/posts -d '{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}' -H \"Content-type: application/json; charset=UTF-8\"",
                "curl -H 'Accept-Language: en-US,en;q=0.5' -H \"Content-Type: application/x-www-form-urlencoded\" -H 'Referer: https://www.google.com/' -X GET http://jsonplaceholder.typicode.com/users",
                "curl http://jsonplaceholder.typicode.com/todos"
        };

        CurlToClassWithDependencies.generateClassWithDependencies(curls);
    }
}