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

    public Random getRandom() {
        return random;
    }

    public ElGamalSignatureManager setRandom(Random random) {
        this.random = random;
        return this;
    }

    public BigInteger getP() {
        return p;
    }

    public ElGamalSignatureManager setP(BigInteger p) {
        this.p = p;
        return this;
    }

    public BigInteger getQ() {
        return q;
    }

    public ElGamalSignatureManager setQ(BigInteger q) {
        this.q = q;
        return this;
    }

    public BigInteger getA() {
        return a;
    }

    public ElGamalSignatureManager setA(BigInteger a) {
        this.a = a;
        return this;
    }


    // Signature manager interface implementation

    @Override
    public Signature signature(BigInteger hash, BigInteger privateKey) {
        BigInteger U = new BigInteger(p.bitLength(), random).mod(p);

        BigInteger Z = a.modPow(U, p);

        BigInteger k = U.add(BigInteger.ONE).subtract(privateKey.multiply(hash).multiply(Z.modInverse(q))).mod(q);
        BigInteger g = hash.multiply(privateKey).multiply(Z.modInverse(q)).subtract(BigInteger.ONE).mod(q);

        BigInteger S = a.modPow(g, p);

        return new Signature(k, S)
                .setU(U)
                .setG(g)
                .setY(getPublicKey(privateKey))
                .setZ(Z)
                .setFormattedHash(hash);
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
        return signature(format(hash), privateKey).setHash(hash);
    }

    @Override
    public boolean verify(Signature signature, long message, BigInteger publicKey) {
        return verify(signature, format(message), publicKey);
    }

    @Override
    public BigInteger getPrivateKey() {
        return new BigInteger(p.bitLength(), random).mod(p.subtract(BigInteger.ONE));
    }

    @Override
    public BigInteger getPublicKey(BigInteger privateKey) {
        return a.modPow(privateKey, p);
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
