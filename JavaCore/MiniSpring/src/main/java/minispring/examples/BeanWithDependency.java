package minispring.examples;

import minispring.annotation.Autowired;
import minispring.annotation.Component;
import minispring.lifecycle.InitializingBean;

@Component
public class BeanWithDependency implements InitializingBean {
    @Autowired
    private SomeBean someBean;

    public void work() {
        someBean.work();
        System.out.println("BeanWithDependency is working");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("BeanWithDependency initialized");
    }
}
