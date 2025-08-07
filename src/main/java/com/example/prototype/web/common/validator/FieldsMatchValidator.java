package com.example.prototype.web.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.prototype.common.constants.Constants;

/**
 * 相関チェックカスタムBeanバリデーター
 */
public class FieldsMatchValidator implements ConstraintValidator<FieldsMatch, Object> {
    /** ロガー */
    private static final Logger logger = LoggerFactory.getLogger(FieldsMatchValidator.class);
    /** フィールド */
    private String field;
    /** 確認フィールド */
    private String confirmField;
    /** Beanバリデーションエラー出力箇所 */
    private String errorField;
    /** 相関チェックエラーメッセージ */
    private String message;

    @Override
    public void initialize(FieldsMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.confirmField = constraintAnnotation.confirmField();
        this.errorField = constraintAnnotation.errorField().isEmpty() ? confirmField : constraintAnnotation.errorField();
        this.message = constraintAnnotation.message();
    }
    
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            String fieldValue = BeanUtils.getProperty(value, field);
            String confirmValue = BeanUtils.getProperty(value, confirmField);
            
            // 双方のフィールドに値があれば相関チェック実施
            if (fieldValue != null && confirmValue != null && !fieldValue.equals(confirmValue)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                       .addPropertyNode(errorField)
                       .addConstraintViolation();
                return false;
            }
            return true;
        } catch (Exception ex) {
            // BeanUtilsにてフィールド取得失敗
            logger.warn("\n★★フィールド取得失敗★★\n・フィールド①: {}\n・フィールド②: {}\n", field, confirmField);
            throw new IllegalArgumentException(Constants.ERR_MSG_PROPERTY_ACCESS_FAILED);
        }
    }
}
