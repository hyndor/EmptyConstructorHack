package ru.hyndo.emptyconstructorhack;

import org.junit.*;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.Writer;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class AsmClassGeneratorTest {


    private static ClassDefiner classDefiner;
    private boolean finalized;

    @BeforeClass
    public static void setUp() throws Exception {
        classDefiner = SafeClassDefiner.getInstance();
    }

    @Test
    public void checkInstanceCreation_WithoutCallingConstructor() {
        AsmClassGenerator generator = new AsmClassGenerator(classDefiner);
        EmptyConstructorCreator creator = generator.newClassInstanceManipulator(ClassWithConstructor.class);
        ClassWithConstructor obj = (ClassWithConstructor) creator.newInstance();
        assertNotNull(obj);
        assertNull(obj.getB());
        assertNull(obj.getA());
        obj.setB("test1");
        obj.setA("test12");
        assertEquals("test1", obj.getB());
        assertEquals("test12", obj.getA());
        obj.setB("newB");
        assertEquals("newB", obj.getB());
    }

    @Test
    public void finalize() throws Exception {
        EmptyConstructorCreator creator = new AsmClassGenerator(classDefiner).newClassInstanceManipulator(ClassWithConstructor.class);
        for (int i = 0; i < 100; i++) {
            ClassWithConstructor o = (ClassWithConstructor) creator.newInstance();
            o.setPrintWriter(() -> this.finalized = true);
            o = null;
            System.gc();
        }
        assertTrue(finalized);
    }

    private static class TestPrintWriter extends PrintWriter {

        public boolean written;

        @SuppressWarnings("ConstantConditions")
        public TestPrintWriter() {
            super((Writer) null);
        }

        @Override
        public void print(String s) {
            written = true;
        }
    }

    @SuppressWarnings("UnusedAssignment")
    @Test
    public void checkInstancesCreatedWithoutConstructor_CanBeGarbageCollected() throws Exception {
        EmptyConstructorCreator creator = new AsmClassGenerator(classDefiner).newClassInstanceManipulator(ClassWithConstructor.class);
        ReferenceQueue<ClassWithConstructor> queue = new ReferenceQueue<>();
        ClassWithConstructor obj = (ClassWithConstructor) creator.newInstance();
        PhantomReference<ClassWithConstructor> phantomReference = new PhantomReference<>(obj, queue);
        obj = null;
        System.gc();
        Reference<? extends ClassWithConstructor> removed = queue.remove(TimeUnit.SECONDS.toMillis(5));
        assertNotNull(removed);
    }

}