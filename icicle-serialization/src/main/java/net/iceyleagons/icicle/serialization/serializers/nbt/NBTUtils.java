package net.iceyleagons.icicle.serialization.serializers.nbt;

import net.querz.nbt.tag.*;

import java.util.List;

public final class NBTUtils {

    public static IntTag parseBoolean(boolean value) {
        return new IntTag(value ? 1 : 0);
    }

    public static boolean getFromIntTag(IntTag intTag) {
        return Boolean.parseBoolean(intTag.valueToString());
    }

    public static Tag getAsTag(Object object) {
        Class<?> type = object.getClass();

        if (object instanceof Byte) {
            return new ByteTag((byte) object);
        } else if (object instanceof Short) {
            return new ShortTag((short) object);
        } else if (object instanceof Integer) {
            return new IntTag((int) object);
        } else if (object instanceof Long) {
            return new LongTag((long) object);
        } else if (object instanceof Float) {
            return new FloatTag((float) object);
        } else if (object instanceof Double) {
            return new DoubleTag((double) object);
        } else if (object instanceof byte[]) {
            return new ByteArrayTag((byte[]) object);
        } else if (object instanceof String) {
            return new StringTag(object.toString());
        } else if (object instanceof List) {
            List<?> list = (List<?>) object;
            //TODO if I have a fricking idea how to properly do this
            return null;
        } else if (object instanceof int[]) {
            return new IntArrayTag((int[]) object);
        } else if (object instanceof long[]) {
            return new LongArrayTag((long[]) object);

        } else if (object instanceof Boolean) {
            return parseBoolean((boolean) object);
        } else {
            throw new IllegalArgumentException("Unsupported NBT type!");
        }
    }

    public static Class<? extends Tag> getTagType(Class<?> javaType) {
        if (javaType.equals(Byte.class) || javaType.equals(byte.class)) {
            return ByteTag.class;
        } else if (javaType.equals(Short.class) || javaType.equals(short.class)) {
            return ShortTag.class;
        } else if (javaType.equals(Integer.class) || javaType.equals(int.class)) {
            return IntTag.class;
        } else if (javaType.equals(Long.class) || javaType.equals(long.class)) {
            return LongTag.class;
        } else if (javaType.equals(Float.class) || javaType.equals(float.class)) {
            return FloatTag.class;
        } else if (javaType.equals(Double.class) || javaType.equals(double.class)) {
            return DoubleTag.class;
        } else if (javaType.equals(Byte[].class) || javaType.equals(byte[].class)) {
            return ByteArrayTag.class;
        } else if (javaType.equals(String.class)) {
            return StringTag.class;
        } else if (javaType.isArray() || javaType.isAssignableFrom(List.class)) {
            //TODO handle
            return ListTag.class;
        } else if (javaType.equals(Integer[].class) || javaType.equals(int[].class)) {
            return IntArrayTag.class;
        } else if (javaType.equals(Long[].class) || javaType.equals(long[].class)) {
            return LongArrayTag.class;
        } else {
            return null;
        }
    }
}
