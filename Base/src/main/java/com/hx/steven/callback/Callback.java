package com.hx.steven.callback;


public interface Callback<T,V> {
    void success(T t);
    void failure(V v);
}