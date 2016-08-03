package ca.sfu.cmpt373.alpha.vrcladder.exceptions;

/**
 * This exception will be thrown when the template for
 * notifications or pdf files could not be open.
 */
public class TemplateNotFoundException extends BaseException{

    public TemplateNotFoundException(){
        super();
    }

    public TemplateNotFoundException(String message){
        super(message);
    }
}
