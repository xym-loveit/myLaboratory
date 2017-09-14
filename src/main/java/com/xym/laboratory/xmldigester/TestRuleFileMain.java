package com.xym.laboratory.xmldigester;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.RuleSet;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.digester3.binder.RulesBinder;
import org.apache.commons.digester3.binder.RulesModule;
import org.apache.commons.digester3.plugins.RuleLoader;
import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * Created by Administrator on 2017/9/14.
 */
public class TestRuleFileMain {

    static DigesterLoader loader = DigesterLoader.newLoader(new FromXmlRulesModule() {
        @Override
        protected void loadRules() {
            loadXMLRules(TestRuleFileMain.class.getResource("/books-rule.xml"));
        }
    });


    public static void main(String[] args) {
        try {
            Digester digester = loader.newDigester();
            digester.setValidating(true);
            // 解析XML文件,并得到ROOT元素
            Library library = (Library) digester.parse(TestBookMain.class.getResourceAsStream("/books.xml"));
            System.out.println(" 图书馆: " + library.getName());
            System.out.println(" 共藏书: " + library.getBookList().size() + " 本 ");
            System.out.println(" ***************************** ");

            for (Book book : library.getBookList()) {
                System.out.println(" 书名: " + book.getTitle() + "        作者: " + book.getAuthor());
                System.out.println(" ------------------------------ ");
                // 显示章节
                System.out.println(" 共 " + book.getChapters().size() + " 章 ");
                for (Chapter chapter : book.getChapters()) {
                    System.out.println(chapter.getNo() + ": " + chapter.getCaption());
                }
                System.out.println(" ------------------------------ ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
