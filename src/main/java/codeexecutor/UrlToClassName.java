package codeexecutor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UrlToClassName {
    public static String urlToClassName(String url) {
        url = url.replace("https://", "");
        url = url.replace("http://", "");

        if(url.contains("?")){
            url = url.substring(0, url.indexOf('?'));
        }

        List<Character> urlCharList =  url.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        urlCharList.set(0, Character.toUpperCase(urlCharList.get(0)));

        int nameCharListSize = urlCharList.size();
        for (int characterPos = 0; characterPos < nameCharListSize; characterPos++) {
            if(urlCharList.get(characterPos) == '.' || urlCharList.get(characterPos) == '/') {
                if(characterPos +1 < nameCharListSize) {
                    urlCharList.set(characterPos+1, Character.toUpperCase(urlCharList.get(characterPos+1)));
                    urlCharList.remove(characterPos);
                    nameCharListSize--;
                }
            }
        }
        return characterListToString(urlCharList);
    }

    private static String characterListToString (List<Character> charList) {
        String returnedString = "";
        for (Character character: charList) {
            returnedString += character;
        }
        return returnedString;
    }


}
