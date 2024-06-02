package com.lubase.core.right;



public  enum ETest {
   b(1),
    a(4);
    private int index;

    ETest(int index) {
        this.index = index;
    }

    public static ETest fromIndex(int index) {
        if(index==4) return  ETest.a;
        else         return ETest.b;
    }
}
