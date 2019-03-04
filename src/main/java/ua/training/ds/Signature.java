package ua.training.ds;

import java.math.BigInteger;

public class Signature {
    private BigInteger k;
    private BigInteger S;

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
}
