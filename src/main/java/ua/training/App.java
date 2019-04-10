package ua.training;


import ua.training.ds.ElGamalSignatureManager;
import ua.training.ds.Signature;
import ua.training.hash.SimpleHashFunction;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static java.lang.Long.parseUnsignedLong;
import static java.lang.Long.toUnsignedString;
import static java.lang.String.valueOf;

public class App {
    private static ComponentFactory componentFactory = new ComponentFactory();

    public static void main( String[] args ) throws Exception {
        String mode = args[0];
        Path path = Paths.get(args[1]);

        if (!path.toFile().exists()) {
            return;
        }

        switch (mode.toLowerCase()) {
            case "-sign":
                sing(path);
                return;
            case "-check":
                check(path, Paths.get(args[2]));
                return;
        }
    }

    private static void sing(Path path) throws IOException {
        try (InputStream inputStream = componentFactory.inputStream(path)) {
            ElGamalSignatureManager signatureManager = componentFactory.signatureManager();
            SimpleHashFunction hashFunction = componentFactory.hashFunction();

            Signature signature = signatureManager.signature(hashFunction.hash(inputStream), signatureManager.getPrivateKey());

            createResultFile(path, signature);
            createMetadataFile(path, signature);
        }
    }

    private static void check(Path path, Path properties) throws IOException {
        if (!properties.toFile().exists()) {
            return;
        }

        Signature signature = loadFromProperties(properties.toString());

        try (InputStream inputStream = componentFactory.inputStream(path)) {
            ElGamalSignatureManager signatureManager = componentFactory.signatureManager();
            SimpleHashFunction hashFunction = componentFactory.hashFunction();

            if (signatureManager.verify(signature, hashFunction.hash(inputStream), signature.getY())) {
                System.out.println("Signature is correct");
            } else {
                System.out.println("Signature is incorrect");
            }
        }
    }

    private static void createResultFile(Path path, Signature signature) throws IOException {
        Properties properties = new Properties();

        properties.setProperty("H", toUnsignedString(signature.getHash(), 16));
        properties.setProperty("Y", signature.getY().toString(16));
        properties.setProperty("K", signature.getK().toString(16));
        properties.setProperty("S", signature.getS().toString(16));

        try (Writer writer = componentFactory.writer(path.toString() + ".sig")) {
            properties.store(writer, path.getFileName().toString());
        }
    }

    private static void createMetadataFile(Path path, Signature signature) throws IOException {
        Properties properties = new Properties();

        properties.setProperty("U", signature.getU().toString(16));
        properties.setProperty("Z", signature.getZ().toString(16));
        properties.setProperty("G", signature.getG().toString(16));

        try (Writer writer = componentFactory.writer(path.toString() + ".sig.add")) {
            properties.store(writer, path.getFileName().toString());
        }
    }

    private static Signature loadFromProperties(String propertiesFile) throws IOException {
        Properties properties = new Properties();

        try (Reader reader = componentFactory.reader(propertiesFile)) {
            properties.load(reader);

            return new Signature(new BigInteger(properties.getProperty("K"), 16), new BigInteger(properties.getProperty("S"), 16))
                    .setY(new BigInteger(properties.getProperty("Y"), 16))
                    .setHash(parseUnsignedLong(properties.getProperty("H"), 16));
        }
    }
}
