package ua.training.hash;

import ua.training.GlobalConfig;
import ua.training.SimpleMistyConfig;
import ua.training.cipher.SimpleMistyCipher;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLOutput;

import static java.lang.Byte.toUnsignedLong;

public class MerkleDamgardSimpleHashFunction implements SimpleHashFunction {
    private SimpleMistyCipher mistyCipher;

    public MerkleDamgardSimpleHashFunction() {}

    public MerkleDamgardSimpleHashFunction(SimpleMistyCipher mistyCipher) {
        this.mistyCipher = mistyCipher;
    }

    public SimpleMistyCipher getMistyCipher() {
        return mistyCipher;
    }

    public void setMistyCipher(SimpleMistyCipher mistyCipher) {
        this.mistyCipher = mistyCipher;
    }

    // Merkle Damgard Simple Hash Function interface implementation

    @Override
    public long hash(byte[] bytes) {
        long result = 0;

        for (long message : convertToBlockSequenceWithPadding(bytes)) {
            result = roundFunction(message, result);
        }

        return result;
    }

    @Override
    public long hash(InputStream stream)throws IOException {
        try (InputStream inputStream = stream) {
            long result = 0;

            byte[] buffer = new byte[SimpleMistyConfig.BLOCK_BYTE_LENGTH];

            int consumed;

            while((consumed = inputStream.read(buffer)) == buffer.length) {
                result = roundFunction(boxBytesInLong(buffer), result);
            }

            return roundFunction(getPuddingBox(buffer, consumed > -1 ? consumed : 0), result);
        }
    }

    // Internal realization

    private long roundFunction(long m, long h) {
        mistyCipher.setKey(m);

        return mistyCipher.encrypt(m ^ h) ^ h;
    }

    private int getPaddingLength(int sequenceLength) {
        return SimpleMistyConfig.BLOCK_BYTE_LENGTH - (sequenceLength % SimpleMistyConfig.BLOCK_BYTE_LENGTH);
    }

    private int getSequenceLength(int sparseSequenceLength) {
        return sparseSequenceLength / SimpleMistyConfig.BLOCK_BYTE_LENGTH;
    }

    private long[] convertToBlockSequenceWithPadding(byte[] bytes) {
        long[] message = new long[getSequenceLength(bytes.length + getPaddingLength(bytes.length))];

        for (int i = 0; i < bytes.length; i++) {
            message[i / SimpleMistyConfig.BLOCK_BYTE_LENGTH] ^= (toUnsignedLong(bytes[i]) << (SimpleMistyConfig.BYTE_LENGTH * (i % SimpleMistyConfig.BLOCK_BYTE_LENGTH)));
        }

        message[bytes.length / SimpleMistyConfig.BLOCK_BYTE_LENGTH] ^= (0x80L << (SimpleMistyConfig.BYTE_LENGTH * (bytes.length % SimpleMistyConfig.BLOCK_BYTE_LENGTH)));

        return message;
    }

    private long boxBytesInLong(byte[] buffer) {
        long box = 0;
        for (int i = 0; i < SimpleMistyConfig.BLOCK_BYTE_LENGTH; i++) {
            box ^= (toUnsignedLong(buffer[i])) << (SimpleMistyConfig.BYTE_LENGTH * i);
        }
        return box;
    }

    private long getPuddingBox(byte[] buffer, int consumed) {
        long box = 0;
        for (int i = 0; i < consumed; i++) {
            box ^= (toUnsignedLong(buffer[i])) << (SimpleMistyConfig.BYTE_LENGTH * i);
        }
        return box ^ (0x80L << (SimpleMistyConfig.BYTE_LENGTH * consumed));
    }
}
