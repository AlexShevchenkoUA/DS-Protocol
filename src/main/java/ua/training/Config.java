package ua.training;

import java.math.BigInteger;

/**
 * Class contains basic parameters for working and test parameters of ds system. Attention: MistyCipher works only
 * with block size 8 bytes, for another size use LongArithmeticMistyCipher.
 */
public interface Config {
    //Misty cipher parameters
    int BLOCK_BYTE_LENGTH = 8;
    int HALF_BLOCK_BYTE_LENGTH = BLOCK_BYTE_LENGTH / 2;
    int BYTE_LENGTH = 8;
    int UNSIGNED_BYTE_MASK = (1 << BYTE_LENGTH) - 1;
    int MISTY_ROUND_FUNCTION_SHIFT = 13;

    //Test parameters
    BigInteger p = new BigInteger("AF5228967057FE1CB84B92511BE89A47", 16);
    BigInteger q = new BigInteger("57A9144B382BFF0E5C25C9288DF44D23", 16);
    BigInteger a = new BigInteger("9E93A4096E5416CED0242228014B67B5", 16);
}
