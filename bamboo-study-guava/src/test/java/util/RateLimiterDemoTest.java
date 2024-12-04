package util;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.RateLimiter;
import junit.framework.TestCase;
import org.junit.Test;

public class RateLimiterDemoTest extends TestCase {
    @Test
    public void testInit() {
        RateLimiter limiter = RateLimiter.create(1);
        for(int i = 1; i < 10; i = i + 2 ) {
            double waitTime = limiter.acquire(i);
            System.out.println("cutTime=" + System.currentTimeMillis() + " acq:" + i + " waitTime:" + waitTime);
        }
    }
}