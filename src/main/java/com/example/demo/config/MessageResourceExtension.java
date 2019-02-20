package com.example.demo.config;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Component("messageSource")
@Slf4j
public class MessageResourceExtension extends ResourceBundleMessageSource {

    /**
     * 指定的国际化文件目录
     */
    @Value(value = "${spring.messages.baseFolder:i18n}")
    private String baseFolder;

    /**
     * 父MessageSource指定的国际化文件
     */
    @Value(value = "${spring.messages.basename:message}")
    private String basename;

    
    /**
     * init()方法上有一个@PostConstruct注解，这会在MessageResourceExtension类被实例化之后自动调用init()方法。
     * 这个方法获取到baseFolder目录下所有的国际化文件并设置到basenameSet中。并且设置一个ParentMessageSource，
     * 这会在找不到国际化信息的时候，调用父MessageSource来查找国际化信息。
     */
    @PostConstruct
    public void init() {
        log.info("init MessageResourceExtension...");
        if (!StringUtils.isEmpty(baseFolder)) {
            try {
                this.setBasenames(getAllBaseNames(baseFolder));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        //设置父MessageSource
        
        ResourceBundleMessageSource parent = new ResourceBundleMessageSource();
        parent.setBasename(basename);
        this.setParentMessageSource(parent);
    }

    /**
     * 获取文件夹下所有的国际化文件名
     *
     *getAllBaseNames()方法获取到baseFolder的路径，
     *然后调用getAllFile()方法获取到该目录下所有的国际化文件的文件名。
     *
     * @param folderName 文件名
     * @return
     * @throws IOException
     */
    private String[] getAllBaseNames(final String folderName) throws IOException {
        URL url = Thread.currentThread().getContextClassLoader()
                .getResource(folderName);
        if (null == url) {
            throw new RuntimeException("无法获取资源文件路径");
        }

        List<String> baseNames = new ArrayList<>();
        if (url.getProtocol().equalsIgnoreCase("file")) {
            // 文件夹形式,用File获取资源路径
            File file = new File(url.getFile());
            if (file.exists() && file.isDirectory()) {
                baseNames = Files.walk(file.toPath())
                        .filter(path -> path.toFile().isFile())
                        .map(Path::toString)
                        .map(path -> path.substring(path.indexOf(folderName)))
                        .map(this::getI18FileName)
                        .distinct()
                        .collect(Collectors.toList());
            } else {
                logger.error("指定的baseFile不存在或者不是文件夹");
            }
        } else if (url.getProtocol().equalsIgnoreCase("jar")) {
            // jar包形式，用JarEntry获取资源路径
            String jarPath = url.getFile().substring(url.getFile().indexOf(":") + 2, url.getFile().indexOf("!"));
            JarFile jarFile = new JarFile(new File(jarPath));
            List<String> baseJars = jarFile.stream()
                    .map(ZipEntry::toString)
                    .filter(jar -> jar.endsWith(folderName + "/")).collect(Collectors.toList());
            if (baseJars.isEmpty()) {
                log.info("不存在{}资源文件夹", folderName);
                return new String[0];
            }

            baseNames = jarFile.stream().map(ZipEntry::toString)
                    .filter(jar -> baseJars.stream().anyMatch(jar::startsWith))
                    .filter(jar -> jar.endsWith(".properties"))
                    .map(jar -> jar.substring(jar.indexOf(folderName)))
                    .map(this::getI18FileName)
                    .distinct()
                    .collect(Collectors.toList());

        }
        return baseNames.toArray(new String[0]);
    }

    /**
     * 遍历所有文件
     * 
     * getAllFile()遍历目录，如果是文件夹就继续遍历，
     * 如果是文件就调用getI18FileName()把文件名转为’i18n/basename/‘格式的国际化资源名。
     *
     * @param basenames
     * @param folder
     * @param path
     */
    private void getAllFile(List<String> basenames, File folder, String path) {
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                this.getAllFile(basenames, file, path + folder.getName() + File.separator);
            }
        } else {
            String i18Name = this.getI18FileName(path + folder.getName());
            if (!basenames.contains(i18Name)) {
                basenames.add(i18Name);
            }

        }
    }

    /**
     * 把普通文件名转换成国际化文件名
     *
     * @param filename
     * @return
     */
    private String getI18FileName(String filename) {
        filename = filename.replace(".properties", "");
        for (int i = 0; i < 2; i++) {
            int index = filename.lastIndexOf("_");
            if (index != -1) {
                filename = filename.substring(0, index);
            }
        }
        return filename;
    }
    
    
    
public static String I18N_ATTRIBUTE = "i18n_attribute";
    
    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        // 获取request中设置的指定国际化文件名
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final String i18File = (String) attr.getAttribute(I18N_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        if (!StringUtils.isEmpty(i18File)) {
            //获取在basenameSet中匹配的国际化文件名
            String basename = getBasenameSet().stream()
                    .filter(name -> StringUtils.endsWithIgnoreCase(name, i18File))
                    .findFirst().orElse(null);
            if (!StringUtils.isEmpty(basename)) {
                //得到指定的国际化文件资源
                ResourceBundle bundle = getResourceBundle(basename, locale);
                if (bundle != null) {
                    return getStringOrNull(bundle, code);
                }
            }
        }
        //如果指定i18文件夹中没有该国际化字段,返回null会在ParentMessageSource中查找
        return null;
    }

}