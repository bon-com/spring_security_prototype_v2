package com.example.prototype.web.common.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 相関チェックを行うカスタムバリデーション
 */
@Documented
@Constraint(validatedBy = FieldsMatchValidator.class)
@Retention(RUNTIME)
@Target(TYPE)
public @interface FieldsMatch {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    // 比較対象のフィールド名
    String field();
    // 確認用フィールド名
    String confirmField();
    // エラーメッセージを表示するフィールド（confirmFieldと同じが一般的）
    String errorField() default "";
}
