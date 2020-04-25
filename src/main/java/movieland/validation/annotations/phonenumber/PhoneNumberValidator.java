package movieland.validation.annotations.phonenumber;

import movieland.validation.annotations.ValidationStatements;
import movieland.validation.annotations.ValidatorUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, CharSequence> {

   private boolean nullable;

   private String message;

   private Pattern pattern;

   public void initialize(PhoneNumber phoneNumberAnnotation) {
      nullable = phoneNumberAnnotation.nullable();
      message = phoneNumberAnnotation.message();
      pattern = Pattern.compile(phoneNumberAnnotation.pattern());
   }

   public boolean isValid(CharSequence inputValue, ConstraintValidatorContext validatorContext) {
      if (inputValue == null) {
         if (nullable) {
            return true;
         } else {
            ValidatorUtils.setErrorMessage(validatorContext, ValidationStatements.PHONE_NUMBER_CAN_NOT_BE_NULL);
            return false;
         }
      }

      String phoneNumber = inputValue.toString();

      if (!pattern.matcher(phoneNumber).matches()) {
         ValidatorUtils.setErrorMessage(validatorContext, message);
         return false;
      }

      return true;
   }
}
