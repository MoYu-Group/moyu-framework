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
     * @param path          检查的路径
     * @param checkPathList 路径规则列表
     * @return 检查结果
     */
    public static boolean isMatch(String path, List<String> checkPathList) {
        for (String checkPath : checkPathList) {
            if (pathMatcher.match(checkPath, path)) {
                return true;
            }
        }
        return false;
    }

}
