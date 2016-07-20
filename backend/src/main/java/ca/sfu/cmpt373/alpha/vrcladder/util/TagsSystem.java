package ca.sfu.cmpt373.alpha.vrcladder.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class contains the functions needed for tagging system.
 * Those tags are in HTML templates for PDF and emails. It is
 * also used in text emails.
 */
public class TagsSystem {
    private static final String NEW_LINE = "\n";
    private static final char TAG_INDICATOR = '#';
    private static final char TAG_END_INDICATOR[] = {',', '/', '<', ';', ' ', '"'};


    public String replaceTags(String content, List<String> tags, Map<String, String> values) {
        for (String tag : tags) {
            if (values.containsKey(tag)) {
                String value = values.get(tag);
                content = content.replaceAll(tag, value);
            }
        }
        return content;
    }

    public String buildContentsAndTags(BufferedReader reader, List<String> tags) throws IOException {
        String line;
        String results = "";
        while ((line = reader.readLine()) != null) {
            results += line;
            String tagIndicator = String.valueOf(TAG_INDICATOR);
            if (line.contains(tagIndicator)) {
                tags.addAll(findTags(line));
            }
            results += NEW_LINE;
        }
        return results;
    }

    private static List<String> findTags(String currentLine) {
        List<String> results = new ArrayList<>();
        String currentTag = "";
        char[] lineContents = currentLine.toCharArray();
        boolean currentStatus = false;
        boolean addToTags = false;
        for (char c : lineContents) {
            if (addToTags) {
                results.add(currentTag);
                addToTags = false;
                currentTag = "";
            }

            if (c == TAG_INDICATOR) {
                currentStatus = true;
            }

            if (currentStatus) {
                if (isEndOfTag(c)) {
                    currentStatus = false;
                    addToTags = true;
                }else{
                    currentTag = currentTag + c;
                }
            }
        }

        if (!currentTag.isEmpty()) {
            results.add(currentTag);
        }
        return results;
    }

    private static boolean isEndOfTag(char currentLetter) {
        for(char indicator : TAG_END_INDICATOR){
            if(currentLetter == indicator){
                return true;
            }
        }
        return false;
    }

}
