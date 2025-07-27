package com.zcom.usercenter.common;

import lombok.Data;
import java.io.Serializable;


/**
 * 通用返回类
 *
 * @param <T>
 * @author 人机玛巴
 */
@Data
public class BaseResponse<T> implements Serializable {

        private int code;

        // 设置为通用类型, 提高代码可复用性
        private T data;

        private String message;

        private String description;


        // 初始化
        public BaseResponse(int code, T data, String message, String description) {
            this.code = code;
            this.data = data;
            this.message = message;
            this.description = description;
        }


        public BaseResponse(ErrorCode errorCode) {
            this(errorCode.getCode(), null, errorCode.getMessage());
        }

        public BaseResponse(int code, T data) {
            this(code, data, "", "");
        }

        public BaseResponse(int code, T data, String message) {
            this(code, data, message, "");
        }

}
