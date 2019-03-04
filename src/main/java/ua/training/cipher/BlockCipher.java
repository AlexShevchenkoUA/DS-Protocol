package ua.training.cipher;

import java.math.BigInteger;

public interface BlockCipher {
    BigInteger encrypt(BigInteger text);
}
