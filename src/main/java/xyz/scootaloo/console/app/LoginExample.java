package xyz.scootaloo.console.app;

import xyz.scootaloo.console.app.anno.*;
import xyz.scootaloo.console.app.application.ApplicationRunner;
import xyz.scootaloo.console.app.common.DefaultConsole;
import xyz.scootaloo.console.app.listener.AppListenerAdapter;
import xyz.scootaloo.console.app.parser.AssemblyFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 简易的登陆注册功能
 * @author flutterdash@qq.com
 * @since 2021/1/22 16:50
 */
public class LoginExample extends DefaultConsole implements AppListenerAdapter {

    private final Map<String, User> userMap; // 用户map, key=用户名, value=密码
    private String curCmd; // 当前执行的命令
    private boolean hasLogin = false; // 当前是否登陆了
    private Set<String> allowCmdSet; // 使用一个集合保存放行的命令

    public static void main(String[] args) {
        ApplicationRunner.consoleApplication().run();
    }

    // 提供public的无参构造方法
    public LoginExample() {
        userMap = new HashMap<>();
    }

    @Cmd(type = CmdType.Init)
    private void init() {
        allowCmdSet = AssemblyFactory.getSysCommands();
        allowCmdSet.add("login");    allowCmdSet.add("log");
        allowCmdSet.add("register"); allowCmdSet.add("reg");
    }

    // 在系统启动后载入用户数据
    @Cmd(type = CmdType.Init)
    private void loadUsers() {
        // 加载用户数据，可以修改成其他实现方式，例如从数据库中或者文件中获取
        // 这里向map中预先存储3个用户
        userMap.put("user1", new User("user1", "pwd"));
        userMap.put("admin", new User("admin", "admin"));
        userMap.put("test", new User("test", "admin"));
    }

    // 检查用户当前的操作是否被运行
    @Cmd(type = CmdType.Pre,onError = "未登陆，无法执行此操作")
    private boolean checkLogin() {
        // 当前用户已登陆，放行； 当前用户访问允许的命令，放行
        return this.hasLogin || allowCmdSet.contains(this.curCmd);
    }

    // 简易的登陆判断
    @Cmd(name = "log")
    private void login(User user) {
        User userInfo = userMap.get(user.username);
        if (userInfo == null) {
            println("用户名不存在");
        } else {
            if (userInfo.password.equals(user.password)) {
                this.hasLogin = true;
                println("登陆成功");
            } else {
                println("密码错误!");
            }
        }
    }

    // 简易的注册功能
    @Cmd(name = "reg")
    private void register(User user) {
        User userInfo = userMap.get(user.username);
        if (userInfo != null) {
            println("用户名已存在");
        } else {
            userMap.put(user.username, user);
            println("注册成功!");
        }
    }

    // 退出登陆
    @Cmd
    private void logout() {
        hasLogin = false;
        println("退出登陆状态");
    }

    //------------------启用监听器，辅助登陆检查功能实现-----------------------

    @Override
    public String getName() {
        return "loginLis";
    }

    @Override
    public boolean accept(Moment moment) {
        return Moment.OnInput == moment; // 监听用户输入的命令
    }

    @Override
    public String onInput(String cmdline) {
        // 在用户输入之后进行拦截，此方法将在过滤器之前执行，保存用户当前执行的命令名
        String[] items = cmdline.split(" ");
        this.curCmd = items.length == 0 ? null : items[0];
        return cmdline;
    }

    //---------------------------表单---------------------------------

    @Form
    private static class User {

        @Prop(prompt = "输入用户名", isRequired = true)
        private String username;

        @Prop(prompt = "输入密 码", isRequired = true)
        private String password;

        public User() {
        }

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

    }

}
