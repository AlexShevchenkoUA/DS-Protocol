package ua.training.ds;

import ua.training.GlobalConfig;

import java.math.BigInteger;
import java.util.Random;

public class ElGamalSignatureManager implements SignatureManager, SimpleSignatureManager {
    private Random random;

    private BigInteger p;
    private BigInteger q;
    private BigInteger a;

    // Dependency injection interface

    public void setQ(BigInteger q) {
        this.q = q;
        this.p = q.shiftLeft(1).add(BigInteger.ONE);
    }

    public void setA(BigInteger a) {
        this.a = a;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getQ() {
        return q;
    }

    public BigInteger getA() {
        return a;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Random getRandom() {
        return random;
    }

    // Signature manager interface implementation

    @Override
    public Signature signature(BigInteger hash, BigInteger privateKey) {
        BigInteger U = new BigInteger(p.bitLength(), random).mod(p);

        BigInteger Z = a.modPow(U, p);

        BigInteger k = U.add(BigInteger.ONE).subtract(privateKey.multiply(hash).multiply(Z.modInverse(q))).mod(q);
        BigInteger g = hash.multiply(privateKey).multiply(Z.modInverse(q)).subtract(BigInteger.ONE).mod(q);

        BigInteger S = a.modPow(g, p);

        return new Signature(k, S);
    }

    @Override
    public boolean verify(Signature signature, BigInteger message, BigInteger publicKey) {
        BigInteger left = publicKey.modPow(message, p);

        BigInteger exponent = a.modPow(signature.getK(), p).multiply(signature.getS()).mod(p);
        BigInteger right = a.multiply(signature.getS()).modPow(exponent, p);

        return left.equals(right);
    }

    // Simple signature manager implementation

    @Override
    public Signature signature(long hash, BigInteger privateKey) {
        return signature(format(hash), privateKey);
    }

    @Override
    public boolean verify(Signature signature, long message, BigInteger publicKey) {
        return verify(signature, format(message), publicKey);
    }

    private BigInteger format(long value) {
        return BigInteger.ONE
                .shiftLeft((GlobalConfig.BLOCK_BYTE_LENGTH - 2) * GlobalConfig.BYTE_LENGTH)
                .subtract(BigInteger.ONE)
                .shiftLeft(GlobalConfig.BYTE_LENGTH)
                .shiftLeft(GlobalConfig.BYTE_LENGTH * GlobalConfig.BLOCK_BYTE_LENGTH)
                .xor(new BigInteger(Long.toUnsignedString(value, 16), 16));
    }
}
