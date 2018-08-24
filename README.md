# EmptyConstructorHack
Allows you to create objects without invoking constructor

To get some code examples check test files.

```java
        AsmClassGenerator generator = new AsmClassGenerator(SafeClassDefiner.getInstance());
        EmptyConstructorCreator creator = generator.newClassInstanceManipulator(ClassWithConstructor.class);
        ClassWithConstructor o = (ClassWithConstructor) creator.newInstance();
```
And you are done. 

**Be aware that creating 'EmptyConstructorCreator' is a heavy operation and should be done only once per class.**
