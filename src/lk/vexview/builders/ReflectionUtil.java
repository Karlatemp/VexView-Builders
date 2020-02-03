/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ReflectionUtil.java@author: karlatemp@vip.qq.com: 2020/1/27 下午6:30@version: 2.0
 */

package lk.vexview.builders;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 别看了，内部实现不是给你用的
 *
 * @author Karlatemp
 * @since 1.0.0
 */
public class ReflectionUtil {
    @SuppressWarnings("unchecked")
    static <T> T allocate(Class<T> type) {
        return (T) allocate.apply(type);
    }

    private static final Function<Class<?>, Object> allocate;

    static {
        try {
            final MethodHandles.Lookup lk = MethodHandles.lookup();
            final Class<?> Const = Class.forName("java.lang.invoke.DirectMethodHandle$Constructor");
            final Class<?> Direct = Class.forName("java.lang.invoke.DirectMethodHandle");
            final Constructor<Object> constructor = Object.class.getConstructor();

            Field instanceClass = null;
            for (Field f : Const.getDeclaredFields()) {
                if (f.getType() == Class.class) {
                    instanceClass = f;
                    break;
                }
            }
            assert instanceClass != null;
            instanceClass.setAccessible(true);
            Field instanceClass0 = instanceClass;


            Method allocateInstance = null;
            for (Method met : Direct.getDeclaredMethods()) {
                if (met.getParameterCount() == 1) {
                    if (met.getName().equals("allocateInstance")) {
                        allocateInstance = met;
                    }
                }
            }
            assert allocateInstance != null;
            allocateInstance.setAccessible(true);
            Method allocateInstance0 = allocateInstance;


            allocate = clazz -> {
                try {
                    final MethodHandle base = lk.unreflectConstructor(constructor);
                    instanceClass0.set(base, clazz);
                    try {
                        return allocateInstance0.invoke(null, base);
                    } catch (InvocationTargetException target) {
                        throw target.getTargetException();
                    }
                } catch (RuntimeException | Error re) {
                    throw re;
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (Throwable any) {
            throw new ExceptionInInitializerError(any);
        }
    }

    private static final Map<Class<?>, Collection<BiConsumer<Object, Object>>> fields = new ConcurrentHashMap<>();

    static {
        fields.put(Object.class, Collections.emptyList());
    }

    static void register(Class<?> self, MethodHandles.Lookup lk) {
        if (fields.containsKey(self)) return;
        if (!fields.containsKey(self.getSuperclass()))
            throw new ClassCastException(self + "'s parent not registered.");
        Collection<BiConsumer<Object, Object>> fieldChanger = new ArrayDeque<>();
        fields.put(self, fieldChanger);
        for (Field f : self.getDeclaredFields()) {
            if (!Modifier.isStatic(f.getModifiers()))
                fieldChanger.add(build(f, lk));
        }
    }

    @BuildersModuleVersion("1.0.3")
    static <T, V extends T> V copyTo(T from, Class<?> start, Class<V> to) {
        if (!start.isAssignableFrom(to)) {
            throw new ClassCastException(to + " not " + start + "'s sub class");
        }
        if (!fields.containsKey(start)) {
            throw new ClassCastException(start + " is not registered in reflection util.");
        }
        V v = allocate(to);
        Class<?> matching = start;
        while (matching != null) {
            for (BiConsumer<Object, Object> copyor : fields.get(matching)) {
                copyor.accept(from, v);
            }
            matching = matching.getSuperclass();
        }
        return v;

    }

    static <T, V extends T> V copyTo(T from, Class<V> to) {
        return copyTo(from, from.getClass(), to);
    }

    private static BiConsumer<Object, Object> build(Field f, MethodHandles.Lookup lk) {
        try {
            MethodHandle getter = lk.unreflectGetter(f);
            MethodHandle setter = lk.unreflectSetter(f);
            return (from, to) -> {
                try {
                    setter.invoke(to, getter.invoke(from));
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            };
        } catch (Throwable any) {
            throw new ExceptionInInitializerError(any);
        }
    }

    /**
     * 公开方法, 快速把一个Collection转为List
     *
     * @param components 一个集合
     * @return Wrapped List
     */
    public static <T> List<T> wrappedList(Collection<T> components) {
        if (components instanceof List) {
            return (List<T>) components;
        } else {
            return new AbstractList<T>() {
                @Override
                public T get(int index) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Iterator<T> iterator() {
                    return components.iterator();
                }

                @Override
                public boolean isEmpty() {
                    return components.isEmpty();
                }

                @Override
                public int size() {
                    return components.size();
                }

                @Override
                public boolean add(T components0) {
                    return components.add(components0);
                }

                @Override
                public boolean addAll(Collection<? extends T> c) {
                    return components.addAll(c);
                }

                @Override
                public boolean remove(Object o) {
                    return components.remove(o);
                }

                @Override
                public boolean contains(Object o) {
                    return components.contains(o);
                }

                @Override
                public boolean containsAll(Collection<?> c) {
                    return components.containsAll(c);
                }

                @Override
                public boolean removeAll(Collection<?> c) {
                    return components.removeAll(c);
                }

                @Override
                public boolean removeIf(Predicate<? super T> filter) {
                    return components.removeIf(filter);
                }

                @Override
                public void clear() {
                    components.clear();
                }
            };
        }
    }
}
