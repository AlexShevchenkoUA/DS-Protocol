package ua.training.cipher;

import ua.training.GlobalConfig;

import java.math.BigInteger;

public class MistyCipher implements BlockCipher {
    private BigInteger key;

    public MistyCipher(BigInteger key) {
        this.key = key;
    }

    public void setKey(BigInteger key) {
        this.key = key;
    }

    public BigInteger getKey() {
        return key;
    }

    // Block cipher interface realization

    @Override
    public BigInteger encrypt(BigInteger text) {
        BigInteger cipherText = text;

        for (BigInteger key : generateRoundKeys(key)) {
            cipherText = encryptionRound(cipherText, key);
        }

        return rightPart(cipherText)
                .shiftLeft(GlobalConfig.BYTE_LENGTH * GlobalConfig.HALF_BLOCK_BYTE_LENGTH)
                .add(leftPart(cipherText));
    }

    // Internal realization

    private BigInteger encryptionRound(BigInteger text, BigInteger roundKey) {
        return leftPart(text)
                .xor(roundFunction(rightPart(text), roundKey))
                .shiftLeft(GlobalConfig.BYTE_LENGTH * GlobalConfig.HALF_BLOCK_BYTE_LENGTH)
                .xor(leftPart(text));
    }

    private BigInteger roundFunction(BigInteger text, BigInteger roundKey) {
        return cyclicRotation(
                SBox.map(text.xor(roundKey)),
                GlobalConfig.MISTY_ROUND_FUNCTION_SHIFT,
                GlobalConfig.BYTE_LENGTH * GlobalConfig.HALF_BLOCK_BYTE_LENGTH
        );
    }

    private BigInteger[] generateRoundKeys(BigInteger masterKey) {
        BigInteger leftPart = leftPart(masterKey);
        BigInteger rightPart = rightPart(masterKey);

        return new BigInteger[] {
                leftPart,
                rightPart,
                not(rightPart, GlobalConfig.BYTE_LENGTH * GlobalConfig.HALF_BLOCK_BYTE_LENGTH),
                not(leftPart, GlobalConfig.BYTE_LENGTH * GlobalConfig.HALF_BLOCK_BYTE_LENGTH)
        };
    }

    private BigInteger leftPart(BigInteger bigInteger) {
        return bigInteger.shiftRight(GlobalConfig.BYTE_LENGTH * GlobalConfig.HALF_BLOCK_BYTE_LENGTH);
    }

    private BigInteger rightPart(BigInteger bigInteger) {
        return bigInteger.mod(BigInteger.ONE.shiftLeft(GlobalConfig.BYTE_LENGTH * GlobalConfig.HALF_BLOCK_BYTE_LENGTH));
    }

    private BigInteger not(BigInteger bigInteger, int bitLength) {
        return bigInteger.xor(BigInteger.ONE.shiftLeft(bitLength).subtract(BigInteger.ONE));
    }

    private BigInteger cyclicRotation(BigInteger bigInteger, int shift, int bitLength) {
        BigInteger value = bigInteger.mod(BigInteger.ONE.shiftLeft(bitLength));
        return value.shiftLeft(shift)
                .xor(value.shiftRight(bitLength - shift))
                .mod(BigInteger.ONE.shiftLeft(bitLength));
    }
}
