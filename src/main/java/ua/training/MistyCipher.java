package ua.training;

import java.math.BigInteger;
import java.util.Arrays;

public class MistyCipher {
    private long key;

    public MistyCipher() {}

    public MistyCipher(long key) {
        this.key = key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public long encrypt(long plainText) {
        long cipherText = plainText;
        long[] keySchedule = generateRoundKeys(key);

        for (int i = 0; i < keySchedule.length; i++) {
            cipherText = encryptionRound(cipherText, keySchedule[i]);
        }

        return (rightPart(cipherText) << (Config.BYTE_LENGTH * Config.HALF_BLOCK_BYTE_LENGTH)) ^ leftPart(cipherText);
    }

    private long encryptionRound(long text, long roundKey) {
        long left = leftPart(text) ^ roundFunction(rightPart(text), roundKey);

        return (left << (Config.BYTE_LENGTH * Config.HALF_BLOCK_BYTE_LENGTH)) ^ leftPart(text);
    }

    private long roundFunction(long text, long roundKey) {
        return Integer.rotateLeft((int) SBox.map(text ^ roundKey), Config.MISTY_ROUND_FUNCTION_SHIFT);
    }

    private long[] generateRoundKeys(long masterKey) {
        long leftPart = leftPart(masterKey);
        long rightPart = rightPart(masterKey);

        return new long[] { leftPart, rightPart, ~rightPart, ~leftPart };
    }

    private long leftPart(long value) {
        return value >>> Config.BYTE_LENGTH * Config.HALF_BLOCK_BYTE_LENGTH;
    }

    private long rightPart(long value) {
        return Long.remainderUnsigned(value, 1L << (Config.BYTE_LENGTH * Config.HALF_BLOCK_BYTE_LENGTH));
    }
}
