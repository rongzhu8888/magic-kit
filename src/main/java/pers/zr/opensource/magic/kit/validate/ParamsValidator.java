package pers.zr.opensource.magic.kit.validate;

import pers.zr.opensource.magic.kit.response.CommonResponse;
import pers.zr.opensource.magic.kit.response.CommonResponseCode;


import javax.validation.*;
import java.util.Set;

/**
 * JSR 303校验解析<br>
 * 主要用于API（Controller）层参数的注解校验
 *
 * Created by zhurong on 2016-5-25.
 */
public class ParamsValidator {

    public static <T> CommonResponse validate(T paramObj) {
        CommonResponse response = null;
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(paramObj);
        if(null != violations && !violations.isEmpty()) {
            ConstraintViolation<T> firstViolation = null;
            int i = 0;
            for(ConstraintViolation<T> violation : violations) {
                if(i == 0) {
                    firstViolation = violation;
                    break;
                }
            }
            response = new CommonResponse(CommonResponseCode.PARAMS_ERROR, firstViolation.getMessage());
        }
        return response;
    }
}
