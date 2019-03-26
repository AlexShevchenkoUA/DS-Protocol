package ua.training.hash;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import static java.lang.Byte.toUnsignedInt;
import static java.lang.String.valueOf;
import static java.nio.file.Paths.get;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class AsyncInputStream extends InputStream {
    private static final int STANDARD_INNER_BUFFER_SIZE = 16 * (1 << 10);   // 16 KB
    private static final int STANDARD_OUTER_BUFFER_SIZE = 4 * (1 << 20);    // 4 MB;

    private InputStream inputStream;

    private CompletableFuture<Integer> backgroundRead;
    private byte[] batch;
    private byte[] cache;

    private int length;
    private int offset;

    private int test = 0;

    public AsyncInputStream(InputStream inputStream, int batchSize) {
        this.inputStream = inputStream;

        batch = new byte[batchSize];
        cache = new byte[batchSize];
    }

    @Override
    public int read() throws IOException {
        if (offset >= length) {
            System.out.println(test++);
            if (backgroundRead == null) {
                backgroundRead = supplyAsync(() -> {
                    try {
                        return inputStream.read(cache);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                if (length <= 0) {
                    return -1;
                }
            }

            try {
                length = backgroundRead.get();
                offset = 0;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (length <= 0) {
                return -1;
            }

            byte[] temp = cache;
            cache = batch;
            batch = temp;

            backgroundRead = supplyAsync(() -> {
                try {
                    return inputStream.read(cache);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return toUnsignedInt(batch[offset++]);
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    public static InputStream getDefault(String path) throws IOException {
        return new AsyncInputStream(
                new BufferedInputStream(
                        new FileInputStream(valueOf(get(path))),
                        STANDARD_INNER_BUFFER_SIZE
                ),
                STANDARD_OUTER_BUFFER_SIZE
        );
    }

}
