package ua.training;

import ua.training.cipher.BlockCipher;
import ua.training.cipher.MistyCipher;
import ua.training.cipher.SimpleMistyCipher;
import ua.training.ds.ElGamalSignatureManager;
import ua.training.ds.Signature;
import ua.training.hash.MerkleDamgardSimpleHashFunction;
import ua.training.hash.SimpleHashFunction;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class App 
{
    public static void main( String[] args ) throws Exception {
        Random random = new Random();

        System.out.println(Long.toUnsignedString(new SimpleMistyCipher(1234L).encrypt(1234L), 16));
        System.out.println(new MistyCipher(BigInteger.valueOf(1234L)).encrypt(BigInteger.valueOf(1234L)).toString(16));

        /*ElGamalSignatureManager digitalSignature = new ElGamalSignatureManager();

        digitalSignature.setQ(new BigInteger("57A9144B382BFF0E5C25C9288DF44D23", 16));
        digitalSignature.setA(new BigInteger("9E93A4096E5416CED0242228014B67B5", 16));


        long message = random.nextLong();

        digitalSignature.setRandom(random);

        BigInteger x = new BigInteger(digitalSignature.getP().bitLength(), random).mod(digitalSignature.getP());
        BigInteger y = digitalSignature.getA().modPow(x, digitalSignature.getP());

        Signature signature = digitalSignature.signature(message, x);

        System.out.println(digitalSignature.verify(signature, message, y));*/

        //

        /*SimpleHashFunction hashFunction = new MerkleDamgardSimpleHashFunction(new SimpleMistyCipher());

        InputStream stream = new FileInputStream("1.txt");

        List<Byte> bytes = new ArrayList<>();

        int b = stream.read();

        while (b != -1) {
            bytes.add((byte) b);
            b = stream.read();

        }

        byte[] bytes_array = new byte[bytes.size()];

        for (int i = 0; i < bytes.size(); i++) {
            bytes_array[i] = bytes.get(i).byteValue();
        }

        stream.close();

        System.out.println(Long.toUnsignedString(hashFunction.hash(bytes_array), 16));*/
    }
}
