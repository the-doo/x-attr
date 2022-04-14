package com.doo.xattr.interfaces;

public interface Expirable {

    void expired(long remaining);

    boolean isExpired();
}
