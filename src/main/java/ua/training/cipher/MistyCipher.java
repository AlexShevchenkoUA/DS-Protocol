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

        return bytesReverse(
                leftPart(bytesReverse(cipherText, GlobalConfig.BYTE_LENGTH))
                        .shiftLeft(GlobalConfig.BYTE_LENGTH * GlobalConfig.HALF_BLOCK_BYTE_LENGTH)
                        .xor(rightPart(bytesReverse(cipherText, GlobalConfig.BYTE_LENGTH))),
                GlobalConfig.BYTE_LENGTH
        );
    }

    // Internal realization

    private BigInteger encryptionRound(BigInteger text, BigInteger roundKey) {
        return leftPart(text)
                .xor(roundFunction(rightPart(text), roundKey))
                .xor(leftPart(text).shiftLeft(GlobalConfig.BYTE_LENGTH * GlobalConfig.HALF_BLOCK_BYTE_LENGTH));
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

    private BigInteger rightPart(BigInteger bigInteger) {
        return bigInteger.shiftRight(GlobalConfig.BYTE_LENGTH * GlobalConfig.HALF_BLOCK_BYTE_LENGTH);
    }

    private BigInteger leftPart(BigInteger bigInteger) {
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

    private BigInteger bytesReverse(BigInteger bigInteger, int byteLength) {
        BigInteger mask = BigInteger.valueOf(GlobalConfig.UNSIGNED_BYTE_MASK);
        BigInteger reversed = BigInteger.ZERO;

        for (int i = 0; i < byteLength; i++) {
            reversed = reversed.xor(bigInteger.shiftRight(GlobalConfig.BYTE_LENGTH * i).and(mask).shiftLeft(GlobalConfig.BYTE_LENGTH * (byteLength - i - 1)));
        }

        return reversed;
    }
}
