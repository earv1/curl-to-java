package codeexecutor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlToClassNameTest {

    @Test
    void urlToClassNameTestWithParams() {
        String url = "https://jsonplaceholder.typicode.com/users?123=";
        String className = UrlToClassName.urlToClassName(url);
        assertEquals("JsonplaceholderTypicodeComUsers", className);
    }

    @Test
    void urlToClassNameTestWithPort() {
        String url = "https://jsonplaceholder.typicode.com:8080/users?123=";
        String className = UrlToClassName.urlToClassName(url);
        assertEquals("JsonplaceholderTypicodeComUsers", className);
    }

    @Test
    void urlToClassNameTest() {
        String url = "https://jsonplaceholder.typicode.com/users";
        String className = UrlToClassName.urlToClassName(url);
        assertEquals("JsonplaceholderTypicodeComUsers", className);
    }

    @Test
    void urlToClassNameTestWithDashes() {
        String url = "https://jsonplace-holder.typicode.com/users";
        String className = UrlToClassName.urlToClassName(url);
        assertEquals("JsonplaceHolderTypicodeComUsers", className);
    }

    @Test
    void urlToClassNameTestHttp() {
        String url = "http://jsonplaceholder.typicode.com/users";
        String className = UrlToClassName.urlToClassName(url);
        assertEquals("JsonplaceholderTypicodeComUsers", className);
    }

}