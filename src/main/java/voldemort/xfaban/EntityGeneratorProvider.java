package voldemort.xfaban;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.g414.dgen.EntityGenerator;
import com.g414.dgen.field.Field;
import com.g414.dgen.field.Fields;
import com.g414.hash.impl.MurmurHash;
import com.google.inject.Provider;

public class EntityGeneratorProvider implements Provider<EntityGenerator> {

    private final int keyLength;
    private final int payloadLength;
    private final long seed;

    public EntityGeneratorProvider(int keyLength, int payloadLength, long seed) {
        this.keyLength = keyLength;
        this.payloadLength = payloadLength;
        this.seed = seed;
    }

    public EntityGenerator get() {
        List<Field<?>> fields = Arrays.asList((Field<?>) Fields.getRandomBinaryField("key",
                                                                                     keyLength),
                                              (Field<?>) Fields.getRandomBinaryField("value",
                                                                                     payloadLength));

        return new EntityGenerator(new MurmurHash(), fields, seed);
    }

    public static Random getRandom(long seed) {
        try {
            Random theRandom = SecureRandom.getInstance("sha1prng");
            theRandom.setSeed(seed);

            return theRandom;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
