package ua.training;

public class HashFunction {
    private MistyCipher mistyCipher;

    public MistyCipher getMistyCipher() {
        return mistyCipher;
    }

    public void setMistyCipher(MistyCipher mistyCipher) {
        this.mistyCipher = mistyCipher;
    }

    public long hash(byte[] bytes) {
        long[] sequence = convertToDenseLongSequence(padding(convertToUnsignedInt(bytes)));
        long result = 0;

        for (long message : sequence) {
            result = roundFunction(message, result);
        }

        return result;
    }

    public long roundFunction(long m, long h) {
        mistyCipher.setKey(m);

        return mistyCipher.encrypt(m ^ h) ^ h;
    }

    private int[] convertToUnsignedInt(byte[] bytes) {
        int[] unsignedBytes = new int[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            unsignedBytes[i] = Byte.toUnsignedInt(bytes[i]);
        }

        return unsignedBytes;
    }

    private long[] convertToDenseLongSequence(int[] bytes) {
        long[] blocks = new long[bytes.length / Config.BLOCK_BYTE_LENGTH];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < Config.BLOCK_BYTE_LENGTH; j++) {
                blocks[i] = blocks[i] ^ (bytes[Config.BLOCK_BYTE_LENGTH * i + j] << (Config.BYTE_LENGTH * j));
            }
        }
        return blocks;
    }

    private int[] padding(int[] bytes) {
        int paddingLength = Config.BLOCK_BYTE_LENGTH - (bytes.length % Config.BLOCK_BYTE_LENGTH);

        int[] bytesWithPadding = new int[bytes.length + paddingLength];

        System.arraycopy(bytes, 0, bytesWithPadding, 0, bytes.length);

        bytesWithPadding[bytes.length] = 0x80;

        return bytesWithPadding;
    }
}
