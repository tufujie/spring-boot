/*
package com.jef.service.liteflow;

import com.jef.dao.IUserDao;
import com.jef.entity.User;
import com.jef.util.ThreadLocalUtil;
import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

*/
/**
 * @author tufujie
 * @date 2023/8/16
 *//*

@Component("serviceA")
public class ACmp extends NodeComponent {

    @Autowired
    private IUserDao userDao;

    @Override
    public void process() {
        //do your business
        // 上下文信息
        User user = ThreadLocalUtil.getThreadLocalUser();
        if (Objects.nonNull(user)) {
            System.out.println("用户信息=" + user + "；获取时的线程名称=" + Thread.currentThread().getName());
            System.out.println("从数据库中获取信息");
            User userDb = userDao.getByUser(user);
            System.out.println("根据数据库中信息去执行一些操作：用户ID=" + userDb.getId());
        }
        System.out.println("服务A业务执行完毕");
        ThreadLocalUtil.removeThreadLocalName();
    }
}
*/
