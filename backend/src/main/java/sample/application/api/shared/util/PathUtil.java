package sample.application.api.shared.util;

import java.net.URI;
import java.nio.file.Paths;

/**
 * Utility functions for handling paths (paths, URIs).
 * @author Manoel Campos
 */
public class PathUtil {
    /** Private constructor to prevent instantiating the class */
    private PathUtil() { throw new UnsupportedOperationException(); }

    /**
     * Creates a URI by concatenating paths
     * @param first initial path
     * @param others other parts to be concatenated
     * @return the complete (concatenated) URI
     */
    public static URI createUri(final String first, final String ...others) {
        return URI.create(concat(first, others));
    }

    /**
     * Concatenates URIs with /
     * @param first initial path
     * @param others other parts to be concatenated
     * @return the complete (concatenated) path
     */
    public static String concat(final String first, String ...others){
        final String path = Paths.get(first, others).toString();
        // Replace  with / when on Windows, as it is dealing with URIs, not local paths. On other OSs, it has no effect.
        return path.replaceAll("\\\\", "/");
    }
}
