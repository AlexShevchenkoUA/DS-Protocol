package ua.training.cipher;

import ua.training.SimpleMistyConfig;

import static java.lang.Integer.rotateLeft;
import static java.lang.Integer.toUnsignedLong;
import static java.lang.Long.remainderUnsigned;

public class SimpleMistyCipher implements SimpleBlockCipher {
    private long key;

    public SimpleMistyCipher() {}

    public SimpleMistyCipher(long key) {
        this.key = key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public long getKey() {
        return key;
    }

    // Simple block cipher interface implementation

    @Override
    public long encrypt(long plainText) {
        long cipherText = plainText;

        for (long key : generateRoundKeys(key)) {
            cipherText = encryptionRound(cipherText, key);
        }

        return Long.reverseBytes((leftPart(Long.reverseBytes(cipherText)) << (SimpleMistyConfig.BYTE_LENGTH * SimpleMistyConfig.HALF_BLOCK_BYTE_LENGTH)) ^ rightPart(Long.reverseBytes(cipherText)));
    }

    // Internal realization

    private long encryptionRound(long text, long roundKey) {
        long left = leftPart(text) ^ roundFunction(rightPart(text), roundKey);

        return (leftPart(text) << (SimpleMistyConfig.BYTE_LENGTH * SimpleMistyConfig.HALF_BLOCK_BYTE_LENGTH)) ^ left;
    }

    private long roundFunction(long text, long roundKey) {
        return toUnsignedLong(rotateLeft((int) SBox.map(text ^ roundKey), SimpleMistyConfig.MISTY_ROUND_FUNCTION_SHIFT));
    }

    private long[] generateRoundKeys(long masterKey) {
        long leftPart = leftPart(masterKey);
        long rightPart = rightPart(masterKey);

        return new long[] { leftPart, rightPart, ~rightPart, ~leftPart };
    }

    private long rightPart(long value) {
        return value >>> SimpleMistyConfig.BYTE_LENGTH * SimpleMistyConfig.HALF_BLOCK_BYTE_LENGTH;
    }

    private long leftPart(long value) {
        return remainderUnsigned(value, 1L << (SimpleMistyConfig.BYTE_LENGTH * SimpleMistyConfig.HALF_BLOCK_BYTE_LENGTH));
    }
}
