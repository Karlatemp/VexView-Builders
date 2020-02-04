/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: Resource.java@author: karlatemp@vip.qq.com: 2020/2/4 下午8:42@version: 2.0
 */

package com.mcres.karlatemp.vexview.extpend.MFB;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public abstract class Resource {
    private final NamespaceKey location;
    private final ResourceLoader source;
    protected ByteBuffer cache;

    public Resource(NamespaceKey location, ResourceLoader source) {
        this.location = location;
        this.source = source;
    }

    public NamespaceKey getLocation() {
        return location;
    }

    public ByteBuffer read() throws IOException {
        if (cache != null) return cache;
        int size = getSize();
        if (size != -1) {
            if (size == 0) return cache = ByteBuffer.allocate(0);
            byte[] data = new byte[size];
            getStream().read(data);
            cache = ByteBuffer.allocateDirect(size);
            cache.put(data);
        } else {
            ByteArrayOutputStream os = new ByteArrayOutputStream(0xFFFF);
            final InputStream stream = getStream();
            byte[] buffer = new byte[1024];
            do {
                int len = stream.read(buffer);
                if (len == -1) break;
                os.write(buffer, 0, len);
            } while (true);

            cache = ByteBuffer.allocateDirect(os.size());
            cache.put(os.toByteArray());
        }
        return cache.position(0);
    }

    public abstract int getSize();

    public ResourceLoader getSource() {
        return source;
    }

    public abstract InputStream getStream() throws IOException;
}
