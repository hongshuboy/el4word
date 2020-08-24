package com.github.hongshuboy.usermodel;

public class Tuple<A, B> {
    public final A left;
    public final B right;

    public Tuple(A a, B b) {
        this.left = a;
        this.right = b;
    }
}
