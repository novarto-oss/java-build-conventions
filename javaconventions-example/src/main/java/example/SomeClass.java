package example;

public class SomeClass
{

    /**
     * A very useful method
     * @return the meaning
     */
    public static int foo()
    {
        return 42;
    }


    //uncomment this and run `gradlew check` to see that checkstyle is configured with our default template
//    public void breaksCheckstyle() {
//    }


    //uncomment and run `gradlew check` with ci=true to see that fundbugs is configured

//    public int boom()
//    {
//        Integer x = null;
//        return x.hashCode();
//    }
}
