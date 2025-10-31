package minispring.examples;


import minispring.MiniApplicationContext;

public class Main {
    public static void main(String[] args) {
        MiniApplicationContext context = new MiniApplicationContext("minispring.examples");
        BeanWithDependency beanWithDependency = context.getBean(BeanWithDependency.class);
        beanWithDependency.work();
    }
}
