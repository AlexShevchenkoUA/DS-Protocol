package ua.training.ds;

import java.math.BigInteger;

public class Signature {
    private BigInteger k;
    private BigInteger S;

    //Metadata
    private BigInteger y;
    private BigInteger u;
    private BigInteger z;
    private BigInteger g;

    //Hash
    private long hash;
    private BigInteger formattedHash;

    public Signature(BigInteger k, BigInteger s) {
        this.k = k;
        S = s;
    }

    public BigInteger getK() {
        return k;
    }

    public void setK(BigInteger k) {
        this.k = k;
    }

    public BigInteger getS() {
        return S;
    }

    public void setS(BigInteger s) {
        S = s;
    }

    public BigInteger getY() {
        return y;
    }

    public Signature setY(BigInteger y) {
        this.y = y;
        return this;
    }

    public BigInteger getU() {
        return u;
    }

    public Signature setU(BigInteger u) {
        this.u = u;
        return this;
    }

    public BigInteger getZ() {
        return z;
    }

    public Signature setZ(BigInteger z) {
        this.z = z;
        return this;
    }

    public BigInteger getG() {
        return g;
    }

    public Signature setG(BigInteger g) {
        this.g = g;
        return this;
    }

    public long getHash() {
        return hash;
    }

    public Signature setHash(long hash) {
        this.hash = hash;
        return this;
    }

    public BigInteger getFormattedHash() {
        return formattedHash;
    }

    public Signature setFormattedHash(BigInteger formattedHash) {
        this.formattedHash = formattedHash;
        return this;
    }
}
