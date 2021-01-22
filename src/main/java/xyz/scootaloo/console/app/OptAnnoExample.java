package xyz.scootaloo.console.app;

import xyz.scootaloo.console.app.anno.Cmd;
import xyz.scootaloo.console.app.anno.Opt;
import xyz.scootaloo.console.app.application.ApplicationRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * - @Opt 注解的详细使用方法
 * @author flutterdash@qq.com
 * @since 2021/1/22 14:22
 */
public class OptAnnoExample {

    public static void main(String[] args) {
        ApplicationRunner.consoleApplication().run();
    }

    /**
     * 用法一
     * 使用 -参数名 参数值 的方式调用
     * -@Opt注解的属性 value 就是这个简写的参数，例如这个方法，可以这样传参
     *      opt -a true -c true
     * 相较于不带 @Opt 注解的方式，带注解时可以随意调换参数的顺序，例如这样
     *      opt -c true -a true
     * 上面两个例子，也许你注意到了，这是带有注解的另外一个功能，参数是可选的，上面两条命令都没有给参数b赋值，
     *      所以b的默认值是false。 数值类型未选中时为0, 布尔类型是false, 引用类型为null
     * 另外，带注解的时候，还有一种传参方式，即按照顺序传参
     *      opt true  等于调用  opt(true, false, false)
     *      opt true true 等于调用 opt(true, true, true)
     *      以此类推，这是按照顺序给方法参数赋值的示例，只对有@Opt注解的方法参数有效
     * 为了简化布尔类型的参数调用方式，可以这样调用
     *      opt -a -b
     *      这条命令与 opt -a true -b true 效果相同
     *      还可以这样:  opt -ab
     *      这种方式只对布尔类型有效
     * 一条命令中，各种赋值方式都是可以混合出现的，比如
     *      opt -ac -b true
     */
    @Cmd
    public void opt(@Opt('a') boolean a, @Opt('b') boolean b, @Opt('c') boolean c) {
        List<Character> options = new ArrayList<>();
        if (a) options.add('a');
        if (b) options.add('b');
        if (c) options.add('c');
        System.out.println("选中: " + options);
    }

    /**
     * 默认值功能，通过dftVal属性实现
     * 尝试输入命令
     *      dft 90
     * 或者
     *      dft
     * 观察控制台的输出
     */
    @Cmd
    public void dft(@Opt(value = 'n', dftVal = "-3") int num) {
        System.out.println("num: " + num);
    }

    /**
     * 参数的全称，使用 fullName 属性实现
     * 有时候希望使用一个完整的参数名来给方法参数赋值
     * 例如下面这个方法，尝试在控制台输入
     *      name -s jack
     *      name --name jack
     *      name jack
     * 控制台输出的结果一致
     */
    @Cmd
    public void name(@Opt(value = 's', fullName = "name") String name) {
        System.out.println(name);
    }

    /**
     * 指定必选项
     * 假如需要让方法选中了某参数才能进行调用
     * 只需要将  required 属性标记为 true
     * 意思是只有含有此参数，方法才能被调用
     * 尝试一下此参数缺省的效果
     *      req -a 18
     *      req --age 19
     */
    @Cmd
    public void req(@Opt(value = 'n', required = true) String name, @Opt(value = 'a', fullName = "age") int age) {
        System.out.println(name + ", " + age);
    }

    /**
     * 最后一个功能，
     * 当前命令行参数名和参数值都是按照空格分隔的，但是有时候传进来的参数需要是一句话，里面含有空格，
     * 为了得到这句完整的话，需要使用 joint 这个属性
     * 一般这个属性放在方法参数的最后面，在输入命令行的时候，从这个系统将这个参数的参数值全部拼接起来合成一个参数
     * 尝试输入
     *      s I never getup early  on   Sundays.
     * 观察 how 这个属性，所有空格都原样保留下来了.
     */
    @Cmd
    public void s(@Opt(value = 'x', fullName = "who") String who,
                  @Opt(value = 'v', fullName = "action") String action,
                  @Opt(value = 'h',fullName = "what") String what,
                  @Opt(value = 't', fullName = "how", joint = true) String how) {
        System.out.println("主语: " + who);
        System.out.println("谓语: " + action);
        System.out.println("宾语: " + what);
        System.out.println("状语: \"" + how + "\"");
    }

    /**
     * 有了这两个功能以后，下面写一个方法来模拟Docker的run的命令
     *
     * ----------------测试命令----------------
     * run bootshiro --name bootshiro
     * run --name bootshiro -dit -v /data:/home/usr -p 8080:80 bootshiro
     * run --name bootshiro -it -v /data:/home/usr -p 8080:80 bootshiro shell
     * --------------------------------------
     */
    @Cmd
    private void run(String imageName,
                     @Opt('d') boolean d, @Opt('i') boolean i, @Opt('t') boolean t,
                     @Opt(value = 'n', fullName = "name") String name,
                     @Opt('v') String pathMapping, @Opt('p') String portMapping,
                     @Opt(value = '*', dftVal = "/bin/bash") String interpreter) {

        List<Character> options = new ArrayList<>();
        if (d) options.add('d');
        if (i) options.add('i');
        if (t) options.add('t');

        System.out.println("镜像名称 => " + imageName);
        System.out.println("参数    => " + options);
        System.out.println("容器名称 => " + name);
        System.out.println("路径映射 => " + pathMapping);
        System.out.println("端口映射 => " + portMapping);
        System.out.println("解释器  => " + interpreter);
    }

}
