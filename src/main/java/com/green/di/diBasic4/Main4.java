package com.green.di.diBasic4;

import com.google.common.reflect.ClassPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component class Car {
    @Resource Engine engine;
    @Resource Door door;

    @Override
    public String toString() {
        return "Car{" +
                "engine=" + engine +
                ", door=" + door +
                '}';
    }
}
@Component class SportsCar extends Car {}
@Component class Truck extends Car {}
@Component class Engine {}
@Component class Door {}

class AppContext {
    Map map; // 객체 저장소

    AppContext() {
        map = new HashMap();

        doComponentScan();
        doAutowired();
        doResource();
    }

    private void doResource() {
        // map에 저장된 객체의 iv 중에서 @Resource가 붙어있으면 map에서 iv의 타입에 맞는 객체를 찾아서 연결(객체의 주소를 iv에 저장)
        try {
            for(Object bean : map.values()) {
                for(Field fld : bean.getClass().getDeclaredFields()) {
                    if(fld.getAnnotation(Resource.class) != null) {
                        fld.set(bean, getBean(fld.getName()));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void doAutowired() {
        // map에 저장된 객체의 iv 중에서 @Autowired가 붙어있으면 map에서 iv의 타입에 맞는 객체를 찾아서 연결(객체의 주소를 iv에 저장)
        try {
            for(Object bean : map.values()) {
                for(Field fld : bean.getClass().getDeclaredFields()) {
                    if(fld.getAnnotation(Autowired.class) != null) {
                        fld.set(bean, getBean(fld.getType()));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void doComponentScan() {
        // 1. 패키지 내의 클래스 목록을 가져옴
        // 2. 반복문으로 클래스를 하나씩 읽어와서 @Component가 붙어있는지 확인
        // 3. @Component가 붙어있으면 객체를 생성해서 map에 저장

        try {
            // 클래스의 정보를 확인
            ClassLoader classLoader = AppContext.class.getClassLoader();
            ClassPath classPath = ClassPath.from(classLoader);

            // 클래스의 목록을 가져옴
            // getTopLevelClasses를 이용하여 패키지 이름을 적어주고 set으로 받음
            Set<ClassPath.ClassInfo> set = classPath.getTopLevelClasses("com.green.di.diBasic4");
            
            // for문을 이용하여 @Component가 있는지를 확인
            for(ClassPath.ClassInfo classInfo : set) {
                Class clazz = classInfo.load();
                Component component = (Component) clazz.getAnnotation(Component.class);

                // 만약 component가 null이 아니면 == @Component가 있음
                if(component != null) {
                    // uncapitalize를 이용하여 첫글자를 소문자로 변경하여 클래스 이름을 가져옴
                    String id = StringUtils.uncapitalize(classInfo.getSimpleName());

                    // map에 객체를 만들어서 저장
                    map.put(id, clazz.newInstance());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Object getBean(String key) {
        return map.get(key);
    } // key로 찾기

    Object getBean(Class clazz) {
        for(Object obj : map.values()) {
            if(clazz.isInstance(obj)) {
                return obj;
            }
        }

        return null;
    } // type으로 찾기
}

public class Main4 {
    public static void main(String[] args) throws Exception {
        AppContext ac = new AppContext();

        Car car = (Car) ac.getBean("car");
        Door door = (Door) ac.getBean(Door.class);
        Engine engine = (Engine) ac.getBean("engine");

        // 수동 연결
//        car.engine = engine;
//        car.door = door;

        System.out.println("car = " + car);
        System.out.println("engine = " + engine);
        System.out.println("door = " + door);
    }
}
