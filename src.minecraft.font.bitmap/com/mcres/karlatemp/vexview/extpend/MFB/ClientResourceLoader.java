/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ClientResourceLoader.java@author: karlatemp@vip.qq.com: 2020/2/4 下午8:46@version: 2.0
 */

package com.mcres.karlatemp.vexview.extpend.MFB;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ClientResourceLoader implements ResourceLoader {
    private final ZipFile zip;
    private final String type;
    private final String prefix;

    @Override
    public String getType() {
        return type;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public Resource getResource(NamespaceKey entry) {
        final ZipEntry entry1 = zip.getEntry(getPath(entry));
        if (entry1 != null)
            return new Resource(entry, this) {
                @Override
                public int getSize() {
                    return (int) entry1.getSize();
                }

                @Override
                public InputStream getStream() throws IOException {
                    return zip.getInputStream(entry1);
                }
            };
        return null;
    }

    public ClientResourceLoader(ZipFile zip, String type, String prefix) {
        this.zip = zip;
        this.type = type;
        this.prefix = prefix;
    }

}
