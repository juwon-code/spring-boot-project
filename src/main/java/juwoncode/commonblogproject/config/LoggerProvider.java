package juwoncode.commonblogproject.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerProvider {
    /**
     * 해당 클래스에 해당하는 Logger 인스턴스를 가져온다.
     * @param clazz
     *      Logger 인스턴스를 생성할 클래스.
     * @return
     *      클래스에 관련한 Logger 인스턴스.
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
