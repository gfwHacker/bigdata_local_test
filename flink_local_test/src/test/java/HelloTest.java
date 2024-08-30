import org.example.Hello;
import org.junit.Test;

public class HelloTest {
    @Test
    public void test() {
        Hello hello = new Hello();
        String result = hello.hi("张三");
        //断言
        assert "Hello 张三".equals(result);
    }
}
