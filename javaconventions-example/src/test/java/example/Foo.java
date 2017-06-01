package example;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Foo
{
    @Test
    public void foo()
    {
        System.out.println("hi from test");
        assertThat(SomeClass.foo(), is(42));
    }
}
