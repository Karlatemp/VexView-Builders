/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: WrappedGameProfile.java@author: karlatemp@vip.qq.com: 2020/2/2 上午12:48@version: 2.0
 */

package lk.vexview.gameprofile;

import com.google.common.collect.ForwardingMultimap;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;

/**
 * 映射的GameProfile
 *
 * @author Karlatemp
 * @since 2.6
 */
public class WrappedGameProfile {
    static Class<?> GameProfile, Property;
    static Constructor<?> GameProfile_init_Ljava_util_UUID_Ljava_util_String,
            Property_init_name_value_sign;
    static Method GameProfile_getProperties_LPropertyMap,
            Property_getSign, Property_getValue,
            GP_getUniqueId, Gp_getName;
    Object profile;
    WrappedPropertyMap propertyMap;

    static {
        try {
            Class<?> GP;
            Class<?> P;
            try {
                GP = Class.forName("com.mojang.authlib.GameProfile");
                P = Class.forName("com.mojang.authlib.properties.Property");
            } catch (ClassNotFoundException notFound) {
                GP = Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
                P = Class.forName("net.minecraft.util.com.mojang.authlib.properties.Property");
            }

            Property = P;
            (Property_init_name_value_sign = P.getConstructor(String.class, String.class, String.class)).setAccessible(true);
            GameProfile = GP;
            (GameProfile_init_Ljava_util_UUID_Ljava_util_String = GP.getConstructor(UUID.class, String.class)).setAccessible(true);
            (GameProfile_getProperties_LPropertyMap = GP.getDeclaredMethod("getProperties")).setAccessible(true);
            (Property_getValue = P.getDeclaredMethod("getValue")).setAccessible(true);
            (Property_getSign = P.getDeclaredMethod("getSignature")).setAccessible(true);
            (GP_getUniqueId = GP.getDeclaredMethod("getId")).setAccessible(true);
            (Gp_getName = GP.getDeclaredMethod("getName")).setAccessible(true);
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public WrappedGameProfile(UUID uuid, String name) {
        try {
            profile = GameProfile_init_Ljava_util_UUID_Ljava_util_String.newInstance(uuid, name);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        init();
    }

    private void init() {
        propertyMap = new WrappedPropertyMap(this);
    }

    public WrappedGameProfile(Object profile) {
        Objects.requireNonNull(profile, "profile");
        GameProfile.cast(profile);
        this.profile = profile;
        init();
    }

    public WrappedPropertyMap getPropertyMap() {
        return propertyMap;
    }

    public UUID getId() {
        try {
            return (UUID) GP_getUniqueId.invoke(profile);
        } catch (IllegalAccessException e) {
            throw new InternalError(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        }
    }

    public Object getProfile() {
        return profile;
    }

    public String getName() {
        try {
            return (String) Gp_getName.invoke(profile);
        } catch (IllegalAccessException e) {
            throw new InternalError(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        }
    }
}
