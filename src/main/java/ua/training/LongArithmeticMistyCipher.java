package ua.training;

import java.math.BigInteger;
import java.util.Arrays;

public class LongArithmeticMistyCipher {
    private BigInteger key;

    public LongArithmeticMistyCipher(BigInteger key) {
        this.key = key;
    }

    public void setKey(BigInteger key) {
        this.key = key;
    }

    public BigInteger encrypt(BigInteger plainText) {
        BigInteger cipherText = plainText;
        BigInteger[] keySchedule = generateRoundKeys(key);

        for (int i = 0; i < keySchedule.length; i++) {
            cipherText = encryptionRound(cipherText, keySchedule[i]);
        }
        return rightPart(cipherText).shiftLeft(Config.BYTE_LENGTH * Config.HALF_BLOCK_BYTE_LENGTH).add(leftPart(cipherText));
    }

    private BigInteger encryptionRound(BigInteger text, BigInteger roundKey) {
        BigInteger left = leftPart(text).xor(roundFunction(rightPart(text), roundKey));

        return left.shiftLeft(Config.BYTE_LENGTH * Config.HALF_BLOCK_BYTE_LENGTH).xor(leftPart(text));
    }

    private BigInteger roundFunction(BigInteger text, BigInteger roundKey) {
        return cyclicRotation(SBox.map(text.xor(roundKey)), Config.MISTY_ROUND_FUNCTION_SHIFT, Config.BYTE_LENGTH * Config.HALF_BLOCK_BYTE_LENGTH);
    }

    private BigInteger[] generateRoundKeys(BigInteger masterKey) {
        BigInteger leftPart = leftPart(masterKey);
        BigInteger rightPart = rightPart(masterKey);
        return new BigInteger[] {
                leftPart,
                rightPart,
                not(rightPart, Config.BYTE_LENGTH * Config.HALF_BLOCK_BYTE_LENGTH),
                not(leftPart, Config.BYTE_LENGTH * Config.HALF_BLOCK_BYTE_LENGTH)
        };
    }

    private BigInteger leftPart(BigInteger bigInteger) {
        return bigInteger.shiftRight(Config.BYTE_LENGTH * Config.HALF_BLOCK_BYTE_LENGTH);
    }

    private BigInteger rightPart(BigInteger bigInteger) {
        return bigInteger.mod(BigInteger.ONE.shiftLeft(Config.BYTE_LENGTH * Config.HALF_BLOCK_BYTE_LENGTH));
    }

    private BigInteger not(BigInteger bigInteger, int bitLength) {
        return bigInteger.xor(BigInteger.ONE.shiftLeft(bitLength).subtract(BigInteger.ONE));
    }

    private BigInteger cyclicRotation(BigInteger bigInteger, int shift, int bitLength) {
        BigInteger value = bigInteger.mod(BigInteger.ONE.shiftLeft(bitLength));
        return value.shiftLeft(shift).add(value.shiftRight(bitLength - shift)).mod(BigInteger.ONE.shiftLeft(bitLength));
    }
}
