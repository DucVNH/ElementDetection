package core.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import core.entity.PageLocators;
import java.io.File;

public class JsonHelper {
    private static final String PAGE_REPO = "src/test/java/page/";
    private JsonHelper() {
    }

    public static PageLocators loadLocators(String webName, String pageClassName) {
        ObjectMapper mapper = new ObjectMapper();
        // Assuming JSON file is named after the page class, e.g., "HomePage.json"
        try {
            File file = new File(PAGE_REPO + webName + File.separator + pageClassName + ".json");
            return mapper.readValue(file, PageLocators.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static void saveLocatorsToFile(PageLocators locators) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(PAGE_REPO + locators.getWebName() + File.separator + locators.getPageName() + ".json");
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(file, locators);
    }
}
