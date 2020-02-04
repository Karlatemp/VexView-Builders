/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: NamespaceKey.java@author: karlatemp@vip.qq.com: 2020/2/4 下午8:43@version: 2.0
 */

package com.mcres.karlatemp.vexview.extpend.MFB;

import java.util.Objects;

public class NamespaceKey {
    private final String namespace;
    private final String key;

    public String getKey() {
        return key;
    }

    public String getNamespace() {
        return namespace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NamespaceKey that = (NamespaceKey) o;

        if (!Objects.equals(namespace, that.namespace)) return false;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        int result = namespace != null ? namespace.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        return result;
    }

    public NamespaceKey(String namespace, String key) {
        this.namespace = namespace;
        this.key = key;
    }

    public static NamespaceKey parse(String key) {
        int i = key.indexOf(':');
        if (i == -1) {
            return new NamespaceKey("minecraft", key);
        }
        if (key.lastIndexOf(':') != i) throw new IllegalArgumentException(key);
        return new NamespaceKey(key.substring(0, i), key.substring(i + 1));
    }
}
