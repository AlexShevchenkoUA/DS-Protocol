package ua.training.cipher;

import ua.training.SimpleMistyConfig;

import static java.lang.Integer.rotateLeft;
import static java.lang.Integer.toUnsignedLong;
import static java.lang.Long.remainderUnsigned;
import static java.lang.Long.reverseBytes;

public class SimpleMistyCipher implements SimpleBlockCipher {
    private long masterKey;
    private long[] keySchedule = new long[4];

    public SimpleMistyCipher() {}

    public SimpleMistyCipher(long key) {
        this.masterKey = key;
    }

    public void setMasterKey(long masterKey) {
        this.masterKey = masterKey;
    }

    public long getMasterKey() {
        return masterKey;
    }

    // Simple block cipher interface implementation

    @Override
    public long encrypt(long plainText) {
        long cipherText = plainText;

        for (long key : generateRoundKeys(masterKey, keySchedule)) {
            cipherText = encryptionRound(cipherText, key);
        }

        long reversed = reverseBytes(cipherText);

        return reverseBytes((reversed << (SimpleMistyConfig.BYTE_LENGTH * SimpleMistyConfig.HALF_BLOCK_BYTE_LENGTH)) ^ rightPart(reversed));
    }

    // Internal realization

    private long encryptionRound(long text, long roundKey) {
        long left = leftPart(text) ^ roundFunction(rightPart(text), roundKey);

        return (text << (SimpleMistyConfig.BYTE_LENGTH * SimpleMistyConfig.HALF_BLOCK_BYTE_LENGTH)) ^ left;
    }

    private long roundFunction(long text, long roundKey) {
        return toUnsignedLong(rotateLeft((int) SBox.map(text ^ roundKey), SimpleMistyConfig.MISTY_ROUND_FUNCTION_SHIFT));
    }

    private long[] generateRoundKeys(long masterKey) {
        long leftPart = leftPart(masterKey);
        long rightPart = rightPart(masterKey);

        return new long[] { leftPart, rightPart, ~rightPart, ~leftPart };
    }

    private long[] generateRoundKeys(long masterKey, long[] keyScheduleContainer) {
        long leftPart = leftPart(masterKey);
        long rightPart = rightPart(masterKey);

        keyScheduleContainer[0] = leftPart;
        keyScheduleContainer[1] = rightPart;
        keyScheduleContainer[2] = ~rightPart;
        keyScheduleContainer[3] = ~leftPart;

        return keyScheduleContainer;
    }

    private long rightPart(long value) {
        return value >>> (SimpleMistyConfig.BYTE_LENGTH * SimpleMistyConfig.HALF_BLOCK_BYTE_LENGTH);
    }

    private long leftPart(long value) {
        return remainderUnsigned(value, 1L << (SimpleMistyConfig.BYTE_LENGTH * SimpleMistyConfig.HALF_BLOCK_BYTE_LENGTH));
    }
}
