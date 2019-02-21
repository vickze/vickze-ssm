package io.vickze.shiro.cache;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

import org.apache.shiro.session.Session;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2018-02-08 16:09
 */
public class KryoSerializer {


    public static <T> byte[] serialize(T t) {
        if (t == null) {
            return new byte[0];
        }

        Output output = new Output(4096);
        kryoLocal.get().writeClassAndObject(output, t);
        output.close();

        return output.toBytes();
    }

    public static <T> T deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        Input input = new Input(bytes);
        input.close();

        return (T) kryoLocal.get().readClassAndObject(input);
    }

    //<3>
    private static final ThreadLocal<Kryo> kryoLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        //Shiro Session类无无参构造函数，使用JavaSerializer
        kryo.addDefaultSerializer(Session.class, new JavaSerializer());
        return kryo;
    });
}
