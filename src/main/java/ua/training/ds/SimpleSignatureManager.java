package ua.training.ds;

import java.math.BigInteger;

public interface SimpleSignatureManager {
    Signature signature(long hash, BigInteger privateKey);
    boolean verify(Signature signature, long message, BigInteger publicKey);
}
