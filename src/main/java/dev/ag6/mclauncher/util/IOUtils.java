package dev.ag6.mclauncher.util;

import dev.ag6.mclauncher.MCLauncher;

import java.io.InputStream;
import java.net.URL;

public final class IOUtils {
    public static InputStream getResourceAsStream(String path) {
        return MCLauncher.class.getClassLoader().getResourceAsStream(path);
    }

    public static URL getResource(String path) {
        return MCLauncher.class.getClassLoader().getResource(path);
    }
}
