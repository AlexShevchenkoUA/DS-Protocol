package ua.training.hash;

import ua.training.GlobalConfig;
import ua.training.SimpleMistyConfig;
import ua.training.cipher.SimpleMistyCipher;

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
}
