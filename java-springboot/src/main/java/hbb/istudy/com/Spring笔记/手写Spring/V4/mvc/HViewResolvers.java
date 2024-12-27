package hbb.istudy.com.Spring笔记.手写Spring.V4.mvc;

import org.springframework.util.StringUtils;

import java.io.File;

/**
 * @Author binbin
 * @Date 2024 12 25 18 50
 **/
public class HViewResolvers {
    private File templateRoot;
    private String viewName;
    //.vm .ftl .jsp
    private final String DEFAULT_TEMP_SUFFIX = ".html";


    public HViewResolvers(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        templateRoot = String.valueOf(new File(templateRootPath));
    }

    public HView resolveViewName(String viewName) {
        if (StringUtils.isEmpty(viewName)) {
            return null;
        }
        viewName = viewName.endsWith(DEFAULT_TEMP_SUFFIX) ? viewName : viewName + DEFAULT_TEMP_SUFFIX;
        File tmpFile=new File(templateRoot.getPath()+"/"+viewName);

        return new HView(tmpFile);
    }
}
