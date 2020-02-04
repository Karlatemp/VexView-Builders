/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ResourceLoader.java@author: karlatemp@vip.qq.com: 2020/2/4 下午8:42@version: 2.0
 */

package com.mcres.karlatemp.vexview.extpend.MFB;

public interface ResourceLoader {
    String getType();

    String getPrefix();

    Resource getResource(NamespaceKey loc);

    default String getPath(NamespaceKey key) {
        StringBuilder sb = new StringBuilder(getPrefix()).append(key.getNamespace());
        String type = getType();
        if (type != null) sb.append('/').append(type);
        return sb.append('/').append(key.getKey()).toString();
    }
}
