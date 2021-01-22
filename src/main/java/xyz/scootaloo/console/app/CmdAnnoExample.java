package xyz.scootaloo.console.app;

import xyz.scootaloo.console.app.anno.Cmd;
import xyz.scootaloo.console.app.anno.CmdType;
import xyz.scootaloo.console.app.application.ApplicationRunner;

/**
 * Cmd注解的详细用法
 * @author flutterdash@qq.com
 * @since 2021/1/22 15:39
 */
public class CmdAnnoExample {

    public static void main(String[] args) {
        ApplicationRunner.consoleApplication().run();
    }

    /**
     * 给命令方法指定一个别名
     * 现在你可以用
     * name1 或者 name2 调用这个方法
     */
    @Cmd(name = "name2")
    private void name1() {
        System.out.println("你好");
    }

    /**
     * [初始化方法] type = CmdType.Init
     * 在 @Cmd 注解中修改type属性为 CmdType.Init ， 则这个方法会在系统装配完成所有命令后被调用。
     * 注意：
     *      - 标记为 Init 的命令不能含有参数
     *      - 当初始化方法中抛出了异常，则系统启动失败
     *      - 可以使用order注解参数决定方法的调用顺序，数字越小越优先，默认数值是5
     *      - 虽然Init方法有 @Cmd 注解，但是不能做为命令来被调用
     */
    @Cmd(type = CmdType.Init, order = 1)
    private void init1() {
        System.out.println("系统启动完成 优先级 1");
    }

    // 参见上一条，因为这里的order值是-1，所有会先被调用
    @Cmd(type = CmdType.Init, order = -1)
    private void init2() {
        System.out.println("系统启动完成 优先级 -1");
    }

    /**
     * [销毁时方法] type = CmdType.Destroy
     * 规则和Init一样，都不能有参数，返回值无所谓，跟其他Cmd方法不一样，没有地方接收这种返回值
     * 标记为 CmdType.Destroy 的方法，会在系统关闭时被调用，回收资源的代码可以写在这里，比如在程序退出时检查是否需要保存文本。
     */
    @Cmd(type = CmdType.Destroy)
    private void onExit() {
        System.out.println("程序被关闭");
    }

    /**
     * [过滤器方法] type = CmdType.Pre
     * 标记为Pre的方法，会在普通Cmd方法之前被调用，同时过滤器方法必须有一个boolean类型的返回值，
     * 当这个方法返回true，则轮到下一个过滤器处理，假如没有下一个过滤器，再才执行当前的命令
     * 提示:
     *      - 这样的多个过滤方法可以组成一个过滤链，过滤链中任意一环返回false则过滤链中断，当前命令不被执行
     *      - 可以通过order参数指定过滤链的顺序
     *      - 当过滤链返回false时，可以通过onError参数在控制台输出过滤失败的原因
     * @return 是否放行
     */
    @Cmd(type = CmdType.Pre, order = 1, onError = "过滤器没有放行的原因")
    public boolean filter1() {
        System.out.println("经过第一个过滤器");
        return true; // 尝试将true修改为false，重新运行查看命令调用情况
    }

    // 同上一条
    @Cmd(type = CmdType.Pre, order = 2)
    public boolean filter2() {
        System.out.println("经过第二个过滤器");
        return true;
    }

    /**
     * 当系统内命令方法比较多的时候，可以给这些命令方法加标签
     * 使用命令
     *      find -t [标签名]
     * 来查找带有某标签的可用命令
     * 默认标签是 usr
     *      find -t usr
     */
    @Cmd(tag = "my")
    private void myCmd() {

    }

    /**
     * 自定义某种类型的处理方式
     * 这里示例对byte类型的自定义处理方式，将byte结果进行自增1
     * 注意，方法参数是String，这个是不能变的
     * @param bt String，且只能有这一个参数
     * @return 最终类型，于targets一致，起码是返回值能向targets转换的
     */
    @Cmd(type = CmdType.Parser, targets = {byte.class, Byte.class})
    public byte resolveByte(String bt) {
        byte b = Byte.parseByte(bt);
        return ++b;
    }

    /**
     * 测试上面的功能，尝试输入
     *      tb 127
     *      tb 128
     * @param b 键盘输入的一个byte值
     */
    @Cmd(name = "tb")
    public void testByte(Byte b) {
        System.out.println(b);
    }

}
