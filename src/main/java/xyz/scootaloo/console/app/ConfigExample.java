package xyz.scootaloo.console.app;

import xyz.scootaloo.console.app.application.ApplicationRunner;
import xyz.scootaloo.console.app.config.ConsoleConfig;
import xyz.scootaloo.console.app.parser.preset.SimpleParameterParser;

/**
 * 配置方式
 * @author flutterdash@qq.com
 * @since 2021/1/22 16:19
 */
public class ConfigExample {

    public static void main(String[] args) {
        // 完整的java配置方式获取配置，现在不推荐这样做
        ConsoleConfig config1 = Console.config()
                        // 应用信息
                        .appName("测试应用示例")   // 应用的名称
                        .printWelcome(true)     // 是否打印欢迎信息
                        .prompt("example> ")    // 控制台输入的提示符
                        .printStackTrace(true)  // 遇到异常时是否打印调用栈
                        .exitCmd(new String[] {"exit", "e.", "q"}) // 使用这些命令可以退出应用
                        .maxHistory(128) // 最多保存的历史记录数量
                        .enableVariableFunction(true) // 开启变量功能，get set命令可用，占位符功能可用
                        // 编辑作者信息，当printWelcome设置为false时，这些信息不会被输出
                        .editAuthorInfo()
                            .authorName("fd")
                            .email("~~")
                            .comment("备注: ~~")
                            .createDate("2020/12/27")
                            .updateDate("2021/1/22")
                            .ok()
                        // 设置系统启动时执行的命令
                        .addInitCommands()
                            .getFromFile("init.txt") // 从文件中读取
                            .add("find --tag usr")   // 查询所有的用户命令
                            .add("help --name help") // 获取 help 命令的使用帮助
                        .ok()
                        // 增加命令工厂，enable参数决定是否启用该命令工厂，将false修改为true可以开启对应命令工厂的测试
                        .addCommandFactories()
                            .add(QuickStart.class, true)
                            .add(ConfigExample::new, true)
                        .ok()
                        // 添加自定义的解析器实现，注意，下面这条 "raw" 无论是否设置，都会被载入系统，这里只是演示如何扩展解析器功能
                        .addParameterParser()
                            .addParser("raw", SimpleParameterParser.INSTANCE) // 现在可以用"raw"这个解析器了
                            .ok()
                        // 设置帮助工厂
                        .addHelpDoc(HelpForDemo.INSTANCE)
                        .setConfFile("") // 使用这种配置方式时，假如resource目录下有console.yml文件时，这一步是必须的，否则之前的配置会被配置文件的内容覆盖
                        // 设置完成
                        .build();

        // 推荐使用这种方式，这里只需要添加各种工厂，其余配置放在配置文件中
        ConsoleConfig config2 = Console.factories()
                                    .addFactories()
                                        .add(QuickStart.class)
                                        .add(ConfigExample.class)
                                        .add(SimpleParameterParser.INSTANCE, true)
                                        .add(HelpForDemo.INSTANCE, true)
                                        .ok()
                                    .setConfFile("console.yml") // 设置配置文件，默认是console.yml
                                    .build();
        // 使用配置运行
        ApplicationRunner.consoleApplication(config1).run();
    }

}
