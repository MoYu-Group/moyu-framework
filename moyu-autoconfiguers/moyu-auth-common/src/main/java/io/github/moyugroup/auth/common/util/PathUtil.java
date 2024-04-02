package io.github.moyugroup.auth.common.util;

import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * 路径工具类
 * <p>
 * Created by fanfan on 2024/01/31.
 */
public class PathUtil {

    private final static AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 检查路径是否匹配规则
     *
     * @param checkPath 路径规则
     * @param path      检查的路径
     * @return 检查结果
     */
    public static boolean isMatch(String checkPath, String path) {
        return pathMatcher.match(checkPath, path);
    }

    /**
     * 检查路径是否匹配规则
     *
     * @param checkPathList 路径规则列表
     * @param path          检查的路径
     * @return 检查结果
     */
    public static boolean isMatch(List<String> checkPathList, String path) {
        for (String checkPath : checkPathList) {
            return isMatch(checkPath, path);
        }
        return false;
    }

}
