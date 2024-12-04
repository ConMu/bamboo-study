package util;

import com.google.common.base.Optional;
import junit.framework.TestCase;
import org.junit.Test;


public class OptionalDemoTest extends TestCase {

    @Test
    public void testInit() {
        Optional<Integer> possible = Optional.of(5);
        System.out.println(possible.isPresent()); // returns true

        System.out.println(possible.get()); // returns 5

    }


    @Test
    public void testInitForm() {
        // 将一个T的实例转换为Optional对象，T的实例可以不为空，也可以为空
        Optional<Integer> possible = Optional.fromNullable(null);
        System.out.println(possible.isPresent()); // returns true

        Optional<Integer> possible2 = Optional.fromNullable(2);
        System.out.println(possible2.isPresent()); // returns true
    }
}