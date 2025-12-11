package utils.exceptions;

public class InvalidInputException extends IllegalArgumentException{
    private String fieldName;
    public InvalidInputException(String fieldName, String message){
        super(message);
        this.fieldName = fieldName;
    }
    public String getFieldName(){
        return fieldName;
    }
}
