package xyz.scootaloo.console.app;

import xyz.scootaloo.console.app.anno.Cmd;
import xyz.scootaloo.console.app.anno.Form;
import xyz.scootaloo.console.app.anno.Prop;
import xyz.scootaloo.console.app.application.ApplicationRunner;

/**
 * 使用表单功能
 * @author flutterdash@qq.com
 * @since 2021/1/22 15:54
 */
public class FormExample {

    public static void main(String[] args) {
        ApplicationRunner.consoleApplication().run();
    }

    /**
     * 有时候需要在参数中使用一个对象
     * 但是对象的属性有一些必填项，并且需要由键盘输入，这个时候可用使用系统的表单功能
     * 这个类上假如有 @Form 注解，那么就能由系统来处理
     * 尝试输入
     *      addStu #
     * 因为这也是个参数，但是需要触发表单功能，所以用了一个占位符
     * 对于表单功能，有以下规则
     * - `@Form`注解的`dftExtCmd`的属性可用于在输入过程中退出剩余的属性输入。
     * - 用于区别可选项和必选项(`isRequired`为true)，可选项的提示符是`~`，必选项的提示符是`!`
     * - 回车可以跳过当前属性的输入，前提是当前属性是必选项，否则无法跳过。
     * - 输入了`dftExtCmd`的退出命令后，假如当前输入的是必选项，则无法退出；假如当前是可选项，则跳转到下一条必选项属性的输入，假如余下没有必选项了，则进行选择是否需要对当前的输入修改。
     * - 在修改模式，输入`dftExtCmd`可以直接退出，无视必选项
     * - 在修改模式，回车可以跳到下一条属性的检查，无视当前是否是必选项。
     */
    @Cmd
    public Student addStu(Student student) {
        System.out.println(student.name);
        System.out.println(student.clazz);
        System.out.println(student.age);
        System.out.println(student.score);
        return student;
    }

    @Form(dftExtCmd = "/")
    public static class Student {

        // prompt属性表示输入此项数据时的提示，isRequired表示此属性是必选项，未获得有效数据的时候无法退出
        @Prop(prompt = "输入学生姓名", isRequired = true)
        private String name;

        @Prop(prompt = "输入班级", isRequired = true)
        private String clazz;

        // @Prop 注解默认 isRequired=false 可以跳过，或者输入退出标志退出输入
        @Prop(prompt = "输入学生年龄")
        private int age;

        @Prop(prompt = "输入成绩")
        private int score;

        // 表单类需要提供一个无参构造方法，private 或者 public 无所谓
        protected Student() {
        }

    }

}
