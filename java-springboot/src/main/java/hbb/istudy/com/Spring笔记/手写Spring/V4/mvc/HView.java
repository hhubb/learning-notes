package hbb.istudy.com.Spring笔记.手写Spring.V4.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author binbin
 * @Date 2024 12 26 16 37
 **/
public class HView {
    private File tmpFile;

    public HView(File tmpFile) {
        this.tmpFile = tmpFile;
    }

    public void render(Map<String, Object> model, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //无反射不框架，无正则不架构
        StringBuffer sb=new StringBuffer();
        RandomAccessFile re=new RandomAccessFile(this.tmpFile,"r");
        String line=null;
        //处理模板内的参数，替换掉变量
        while (null!=(line=re.readLine())){
            line=new String(line.getBytes(StandardCharsets.UTF_8));
            Pattern pattern=Pattern.compile("￥\\{[^\\}]+\\}}");
            Matcher matcher=pattern.matcher(line);
            while (matcher.find()){
                String paramName=matcher.group();
                paramName=paramName.replace("￥\\{|\\}","");
                Object paramValue=model.get(paramName);
                if (null==paramValue){
                    continue;
                }
                line=matcher.replaceFirst(paramValue.toString());
                matcher=pattern.matcher(line);
            }
        }
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().write(sb.toString());
    }
}
