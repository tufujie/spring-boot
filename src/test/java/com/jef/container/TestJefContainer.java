package com.jef.container;

import com.jef.service.IUserService;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author tufujie
 * @date 2023/9/20
 */
@ActiveProfiles({"integrated", "dev"})
@SpringBootTest(classes = TestApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class TestJefContainer {

    @Autowired
    IUserService userService;

/*    @Autowired
    RedisTemplate<String, String> redisTemplate;*/

  /*  @Test
    @DisplayName("测试通过数据库获取用户信息")
    public void testGetUserListOfMySQL() {
        List<User> userList = userService.getAllUser();
        System.out.println(userList);
    }*/

   /* @Test
    @DisplayName("测试通过Redis设置并获取数据")
    public void testGetDataByREdis() {
        String key = "test", value = CommonConstant.NAME;
        redisTemplate.opsForValue().set(key, value);
        String redisValue = redisTemplate.opsForValue().get(key);
        Assertions.assertEquals(value, redisValue);
    }*/
}