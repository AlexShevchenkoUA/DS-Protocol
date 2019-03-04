package ua.training.hash;

import java.math.BigInteger;

public interface HashFunction {
    BigInteger hash(byte[] bytes);
}
