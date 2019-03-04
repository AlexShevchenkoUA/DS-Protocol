package ua.training.cipher;

import ua.training.SimpleMistyConfig;

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

        return (rightPart(cipherText) << (SimpleMistyConfig.BYTE_LENGTH * SimpleMistyConfig.HALF_BLOCK_BYTE_LENGTH)) ^ leftPart(cipherText);
    }

    // Internal realization

    private long encryptionRound(long text, long roundKey) {
        long left = leftPart(text) ^ roundFunction(rightPart(text), roundKey);

        return (left << (SimpleMistyConfig.BYTE_LENGTH * SimpleMistyConfig.HALF_BLOCK_BYTE_LENGTH)) ^ leftPart(text);
    }

    private long roundFunction(long text, long roundKey) {
        return Integer.rotateLeft((int) SBox.map(text ^ roundKey), SimpleMistyConfig.MISTY_ROUND_FUNCTION_SHIFT);
    }

    private long[] generateRoundKeys(long masterKey) {
        long leftPart = leftPart(masterKey);
        long rightPart = rightPart(masterKey);

        return new long[] { leftPart, rightPart, ~rightPart, ~leftPart };
    }

    private long leftPart(long value) {
        return value >>> SimpleMistyConfig.BYTE_LENGTH * SimpleMistyConfig.HALF_BLOCK_BYTE_LENGTH;
    }

    private long rightPart(long value) {
        return Long.remainderUnsigned(value, 1L << (SimpleMistyConfig.BYTE_LENGTH * SimpleMistyConfig.HALF_BLOCK_BYTE_LENGTH));
    }
}
