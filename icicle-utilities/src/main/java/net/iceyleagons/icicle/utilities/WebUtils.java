/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.icicle.utilities;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static net.iceyleagons.icicle.utilities.ErrorUtils.TODO;

/**
 * Contains basic operations regarding Website connections and requests.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since 1.0.0
 */
public final class WebUtils {

    /**
     * This will open a connection to the given {@link URL} and generate a string with line separators.
     *
     * @param url     the URL
     * @param timeout the timeout in milliseconds
     * @return the read response, can be null!
     */
    @SneakyThrows
    public static String readURL(String url, int timeout) {
        return readURL(new URL(url), timeout);
    }

    /**
     * This will open a connection to the given {@link URL} and generate a string with line separators.
     * It uses a default User-Agent "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36"
     *
     * @param url     the {@link URL}
     * @param timeout the timeout in milliseconds
     * @return the read response, can be null!
     */
    public static String readURL(URL url, int timeout) {
        return readURL(url, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36", timeout);
    }

    /**
     * This will open a connection to the given {@link URL} and generate a string with line separators.
     *
     * @param url the URL
     * @return the read response, can be null!
     */
    @SneakyThrows
    public static String readURL(String url) {
        return readURL(new URL(url));
    }

    /**
     * This will open a connection to the given {@link URL} and generate a string with line separators.
     * It uses a default User-Agent "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36"
     *
     * @param url the {@link URL}
     * @return the read response, can be null!
     */
    public static String readURL(URL url) {
        return readURL(url, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36", 15000);
    }

    /**
     * This will open a connection to the given {@link URL} and generate a string with line separators.
     * It uses a default User-Agent "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36"
     *
     * @param url   the {@link URL}
     * @param agent the user-agent to use
     * @return the read response, can be null!
     */
    public static String readURL(@NotNull URL url, @NotNull String agent, int timeout) {
        Asserts.notNull(url, "Url must not be null!");
        Asserts.notNull(agent, "Agent must not be null!");

        try {
            URLConnection urlConnection = url.openConnection();

            urlConnection.setRequestProperty("User-Agent", agent);
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setReadTimeout(timeout);
            urlConnection.connect();

            try (InputStream inputStream = urlConnection.getInputStream()) {
                try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
                    StringBuilder stringBuilder = new StringBuilder();

                    while (scanner.hasNextLine())
                        stringBuilder.append(scanner.nextLine()).append("\n");

                    return stringBuilder.toString();
                }
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Connection timeout for URL: " + url.getPath());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void something() {
        TODO("This is not implemented bruh");
    }
}
