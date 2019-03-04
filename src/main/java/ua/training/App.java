package ua.training;

import ua.training.ds.ElGamalSignatureManager;
import ua.training.ds.Signature;

import java.math.BigInteger;
import java.util.Random;


public class App 
{
    public static void main( String[] args ) {
        ElGamalSignatureManager digitalSignature = new ElGamalSignatureManager();

        digitalSignature.setQ(new BigInteger("57A9144B382BFF0E5C25C9288DF44D23", 16));
        digitalSignature.setA(new BigInteger("9E93A4096E5416CED0242228014B67B5", 16));

        Random random = new Random();

        long message = random.nextLong();

        digitalSignature.setRandom(random);

        BigInteger x = new BigInteger(digitalSignature.getP().bitLength(), random).mod(digitalSignature.getP());
        BigInteger y = digitalSignature.getA().modPow(x, digitalSignature.getP());

        Signature signature = digitalSignature.signature(message, x);

        System.out.println(digitalSignature.verify(signature, message, y));
    }
}
