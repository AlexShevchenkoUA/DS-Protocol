package ua.training;

import ua.training.cipher.SimpleMistyCipher;
import ua.training.ds.ElGamalSignatureManager;
import ua.training.ds.SignatureManager;
import ua.training.hash.AsyncInputStream;
import ua.training.hash.MerkleDamgardSimpleHashFunction;
import ua.training.hash.SimpleHashFunction;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class ComponentFactory {
    public Random random() {
        try {
            return SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public ElGamalSignatureManager signatureManager() {
        return new ElGamalSignatureManager()
                .setRandom(random())
                .setA(new BigInteger("9E93A4096E5416CED0242228014B67B5", 16))
                .setQ(new BigInteger("57A9144B382BFF0E5C25C9288DF44D23", 16))
                .setP(new BigInteger("AF5228967057FE1CB84B92511BE89A47", 16));
    }

    public SimpleHashFunction hashFunction() {
        return new MerkleDamgardSimpleHashFunction(mistyCipher());
    }

    public SimpleMistyCipher mistyCipher() {
        return new SimpleMistyCipher();
    }

    public InputStream inputStream(Path path) {
        try {
            return AsyncInputStream.getDefault(path.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Writer writer(String path) {
        try {
            return new FileWriter(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Reader reader(String path) {
        try {
            return new FileReader(path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
