package ua.training;

import java.math.BigInteger;
import java.util.Random;

public class DigitalSignature {
    private Random random;

    private BigInteger p;
    private BigInteger q;
    private BigInteger a;

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

    public BigInteger format(long value) {
        BigInteger result = new BigInteger(Long.toUnsignedString(value, 16), 16);
        BigInteger padding = BigInteger.ONE.shiftLeft((Config.BLOCK_BYTE_LENGTH - 2) * Config.BYTE_LENGTH)
                .subtract(BigInteger.ONE).shiftLeft(Config.BYTE_LENGTH);
        return result.xor(padding.shiftLeft(Config.BYTE_LENGTH * Config.BLOCK_BYTE_LENGTH));
    }

    public Signature signature(long hash, BigInteger x) {
        BigInteger H = format(hash);
        BigInteger U = new BigInteger(p.bitLength(), random).mod(p);

        BigInteger Z = a.modPow(U, p);

        BigInteger k = U.add(BigInteger.ONE).subtract(x.multiply(H).multiply(Z.modInverse(q))).mod(q);
        BigInteger g = H.multiply(x).multiply(Z.modInverse(q)).subtract(BigInteger.ONE).mod(q);

        BigInteger S = a.modPow(g, p);
        return new Signature(k, S);
    }

    public boolean verify(Signature s, long message, BigInteger y) {
        BigInteger left = y.modPow(format(message), p);

        BigInteger exponent = a.modPow(s.getK(), p).multiply(s.getS()).mod(p);
        BigInteger right = a.multiply(s.getS()).modPow(exponent, p);

        return left.equals(right);
    }
}
