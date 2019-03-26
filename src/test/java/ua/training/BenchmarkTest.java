package ua.training;

import org.junit.Before;
import org.junit.Test;
import ua.training.cipher.SimpleMistyCipher;
import ua.training.hash.AsyncInputStream;
import ua.training.hash.MerkleDamgardSimpleHashFunction;
import ua.training.hash.SimpleHashFunction;

import java.io.*;
import java.nio.file.Paths;
import java.util.Random;

import static java.lang.Long.toUnsignedString;
import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.nanoTime;
import static java.nio.file.Paths.get;

public class BenchmarkTest {
    private SimpleHashFunction hashFunction;

    private long size = 256 * (1L << 20) + 1L;    //Size 2 GB
    private String tempFile = "temp.bin";

    @Before
    public void initHashFunction() {
        hashFunction = new MerkleDamgardSimpleHashFunction(new SimpleMistyCipher());
    }

    @Before
    public void createTempFile() throws IOException {
        Random random = new Random();
        byte[] bytes = new byte[128];
        long time = currentTimeMillis();

        if (Paths.get(tempFile).toFile().exists()) {
            return;
        }

        try (OutputStream stream = new BufferedOutputStream(new FileOutputStream(valueOf(get(tempFile))))) {
            for (long i = 0; i < (size >>> 7); i++) {
                random.nextBytes(bytes);
                stream.write(bytes);
            }
        }

        System.out.println("Writing file time: " + (currentTimeMillis() - time));
    }

    @Test
    public void benchmark() throws IOException {
        /*try (InputStream stream = AsyncInputStream.getDefault(tempFile)) {
            long time = currentTimeMillis();
            byte[] bytes = new byte[8];
            while (stream.read(bytes) != -1);
            time = currentTimeMillis() - time;
            System.out.println("Speed - " + (((double) (size >>> 20)) / time) + " MB/ms");
            System.out.println(time);
        }*/

        try (InputStream stream = new InputStream() {
            Random random = new Random();

            long size1 = (1L << 30);
            long count;

            @Override
            public int read() throws IOException {
                if (count < size1) {
                    count++;
                    return random.nextInt(1 << 8);
                } else {
                    return -1;
                }
            }
        }) {
            long time = currentTimeMillis();
            long hash = hashFunction.hash(stream);
            System.out.println(Long.toUnsignedString(hash, 16));
            time = currentTimeMillis() - time;
            System.out.println("Speed - " + (((double) (size >>> 20)) / time) + " MB/ms");
            System.out.println(time);
        }

        /*try (InputStream stream = AsyncInputStream.getDefault(tempFile)) {
            long time = currentTimeMillis();
            long hash = hashFunction.hash(stream);
            System.out.println(Long.toUnsignedString(hash, 16));
            System.out.println("Speed - " + (((double) (size >>> 20)) / (currentTimeMillis() - time)) + " MB/ms");
        }*/
    }
}
