package ua.training;

public interface SimpleMistyConfig {
    int BLOCK_BYTE_LENGTH = 8;
    int HALF_BLOCK_BYTE_LENGTH = BLOCK_BYTE_LENGTH / 2;
    int BYTE_LENGTH = 8;
    int UNSIGNED_BYTE_MASK = (1 << BYTE_LENGTH) - 1;
    int MISTY_ROUND_FUNCTION_SHIFT = 13;
}
