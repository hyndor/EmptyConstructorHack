package ru.hyndo.emptyconstructorhack;

import java.io.PrintWriter;
import java.util.function.Supplier;

public class ClassWithConstructor {

    private String b;
    private String a;
    private Runnable finalizationRunnable;

    public ClassWithConstructor(String b, String a) {
        this.b = b;
        this.a = a;
    }

    public void setPrintWriter(Runnable finalizationRunnable) {
        this.finalizationRunnable = finalizationRunnable;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    @Override
    protected void finalize() throws Throwable {
        if(finalizationRunnable != null) {
            finalizationRunnable.run();
        }
    }
}
