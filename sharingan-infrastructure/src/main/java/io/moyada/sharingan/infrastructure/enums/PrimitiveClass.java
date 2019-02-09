package io.moyada.sharingan.infrastructure.enums;

/**
 * 基础类型
 */
public enum PrimitiveClass {

    VOID("void", void.class),
    INT("int", int.class),
    SHORT("short", short.class),
    LONG("long", long.class),
    FLOAT("float", float.class),
    DOUBLE("double", double.class),
    BYTE("byte", byte.class),
    CHAR("char", char.class),
    BOOLEAN("boolean", boolean.class),
    ;

    private String name;
    private Class<?> clazz;

    PrimitiveClass(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public static Class<?> forName(String className) {
        for (PrimitiveClass clazz : PrimitiveClass.values()) {
            if (clazz.name.equals(className)) {
                return clazz.clazz;
            }
        }

        return null;
    }
}
