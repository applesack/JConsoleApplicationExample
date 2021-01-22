package xyz.scootaloo.console.app;

import xyz.scootaloo.console.app.anno.Cmd;
import xyz.scootaloo.console.app.application.ApplicationRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 快速开始
 * @author flutterdash@qq.com
 * @since 2021/1/22 14:01
 */
public class QuickStart {

    // 最快捷的运行方式，直接用以下一行代码启动
    // 默认以当前类做为命令工厂
    // 默认使用 resource 目录下的 console.yml 做为配置
    public static void main(String[] args) {
        ApplicationRunner.consoleApplication().run();
    }

    /**
     * 使用 @Cmd 注解注册一个命令方法到系统
     * 尝试在控制台输入
     *      add 1 2
     *      add 13 90
     * @param a -
     * @param b -
     */
    @Cmd
    private int add(int a, int b) {
        System.out.println(a + b);
        return a + b;
    }

    /**
     * 方法参数中可以使用集合，对于数组，建议使用基本类型的包装类，否则会引发异常
     * 尝试输入
     *      g 1,2,4,3 1.1,1.2,1.3 11,12,11,13
     * 所有数值都被正确处理了，这里参数之间还是使用空格隔开，用逗号分割集合参数的各个数据项
     * 目前支持 数组array 列表List 集Set
     * List 实现是 ArrayList
     * Set  实现是 LinkedHashSet
     * : 后续可能会增加对Queue和Map的支持
     * @param arr -
     * @param floats -
     * @param set -
     */
    @Cmd
    private void g(Integer[] arr, List<Float> floats, Set<String> set) {
        System.out.println(Arrays.toString(arr));
        System.out.println(floats);
        System.out.println(set);
    }

}
