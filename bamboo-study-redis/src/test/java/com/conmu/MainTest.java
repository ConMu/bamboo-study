package com.conmu;

import junit.framework.TestCase;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisMovedDataException;

import java.io.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MainTest extends TestCase {
    private static final Logger log = LoggerFactory.getLogger(MainTest.class);
    private static int noTtl = 0;
    private static int scanCnt = 0;
    private static long lastTime = 0L;
    Executor executor;

    @Test
    public void  test1() throws InterruptedException {
        log.info("aaa");
    }
    //单实例链接测试
    @Test
    public void  testJedisSingelAAA() throws InterruptedException {
        //创建jedis对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(5);
        JedisPool jedisResourse=new JedisPool(poolConfig,"10.44.94.12",6811,2000,"d750e870db06");
        Jedis jedis = jedisResourse.getResource();
        String cursor = ScanParams.SCAN_POINTER_START;
        ScanParams params = new ScanParams().count(500).match("*13112180686*"); // 设置匹配模式为*，匹配所有key
        do {
            ScanResult<String> scanResult = jedis.scan(cursor, params);
            get(scanResult, jedis);
            cursor = scanResult.getStringCursor();
        } while (!cursor.equals("0"));
        System.out.println("no find");
        jedis.close();
    }

    @Test
    public void testJedisSingel1() throws InterruptedException {
        //done
        lastTime = System.currentTimeMillis();
        String ip = "10.44.40.35";
        int port = 6808;
        log.info("ip: {},port: {}",ip,port);
        connectAndRevmoveNottl2(ip, port, 10000, "d750e870db06");
        log.info("noTtl:{}",noTtl);
        Thread.sleep(1000000000);
    }

    @Test
    public void testJedisSingel2() throws InterruptedException {
        //done
        lastTime = System.currentTimeMillis();
        String ip = "10.44.94.15";
        int port = 6805;
        log.info("ip: {},port: {}",ip,port);
        connectAndRevmoveNottl2(ip, port, 10000, "d750e870db06");
        log.info("noTtl:{}",noTtl);
        Thread.sleep(1000000000);
    }

    @Test
    public void testJedisSingel3() throws InterruptedException {
        //done
        lastTime = System.currentTimeMillis();
        String ip = "10.44.94.15";
        int port = 6806;
        log.info("ip: {},port: {}",ip,port);
        connectAndRevmoveNottl2(ip, port, 10000, "d750e870db06");
        log.info("noTtl:{}",noTtl);
        Thread.sleep(1000000000);
    }

    @Test
    public void testJedisSingel4() throws InterruptedException {
        //done
        lastTime = System.currentTimeMillis();
        String ip = "10.44.94.15";
        int port = 6807;
        log.info("ip: {},port: {}",ip,port);
        connectAndRevmoveNottl2(ip, port, 10000, "d750e870db06");
        log.info("noTtl:{}",noTtl);
        Thread.sleep(1000000000);
    }

    @Test
    public void testJedisSingel5() throws InterruptedException {
        //done
        lastTime = System.currentTimeMillis();
        String ip = "10.44.94.14";
        int port = 6801;
        log.info("ip: {},port: {}",ip,port);
        connectAndRevmoveNottl2(ip, port, 10000, "d750e870db06");
        log.info("noTtl:{}",noTtl);
        Thread.sleep(1000000000);
    }

    @Test
    public void testJedisSingel6() throws InterruptedException {
        //done
        lastTime = System.currentTimeMillis();
        String ip = "10.44.94.14";
        int port = 6802;
        log.info("ip: {},port: {}",ip,port);
        connectAndRevmoveNottl2(ip, port, 10000, "d750e870db06");
        log.info("noTtl:{}",noTtl);
        Thread.sleep(1000000000);
    }

    @Test
    public void testJedisSingel7() throws InterruptedException {
        //done
        lastTime = System.currentTimeMillis();
        String ip = "10.44.94.16";
        int port = 6803;
        log.info("ip: {},port: {}", ip, port);
        connectAndRevmoveNottl2(ip, port, 10000, "d750e870db06");
        log.info("noTtl:{}", noTtl);
        Thread.sleep(1000000000);
    }


    @Test
    public void testJedisSingel8() throws InterruptedException {
        //done
        lastTime = System.currentTimeMillis();
        String ip = "10.44.94.16";
        int port = 6804;
        log.info("ip: {},port: {}", ip, port);
        connectAndRevmoveNottl2(ip, port, 10000, "d750e870db06");
        log.info("noTtl:{}", noTtl);
        Thread.sleep(1000000000);
    }

    @Test
    public void testJedisSingel9() throws InterruptedException {
        //done
        lastTime = System.currentTimeMillis();
        String ip = "10.44.94.12";
        int port = 6809;
        log.info("ip: {},port: {}",ip,port);
        connectAndRevmoveNottl2(ip, port, 10000, "d750e870db06");
        log.info("noTtl:{}",noTtl);
        Thread.sleep(1000000000);
    }

    @Test
    public void testJedisSingel10() throws InterruptedException {
        //done
        lastTime = System.currentTimeMillis();
        String ip = "10.44.94.12";
        int port = 6810;
        log.info("ip: {},port: {}", ip, port);
        connectAndRevmoveNottl2(ip, port, 10000, "d750e870db06");
        log.info("noTtl:{}", noTtl);
        Thread.sleep(1000000000);
    }

    @Test
    public void testJedisSingel11() throws InterruptedException {
        //done
        lastTime = System.currentTimeMillis();
        String ip = "10.44.94.12";
        int port = 6811;
        log.info("ip: {},port: {}",ip,port);
        connectAndRevmoveNottl2(ip, port, 10000, "d750e870db06");
        log.info("noTtl:{}",noTtl);
        Thread.sleep(1000000000);
    }

    @Test
    public void testJedisSingel12() throws InterruptedException {
        //done
        lastTime = System.currentTimeMillis();
        String ip = "10.44.94.12";
        int port = 6812;
        log.info("ip: {},port: {}",ip,port);
        connectAndRevmoveNottl2(ip, port, 10000, "d750e870db06");
        log.info("noTtl:{}",noTtl);
        Thread.sleep(1000000000);
    }

    @Test
    public void testX() throws InterruptedException {
        //done
        lastTime = System.currentTimeMillis();
        String ip = "10.44.94.15";
        int port = 6807;
        log.info("ip: {},port: {}",ip,port);
        connectAndRevmoveNottl3(ip, port, 10000, "d750e870db06");
        log.info("noTtl:{}",noTtl);
        Thread.sleep(1000000000);
    }

    public void connectAndRevmoveNottl2(String host, int port, int timeout, String password) throws InterruptedException {
        //创建jedis对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        JedisPool jedisResourse = new JedisPool(poolConfig, host, port, timeout, password);

        Jedis jedis = jedisResourse.getResource();
        String cursor = ScanParams.SCAN_POINTER_START;
        // 设置匹配模式为*，匹配所有key
        ScanParams params = new ScanParams().count(500);
        do {
            scanCnt++;
            log.info("{} {} scanCnt:{}", host, port, scanCnt);
            log.info("{} {} scanTime:{}", host, port, System.currentTimeMillis() - lastTime);
            log.info("========================");
            lastTime = System.currentTimeMillis();
            ScanResult<String> scanResult = jedis.scan(cursor,params);
            for (String key : scanResult.getResult()) {
                if (key.startsWith("mobile_freq") || key.startsWith("report_cache") || key.startsWith("mobile_app_freq")
                || key.startsWith("direct_user_mobile_freq") || key.startsWith("sa_m_limit_") || key.startsWith("direct_user_mobile_new_freq")
                || key.startsWith("mobile_template_new_freq") || key.startsWith("mobile_country_new_freq")) {
                    Thread.sleep(2);
                    Long ttl = jedis.ttl(key);
                    if (ttl == -1) {
                        noTtl++;
                        // key没有设置TTL，进行相应操作
                        jedis.del(key);
                        log.info("{} {} del success, noTtlCnt:{},key:{}", host, port, noTtl, key);
                    }
                }
            }
            cursor = scanResult.getStringCursor();
            log.info("{} {} cursor,{}", host, port, cursor);
            log.info("========================");
        } while (!cursor.equals("0"));
        jedis.close();
    }

    public void connectAndRevmoveNottl3(String host, int port, int timeout, String password) throws InterruptedException {
        //创建jedis对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        JedisPool jedisResourse = new JedisPool(poolConfig, host, port, timeout, password);

        Jedis jedis = jedisResourse.getResource();
        String cursor = ScanParams.SCAN_POINTER_START;
        // 设置匹配模式为*，匹配所有key
        ScanParams params = new ScanParams().count(500);
        do {
            scanCnt++;
            log.info("{} {} scanCnt:{}", host, port, scanCnt);
            log.info("{} {} scanTime:{}", host, port, System.currentTimeMillis() - lastTime);
            log.info("========================");
            lastTime = System.currentTimeMillis();
            ScanResult<String> scanResult = jedis.scan(cursor,params);
            for (String key : scanResult.getResult()) {
                Thread.sleep(2);
                Long ttl1 = jedis.ttl(key);
                if (ttl1 == -1) {
                    log.info("{} {} nottl key:{}", host, port, key);
                }
            }
            cursor = scanResult.getStringCursor();
            log.info("{} {} cursor,{}", host, port, cursor);
            log.info("========================");
        } while (!cursor.equals("0"));
        jedis.close();
    }


    public void connectAndRevmoveNottl(String host, int port, int timeout, String password, String matchPattern) {
        //创建jedis对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        JedisPool jedisResourse = new JedisPool(poolConfig, host, port, timeout, password);

        Jedis jedis = jedisResourse.getResource();
        String cursor = ScanParams.SCAN_POINTER_START;
        // 设置匹配模式为*，匹配所有key
        ScanParams params = new ScanParams().count(500).match(matchPattern);
        do {
            ScanResult<String> scanResult = jedis.scan(cursor, params);
            ttlTestAndRemove(scanResult, jedis);
            cursor = scanResult.getStringCursor();
        } while (!cursor.equals("0"));
        jedis.close();
    }

    private void ttlTestAndRemove(ScanResult<String> scanResult, Jedis jedis) {
        for (String key : scanResult.getResult()) {
            Long ttl = jedis.ttl(key);
            if (ttl == -1) {
                // key没有设置TTL，进行相应操作
                jedis.del(key);
                System.out.println("del success: " + key);
            }
        }
    }

    private static Map<String, String[]> map;
    //单实例链接测试
    @Test
    public void  testJedisSingel2XX() throws InterruptedException, IOException {
        FileInputStream fileInputStream = new FileInputStream("/Users/mucongcong/tmp/cmpptouch");
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        List<String[]> ss = new ArrayList<>();
        map = new HashMap<>();
        while ((line = reader.readLine()) != null) {
            String[] newStr = line.split("\t");
            ss.add(newStr);
            map.put(newStr[2], newStr);
        }
        for (String[] s : ss) {
            String businessid = s[0];
            String businessname = s[1];
            String cmppaccount = s[2];
//            System.out.println(businessid + "," + businessname + "," + cmppaccount);
            try {
                extracted("10.44.94.15",6808,2000,"d750e870db06",cmppaccount);
            } catch (JedisMovedDataException e) {
                HostAndPort targetNode = e.getTargetNode();
                String host = targetNode.getHost();
                int port = targetNode.getPort();
                extracted(host,port,2000,"d750e870db06", cmppaccount);
            }
        }
    }

    //单实例链接测试
    @Test
    public void  testJedisSingel4XX() throws InterruptedException, IOException {
        FileInputStream fileInputStream = new FileInputStream("/Users/mucongcong/Downloads/log-new.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        Map<String, Map<String, Integer>> map = new HashMap<>();
        String curName = "";
        int lineIndex = 0;
        Set<String> machineSet = new HashSet<>();
        while ((line = reader.readLine()) != null) {
            lineIndex++;
            String[] newStr = line.split(":",2);
            String machineName = newStr[0];
            String content = newStr[1];
            if (curName.equals(machineName)) {
                //正在处理的机器
                if (content.contains("Disk")) {
                    //todo 处理disk
                    String line2;
                    while ((line2 = reader.readLine()) != null) {
                        lineIndex++;
                        String[] newStr2 = line2.split(":", 2);
                        String machineName2 = newStr2[0];
                        String content2 = newStr2[1];
                        if (machineName2.equals(curName)) {
                            if (content2.contains("RAID") || content2.contains("raid")) {
                                continue;
                            }
                            if (content2.contains("SSD") || content2.contains("HDD")) {
                                String[] split = content2.trim().split(" |:");
                                String prefix = "";
                                for (String s : split) {
                                    if (s.contains("SSD") || s.contains("HDD")) {
                                        prefix = s;
                                        break;
                                    }
                                }
                                try {
                                    String s = split[2];
                                    String unit = split[3];
                                    String size = prefix + "\t" + s + unit;
                                    if (map.containsKey(curName)) {
                                        Map<String, Integer> mapp = map.get(curName);
                                        if (mapp.containsKey(size)) {
                                            mapp.put(size, mapp.get(size) + 1);
                                        } else {
                                            mapp.put(size, 1);
                                        }
                                    } else {
                                        Map<String, Integer> sizeMap = new HashMap<>();
                                        sizeMap.put(size, 1);
                                        map.put(curName, sizeMap);
                                    }
                                } catch (Exception e) {
                                    System.out.println("error, " + lineIndex + e.getMessage());
                                    break;
                                }
                            }

                        } else {
                            // 跳出
                            break;
                        }
                    }
                }
            } else {
                curName = machineName;
                machineSet.add(curName);
            }
         }
        for (Map.Entry<String, Map<String, Integer>> stringMapEntry : map.entrySet()) {
            Map<String, Integer> value = stringMapEntry.getValue();
            for (Map.Entry<String, Integer> stringIntegerEntry : value.entrySet()) {
                System.out.println(stringMapEntry.getKey() + "\t" + stringIntegerEntry.getKey() + "\t" + stringIntegerEntry.getValue());
            }
        }
        System.out.println("machineNum " + machineSet.size());
        System.out.println("mapSize " + map.size());
        for (String s : machineSet) {
            if (!map.containsKey(s)) {
                System.out.println("error parse\t" + s);
            }
        }
    }

    private static void extracted(String host, int port, int timeout, String secret, String cmppaccount) {
        //创建jedis对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        JedisPool jedisResourse=new JedisPool(poolConfig,host,port,timeout,secret);
        Jedis jedis = jedisResourse.getResource();
        String key = "sms_gateway_conn|" + cmppaccount;
//        String type = jedis.type(key);
//        System.out.println(type);
        Set<String> zrange = jedis.zrange(key, 0, -1);

        String string = Arrays.toString(zrange.toArray());
        if (string.contains("10.59")) {

        } else if (string.contains("10.189")) {
            System.out.println(Arrays.toString(zrange.toArray()));
//            System.out.println(cmppaccount);
            String[] strings = map.get(cmppaccount);
            System.out.println(strings[0] + "," + strings[1] + "," + cmppaccount);
            System.out.println("=======================");
        } else {
//            System.out.println("未连接");
        }

        jedis.close();
    }

    public void get(ScanResult<String> scanResult, Jedis jedis) throws InterruptedException {
        for (String key : scanResult.getResult()) {
//            System.out.println(jedis.get(key));
            Long ttl = jedis.ttl(key);
            Thread.sleep(2);
            if (ttl == -1) {
                // key没有设置TTL，进行相应操作
                System.out.println(key);
            }
        }
    }

    public void ttlTest(ScanResult<String> scanResult, Jedis jedis) throws InterruptedException {
        for (String key : scanResult.getResult()) {
//            System.out.println(jedis.get(key));
            Long ttl = jedis.ttl(key);
            Thread.sleep(2);
            if (ttl == -1) {
                // key没有设置TTL，进行相应操作
                System.out.println(key);
//                jedis.del(key);
            }
        }
    }
    @Test
    public void testJedisCluster() throws InterruptedException {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        JedisPool jedisResourse = new JedisPool(poolConfig, "10.189.27.58", 6380,2000);

        Jedis jedis = jedisResourse.getResource();
        String cursor = ScanParams.SCAN_POINTER_START;
        // 设置匹配模式为*，匹配所有key
        ScanParams params = new ScanParams().count(100000).match("*slo_collect_cluster_info_nim-app_online*");
        do {
            ScanResult<String> scanResult = jedis.scan(cursor, params);
            for (String key : scanResult.getResult()) {
                System.out.println(key);
                String s;
                try {
                    s = jedis.get(key);
                }catch (JedisMovedDataException e) {
                    HostAndPort targetNode = e.getTargetNode();
                    String host = targetNode.getHost();
                    int port = targetNode.getPort();
                    //创建jedis对象
                    JedisPoolConfig poolConfig2 = new JedisPoolConfig();
                    poolConfig2.setMaxTotal(20);
                    JedisPool jedisResourse2=new JedisPool(poolConfig2,host,port,2000);
                    Jedis jedis2 = jedisResourse2.getResource();
                    s = jedis2.get(key);
                    jedis2.close();
                }
                System.out.println(s);
                System.out.println("=======================");
            }
            cursor = scanResult.getStringCursor();
        } while (!cursor.equals("0"));
        jedis.close();
    }

    @Test
    public void testJedisCluster1() throws InterruptedException {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);

        JedisPool jedisResourse = new JedisPool(poolConfig, "10.59.133.81", 6380,2000,"075544cb3996");

        Jedis jedis = jedisResourse.getResource();
        // mobile_freq|min|58858|+86-13112180686
        // direct_user_mobile_new_freq|day|1|58858|+86-13112180686
        String ss = "mobile_freq|min|27969|+86-15810496693";
        String s;
        try {
            s = jedis.get(ss);
        } catch (JedisMovedDataException e) {
            jedis.close();
            HostAndPort targetNode = e.getTargetNode();
            String host = targetNode.getHost();
            int port = targetNode.getPort();
            jedisResourse = new JedisPool(poolConfig, host, port,2000,"075544cb3996");
            jedis = jedisResourse.getResource();
            s = jedis.get(ss);
        }
        System.out.println(s);
        jedis.close();
    }


    /**
     * zrange
     **/
    private final static String CMPP_CONNECT_KEY = "sms_gateway_conn|";
    private String buildCmppKey(String cmppCount) {
        return CMPP_CONNECT_KEY + cmppCount;
    }

    private final static String OLD_JEDIS_IP = "10.44.94.15";
    private final static int OLD_JEDIS_PORT = 6805;
    private final static String NEW_JEDIS_IP = "10.44.85.12";
    private final static int NEW_JEDIS_PORT = 6405;

    private final static String CMPPACCOUNT = "aaa";
    @Test
    public void testRedisRecordEqual() throws InterruptedException {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);

        JedisPool jedisResourse = new JedisPool(poolConfig, "OLD_JEDIS_IP", OLD_JEDIS_PORT,2000,"d750e870db06");

        Jedis jedis = jedisResourse.getResource();
        String cmppKey = buildCmppKey(CMPPACCOUNT);
        Set<String> zrange;
        try {
            zrange = jedis.zrange(cmppKey, 0, -1);
        } catch (JedisMovedDataException e) {
            jedis.close();
            HostAndPort targetNode = e.getTargetNode();
            String host = targetNode.getHost();
            int port = targetNode.getPort();
            jedisResourse = new JedisPool(poolConfig, host, port,2000,"075544cb3996");
            jedis = jedisResourse.getResource();
            zrange = jedis.zrange(cmppKey, 0, -1);
        }
        jedis.close();
        for (String s : zrange) {
            System.out.println(s);
        }
    }
}