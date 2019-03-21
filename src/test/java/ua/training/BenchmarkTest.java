package ua.training;

import org.junit.Before;
import org.junit.Test;
import ua.training.cipher.SimpleMistyCipher;
import ua.training.hash.MerkleDamgardSimpleHashFunction;
import ua.training.hash.SimpleHashFunction;

import java.io.*;
import java.nio.file.Paths;
import java.util.Random;

import static java.lang.Long.toUnsignedString;
import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static java.nio.file.Paths.get;

public class BenchmarkTest {
    private SimpleHashFunction hashFunction;

    private long size = 64 * (1L << 30) + 1L;    //Size 64 GB
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

        try (OutputStream stream = new BufferedOutputStream(new FileOutputStream(valueOf(get(tempFile))))) {
            for (long i = 0; i < size; i++) {
                random.nextBytes(bytes);
                stream.write(bytes);
            }
        }

        System.out.println("Writing file time: " + (currentTimeMillis() - time));
    }

    @Test
    public void benchmark() throws IOException {
        for (int i = 5; i < 10; i++) {
            template((1 << i) * (1 << 20));
        }
    }

    public void template(int batchSize) throws IOException {
        try (InputStream stream = new BufferedInputStream(new FileInputStream(valueOf(get(tempFile))), batchSize)) {
            long time = currentTimeMillis();
            long hash = hashFunction.hash(stream);
            System.out.println("Batch size - " + batchSize + " hash - " + toUnsignedString(hash, 16) + " time - " + (time = currentTimeMillis() - time));
            System.out.println("Speed - " + (((double) (size >>> 20)) / time) + " MB/ms");
        }
    }
}
