package ua.training.ds;

import java.math.BigInteger;

public interface SignatureManager {
    Signature signature(BigInteger hash, BigInteger privateKey);
    boolean verify(Signature signature, BigInteger message, BigInteger publicKey);
}
