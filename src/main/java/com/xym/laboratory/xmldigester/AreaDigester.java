package com.xym.laboratory.xmldigester;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/9/14.
 * <p>
 * Digester一个通过dom驱动解析xml的工具包
 */
public class AreaDigester {
    public static void main(String[] args) {
        Digester digester = new Digester();
        //指定它不要用DTD验证XML文档的合法性——这是因为我们没有为XML文档定义DTD
        digester.setValidating(false);
        //指明匹配模式和要创建的类
        digester.addObjectCreate("viewcache/areas", ViewCache.class);
        digester.addObjectCreate("viewcache/areas/area", Area.class);

        // 设置对象属性,与xml文件对应,不设置则是默认
        digester.addBeanPropertySetter("viewcache/areas/area/id", "id");
        digester.addBeanPropertySetter("viewcache/areas/area/parentId", "parentId");
        digester.addBeanPropertySetter("viewcache/areas/area/areaType", "areaType");
        digester.addBeanPropertySetter("viewcache/areas/area/name", "name");
        digester.addBeanPropertySetter("viewcache/areas/area/phoneArea", "phoneArea");
        digester.addBeanPropertySetter("viewcache/areas/area/ordering", "ordering");

        //当移动到下一个标签时的动作
        digester.addSetNext("viewcache/areas/area", "addArea");
        try {
            ViewCache parse = (ViewCache) digester.parse(new File("D:\\IdeaProjects\\myLaboratory\\src\\main\\resources\\viewcache.xml"));
            for (Object o : parse.getAreaList()) {
                Area area = (Area) o;
                System.out.println(area);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
