/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: WrappedPropertyMap.java@author: karlatemp@vip.qq.com: 2020/2/2 上午12:55@version: 2.0
 */

package lk.vexview.gameprofile;

import com.google.common.collect.ForwardingMultimap;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * 映射的Properties
 *
 * @author Karlatemp
 * @since 2.6
 */
public class WrappedPropertyMap {
    private ForwardingMultimap<String, Object> PropertyMap;
    WrappedGameProfile profile;

    public String getProperty(String key) {
        Object prop = PropertyMap.get(key);
        if (prop == null) return null;
        try {
            return (String) WrappedGameProfile.Property_getValue.invoke(prop);
        } catch (IllegalAccessException e) {
            throw new InternalError(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        }
    }

    @SuppressWarnings("unchecked")
    public WrappedPropertyMap(ForwardingMultimap<String, ?> PropertyMap) {
        this.PropertyMap = (ForwardingMultimap<String, Object>) PropertyMap;
    }

    @SuppressWarnings("unchecked")
    public WrappedPropertyMap(WrappedGameProfile profile) {
        try {
            PropertyMap = (ForwardingMultimap<String, Object>) WrappedGameProfile.GameProfile_getProperties_LPropertyMap.invoke(profile);
        } catch (IllegalAccessException e) {
            throw new InternalError(e);
        } catch (InvocationTargetException e) {
            throw new InternalError(e.getTargetException());
        }
        this.profile = profile;
    }

    public String getSignature(String key) {
        Object prop = PropertyMap.get(key);
        if (prop == null) return null;
        try {
            return (String) WrappedGameProfile.Property_getSign.invoke(prop);
        } catch (IllegalAccessException e) {
            throw new InternalError(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        }
    }

    public WrappedPropertyMap put(String key, String value, String signature) {
        try {
            PropertyMap.put(key, WrappedGameProfile.Property_init_name_value_sign.newInstance(key, value, signature));
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InternalError(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        }
        return this;
    }

    public WrappedGameProfile getProfile() {
        return profile;
    }

    public Set<String> keys() {
        return PropertyMap.keySet();
    }
}
