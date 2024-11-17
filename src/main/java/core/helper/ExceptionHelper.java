package core.helper;

import org.slf4j.Logger;

public class ExceptionHelper {

    private ExceptionHelper() {
        // Do nothing
    }

    private static final Logger logger = LogHelper.getLogger();

    public static <T> T rethrow(Exception e) {
        logger.info("Rethrow exception: {} {}", e.getClass().getName(), e.getMessage());
        throw new IllegalStateException(e);
    }
}