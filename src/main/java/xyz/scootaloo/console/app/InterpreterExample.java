package xyz.scootaloo.console.app;

import xyz.scootaloo.console.app.FormExample.Student;
import xyz.scootaloo.console.app.application.ApplicationRunner;
import xyz.scootaloo.console.app.parser.Interpreter;
import xyz.scootaloo.console.app.parser.InvokeInfo;

/**
 * 解释器
 * @author flutterdash@qq.com
 * @since 2021/1/22 16:55
 */
public class InterpreterExample {

    public static void main(String[] args) {
        // 使用 Commons.simpleConf() 获取更精简的配置类
        Interpreter interpreter = ApplicationRunner.getInterpreter(
                Console.factories()
                        .addFactories()
                            .add(QuickStart::new, true)
                            .add(FormExample::new,  true)
                            .ok()
                        .build());

        // 直接运行命令，得到结果的包装类
        InvokeInfo result1 = interpreter.interpret("add 11 12");
        System.out.println("执行 'add 11 12' 的结果: " + result1.get());

        // 使用参数运行，这里的args等于方法参数，也就是说这里可以看成是调用 add(11, 12)
        InvokeInfo result2 = interpreter.invoke("add", 11, 12);
        System.out.println("使用参数执行，结果: " + result2.get());

        // 解释器调用参数含有对象的方法时，字符串命令中的占位符会触发等待键盘输入，如
//        InvokeInfo result3 = interpreter.interpret("stuAdd #"); // 在 main 方法中调用可以观察到

        // result3的方式调用参数中含有对象的方法，某些场景下可能会引起线程阻塞，可以使用 invoke 方法传入对象调用
        // 或者实现自定义的类型转换器，参考 AdvancedDemo.resolveByte(Str) 方法
        InvokeInfo result4 = interpreter.invoke("addStu", new Student());
        System.out.println("result4: " + result4.get());

        // 在解释器中使用变量占位符
        InvokeInfo result5 = interpreter.interpret("echo -v ${rand.int(10,15)}");
        System.out.println("\"echo -v ${rand.int(10,15)}\"的结果是: " + result5.get());

        System.out.println("\n----------------------------------------------------\n");

        // 变量功能在解释器中的使用
        // 将这个随机整型做为 "randNumber" 这个键的值
        boolean flag = interpreter.set("randNumber", "echo 使用echo时两边的内容${rand.int(10,15)}都被忽略了");
        // 检查设置情况
        if (flag)
            System.out.println("设置成功");
        // 现在可以获取到这个值了，使用get (不需要占位符) 或者 echo (需要占位符)
        InvokeInfo result6 = interpreter.interpret("get randNumber"); // else: echo ${randNumber}
        System.out.println("randNumber is " + result6.get());
    }

}
