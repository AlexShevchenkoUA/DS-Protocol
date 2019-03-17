package ua.training.hash;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public interface HashFunction {
    BigInteger hash(byte[] bytes);
    BigInteger hash(InputStream stream) throws IOException;
}
