package ua.training.hash;

import ua.training.GlobalConfig;
import ua.training.SimpleMistyConfig;
import ua.training.cipher.SimpleMistyCipher;

public class MerkleDamgardSimpleHashFunction implements SimpleHashFunction {
    private SimpleMistyCipher mistyCipher;

    public SimpleMistyCipher getMistyCipher() {
        return mistyCipher;
    }

    public void setMistyCipher(SimpleMistyCipher mistyCipher) {
        this.mistyCipher = mistyCipher;
    }

    // Merkle Damgard Simple Hash Function interface implementation

    @Override
    public long hash(byte[] bytes) {
        long[] sequence = convertToDenseLongSequence(padding(bytes));
        long result = 0;

        for (long message : sequence) {
            result = roundFunction(message, result);
        }

        return result;
    }

    // Internal realization

    private long roundFunction(long m, long h) {
        mistyCipher.setKey(m);

        return mistyCipher.encrypt(m ^ h) ^ h;
    }

    private int[] padding(byte[] bytes) {
        int paddingLength = SimpleMistyConfig.BLOCK_BYTE_LENGTH - (bytes.length % SimpleMistyConfig.BLOCK_BYTE_LENGTH);

        int[] message = new int[bytes.length + paddingLength];

        for (int i = 0; i < bytes.length; i++) {
            message[i] = Byte.toUnsignedInt(bytes[i]);
        }

        message[bytes.length] = 0x80;

        return message;
    }

    private long[] convertToDenseLongSequence(int[] bytes) {
        long[] blocks = new long[bytes.length / GlobalConfig.BLOCK_BYTE_LENGTH];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < GlobalConfig.BLOCK_BYTE_LENGTH; j++) {
                blocks[i] = blocks[i] ^ (bytes[GlobalConfig.BLOCK_BYTE_LENGTH * i + j] << (GlobalConfig.BYTE_LENGTH * j));
            }
        }
        return blocks;
    }
}
