package utils.exceptions;

public class InvalidInputException extends Exception{
    private String fieldName;
    public InvalidInputException(String message){
        super(message);
    }
    public InvalidInputException(String fieldName, String message){
        super(message);
        this.fieldName = fieldName;
    }
    public String getFieldName(){
        return fieldName;
    }
}
