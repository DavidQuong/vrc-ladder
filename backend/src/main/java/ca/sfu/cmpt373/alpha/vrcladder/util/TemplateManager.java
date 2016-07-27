package ca.sfu.cmpt373.alpha.vrcladder.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class handles all the HTML and TEXT templates. It is used
 * by PDFs and Notifications to get content and tags.
 */
public class TemplateManager {
    private static final TagsSystem tags = new TagsSystem();

    public String getContents(String path, Map<String, String> values) throws IOException {
        FileReader file = new FileReader(path);
        BufferedReader reader = new BufferedReader(file);
        List<String> currentTags = new ArrayList<>();
        String content = tags.buildContentsAndTags(reader, currentTags);
        content = tags.replaceTags(content, currentTags, values);
        currentTags.clear();
        reader.close();
        file.close();
        return content;
    }

}
