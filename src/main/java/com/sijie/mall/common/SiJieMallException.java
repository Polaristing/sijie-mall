package com.sijie.mall.common;

/**
 * @author 13
 * @联系QQ 1172895463
 * @email gting0518@163.com
 * @link https://www.xiayuan52.cn
 * @apiNote Exception
 */
public class SiJieMallException extends RuntimeException {

    public SiJieMallException() {
    }

    public SiJieMallException(String message) {
        super(message);
    }

    /**
     * 丢出一个异常
     *
     * @param message
     */
    public static void fail(String message) {
        throw new SiJieMallException(message);
    }

}
