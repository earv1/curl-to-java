package testgenerator;

import org.junit.jupiter.api.Test;


class CurlToClassWithTestsTest {

    @Test
    public void endToEndTestCreation () throws Exception {
        String [] curls = {
//                "curl http://jsonplaceholder.typicode.com/posts -d '{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}' -H \"Content-type: application/json; charset=UTF-8\"",
                "curl 'http://jsonplaceholder.typicode.com/posts' -H \"Content-type: application/json; charset=UTF-8\" -d '{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}'",
                "curl -H 'Accept-Language: en-US,en;q=0.5' -H \"Content-Type: application/x-www-form-urlencoded\" -H 'Referer: https://www.google.com/' -X 'GET' 'http://jsonplaceholder.typicode.com/users'",
                "curl 'http://jsonplaceholder.typicode.com/todos'"
        };

        CurlToClassWithTests.generateClassWithDependencies(curls);
    }
}