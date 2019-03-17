package ua.training.hash;

import java.io.IOException;
import java.io.InputStream;

public interface SimpleHashFunction {
    long hash(byte[] bytes);
    long hash(InputStream stream) throws IOException;
}
