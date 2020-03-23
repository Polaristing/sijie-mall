package com.sijie.mall.common;

/**
 * @author 13
 * @联系QQ 1172895463
 * @email 1172895463@qq.com
 * @link https://www.xiayuan52.cn
 * @apiNote Exception
 */
public class NewBeeMallException extends RuntimeException {

    public NewBeeMallException() {
    }

    public NewBeeMallException(String message) {
        super(message);
    }

    /**
     * 丢出一个异常
     *
     * @param message
     */
    public static void fail(String message) {
        throw new NewBeeMallException(message);
    }

}
