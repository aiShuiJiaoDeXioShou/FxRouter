import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReUtil;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import lh.router.annotation.FXRoute;
import lh.router.utils.RouterUtils;
import lh.utils.ClassRootUtil;
import lh.utils.CurrentLineInfo;
import lh.utils.FileUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Test1 {

    @Test
    public void Test1() {
        String s = ClassRootUtil.BasePath("static/img/ic3.png");
        System.out.println(s);
    }

    @Test
    public void Test2() {
        // 读取该文件所有的内容
        String str = FileUtil.readToStr(ClassRootUtil.BasePath("static/config/base.yt"));
        System.out.println(str);
    }

    @Test
    public void Test3() {
        Set<Class<?>> classes = ClassUtil.scanPackage("lh.view");
        List<FXRoute> fxRoutes = new ArrayList<>();
        for (Class<?> clazz : classes) {
            System.out.println(clazz.getName());
            System.out.println(clazz);
            // 判断是否具有指定的注解
            FXRoute fxRoute = clazz.getAnnotation(FXRoute.class);
            if (fxRoute != null) fxRoutes.add(fxRoute);
        }
    }

    @Test
    public void Test4() {
        Object obj = new VBox();
        Parent ob = (Parent) obj;
    }

    @Test
    public void Test5() {
        String path = "/ui/";
        System.out.println(path.indexOf("/") == path.lastIndexOf("/"));
    }

    @Test
    public void Test6() {
        int i = 999;
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(1);
        arr.add(4);
        arr.add(5);
        arr.add(8);
        arr.add(2);
        arr.add(3);
        arr.sort((o1, o2) -> i - ( o1 - o2));
        System.out.println(arr);
    }


    @Test
    public void Test7() {
        int size = ReUtil.count("/", "/fdsfsd");
        System.out.println(size);
    }

    @Test
    public void Test8() {
        RouterUtils.paths("/sfdf/fsdf/tewrt/qewq").forEach(System.out::println);
    }

    @Test
    public void Test9() {
        String path = "/iu/op";
        String substring = path.substring(path.indexOf("/", 1));
        System.out.println(substring);
    }


    @Test
    public void Test10() {
        List<String> father = new ArrayList<>();
        father.add("sdfsdfsl");
        father.add("sdfs");
        father.add("sdfssdf");
        father.add("sd");
        father.sort((o1, o2) -> o1.length() > o2.length() ? -1 : 0);
        System.out.println(father);
    }

    @Test
    public void Test11() {
        String className = CurrentLineInfo.getClassName();
        System.out.println(className);
    }
}
